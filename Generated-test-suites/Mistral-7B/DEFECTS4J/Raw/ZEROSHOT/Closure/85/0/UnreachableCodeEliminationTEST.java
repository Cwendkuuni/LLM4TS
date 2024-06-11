import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.AbstractPostOrderCallbackType;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.ScopedCallbackType;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ScopedCallback;

import java.util.List;
import java.util.logging.Level;

public class UnreachableCodeEliminationTest {

  private static class TestCompiler extends AbstractCompiler {
    @Override
    public List<CompilerPass> getPasses() {
      return ImmutableList.of(new UnreachableCodeElimination(this, true));
    }

    @Override
    public void emitCode(Node externs, Node root, Compiler compiler) {}

    @Override
    public void process(Node externs, Node root) {
      NodeTraversal.traverse(this, root, new NodeTraversal.TraverseContext());
    }
  }

  @org.junit.Test
  public void testUnreachableCodeElimination() {
    Compiler compiler = new Compiler();
    TestCompiler testCompiler = new TestCompiler();

    String sourceCode =
        "function test() {\n"
            + "if (true) {\n"
            + "  return;\n"
            + "  alert('unreachable');\n"
            + "}\n"
            + "}";

    Node root = abstractParse(sourceCode, null);
    testCompiler.process(null, root);

    NodeTraversal nodeTraversal = new NodeTraversal(testCompiler);
    nodeTraversal.traverse(testCompiler.getRootNode(), new TestVisitor());

    assertTrue(testCompiler.getRootNode().getLastChild().isDead());
  }

  @org.junit.Test
  public void testRemoveNoOpStatements() {
    Compiler compiler = new Compiler();
    TestCompiler testCompiler = new TestCompiler();

    String sourceCode =
        "function test() {\n"
            + "var a = {\n"
            + "  b: {\n"
            + "    MyClass: {\n"
            + "      prototype: {\n"
            + "        propertyName: 42\n"
            + "      }\n"
            + "    }\n"
            + "  }\n"
            + "};\n"
            + "return a.b.MyClass.prototype.propertyName;\n"
            + "}";

    Node root = abstractParse(sourceCode, null);
    testCompiler.process(null, root);

    NodeTraversal nodeTraversal = new NodeTraversal(testCompiler);
    nodeTraversal.traverse(testCompiler.getRootNode(), new TestVisitor());

    assertFalse(testCompiler.getRootNode().getLastChild().isDead());
  }

  @org.junit.Test
  public void testRemoveUnconditionalBranch() {
    Compiler compiler = new Compiler();
    TestCompiler testCompiler = new TestCompiler();

    String sourceCode =
        "function test() {\n"
            + "if (true) {\n"
            + "  break;\n"
            + "}\n"
            + "break;\n"
            + "}";

    Node root = abstractParse(sourceCode, null);
    testCompiler.process(null, root);

    NodeTraversal nodeTraversal = new NodeTraversal(testCompiler);
    nodeTraversal.traverse(testCompiler.getRootNode(), new TestVisitor());

    Node breakStatement1 = testCompiler.getRootNode().getFirstChild().getNext();
    Node breakStatement2 = breakStatement1.getNext();

    assertFalse(breakStatement1.isDead());
    assertFalse(breakStatement2.isDead());
  }

  @org.junit.Test
  public void testRemoveUnreachableFunction() {
    Compiler compiler = new Compiler();
    TestCompiler testCompiler = new TestCompiler();

    String sourceCode =
        "function test() {\n"
            + "function innerFunction() {\n"
            + "  return;\n"
            + "}\n"
            + "innerFunction();\n"
            + "}";

    Node root = abstractParse(sourceCode, null);
    testCompiler.process(null, root);

    NodeTraversal nodeTraversal = new NodeTraversal(testCompiler);
    nodeTraversal.traverse(testCompiler.getRootNode(), new TestVisitor());

    assertTrue(testCompiler.getRootNode().getFirstChild().isDead());
  }

  @org.junit.Test
  public void testRemoveUnreachableIfStatement() {
    Compiler compiler = new Compiler();
    TestCompiler testCompiler = new TestCompiler();

    String sourceCode =
        "function test() {\n"
            + "if (false) {\n"
            + "  return;\n"
            + "}\n"
            + "}";

    Node root = abstractParse(sourceCode, null);
    testCompiler.process(null, root);

    NodeTraversal nodeTraversal = new NodeTraversal(testCompiler);
    nodeTraversal.traverse(testCompiler.getRootNode(), new TestVisitor());

    assertTrue(testCompiler.getRootNode().isDead());
  }

  @org.junit.Test
  public void testRemoveUnreachableIfStatementWithSideEffects() {
    Compiler compiler = new Compiler();
    TestCompiler testCompiler = new TestCompiler();

    String sourceCode =
        "function test() {\n"
            + "if (false) {\n"
            + "  document.write('hello');\n"
            + "}\n"
            + "}";

    Node root = abstractParse(sourceCode, null);
    testCompiler.process(null, root);

    NodeTraversal nodeTraversal = new NodeTraversal(testCompiler);
    nodeTraversal.traverse(testCompiler.getRootNode(), new TestVisitor());

    assertFalse(testCompiler.getRootNode().isDead());
  }

