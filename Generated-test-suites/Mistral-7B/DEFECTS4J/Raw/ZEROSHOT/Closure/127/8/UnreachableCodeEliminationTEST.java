import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractShallowCallback;
import com.google.javascript.jscomp.NodeTraversal.FunctionCallback;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphEdge;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphNode;
import com.google.javascript.jscomp.graph.GraphReachability;
import com.google.javascript.rhino.Token;

public class UnreachableCodeEliminationTest {

  private static final Logger logger = Logger.getLogger(
      UnreachableCodeEliminationTest.class.getName());

  @Test
  public void testUnreachableCodeElimination() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(
        new AbstractCompiler() {}, true);
    CompilerPass pass = unreachableCodeElimination;

    String sourceCode = "function test() {\n"
        + "  if (false) {\n"
        + "    return; // return statement\n"
        + "    alert('unreachable'); // unreachable code\n"
        + "  }\n"
        + "  if (true) {\n"
        + "    return; // return statement\n"
        + "  }\n"
        + "  alert('reachable'); // reachable code\n"
        + "}";

    Node root = AbstractCompiler.parseAndCompile(sourceCode, null, null).getRootNode();

    pass.process(null, root);

    // Check that unreachable code has been removed.
    Node testFunction = root.getFirstChild();
    List<Node> testFunctionBody = Arrays.asList(testFunction.getChildren());
    assertFalse("Unreachable code should not be present.",
        testFunctionBody.contains(AbstractCompiler.findDescendantByType(
            testFunction, Token.ALERT)));

    // Check that return statement after unreachable code has been removed.
    assertTrue("Return statement after unreachable code should be removed.",
        testFunctionBody.get(1).getChildAt(0).getType() == Token.RETURN);

