import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.TokenStream;

import java.util.HashMap;
import java.util.Map;

public class MakeDeclaredNamesUniqueTest {

  private static class TestCompiler extends AbstractCompiler {
    @Override
    public CompilerPass getContextualRenameInverter() {
      return null;
    }
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUnique_constructor() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
   assertTrue(makeDeclaredNamesUnique instanceof NodeTraversal.ScopedCallback);
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUnique_enterScope() {
    String testCode = "function test() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  function testFunction(c) {" +
        "    var d = 3;" +
        "  }" +
        "  try {" +
        "    throw new Error('Test error')" +
        "  } catch (e) {" +
        "    var f = 4;" +
        "  }" +
        "}";

    CompilerOptions options = new CompilerOptions();
    TestCompiler compiler = new TestCompiler();
    Node node = NodeUtil.parseJs(testCode, options);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal.traverse(compiler, node, makeDeclaredNamesUnique);

    Scope globalScope = node.getScope();
    Iterator<Map.Entry<String, Var>> iterator = globalScope.iter();
    Map.Entry<String, Var> entry;

    // Test basic variable declaration
    entry = iterator.next();
    assertEquals("a", entry.getValue().getNameNode().getString());

    // Test function declaration
    entry = iterator.next();
    assertEquals("b", entry.getValue().getNameNode().getString());

    // Test function parameter declaration
    entry = iterator.next();
    assertEquals("c", entry.getValue().getNameNode().getString());

    // Test catch block variable declaration
    entry = iterator.next();
    assertEquals("e", entry.getValue().getNameNode().getString());

    // Test function local variable declaration
    entry = iterator.next();
    assertEquals("d", entry.getValue().getNameNode().getString());

    // Test empty scope
    assertNull(iterator.next());
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUnique_visit() {
    String testCode = "function test() {" +
        "  function testFunction() {" +
        "  }" +
        "  testFunction();" +
        "  testFunction();" +
        "}";

    CompilerOptions options = new CompilerOptions();
    TestCompiler compiler = new TestCompiler();
    Node node = NodeUtil.parseJs(testCode, options);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal.traverse(compiler, node, makeDeclaredNamesUnique);

    Node testFunctionNode = node.getChildRecursive("testFunction");
    Node testFunctionBody = testFunctionNode.getLastChild();

    assertEquals(0, testFunctionBody.getChildCount());
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUnique_forChildScope() {
    String testCode = "function test() {" +
        "  function testInnerFunction() {" +
        "    var a = 1;" +
        "  }" +
        "  testInnerFunction();" +
        "}";

    CompilerOptions options = new CompilerOptions();
    TestCompiler compiler = new TestCompiler();
    Node node = NodeUtil.parseJs(testCode, options);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    MakeDeclaredNamesUnique makeDeclaredNamesUniqueChild = makeDeclaredNamesUnique.forChildScope();

    NodeTraversal.traverse(compiler, node, makeDeclaredNamesUnique);
    Node testInnerFunctionNode = node.getChildRecursive("testInnerFunction");
    Node testInnerFunctionBody = testInnerFunctionNode.getLastChild();

    assertEquals(1, testInnerFunctionBody.getChildCount());
    Node aNode = testInnerFunctionBody.getFirstChild();
    assertEquals("a", aNode.getString());
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUnique_getReplacementName() {
    String testCode = "function test() {" +
        "  var a = 1;" +
        "  var a = 2;" +
        "}";

    CompilerOptions options = new CompilerOptions();
    TestCompiler compiler = new TestCompiler();
    Node node = NodeUtil.parseJs(testCode, options);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal.traverse(compiler, node, makeDeclaredNamesUnique);

    Scope scope = node.getScope();
    Iterator<Map.Entry<String, Var>> iterator = scope.iter();
    Map.Entry<String, Var> entry;

    // Test duplicate variable declaration
    entry = iterator.next();
    assertEquals("a_1", entry.getValue().getNameNode().getString());

    entry = iterator.next();
    assertEquals("a_2", entry.getValue().getNameNode().getString());

    // Test global variable declaration
    entry = iterator.next();
    assertEquals("a_3", entry.getValue().getNameNode().getString());
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUnique_stripConstIfReplaced() {
    String testCode = "function test() {" +
        "  const a = 1;" +
        "  const a = 2;" +
        "}";

    CompilerOptions options = new CompilerOptions();
    TestCompiler compiler = new TestCompiler();
    Node node = NodeUtil.parseJs(testCode, options);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal.traverse(compiler, node, makeDeclaredNamesUnique);

    Scope scope = node.getScope();
    Iterator<Map.Entry<String, Var>> iterator = scope.iter();
    Map.Entry<String, Var> entry;

    // Test duplicate const declaration
    entry = iterator.next();
    assertFalse(entry.getValue().getNameNode().hasProp(Node.IS_CONSTANT_NAME));

    entry = iterator.next();
    assertFalse(entry.getValue().getNameNode().hasProp(Node.IS_CONSTANT_NAME));

    // Test global const declaration
    entry = iterator.next();
    assertTrue(entry.getValue().getNameNode().hasProp(Node.IS_CONSTANT_NAME));
  }
}