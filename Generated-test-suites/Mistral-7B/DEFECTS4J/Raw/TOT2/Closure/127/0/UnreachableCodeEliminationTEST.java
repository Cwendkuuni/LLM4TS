import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.parser.JsParser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  private Compiler compiler;
  private String sourceCode;

  @Before
  public void setUp() {
    compiler = new Compiler();
    sourceCode = "function foo() {" +
                 "  if (true) {" +
                 "    return;" +
                 "  }" +
                 "  alert('unreachable');" +
                 "}";
  }

  @After
  public void tearDown() {
    compiler.dispose();
  }

  @Test
  public void testUnreachableCodeElimination() {
    Node ast = JsParser.parse(CompilerOptions.standard() // Use standard options.
        .withCompilerPass(new UnreachableCodeElimination(compiler, false)) // Add the pass.
        .parse(sourceCode));

    Node functionNode = NodeUtil.findFunctionByName(ast, "foo");
    assertTrue("Expected the function to be removed.", functionNode == null);
  }

  @Test
  public void testNoOpStatements() {
    sourceCode = "function foo() {" +
                "  alert('');" + // Empty alert.
                "  return;" +
                "}";

    Node ast = JsParser.parse(CompilerOptions.standard() // Use standard options.
        .withCompilerPass(new UnreachableCodeElimination(compiler, true)) // Add the pass.
        .parse(sourceCode));

    Node functionNode = NodeUtil.findFunctionByName(ast, "foo");
    assertFalse("Expected the function to remain.", functionNode == null);
  }

  @Test
  public void testControlFlowGraphReachability() {
    sourceCode = "function foo() {" +
                "  if (true) {" +
                "    return;" +
                "  }" +
                "  if (false) {" + // Dead if statement.
                "    return;" +
                "  }" +
                "}";

    Node ast = JsParser.parse(CompilerOptions.standard().parse(sourceCode));

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    NodeTraversal.traverse(compiler, ast.getFirstChild(), new NodeCallback() {
      @Override
      public void visit(NodeTraversal traversal, Node node) {
        if (node.isFunction()) {
          pass.process(null, node);
          return;
        }
        traversal.traverseChildren(node);
      }
    });

    Node functionNode = NodeUtil.findFunctionByName(ast, "foo");
    assertTrue("Expected the function to be removed.", functionNode == null);
  }

  @Test(expected = AssertionError.class)
  public void testExceptionInElseBlock() {
    sourceCode = "function foo() {" +
                "  try {" +
                "    if (false) {" +
                "      throw new Error('test');" +
                "    } else {" +
                "      return;" +
                "    }" +
                "  } catch (e) {}" +
                "}";

    Node ast = JsParser.parse(CompilerOptions.standard().parse(sourceCode));

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    NodeTraversal.traverse(compiler, ast.getFirstChild(), new NodeCallback() {
      @Override
      public void visit(NodeTraversal traversal, Node node) {
        if (node.isFunction()) {
          pass.process(null, node);
          return;
        }
        traversal.traverseChildren(node);
      }
    });

    Node functionNode = NodeUtil.findFunctionByName(ast, "foo");
    Node elseNode = NodeUtil.findElseNode(functionNode);
    assertTrue("Expected the else block to be removed.", elseNode == null);
  }
}