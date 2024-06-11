import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.FunctionRewriter.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.HashMultimap;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;

public class FunctionRewriterTest {
  private AbstractCompiler compiler;
  private Node root;
  private FunctionRewriter functionRewriter;

  @org.junit.Before
  public void setUp() {
    compiler = new CompilerOptions();
    root = compiler.parseTree("test.js", "sourceCode");
    functionRewriter = new FunctionRewriter(compiler);
  }

  @org.junit.Test
  public void testEmptyFunctionReduction() {
    List<Reducer> reducers = ImmutableList.of(new EmptyFunctionReducer());
    Multimap<Reducer, Reduction> reductionMap = HashMultimap.create();

    NodeTraversal.traverse(compiler, root,
                           new ReductionGatherer(reducers, reductionMap));

    FunctionRewriter.parseHelperCode(EmptyFunctionReducer.class);

    int savings = 0;
    for (Reducer reducer : reducers) {
      Collection<Reduction> reductions = reductionMap.get(reducer);
      for (Reduction reduction : reductions) {
        savings += reduction.estimateSavings();
        reduction.apply();
      }
    }

    Node addedHelper = compiler.getNodeForCodeInsertion(null);
    Node helperCode = functionRewriter.parseHelperCode(EmptyFunctionReducer.class);
    addedHelper.addChildrenToFront(helperCode);
    compiler.reportCodeChange();

    int helperCost = InlineCostEstimator.getCost(helperCode);
    int estimatedSavings = savings - helperCost;

    assertTrue("Estimated savings is less than helper cost",
               estimatedSavings > 0);
  }

  @org.junit.Test
  public void testIdentityFunctionReduction() {
    List<Reducer> reducers = ImmutableList.of(new IdentityReducer());
    Multimap<Reducer, Reduction> reductionMap = HashMultimap.create();

    NodeTraversal.traverse(compiler, root,
                           new ReductionGatherer(reducers, reductionMap));

    FunctionRewriter.parseHelperCode(IdentityReducer.class);

    int savings = 0;
    for (Reducer reducer : reducers) {
      Collection<Reduction> reductions = reductionMap.get(reducer);
      for (Reduction reduction : reductions) {
        savings += reduction.estimateSavings();
        reduction.apply();
      }
    }

    Node addedHelper = compiler.getNodeForCodeInsertion(null);
    Node helperCode = functionRewriter.parseHelperCode(IdentityReducer.class);
    addedHelper.addChildrenToFront(helperCode);
    compiler.reportCodeChange();

    int helperCost = InlineCostEstimator.getCost(helperCode);
    int estimatedSavings = savings - helperCost;

    assertTrue("Estimated savings is less than helper cost",
               estimatedSavings > 0);

    Node identityFunction = root.getFirstChild().getNext();
    Node identityFunctionReduced = functionRewriter.reduce(identityFunction);

    Node expectedCall = new Node(Token.CALL, identityFunction.getLineno(),
                                identityFunction.getCharno());
    expectedCall.putBooleanProp(Node.FREE_CALL, true);
    expectedCall.addChildToBack(
        new Node(Token.NAME, "JSCompiler_identityFn"));

    assertEquals(expectedCall, identityFunctionReduced);
  }

  @org.junit.Test
  public void testReturnConstantReduction() {
    List<Reducer> reducers = ImmutableList.of(new ReturnConstantReducer());
    Multimap<Reducer, Reduction> reductionMap = HashMultimap.create();

    NodeTraversal.traverse(compiler, root,
                           new ReductionGatherer(reducers, reductionMap));

    FunctionRewriter.parseHelperCode(ReturnConstantReducer.class);

    int savings = 0;
    for (Reducer reducer : reducers) {
      Collection<Reduction> reductions = reductionMap.get(reducer);
      for (Reduction reduction : reductions) {
        savings += reduction.estimateSavings();
        reduction.apply();
      }
    }

    Node addedHelper = compiler.getNodeForCodeInsertion(null);
    Node helperCode = functionRewriter.parseHelperCode(ReturnConstantReducer.class);
    addedHelper.addChildrenToFront(helperCode);
    compiler.reportCodeChange();

    int helperCost = InlineCostEstimator.getCost(helperCode);
    int estimatedSavings = savings - helperCost;

    assertTrue("Estimated savings is less than helper cost",
               estimatedSavings > 0);

    Node returnConstantFunction = root.getNext();
    Node returnConstantFunctionReduced = functionRewriter.reduce(returnConstantFunction);

    Node expectedCall = new Node(Token.CALL, returnConstantFunction.getLineno(),
                                returnConstantFunction.getCharno());
    expectedCall.putBooleanProp(Node.FREE_CALL, true);
    expectedCall.addChildToBack(
        new Node(Token.NAME, "JSCompiler_returnArg"));

    Node valueNode = returnConstantFunction.getLastChild();
    expectedCall.addChildToBack(valueNode.cloneTree());

    assertEquals(expectedCall, returnConstantFunctionReduced);
  }

