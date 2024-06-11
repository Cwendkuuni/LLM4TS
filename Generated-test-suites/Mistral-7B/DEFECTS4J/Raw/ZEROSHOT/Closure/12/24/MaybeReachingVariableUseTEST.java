import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MaybeReachingVariableUse.*;

import org.junit.Test;

public class MaybeReachingVariableUseTest {

  @Test
  public void testSimpleCase() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg);

    Node entry = Node.newBlock("entry");
    Node assignment = Node.newAssignment("x", Token.INT_LITERAL, entry);
    Node use = Node.newName("x");
    Node print = Node.newCall("print", use, null, null, null);

    entry.addChild(assignment);
    entry.addChild(print);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    analysis.run(entry);

    Collection<Node> usages = analysis.getUsages("x", entry);
    assertFalse(usages.isEmpty());
    assertTrue(usages.contains(use));
  }

  @Test
  public void testConditionalCase() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg);

    Node entry = Node.newBlock("entry");
    Node condition = Node.newBinaryOperator("cond", Node.newName("x"), Token.LOGICAL_OR, Node.newBoolean(false), null);
    Node assignment = Node.newAssignment("x", Token.INT_LITERAL, Node.newBlock("ifBlock"));
    Node use = Node.newName("x");
    Node printTrue = Node.newCall("print", use, null, null, null);
    Node printFalse = Node.newCall("print", Node.newString("false"), null, null, null);

    entry.addChild(condition);
    entry.addChild(Node.newBranch(Branch.JUMP, Node.newLabel("ifBlock"), condition));
    entry.addChild(Node.newBlock("elseBlock"));
    entry.addChild(Node.newLabel("ifBlock"));
    entry.addChild(assignment);
    entry.addChild(printTrue);
    entry.addChild(Node.newBranch(Branch.JUMP, Node.newLabel("elseBlock"), Node.newBoolean(false)));
    entry.addChild(printFalse);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    analysis.run(entry);

    Collection<Node> usages = analysis.getUsages("x", entry);
    assertFalse(usages.isEmpty());
    assertTrue(usages.contains(use));
  }

  @Test
  public void testLoopCase() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg);

    Node entry = Node.newBlock("entry");
    Node condition = Node.newName("i", Node.newLessThan(Node.newName("i"), Node.newInt(10)));
    Node increment = Node.newAssignment("i", Node.newAdd(Node.newName("i"), Node.newInt(1)), entry);
    Node use = Node.newName("i");

    entry.addChild(Node.newFor(Node.newName("i"), Node.newName("i"), Node.newName("i"), Node.newBlock("loopBlock"), condition, increment));
    entry.addChild(Node.newBlock("loopBlock"));
    entry.addChild(Node.newName("print", use));

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    analysis.run(entry);

    Collection<Node> usages = analysis.getUsages("i", entry);
    assertFalse(usages.isEmpty());
    assertTrue(usages.contains(use));
  }

  @Test
  public void testEscapedVariable() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg);

    Node entry = Node.newBlock("entry");
    Node variable = Node.newName("x");
    Node assignment = Node.newAssignment("x", Node.newName("globalX"), entry);
    Node escapedAssignment = Node.newAssignment("globalX", variable, entry);
    Node use = Node.newName("x");

    entry.addChild(assignment);
    entry.addChild(escapedAssignment);
    entry.addChild(use);

    Set<Var> escapedVariables = Sets.newHashSet();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    analysis.run(entry);

    Collection<Node> usages = analysis.getUsages("x", entry);
    assertTrue(usages.isEmpty());
  }

  @Test
  public void testFunctionParameter() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg);

    Node entry = Node.newBlock("entry");
    Node parameter = Node.newName("x");
    Node use = Node.newName("x");
    Node functionCall = Node.newCall("myFunction", parameter, null, null, null);

    entry.addChild(Node.newFunction("myFunction", null, null, null, new Node[0], Node.newBlock("functionBlock")));
    entry.addChild(Node.newName("functionBlock"));
    entry.addChild(use);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    analysis.run(entry);

    Collection<Node> usages = analysis.getUsages("x", entry);
    assertTrue(usages.isEmpty());
  }

  @Test
  public void testNestedFunction() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg);

    Node entry = Node.newBlock("entry");
    Node functionCall = Node.newCall("outerFunction", null, null, null, null);

    Node outerFunction = Node.newFunction("outerFunction", null, null, null, new Node[0], Node.newBlock("outerFunctionBlock"));
    outerFunction.addChild(Node.newName("print", Node.newString("outerFunction")));

    Node innerFunction = Node.newFunction("innerFunction", null, null, null, new Node[0], Node.newBlock("innerFunctionBlock"));
    innerFunction.addChild(Node.newName("print", Node.newString("innerFunction")));

    outerFunction.addChild(Node.newName("innerFunction"));

    entry.addChild(functionCall);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    analysis.run(entry);

    Collection<Node> usages = analysis.getUsages("outerFunction", entry);
    Collection<Node> innerUsages = analysis.getUsages("innerFunction", entry);

    assertFalse(usages.isEmpty());
    assertTrue(usages.contains(Node.newName("print", Node.newString("outerFunction"))));

    assertFalse(innerUsages.isEmpty());
    assertTrue(innerUsages.contains(Node.newName("print", Node.newString("innerFunction"))));
  }
}