import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerResults;
import com.google.javascript.jscomp.ErrorType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SemanticChecker;
import com.google.javascript.jscomp.TraversalType;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Set;

public class VarCheckTest {

  private static CompilerPass createVarCheck(boolean sanityCheck) {
    return new VarCheck(new Compiler(new CompilerOptions()));
  }

  @org.junit.Test
  public void testUndefinedVariable() {
    CompilerInput input = new CompilerInput(
        "test.js", null, "use strict",
        "function foo() {\n" +
        "  var x;\n" +
        "  x = 1;\n" +
        "}"
    );

    List<CompilerPass> passes = ImmutableList.of(createVarCheck(false));

    CompilerPassManager passManager = new CompilerPassManager(passes);
    CompilerResults results = passManager.runPasses(input, null);

    Node ast = results.getRootNode();

    NodeTraversal traversal = new NodeTraversal(ast, TraversalType.BREADTH_FIRST, null);
    SemanticChecker checker = new SemanticChecker(traversal, null);

    checker.check(input, results);

    Node varNode = traversal.getRoot().getFirstChild();
    assertFalse(varNode.hasProperty("declared"));
    assertTrue(varNode.hasSuppression("JSC_UNDEFINED_VARIABLE"));
    assertTrue(results.getDiagnostics().containsError(
        ErrorType.JSC_UNDEFINED_VARIABLE));
  }

