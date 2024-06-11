package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.rhino.TokenStream;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(Parameterized.class)
public class RenamePrototypesTest {

  private Compiler compiler;
  private RenamePrototypes renamePrototypes;
  private boolean aggressiveRenaming;

  public RenamePrototypesTest(boolean aggressiveRenaming) {
    this.aggressiveRenaming = aggressiveRenaming;
  }

  @Parameterized.Parameters(name = "{0} - aggressiveRenaming={1}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
            {false},
            {true}
    });
  }

  @Before
  public void setup() {
    CompilerOptions options = new CompilerOptions();
    options.setCompilerPasses(Arrays.asList(new CompilerPass[] {new RewriteTypeNames()}));
    this.compiler = new Compiler(options);
    this.renamePrototypes = new RenamePrototypes(compiler, aggressiveRenaming, null, null);
  }

  @Test
  public void testSimpleRename() {
    String input = "function Foo() {} Foo.prototype.bar = function() {};";
    Node root = new TokenStream(input).parse();

    this.renamePrototypes.process(null, root);

    NodeTraversal.traverse(compiler, root, new NodeNameVerifier());

    assertNotNull(compiler.getDiagnostics().getErrors().isEmpty());
  }

  @Test
  public void testPrototypePropertyRenaming() {
    String input = "function Foo() {} Foo.prototype.getBar = function() {};";
    Node root = new TokenStream(input).parse();

    this.renamePrototypes.process(null, root);

    NodeTraversal.traverse(compiler, root, new NodeNameVerifier());

    assertNotNull(compiler.getDiagnostics().getErrors().isEmpty());

    Node getBarNode = findNode(root, "getBar");
    assertEquals("getBar_", getBarNode.getName());
  }

  @Test
  public void testObjLitPropertyRenaming() {
    String input = "function Foo() {} Foo.prototype = { bar : function() {} };";
    Node root = new TokenStream(input).parse();

    this.renamePrototypes.process(null, root);

    NodeTraversal.traverse(compiler, root, new NodeNameVerifier());

    assertNotNull(compiler.getDiagnostics().getErrors().isEmpty());

    Node barNode = findNode(root, "bar");
    assertEquals("bar_", barNode.getName());
  }

  @Test
  public void testPrototypeAndObjLitPropertyRenaming() {
    String input = "function Foo() {} Foo.prototype.getBar = function() {}; Foo.prototype = { bar : function() {} };";
    Node root = new TokenStream(input).parse();

    this.renamePrototypes.process(null, root);

    NodeTraversal.traverse(compiler, root, new NodeNameVerifier());

    assertNotNull(compiler.getDiagnostics().getErrors().isEmpty());

    Node getBarNode = findNode(root, "getBar");
    assertEquals("getBar_", getBarNode.getName());

    Node barNode = findNode(root, "bar");
    assertEquals("bar_", barNode.getName());
  }

  @Test
  public void testPrototypePropertyRenamingWithExistingName() {
    String input = "function Foo() {} Foo.prototype.getBar = function() {}; Foo.prototype.getBar = function() {};";
    Node root = new TokenStream(input).parse();

    this.renamePrototypes.process(null, root);

    NodeTraversal.traverse(compiler, root, new NodeNameVerifier());

    assertNotNull(compiler.getDiagnostics().getErrors().isEmpty());

    Node getBarNode1 = findNode(root, "getBar");
    assertEquals("getBar__0_", getBarNode1.getName());

    Node getBarNode2 = findNode(root, "getBar");
    assertEquals("getBar__1_", getBarNode2.getName());
  }

  @Test
  public void testPrototypePropertyRenamingWithAggressiveMode() {
    String input = "function Foo() {} Foo.prototype.getBar = function() {};";
    Node root = new TokenStream(input).parse();

    this.renamePrototypes.process(null, root);

    NodeTraversal.traverse(compiler, root, new NodeNameVerifier());

    assertNotNull(compiler.getDiagnostics().getErrors().isEmpty());

    Node getBarNode = findNode(root, "getBar");
    assertEquals("getBar", getBarNode.getName());
  }

  @Test
  public void testPrototypeRenamingWithReservedNames() {
    String input = "function Foo() {} Foo.prototype.indexOf = function() {};";
    Node root = new TokenStream(input).parse();

    this.renamePrototypes.process(null, root);

    NodeTraversal.traverse(compiler, root, new NodeNameVerifier());

    List<String> diagnostics = compiler.getDiagnostics().getErrors();
    assertEquals(1, diagnostics.size());
    assertEquals("RenamePrototypes: Error: Cannot rename 'indexOf' to a new name.", diagnostics.get(0).getMessage());
  }

  @Test
  public void testPrototypeRenamingWithReservedCharacters() {
    String input = "function Foo() {} Foo.prototype.indexOf$ = function() {};";
    Node root = new TokenStream(input).parse();

    char[] reservedCharacters = new char[]{'$'};

    this.renamePrototypes.process(null, root, null, new VariableMap(), reservedCharacters);

    NodeTraversal.traverse(compiler, root, new NodeNameVerifier());

    List<String> diagnostics = compiler.getDiagnostics().getErrors();
    assertEquals(1, diagnostics.size());
    assertEquals("RenamePrototypes: Error: Cannot use '$' as the first character of a renamed name.", diagnostics.get(0).getMessage());
  }

  private Node findNode(Node node, String name) {
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.GETPROP || child.getType() == Token.GETELEM) {
        Node nameNode = child.getFirstChild().getNext();
        if (nameNode.getType() == Token.STRING && nameNode.getString().equals(name)) {
          return child.getLastChild();
        }
      }
      Node foundNode = findNode(child, name);
      if (foundNode != null) {
        return foundNode;
      }
    }
    return null;
  }

  private static class NodeNameVerifier extends AbstractPostOrderCallback {

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.getType() == Token.FUNCTION) {
        String name = n.getFirstChild().getString();
        assertEquals("Foo", name);
      }
    }
  }
}