    // Check that reachable code remains.
    assertTrue("Reachable code should remain.",
        testFunctionBody.get(2).isIdent("alert"));
  }

  @Test
  public void testRemoveNoOpStatements() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(
        new AbstractCompiler() {}, true);
    CompilerPass pass = unreachableCodeElimination;

    String sourceCode = "function test() {\n"
        + "  if (false) {\n"
        + "    alert('unreachable'); // unreachable code\n"
        + "  }\n"
        + "  if (true) {\n"
        + "    return; // return statement\n"
        + "  }\n"
        + "  if (true) {\n"
        + "    return; // return statement\n"
        + "  } else {\n"
        + "    return; // return statement\n"
        + "  };\n"
        + "  alert('reachable'); // reachable code\n"
        + "}";

    Node root = AbstractCompiler.parseAndCompile(sourceCode, null, null).getRootNode();

    pass.process(null, root);

    // Check that unreachable code has been removed.
    Node testFunction = root.getFirstChild();
    List<Node> testFunctionBody = Arrays.asList(testFunction.getChildren());
    assertFalse("Unreachable code should not be present.",
        testFunctionBody.contains(AbstractCompiler.findDescendantByType(
            testFunction, Token.ALERT)));

    // Check that return statements after unreachable code have been removed.
    assertTrue("Return statements after unreachable code should be removed.",
        testFunctionBody.get(2).getChildAt(0).getType() == Token.IF);

    // Check that reachable code remains.
    assertTrue("Reachable code should remain.",
        testFunctionBody.get(3).isIdent("alert"));
  }

  @Test
  public void testRemoveUnconditionalBranching() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(
        new AbstractCompiler() {}, true);
    CompilerPass pass = unreachableCodeElimination;

    String sourceCode = "function test() {\n"
        + "  if (true) {\n"
        + "    break; // unconditional break\n"
        + "  }\n"
        + "  if (true) {\n"
        + "    break; // unconditional break\n"
        + "  }\n"
        + "}";

    Node root = AbstractCompiler.parseAndCompile(sourceCode, null, null).getRootNode();

    pass.process(null, root);

    // Check that unconditional breaks have been removed.
    Node testFunction = root.getFirstChild();
    List<Node> testFunctionBody = Arrays.asList(testFunction.getChildren());
    assertTrue("Unconditional breaks should be removed.",
        testFunctionBody.get(1).isScript());

    // Check that no additional changes have been made.
    assertTrue("No additional changes should have been made.",
        testFunctionBody.get(1).getNumChildren() == 0);
  }

  @Test
  public void testRemoveUnconditionalReturn() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(
        new AbstractCompiler() {}, true);
    CompilerPass pass = unreachableCodeElimination;

    String sourceCode = "function test() {\n"
        + "  if (true) {\n"
        + "    return; // unconditional return\n"
        + "  }\n"
        + "  return; // reachable return\n"
        + "}";

    Node root = AbstractCompiler.parseAndCompile(sourceCode, null, null).getRootNode();

    pass.process(null, root);

    // Check that unconditional return has been removed.
    Node testFunction = root.getFirstChild();
    List<Node> testFunctionBody = Arrays.asList(testFunction.getChildren());
    assertTrue("Unconditional return should be removed.",
        testFunctionBody.get(0).isScript());

    // Check that reachable return remains.
    assertTrue("Reachable return should remain.",
        testFunctionBody.get(1).isIdent("return"));
  }

  @Test
  public void testRemoveFunctionCallWithNoSideEffects() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(
        new AbstractCompiler() {}, true);
    CompilerPass pass = unreachableCodeElimination;

    String sourceCode = "function test() {\n"
        + "  if (true) {\n"
        + "    a.b.MyClass.prototype.propertyName; // function call with no side effects\n"
        + "  }\n"
        + "  return; // reachable return\n"
        + "}";

    Node root = AbstractCompiler.parseAndCompile(sourceCode, null, null).getRootNode();

    pass.process(null, root);

    // Check that function call with no side effects has been removed.
    Node testFunction = root.getFirstChild();
    List<Node> testFunctionBody = Arrays.asList(testFunction.getChildren());
    assertTrue("Function call with no side effects should be removed.",
        testFunctionBody.get(0).isScript());

    // Check that reachable return remains.
    assertTrue("Reachable return should remain.",
        testFunctionBody.get(1).isIdent("return"));
  }

  @Test
  public void testRemoveEmptyBlock() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(
        new AbstractCompiler() {}, true);
    CompilerPass pass = unreachableCodeElimination;

    String sourceCode = "function test() {\n"
        + "  if (true) {\n"
        + "    {\n"
        + "    }\n"
        + "  }\n"
        + "  return; // reachable return\n"
        + "}";

    Node root = AbstractCompiler.parseAndCompile(sourceCode, null, null).getRootNode();

    pass.process(null, root);

    // Check that empty block has been removed.
    Node testFunction = root.getFirstChild();
    List<Node> testFunctionBody = Arrays.asList(testFunction.getChildren());
    assertTrue("Empty block should be removed.",
        testFunctionBody.get(0).isScript());

    // Check that reachable return remains.
    assertTrue("Reachable return should remain.",
        testFunctionBody.get(1).isIdent("return"));
  }

  @Test
  public void testDoLoop() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(
        new AbstractCompiler() {}, true);
    CompilerPass pass = unreachableCodeElimination;

    String sourceCode = "function test() {\n"
        + "  do {\n"
        + "  } while (false); // unreachable do-while loop\n"
        + "  return; // reachable return\n"
        + "}";

    Node root = AbstractCompiler.parseAndCompile(sourceCode, null, null).getRootNode();

    pass.process(null, root);

    // Check that unreachable do-while loop has been removed.
    Node testFunction = root.getFirstChild();
    List<Node> testFunctionBody = Arrays.asList(testFunction.getChildren());
    assertTrue("Unreachable do-while loop should be removed.",
        testFunctionBody.get(0).isScript());

    // Check that reachable return remains.
    assertTrue("Reachable return should remain.",
        testFunctionBody.get(1).isIdent("return"));
  }

  @Test
  public void testCatchBlock() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(
        new AbstractCompiler() {}, true);
    CompilerPass pass = unreachableCodeElimination;

    String sourceCode = "function test() {\n"
        + "  try {\n"
        + "    throw new Error('unreachable'); // unreachable error\n"
        + "  } catch (e) {\n"
        + "    return; // reachable return\n"
        + "  }\n"
        + "}";

    Node root = AbstractCompiler.parseAndCompile(sourceCode, null, null).getRootNode();

    pass.process(null, root);

    // Check that unreachable error has been ignored.
    Node testFunction = root.getFirstChild();
    List<Node> testFunctionBody = Arrays.asList(testFunction.getChildren());
    assertTrue("Unreachable error should be ignored.",
        testFunctionBody.get(0).isIdent("try"));

    // Check that reachable return remains.
    assertTrue("Reachable return should remain.",
        testFunctionBody.get(1).isIdent("return"));
  }
}