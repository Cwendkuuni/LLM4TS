package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompiler.LifeCycleStage;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class RenamePrototypesTest {

  @Test
  public void testProcess() {
    AbstractCompiler compiler = createCompiler();
    CompilerInput input = createInput("testRenamePrototypes.js");

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    renamePrototypes.process(input.getRootNode(), input.getExternsNode());

    Node root = input.getRootNode();
    assertEquals(3, getPropertyCount(renamePrototypes, root));

    String oldName1 = "prototypeFoo";
    String newName1 = "prototypeRenamedFoo";
    String oldName2 = "_privateBar";
    String newName2 = "_privateRenamedBar";
    String oldName3 = "baz";
    String newName3 = "renamedBaz";

    Property p1 = getProperty(renamePrototypes, oldName1);
    assertSame(newName1, p1.newName);
    Property p2 = getProperty(renamePrototypes, oldName2);
    assertSame(newName2, p2.newName);
    Property p3 = getProperty(renamePrototypes, oldName3);
    assertNull(p3.newName);

    checkStringNodes(renamePrototypes, input, oldName1, newName1);
    checkStringNodes(renamePrototypes, input, oldName2, newName2);
    checkStringNodes(renamePrototypes, input, oldName3, null);
  }

  @Test
  public void testProcess_aggressiveRenamingOff() {
    AbstractCompiler compiler = createCompiler();
    CompilerInput input = createInput("testRenamePrototypes.js");

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    renamePrototypes.process(input.getRootNode(), input.getExternsNode());

    Node root = input.getRootNode();
    assertEquals(1, getPropertyCount(renamePrototypes, root));

    String oldName = "_privateBar";
    Property p = getProperty(renamePrototypes, oldName);
    assertNull(p.newName);

    checkStringNodes(renamePrototypes, input, oldName, null);
  }

  @Test
  public void testProcess_externals() {
    AbstractCompiler compiler = createCompiler();
    CompilerInput input = createInput("testRenamePrototypes.js");

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    renamePrototypes.process(input.getRootNode(), input.getExternsNode());

    Node root = input.getRootNode();
    Node externs = input.getExternsNode();

    assertEquals(2, getPropertyCount(renamePrototypes, root));
    assertEquals(1, getPropertyCount(renamePrototypes, externs));

    String oldName1 = "prototypeFoo";
    Property p1 = getProperty(renamePrototypes, oldName1);
    assertSame(oldName1, p1.oldName);
    assertNull(p1.newName);

    String oldName2 = "indexOf";
    Property p2 = getProperty(renamePrototypes, oldName2);
    assertSame(oldName2, p2.oldName);
    assertNull(p2.newName);
  }

  @Test
  public void testProcess_reservedCharacters() {
    AbstractCompiler compiler = createCompiler();
    CompilerInput input = createInput("testRenamePrototypesWithReservedCharacters.js");

    char[] reservedCharacters = new char[]{'%', '&'};
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, reservedCharacters, null);
    renamePrototypes.process(input.getRootNode(), input.getExternsNode());

    Node root = input.getRootNode();
    assertEquals(2, getPropertyCount(renamePrototypes, root));

    String oldName1 = "_privateBar";
    Property p1 = getProperty(renamePrototypes, oldName1);
    assertNull(p1.newName);

    String oldName2 = "prototype%Foo";
    Property p2 = getProperty(renamePrototypes, oldName2);
    String newName2 = "prototypeRenamedFoo";
    assertSame(newName2, p2.newName);

    checkStringNodes(renamePrototypes, input, oldName1, null);
    checkStringNodes(renamePrototypes, input, oldName2, newName2);
  }

  private AbstractCompiler createCompiler() {
    return new AbstractCompiler() {

      @Override
      public void addToDebugLog(String message) {
        // Do nothing
      }

      @Override
      public void reportCodeChange() {
        // Do nothing
      }

      @Override
      public void setLifeCycleStage(LifeCycleStage stage) {
        // Do nothing
      }

      @Override
      public CompilerInput getInput() {
        throw new UnsupportedOperationException();
      }

      @Override
      public NodeFactory getFactory() {
        throw new UnsupportedOperationException();
      }

      @Override
      public TokenStream createTokenStream(String code, int startPos, int endPos) {
        throw new UnsupportedOperationException();
      }
    };
  }

  private CompilerInput createInput(String fileName) {
    String code = "function Foo() {\n" +
                  "  this.prototypeFoo = function() { %%bar%% }\n" +
                  "};\n" +
                  "\n" +
                  "Foo.prototype.prototypeFoo = function() { return 42; };\n" +
                  "\n" +
                  "Foo._privateBar = function() { return 21; };\n" +
                  "\n" +
                  "var foos = [new Foo(), new Foo()];\n" +
                  "\n" +
                  "function testRenamePrototypes() {\n" +
                  "  // Test that prototypes are renamed\n" +
                  "  assertEquals(42, foos[0].prototypeFoo());\n" +
                  "  assertEquals(42, foos[1].prototypeFoo());\n" +
                  "\n" +
                  "  // Test that private properties are not renamed\n" +
                  "  assertEquals(21, foos[0]._privateBar());\n" +
                  "  assertEquals(21, foos[1]._privateBar());\n" +
                  "}\n" +
                  "testRenamePrototypes();";

    return new CompilerInput(fileName, code, 0, code.length());
  }

  private int getPropertyCount(RenamePrototypes renamePrototypes, Node node) {
    int count = 0;
    NodeTraversal.traverse(renamePrototypes.compiler, node, new PropertyCounter(renamePrototypes));
    return count;
  }

  private Property getProperty(RenamePrototypes renamePrototypes, String name) {
    return renamePrototypes.properties.get(name);
  }

  private void checkStringNodes(RenamePrototypes renamePrototypes, CompilerInput input,
                               String oldName, @Nullable String newName) {
    NodeTraversal.traverse(renamePrototypes.compiler, input.getRootNode(), new StringNodeChecker(oldName, newName));
  }

  private static class PropertyCounter extends AbstractPostOrderCallback {
    RenamePrototypes renamePrototypes;

    PropertyCounter(RenamePrototypes renamePrototypes) {
      this.renamePrototypes = renamePrototypes;
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.getType() == Token.STRING) {
        String name = n.getString();
        Property property = renamePrototypes.properties.get(name);
        if (property != null) {
          property.refCount++;
        }
      }
    }
  }

  private static class StringNodeChecker extends AbstractPostOrderCallback {
    String oldName;
    @Nullable String newName;

    StringNodeChecker(String oldName, @Nullable String newName) {
      this.oldName = oldName;
      this.newName = newName;
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.getType() == Token.STRING) {
        String name = n.getString();
        Property property = renamePrototypes.properties.get(name);
        if (property != null && (property.newName != null || !oldName.equals(name))) {
          if (newName != null && !newName.equals(name)) {
            assertSame(newName, n.getString());
          } else {
            assertNull(n.getString());
          }
        } else {
          fail("String node with name " + name + " should be renamed to " + (newName != null ? newName : "null"));
        }
      }
    }
  }
}