import static org.junit.Assert.*;
import static com.google.javascript.jscomp.testutils.TestUtils.*;

import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class MustBeReachingVariableDefTest {

  @Test
  public void testSimpleVariableAssignment() {
    String code = "function foo() {" +
        "  var x = 1;" +
        "  var y = x;" +
        "  return y;" +
        "}";

    Scope jsScope = createScopeForTests();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(createAstForTests(code), jsScope, compiler);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node useNode = analysis.getCfg().getNode(createNodeForTests(cfg, "return")).getPrevious();
    Node defNode = analysis.getDef("x", useNode);
    assertEquals(analysis.getCfg().getNode(createNodeForTests(cfg, "x=1")), defNode);
  }

  @Test
  public void testMultipleVariableAssignment() {
    String code = "function foo() {" +
        "  var x = 1;" +
        "  var y = x;" +
        "  var z = y;" +
        "  return z;" +
        "}";

    Scope jsScope = createScopeForTests();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(createAstForTests(code), jsScope, compiler);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node useNode = analysis.getCfg().getNode(createNodeForTests(cfg, "return")).getPrevious();
    Node defNodeX = analysis.getDef("x", useNode);
    Node defNodeY = analysis.getDef("y", useNode);
    Node defNodeZ = analysis.getDef("z", useNode);

    assertEquals(analysis.getCfg().getNode(createNodeForTests(cfg, "x=1")), defNodeX);
    assertEquals(analysis.getCfg().getNode(createNodeForTests(cfg, "x=1")), defNodeY);
    assertEquals(analysis.getCfg().getNode(createNodeForTests(cfg, "x=1")), defNodeZ);
  }

  @Test
  public void testVariableAssignmentWithCondition() {
    String code = "function foo() {" +
        "  var x = 1;" +
        "  if (true) {" +
        "    var y = 2;" +
        "  } else {" +
        "    var y = x;" +
        "  }" +
        "  return y;" +
        "}";

    Scope jsScope = createScopeForTests();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(createAstForTests(code), jsScope, compiler);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node useNode = analysis.getCfg().getNode(createNodeForTests(cfg, "return")).getPrevious();
    Node defNodeX = analysis.getDef("x", useNode);
    Node defNodeY = analysis.getDef("y", useNode);

    assertEquals(analysis.getCfg().getNode(createNodeForTests(cfg, "x=1")), defNodeX);

    assertTrue(analysis.dependsOnOuterScopeVars("y", useNode));
  }

  @Test
  public void testVariableAssignmentWithLoop() {
    String code = "function foo() {" +
        "  var sum = 0;" +
        "  for (var i = 0; i < 10; i++) {" +
        "    sum += i;" +
        "  }" +
        "  return sum;" +
        "}";

    Scope jsScope = createScopeForTests();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(createAstForTests(code), jsScope, compiler);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node useNode = analysis.getCfg().getNode(createNodeForTests(cfg, "return")).getPrevious();
    Node defNodeSum = analysis.getDef("sum", useNode);
    Node defNodeI = analysis.getDef("i", useNode);

    assertEquals(analysis.getCfg().getNode(createNodeForTests(cfg, "sum=0")), defNodeSum);

    // i is not defined in the current scope, but it's defined in the loop's scope
    assertTrue(analysis.dependsOnOuterScopeVars("i", useNode));
  }

  /**
   * Test the exception handling scenario.
   */
  @Test
  public void testVariableAssignmentWithException() {
    String code = "function foo() {" +
        "  try {" +
        "    var x = 1;" +
        "    throw new Error(\"Exception\");" +
        "  } catch (e) {" +
        "    var y = x;" +
        "  }" +
        "  return y;" +
        "}";

    Scope jsScope = createScopeForTests();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(createAstForTests(code), jsScope, compiler);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node useNode = analysis.getCfg().getNode(createNodeForTests(cfg, "return")).getPrevious();
    Node defNodeX = analysis.getDef("x", useNode);
    Node defNodeY = analysis.getDef("y", useNode);

    assertEquals(analysis.getCfg().getNode(createNodeForTests(cfg, "x=1")), defNodeX);
    assertEquals(analysis.getCfg().getNode(createNodeForTests(cfg, "x=1")), defNodeY);
  }

  @Test
  public void testVariableAssignmentWithFunctionCall() {
    String code = "function foo() {" +
        "  var x = 1;" +
        "  var sum = foo();" +
        "  return sum;" +
        "}" +
        "function bar() {" +
        "  return 2;" +
        "}";

    Scope jsScope = createScopeForTests();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(createAstForTests(code), jsScope, compiler);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node useNode = analysis.getCfg().getNode(createNodeForTests(cfg, "return")).getPrevious();
    Node defNodeX = analysis.getDef("x", useNode);
    Node defNodeSum = analysis.getDef("sum", useNode);

    assertEquals(analysis.getCfg().getNode(createNodeForTests(cfg, "x=1")), defNodeX);
    assertEquals(analysis.getCfg().getNode(createNodeForTests(cfg, "bar()")), defNodeSum);
  }

  @Test
  public void testVariableAssignmentWithFunctionCallAndCondition() {
    String code = "function foo() {" +
        "  var x = 1;" +
        "  if (true) {" +
        "    var sum = foo();" +
        "  } else {" +
        "    var sum = 2;" +
        "  }" +
        "  return sum;" +
        "}" +
        "function bar() {" +
        "  return 2;" +
        "}";

    Scope jsScope = createScopeForTests();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(createAstForTests(code), jsScope, compiler);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node useNode = analysis.getCfg().getNode(createNodeForTests(cfg, "return")).getPrevious();
    Node defNodeX = analysis.getDef("x", useNode);
    Node defNodeSum = analysis.getDef("sum", useNode);

    assertEquals(analysis.getCfg().getNode(createNodeForTests(cfg, "x=1")), defNodeX);

    assertTrue(analysis.dependsOnOuterScopeVars("sum", useNode));
  }

  @Test
  public void testVariableAssignmentWithFunctionCallAndLoop() {
    String code = "function foo() {" +
        "  var sum = 0;" +
        "  for (var i = 0; i < 10; i++) {" +
        "    sum += foo();" +
        "  }" +
        "  return sum;" +
        "}" +
        "function bar() {" +
        "  return 2;" +
        "}";

    Scope jsScope = createScopeForTests();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(createAstForTests(code), jsScope, compiler);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node useNode = analysis.getCfg().getNode(createNodeForTests(cfg, "return")).getPrevious();
    Node defNodeSum = analysis.getDef("sum", useNode);

    // The sum variable depends on the outer scope's foo() function.
    assertTrue(analysis.dependsOnOuterScopeVars("sum", useNode));
  }

  @Test
  public void testVariableAssignmentWithFunctionCallAndLoopAndCondition() {
    String code = "function foo() {" +
        "  var sum = 0;" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (true) {" +
        "      sum += foo();" +
        "    } else {" +
        "      sum += 2;" +
        "    }" +
        "  }" +
        "  return sum;" +
        "}" +
        "function bar() {" +
        "  return 2;" +
        "}";

    Scope jsScope = createScopeForTests();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(createAstForTests(code), jsScope, compiler);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node useNode = analysis.getCfg().getNode(createNodeForTests(cfg, "return")).getPrevious();
    Node defNodeSum = analysis.getDef("sum", useNode);

    // The sum variable depends on the outer scope's foo() function.
    assertTrue(analysis.dependsOnOuterScopeVars("sum", useNode));
  }

  @Test
  public void testVariableAssignmentWithFunctionCallAndConditionAndException() {
    String code = "function foo() {" +
        "  try {" +
        "    var sum = 0;" +
        "    for (var i = 0; i < 10; i++) {" +
        "      if (true) {" +
        "        sum += foo();" +
        "      } else {" +
        "        sum += 2;" +
        "      }" +
        "    }" +
        "    throw new Error(\"Exception\");" +
        "  } catch (e) {" +
        "    var sum = 10;" +
        "  }" +
        "  return sum;" +
        "}" +
        "function bar() {" +
        "  return 2;" +
        "}";

    Scope jsScope = createScopeForTests();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(createAstForTests(code), jsScope, compiler);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node useNode = analysis.getCfg().getNode(createNodeForTests(cfg, "return")).getPrevious();
    Node defNodeSum = analysis.getDef("sum", useNode);

    // The sum variable depends on the outer scope's foo() function.
    assertTrue(analysis.dependsOnOuterScopeVars("sum", useNode));
  }
}