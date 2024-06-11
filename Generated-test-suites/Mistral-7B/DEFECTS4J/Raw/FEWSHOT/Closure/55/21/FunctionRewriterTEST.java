package org.apache.commons.cli.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.FunctionRewriter.Reducer;
import com.google.javascript.jscomp.FunctionRewriter.Reduction;
import com.google.javascript.jscomp.FunctionRewriter.ReductionGatherer;
import com.google.javascript.jscomp.FunctionRewriter.ReturnConstantReducer;
import com.google.javascript.jscomp.FunctionRewriter.IdentityReducer;
import com.google.javascript.jscomp.FunctionRewriter.EmptyFunctionReducer;
import com.google.javascript.jscomp.FunctionRewriter.SingleReturnStatementReducer;
import com.google.javascript.jscomp.FunctionRewriter.GetterReducer;
import com.google.javascript.jscomp.FunctionRewriter.SetterReducer;

@RunWith(Parameterized.class)
public class FunctionRewriterTest {

  private static final Class<?> REDUCER_CLASSES[] = new Class<?>[] {
    EmptyFunctionReducer.class,
    IdentityReducer.class,
    ReturnConstantReducer.class,
    GetterReducer.class,
    SetterReducer.class
  };

  private final String functionSource;
  private final int expectedCodeSize;
  private final Class<? extends Reducer> reducerType;

  public FunctionRewriterTest(String functionSource, int expectedCodeSize,
                             Class<? extends Reducer> reducerType) {
    this.functionSource = functionSource;
    this.expectedCodeSize = functionSource.split("\\n").length;
    this.reducerType = reducerType;
  }

  @Parameters(name = "{index}: reducer={0}")
  public static Iterable<Object[]> data() {
    return java.util.Arrays.asList(new Object[][]{
      new Object[]{"function() {return 1;}", 2, EmptyFunctionReducer.class},
      new Object[]{"function(x) {return x;}", 2, IdentityReducer.class},
      new Object[]{"function() {return 10;}", 2, ReturnConstantReducer.class},
      new Object[]{"function getB() {return this.b_;}", 1, GetterReducer.class},
      new Object[]{"function setB(value) {this.b_ = value;}", 1, SetterReducer.class},
      new Object[]{"function getEmptyFunction() {return function() {};}", 2,
          EmptyFunctionReducer.class},
      new Object[]{"function identityFunction(value) {return value;}", 2,
          IdentityReducer.class},
      new Object[]{"function returnConstant(value) {return value;}", 2,
          ReturnConstantReducer.class},
      new Object[]{"function getter(name) {return function() {return this[name]};}",
          2, GetterReducer.class},
      new Object[]{"function setter(name) {return function(value) {this[name] = value};}", 2,
          SetterReducer.class},
    });
  }

  @Test
  public void testFunctionRewriting() {
    Compiler compiler = new Compiler();
    Node root = compiler.parse(functionSource);

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node reducedFunction = root.getFirstChild();
    int actualCodeSize = reducedFunction.toString().split("\\n").length;
    assertEquals("Expected function to be reduced to " + expectedCodeSize +
                 " lines.", expectedCodeSize, actualCodeSize);

    if (reducerType != null) {
      Reducer reducerInstance = null;
      try {
        reducerInstance = reducerType.newInstance();
      } catch (InstantiationException e) {
        throw new RuntimeException(e);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }

      Reduction reduction = new Reduction(reducedFunction, null, null);
      Node helperCode = reducerInstance.parseHelperCode(reducerInstance);
      assertNotNull("Expected helper code to be generated.", helperCode);
      int helperCodeCost = InlineCostEstimator.getCost(helperCode);
      int savings = reduction.estimateSavings();
      int totalSavings = savings * reducerInstance.reductions().size();

      int estimatedSavings = helperCodeCost + savings;
      if (totalSavings < estimatedSavings) {
        reducerInstance.applyReduction(reduction);
        Node reducedRoot = compiler.getNodeForCodeInsertion(null);
        reducedRoot.addChildrenToFront(helperCode);
        compiler.reportCodeChange();

        String reducedCode = reducedRoot.toString();
        int actualReducedCodeSize = reducedCode.split("\\n").length;
        assertEquals("Expected function to be reduced to " + actualReducedCodeSize +
                     " lines after reduction.", expectedCodeSize,
                     actualReducedCodeSize);
      }
    }
  }

  @Test
  public void testNoReduction() {
    Compiler compiler = new Compiler();
    Node root = compiler.parse("function() {};");

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node reducedFunction = root.getFirstChild();
    assertEquals("Expected function to remain unchanged.", 1,
                 reducedFunction.toString().split("\\n").length);
  }
}