  @org.junit.Test
  public void testGetPropertyReduction() {
    List<Reducer> reducers = ImmutableList.of(new GetterReducer());
    Multimap<Reducer, Reduction> reductionMap = HashMultimap.create();

    NodeTraversal.traverse(compiler, root,
                           new ReductionGatherer(reducers, reductionMap));

    FunctionRewriter.parseHelperCode(GetterReducer.class);

    int savings = 0;
    for (Reducer reducer : reducers) {
      Collection<Reduction> reductions = reductionMap.get(reducer);
      for (Reduction reduction : reductions) {
        savings += reduction.estimateSavings();
        reduction.apply();
      }
    }

    Node addedHelper = compiler.getNodeForCodeInsertion(null);
    Node helperCode = functionRewriter.parseHelperCode(GetterReducer.class);
    addedHelper.addChildrenToFront(helperCode);
    compiler.reportCodeChange();

    int helperCost = InlineCostEstimator.getCost(helperCode);
    int estimatedSavings = savings - helperCost;

    assertTrue("Estimated savings is less than helper cost",
               estimatedSavings > 0);

    Node getterFunction = root.getNext();
    Node getterFunctionReduced = functionRewriter.reduce(getterFunction);

    Node expectedCall = new Node(Token.CALL, getterFunction.getLineno(),
                                getterFunction.getCharno());
    expectedCall.putBooleanProp(Node.FREE_CALL, true);
    expectedCall.addChildToBack(
        new Node(Token.NAME, "JSCompiler_get"));
    expectedCall.addChildToBack(getterFunction.getLastChild());

    assertEquals(expectedCall, getterFunctionReduced);
  }

  @org.junit.Test
  public void testSetPropertyReduction() {
    List<Reducer> reducers = ImmutableList.of(new SetterReducer());
    Multimap<Reducer, Reduction> reductionMap = HashMultimap.create();

    NodeTraversal.traverse(compiler, root,
                           new ReductionGatherer(reducers, reductionMap));

    FunctionRewriter.parseHelperCode(SetterReducer.class);

    int savings = 0;
    for (Reducer reducer : reducers) {
      Collection<Reduction> reductions = reductionMap.get(reducer);
      for (Reduction reduction : reductions) {
        savings += reduction.estimateSavings();
        reduction.apply();
      }
    }

    Node addedHelper = compiler.getNodeForCodeInsertion(null);
    Node helperCode = functionRewriter.parseHelperCode(SetterReducer.class);
    addedHelper.addChildrenToFront(helperCode);
    compiler.reportCodeChange();

    int helperCost = InlineCostEstimator.getCost(helperCode);
    int estimatedSavings = savings - helperCost;

    assertTrue("Estimated savings is less than helper cost",
               estimatedSavings > 0);

    Node setterFunction = root.getNext();
    Node setterFunctionReduced = functionRewriter.reduce(setterFunction);

    Node expectedCall = new Node(Token.CALL, setterFunction.getLineno(),
                                setterFunction.getCharno());
    expectedCall.putBooleanProp(Node.FREE_CALL, true);
    expectedCall.addChildToBack(
        new Node(Token.NAME, "JSCompiler_set"));
    expectedCall.addChildToBack(getSetPropertyName(setterFunction));
    expectedCall.addChildToBack(setterFunction.getLastChild());

    assertEquals(expectedCall, setterFunctionReduced);
  }

  private Node getSetPropertyName(Node setterFunction) {
    Node body = setterFunction.getLastChild();
    Node propName = body.getFirstChild();
    return propName;
  }
}