  @org.junit.Test
  public void testDeclaredVariable() {
    CompilerInput input = new CompilerInput(
        "test.js", null, "use strict",
        "function foo() {\n" +
        "  var x;\n" +
        "  x = 1;\n" +
        "}"
    );

    List<CompilerPass> passes = ImmutableList.of(createVarCheck(true));

    CompilerPassManager passManager = new CompilerPassManager(passes);
    CompilerResults results = passManager.runPasses(input, null);

    Node ast = results.getRootNode();

    NodeTraversal traversal = new NodeTraversal(ast, TraversalType.BREADTH_FIRST, null);
    SemanticChecker checker = new SemanticChecker(traversal, null);

    checker.check(input, results);

    Node varNode = traversal.getRoot().getFirstChild();
    assertTrue(varNode.hasProperty("declared"));
    assertNull(varNode.getFirstChild().getFirstChild());
    assertFalse(varNode.hasSuppression("JSC_UNDEFINED_VARIABLE"));
    assertTrue(results.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testModuleDependency() {
    CompilerInput inputA = new CompilerInput(
        "moduleA.js", null, "use strict",
        "function foo() {\n" +
        "  var x = 1;\n" +
        "}"
    );

    CompilerInput inputB = new CompilerInput(
        "moduleB.js", inputA, "use strict",
        "function foo() {\n" +
        "  var x = 2;\n" +
        "}"
    );

    List<CompilerPass> passes = ImmutableList.of(createVarCheck(false));

    CompilerPassManager passManager = new CompilerPassManager(passes);
    CompilerResults results = passManager.runPasses(inputB, null);

    Node astB = results.getRootNode();

    NodeTraversal traversalB = new NodeTraversal(astB, TraversalType.BREADTH_FIRST, null);
    SemanticChecker checkerB = new SemanticChecker(traversalB, null);

    checkerB.check(inputB, results);

    Node varNodeB = traversalB.getRoot().getFirstChild();
    assertFalse(varNodeB.hasSuppression("JSC_UNDEFINED_VARIABLE"));
    assertFalse(varNodeB.hasSuppression("JSC_VIOLATED_MODULE_DEPENDENCY"));
    assertTrue(results.getDiagnostics().isEmpty());

    List<CompilerPass> passes2 = ImmutableList.of(createVarCheck(true));

    CompilerPassManager passManager2 = new CompilerPassManager(passes2);
    CompilerResults results2 = passManager2.runPasses(inputB, null);

    Node astBStrict = results2.getRootNode();

    NodeTraversal traversalBStrict = new NodeTraversal(astBStrict, TraversalType.BREADTH_FIRST, null);
    SemanticChecker checkerBStrict = new SemanticChecker(traversalBStrict, null);

    checkerBStrict.check(inputB, results2);

    Node varNodeBStrict = traversalBStrict.getRoot().getFirstChild();
    assertTrue(varNodeBStrict.hasSuppression("JSC_UNDEFINED_VARIABLE"));
    assertTrue(varNodeBStrict.hasSuppression("JSC_VIOLATED_MODULE_DEPENDENCY"));
    assertTrue(results2.getDiagnostics().containsError(
        ErrorType.JSC_VIOLATED_MODULE_DEPENDENCY));

    List<CompilerPass> passesC = ImmutableList.of(createVarCheck(false));

    CompilerPassManager passManagerC = new CompilerPassManager(passesC);
    CompilerResults resultsC = passManagerC.runPasses(inputA, null);

    Node astAC = resultsC.getRootNode();

    NodeTraversal traversalAC = new NodeTraversal(astAC, TraversalType.BREADTH_FIRST, null);
    SemanticChecker checkerAC = new SemanticChecker(traversalAC, null);

    checkerAC.check(inputA, resultsC);

    Node varNodeAC = traversalAC.getRoot().getFirstChild();
    assertFalse(varNodeAC.hasSuppression("JSC_UNDEFINED_VARIABLE"));
    assertFalse(varNodeAC.hasSuppression("JSC_VIOLATED_MODULE_DEPENDENCY"));
    assertTrue(resultsC.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testStrictModuleDependency() {
    CompilerInput inputA = new CompilerInput(
        "moduleA.js", null, "use strict",
        "function foo() {\n" +
        "  var x = 1;\n" +
        "}"
    );

    CompilerInput inputB = new CompilerInput(
        "moduleB.js", inputA, "use strict",
        "function foo() {\n" +
        "  var x = 2;\n" +
        "}"
    );

    List<CompilerPass> passes = ImmutableList.of(createVarCheck(false));

    CompilerPassManager passManager = new CompilerPassManager(passes);
    CompilerResults results = passManager.runPasses(inputB, null);

    Node astB = results.getRootNode();

    NodeTraversal traversalB = new NodeTraversal(astB, TraversalType.BREADTH_FIRST, null);
    SemanticChecker checkerB = new SemanticChecker(traversalB, null);

    checkerB.check(inputB, results);

    Node varNodeB = traversalB.getRoot().getFirstChild();
    assertFalse(varNodeB.hasSuppression("JSC_UNDEFINED_VARIABLE"));
    assertFalse(varNodeB.hasSuppression("JSC_VIOLATED_MODULE_DEPENDENCY"));
    assertFalse(varNodeB.hasSuppression("JSC_STRICT_MODULE_DEPENDENCY"));
    assertTrue(results.getDiagnostics().isEmpty());

    List<CompilerPass> passes2 = ImmutableList.of(createVarCheck(true));

    CompilerPassManager passManager2 = new CompilerPassManager(passes2);
    CompilerResults results2 = passManager2.runPasses(inputB, null);

    Node astBStrict = results2.getRootNode();

    NodeTraversal traversalBStrict = new NodeTraversal(astBStrict, TraversalType.BREADTH_FIRST, null);
    SemanticChecker checkerBStrict = new SemanticChecker(traversalBStrict, null);

    checkerBStrict.check(inputB, results2);

    Node varNodeBStrict = traversalBStrict.getRoot().getFirstChild();
    assertTrue(varNodeBStrict.hasSuppression("JSC_UNDEFINED_VARIABLE"));
    assertTrue(varNodeBStrict.hasSuppression("JSC_STRICT_MODULE_DEPENDENCY"));
    assertTrue(results2.getDiagnostics().isEmpty());

    List<CompilerPass> passesC = ImmutableList.of(createVarCheck(false));

    CompilerPassManager passManagerC = new CompilerPassManager(passesC);
    CompilerResults resultsC = passManagerC.runPasses(inputA, null);

    Node astAC = resultsC.getRootNode();

    NodeTraversal traversalAC = new NodeTraversal(astAC, TraversalType.BREADTH_FIRST, null);
    SemanticChecker checkerAC = new SemanticChecker(traversalAC, null);

    checkerAC.check(inputA, resultsC);

    Node varNodeAC = traversalAC.getRoot().getFirstChild();
    assertFalse(varNodeAC.hasSuppression("JSC_UNDEFINED_VARIABLE"));
    assertFalse(varNodeAC.hasSuppression("JSC_VIOLATED_MODULE_DEPENDENCY"));
    assertFalse(varNodeAC.hasSuppression("JSC_STRICT_MODULE_DEPENDENCY"));
    assertTrue(resultsC.getDiagnostics().isEmpty());

    // Test that strict extern checks only emit warnings
    List<CompilerPass> passesD = ImmutableList.of(createVarCheck(false), createVarCheck(true));

    CompilerPassManager passManagerD = new CompilerPassManager(passesD);
    CompilerResults resultsD = passManagerD.runPasses(inputB, null);

    Node astBD = resultsD.getRootNode();

    NodeTraversal traversalBD = new NodeTraversal(astBD, TraversalType.BREADTH_FIRST, null);
    SemanticChecker checkerBD = new SemanticChecker(traversalBD, null);

    checkerBD.check(inputB, resultsD);

    Node varNodeBD = traversalBD.getRoot().getFirstChild();
    assertFalse(varNodeBD.hasSuppression("JSC_UNDEFINED_VARIABLE"));
    assertFalse(varNodeBD.hasSuppression("JSC_VIOLATED_MODULE_DEPENDENCY"));
    assertTrue(varNodeBD.hasSuppression("JSC_STRICT_MODULE_DEPENDENCY"));
    assertTrue(resultsD.getDiagnostics().containsWarning(
        ErrorType.JSC_STRICT_MODULE_DEPENDENCY));
  }

  @org.junit.Test
  public void testNameReferenceInExterns() {
    CompilerInput input = new CompilerInput(
        "test.js", null, "use strict",
        "var x = 1;\n" +
        "function foo() {\n" +
        "  var y = x;\n" +
        "}"
    );

    List<CompilerPass> passes = ImmutableList.of(createVarCheck(false));

    CompilerPassManager passManager = new CompilerPassManager(passes);
    CompilerResults results = passManager.runPasses(input, null);

    Node ast = results.getRootNode();

    NodeTraversal traversal = new NodeTraversal(ast, TraversalType.BREADTH_FIRST, null);
    SemanticChecker checker = new SemanticChecker(traversal, null);

    checker.check(input, results);

    Node functionNode = traversal.getFirstChildBelowType(ast, Token.FUNCTION);

    Node varNodeX = traversal.getFirstChildBelowType(ast, Token.VAR);
    Node varNodeY = traversal.getFirstChildBelow(functionNode, Token.VAR);

    assertFalse(varNodeX.hasSuppression("JSC_NAME_REFERENCE_IN_EXTERNS"));
    assertFalse(varNodeY.hasSuppression("JSC_NAME_REFERENCE_IN_EXTERNS"));
    assertTrue(results.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testUndefinedExternVariable() {
    CompilerInput input = new CompilerInput(
        "test.js", null, "use strict",
        "function foo() {\n" +
        "  function bar() {\n" +
        "    var x = y;\n" +
        "  }\n" +
        "}"
    );

    List<CompilerPass> passes = ImmutableList.of(createVarCheck(false));

    CompilerPassManager passManager = new CompilerPassManager(passes);
    CompilerResults results = passManager.runPasses(input, null);

    Node ast = results.getRootNode();

    NodeTraversal traversal = new NodeTraversal(ast, TraversalType.BREADTH_FIRST, null);
    SemanticChecker checker = new SemanticChecker(traversal, null);

    checker.check(input, results);

    Node functionNode = traversal.getFirstChildBelowType(ast, Token.FUNCTION);
    Node functionNodeBar = traversal.getFirstChildBelowType(functionNode, Token.FUNCTION);
    Node varNodeY = traversal.getFirstChildBelow(functionNodeBar, Token.VAR);

    assertFalse(varNodeY.hasSuppression("JSC_UNDEFINED_EXTERN_VAR_ERROR"));
    assertTrue(results.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testDeclaredExternVariable() {
    CompilerInput input = new CompilerInput(
        "test.js", null, "use strict",
        "function foo() {\n" +
        "  function bar() {\n" +
        "    var x = y;\n" +
        "  }\n" +
        "}"
    );

    List<CompilerPass> passes = ImmutableList.of(createVarCheck(true));

    CompilerPassManager passManager = new CompilerPassManager(passes);
    CompilerResults results = passManager.runPasses(input, null);

    Node ast = results.getRootNode();

    NodeTraversal traversal = new NodeTraversal(ast, TraversalType.BREADTH_FIRST, null);
    SemanticChecker checker = new SemanticChecker(traversal, null);

    checker.check(input, results);

    Node functionNode = traversal.getFirstChildBelowType(ast, Token.FUNCTION);
    Node functionNodeBar = traversal.getFirstChildBelowType(functionNode, Token.FUNCTION);
    Node varNodeY = traversal.getFirstChildBelow(functionNodeBar, Token.VAR);

    assertFalse(varNodeY.hasSuppression("JSC_UNDEFINED_EXTERN_VAR_ERROR"));
    assertFalse(results.getDiagnostics().containsError(
        ErrorType.JSC_UNDEFINED_EXTERN_VAR_ERROR));
  }
}