import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.rhino.NodeFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RenameVarsTest {

  @SuppressWarnings("unchecked")
  private static Node createNode(int type, int lineNumber, String value) {
    Node node = NodeFactory.createNodeFromString("");
    node.setType(type);
    node.setLine(lineNumber);
    node.setString(value);
    return node;
  }

  @SuppressWarnings("unchecked")
  private static CompilerInput createInput(String code) {
    return new CompilerInput(
        "test.js",
        code,
        0,
        0,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null);
  }

  @SuppressWarnings("unchecked")
  private static Node createRootNode(NodeFactory nodeFactory) {
    return nodeFactory.createProgram(
        null,
        null,
        null,
        null,
        null,
        null,
        Collections.emptyList());
  }

  private static void testRenameVars(Compiler compiler, String code) {
    CompilerPass pass = new RenameVars(
        compiler,
        "",
        false,
        false,
        false,
        null,
        null,
        null);

    CompilerInput input = createInput(code);
    Node root = createRootNode(compiler.getNodeFactory());
    compiler.process(input, root, pass);

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.NAME) {
          String oldName = n.getString();
          Assignment assignment = pass.assignments.get(oldName);
          String newName = assignment.newName;
          assertEquals(newName, n.getString());
        }
      }
    });
  }

  @org.junit.Test
  public void testRenameGlobalVars() {
    Compiler compiler = new Compiler(null, null, null, null, null, null);
    String code = "var a = 1, b = 2, c = 3, d = 4";
    testRenameVars(compiler, code);
  }

  @org.junit.Test
  public void testRenameLocalVars() {
    Compiler compiler = new Compiler(null, null, null, null, null, null);
    String code = "function f() { var a = 1, b = 2, c = 3, d = 4 }";
    testRenameVars(compiler, code);
  }

  @org.junit.Test
  public void testRenameGlobalVarsWithPrefix() {
    Compiler compiler = new Compiler(null, null, null, null, null, null);
    String code = "var _a = 1, _b = 2, _c = 3, _d = 4";
    testRenameVars(compiler, code);
  }

  @org.junit.Test
  public void testRenameLocalVarsWithPrefix() {
    Compiler compiler = new Compiler(null, null, null, null, null, null);
    String code = "function f() { var _a = 1, _b = 2, _c = 3, _d = 4 }";
    testRenameVars(compiler, code);
  }

  @org.junit.Test
  public void testRenameVarsWithReservedNames() {
    Compiler compiler = new Compiler(null, null, null, null, null, null);
    String code = "var this = 1, class = 2, new = 3";
    testRenameVars(compiler, code);
  }

  @org.junit.Test
  public void testRenameVarsWithReservedCharacters() {
    Compiler compiler = new Compiler(null, null, null, null, null, null);
    String code = "var $a = 1, %b = 2, @c = 3, ^d = 4";
    testRenameVars(compiler, code);
  }

  @org.junit.Test
  public void testRenameVarsWithExternals() {
    Compiler compiler = new Compiler(null, null, null, null, null, null);
    String code = "var a = 1, exports = { getA: function() { return a } }";
    testRenameVars(compiler, code);
    assertNotNull(compiler.getDebugLog());
    assertEquals("JS var assignments:\n" +
        "a => a$0\n" +
        "exports => exports$0\n", compiler.getDebugLog());
  }

  @org.junit.Test
  public void testRenameVarsWithPreserveAnonymousFunctions() {
    Compiler compiler = new Compiler(null, null, null, null, null, null);
    String code = "var a = function() {}, b = 1";
    testRenameVars(compiler, code);
    assertNotNull(compiler.getDebugLog());
    assertEquals("JS var assignments:\n" +
        "a => a$0\n" +
        "b => b\n", compiler.getDebugLog());
  }

  @org.junit.Test
  public void testRenameVarsWithGeneratePseudoNames() {
    Compiler compiler = new Compiler(null, null, null, null, null, null);
    String code = "var a = 1, b = 2";
    testRenameVars(compiler, code);
    assertNotNull(compiler.getDebugLog());
    assertEquals("JS var assignments:\n" +
        "a => $a$1\n" +
        "b => $a$2\n", compiler.getDebugLog());
  }

  @org.junit.Test
  public void testRenameVarsWithLocalRenamingOnly() {
    Compiler compiler = new Compiler(null, null, null, null, null, null);
    String code = "var a = 1, b = 2, function f() { var c = 3 }";
    testRenameVars(compiler, code);
    assertNotNull(compiler.getDebugLog());
    assertEquals("JS var assignments:\n" +
        "a => a$0\n" +
        "b => b\n" +
        "c => c$1\n", compiler.getDebugLog());
  }

  @org.junit.Test
  public void testRenameVarsWithCustomPrefix() {
    Compiler compiler = new Compiler(null, "customPrefix_", null, null, null, null);
    String code = "var a = 1, b = 2";
    testRenameVars(compiler, code);
    assertNotNull(compiler.getDebugLog());
    assertEquals("JS var assignments:\n" +
        "a => customPrefix_a$0\n" +
        "b => customPrefix_b$0\n", compiler.getDebugLog());
  }

  @org.junit.Test
  public void testRenameVarsWithCustomPrefixAndPreserveAnonymousFunctions() {
    Compiler compiler = new Compiler(null, "customPrefix_", null, null, true, null);
    String code = "var a = function() {}, b = 1";
    testRenameVars(compiler, code);
    assertNotNull(compiler.getDebugLog());
    assertEquals("JS var assignments:\n" +
        "a => customPrefix_a$0\n" +
        "b => b\n", compiler.getDebugLog());
  }

  @org.junit.Test
  public void testRenameVarsWithCustomPrefixAndGeneratePseudoNames() {
    Compiler compiler = new Compiler(null, "customPrefix_", null, null, null, false);
    String code = "var a = 1, b = 2";
    testRenameVars(compiler, code);
    assertNotNull(compiler.getDebugLog());
    assertEquals("JS var assignments:\n" +
        "a => customPrefix_a$0\n" +
        "b => customPrefix_b$0\n", compiler.getDebugLog());
  }

  @org.junit.Test
  public void testRenameVarsWithCustomPrefixAndLocalRenamingOnly() {
    Compiler compiler = new Compiler(null, "customPrefix_", true, null, null, null);
    String code = "var a = 1, b = 2, function f() { var c = 3 }";
    testRenameVars(compiler, code);
    assertNotNull(compiler.getDebugLog());
    assertEquals("JS var assignments:\n" +
        "a => a$0\n" +
        "b => b\n" +
        "c => c$1\n", compiler.getDebugLog());
  }

  @org.junit.Test
  public void testRenameVarsWithCustomPrefixAndReservedNames() {
    Compiler compiler = new Compiler(null, "customPrefix_", null, null, null, null);
    String code = "var this = 1, class = 2, new = 3";
    testRenameVars(compiler, code);
    assertNotNull(compiler.getDebugLog());
    assertEquals("JS var assignments:\n" +
        "this => this$0\n" +
        "class => class$0\n" +
        "new => new$0\n", compiler.getDebugLog());
  }

  @org.junit.Test
  public void testRenameVarsWithCustomPrefixAndReservedCharacters() {
    Compiler compiler = new Compiler(null, "customPrefix_", null, null, null, null);
    String code = "var $a = 1, %b = 2, @c = 3, ^d = 4";
    testRenameVars(compiler, code);
    assertNotNull(compiler.getDebugLog());
    assertEquals("JS var assignments:\n" +
        "$a => customPrefix_$a$0\n" +
        "%b => customPrefix_%b$0\n" +
        "@c => customPrefix_@c$0\n" +
        "^d => customPrefix_^d$0\n", compiler.getDebugLog());
  }

  @org.junit.Test
  public void testRenameVarsWithCustomPrefixAndExternals() {
    Compiler compiler = new Compiler(null, "customPrefix_", null, null, null, null);
    String code = "var a = 1, exports = { getA: function() { return a } }";
    testRenameVars(compiler, code);
    assertNotNull(compiler.getDebugLog());
    assertEquals("JS var assignments:\n" +
        "a => customPrefix_a$0\n" +
        "exports => exports$0\n", compiler.getDebugLog());
  }
}