  @org.junit.Test
  public void testRemoveUnreachableReturnStatement() {
    Compiler compiler = new Compiler();
    TestCompiler testCompiler = new TestCompiler();

    String sourceCode =
        "function test() {\n"
            + "return;\n"
            + "}";

    Node root = abstractParse(sourceCode, null);
    testCompiler.process(null, root);

    NodeTraversal nodeTraversal = new NodeTraversal(testCompiler);
    nodeTraversal.traverse(testCompiler.getRootNode(), new TestVisitor());

    assertTrue(testCompiler.getRootNode().isDead());
  }

  @org.junit.Test
  public void testRemoveUnreachableReturnStatementWithSideEffects() {
    Compiler compiler = new Compiler();
    TestCompiler testCompiler = new TestCompiler();

    String sourceCode =
        "function test() {\n"
            + "document.write('hello');\n"
            + "return;\n"
            + "}";

    Node root = abstractParse(sourceCode, null);
    testCompiler.process(null, root);

    NodeTraversal nodeTraversal = new NodeTraversal(testCompiler);
    nodeTraversal.traverse(testCompiler.getRootNode(), new TestVisitor());

    assertFalse(testCompiler.getRootNode().isDead());
  }

  @org.junit.Test
  public void testRemoveUnreachableScript() {
    Compiler compiler = new Compiler();
    TestCompiler testCompiler = new TestCompiler();

    String sourceCode =
        "function test() {\n"
            + "}\n"
            + "script src=\"example.js\";";

    Node root = abstractParse(sourceCode, null);
    testCompiler.process(null, root);

    NodeTraversal nodeTraversal = new NodeTraversal(testCompiler);
    nodeTraversal.traverse(testCompiler.getRootNode(), new TestVisitor());

    assertTrue(root.getLastChild().isDead());
  }

  @org.junit.Test
  public void testRemoveUnreachableScriptWithSideEffects() {
    Compiler compiler = new Compiler();
    TestCompiler testCompiler = new TestCompiler();

    String sourceCode =
        "function test() {\n"
            + "document.write('hello');\n"
            + "}\n"
            + "script src=\"example.js\";";

    Node root = abstractParse(sourceCode, null);
    testCompiler.process(null, root);

    NodeTraversal nodeTraversal = new NodeTraversal(testCompiler);
    nodeTraversal.traverse(testCompiler.getRootNode(), new TestVisitor());

    assertFalse(root.isDead());
  }

  @org.junit.Test
  public void testRemoveUnreachableTryCatchBlock() {
    Compiler compiler = new Compiler();
    TestCompiler testCompiler = new TestCompiler();

    String sourceCode =
        "function test() {\n"
            + "try {\n"
            + "  return;\n"
            + "} finally {\n"
            + "  alert('unreachable');\n"
            + "}\n"
            + "}";

    Node root = abstractParse(sourceCode, null);
    testCompiler.process(null, root);

    NodeTraversal nodeTraversal = new NodeTraversal(testCompiler);
    nodeTraversal.traverse(testCompiler.getRootNode(), new TestVisitor());

    assertTrue(testCompiler.getRootNode().getLastChild().isDead());
  }

  @org.junit.Test
  public void testTryRemoveUnconditionalBranching() {
    Compiler compiler = new Compiler();
    TestCompiler testCompiler = new TestCompiler();

    String sourceCode =
        "function test() {\n"
            + "switch (0) {\n"
            + "  case 1:\n"
            + "    break;\n"
            + "  case 1:\n"
            + "    break;\n"
            + "  default:\n"
            + "}\n"
            + "}";

    Node root = abstractParse(sourceCode, null);
    testCompiler.process(null, root);

    NodeTraversal nodeTraversal = new NodeTraversal(testCompiler);
    nodeTraversal.traverse(testCompiler.getRootNode(), new TestVisitor());

    Node breakStatement1 = testCompiler.getRootNode().getFirstChild().getNext();
    Node breakStatement2 = breakStatement1.getNext();
    Node defaultStatement = breakStatement2.getNext();

    assertFalse(breakStatement1.isDead());
    assertFalse(breakStatement2.isDead());
    assertFalse(defaultStatement.isDead());
  }

  private static Node abstractParse(String sourceCode, Node root) {
    AbstractParserAbstract parser = new AbstractParserAbstract();
    Node abstractAST = parser.parseAbstract(sourceCode, root);
    return abstractAST;
  }

  private static class TestVisitor extends NodeTraversal.AbstractPostOrderCallback
      implements ScopedCallback {

    @Override
    public AbstractPostOrderCallbackType getType() {
      return AbstractPostOrderCallbackType.SIDE_EFFECTS;
    }

    @Override
    public ScopedCallbackType getScopedType() {
      return ScopedCallbackType.NONE;
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isDead()) {
        System.out.println("Found dead node: " + n.toString());
      }
    }
  }
}