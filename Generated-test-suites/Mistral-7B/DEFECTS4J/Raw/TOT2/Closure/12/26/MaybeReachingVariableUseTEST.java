import com.google.javascript.jscomp.MaybeReachingVariableUse;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class MaybeReachingVariableUseTest {

  @Test
  public void testSimpleVariableUse() {
    Scope scope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    AbstractCompiler compiler = new AbstractCompiler(new CompilerOptions());

    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, scope, compiler);

    Node root = Node.newBlock(Token.BLOCK, null);
    Node assignmentNode = Node.newName("x", Token.NAME);
    Node valueNode = Node.newNumber(1, Token.NUMBER);
    Node printNode = Node.newName("print", Token.NAME);

    Node assignment = Node.newAssign(
        Token.ASSIGN, Token.NAME, Node.newName("x", Token.NAME), valueNode);

    Node printStatement = Node.newCall(
        Token.CALL, printNode, Node.newArray(Node.newName("x", Token.NAME)));

    root.addChild(assignment);
    root.addChild(printStatement);

    maybeReachingVariableUse.process(root);

    Collection<Node> uses = maybeReachingVariableUse.getUses("x", root);
    assertTrue(uses.size() > 0);
    assertTrue(uses.contains(printStatement));
  }

  @Test
  public void testVariableWithMultipleUses() {
    Scope scope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    AbstractCompiler compiler = new AbstractCompiler(new CompilerOptions());

    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, scope, compiler);

    Node root = Node.newBlock(Token.BLOCK, null);
    Node assignment1Node = Node.newName("x", Token.NAME);
    Node value1Node = Node.newNumber(1, Token.NUMBER);
    Node print1Node = Node.newName("print", Token.NAME);
    Node assignment2Node = Node.newName("x", Token.NAME);
    Node value2Node = Node.newNumber(2, Token.NUMBER);
    Node print2Node = Node.newName("print", Token.NAME);

    Node assignment1 = Node.newAssign(
        Token.ASSIGN, Token.NAME, Node.newName("x", Token.NAME), value1Node);
    Node printStatement1 = Node.newCall(
        Token.CALL, print1Node, Node.newArray(Node.newName("x", Token.NAME)));

    root.addChild(assignment1);
    root.addChild(printStatement1);
    root.addChild(assignment2);
    root.addChild(Node.newName("x", Token.NAME)); // read
    root.addChild(Node.newAssign(
        Token.ASSIGN, Token.NAME, Node.newName("x", Token.NAME), value2Node));
    root.addChild(printStatement1); // reprint

    maybeReachingVariableUse.process(root);

    Collection<Node> uses = maybeReachingVariableUse.getUses("x", root);
    assertTrue(uses.size() > 0);
    assertTrue(uses.contains(printStatement1));
    assertTrue(uses.contains(printStatement1)); // should print twice
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVariableNotInScope() {
    Scope scope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    AbstractCompiler compiler = new AbstractCompiler(new CompilerOptions());

    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, scope, compiler);

    Node root = Node.newBlock(Token.BLOCK, null);
    Node variableNode = Node.newName("y", Token.NAME); // not defined in scope

    root.addChild(variableNode);

    maybeReachingVariableUse.process(root);

    maybeReachingVariableUse.getUses("y", root); // should throw an exception
  }

  @Test
  public void testNoUses() {
    Scope scope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    AbstractCompiler compiler = new AbstractCompiler(new CompilerOptions());

    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, scope, compiler);

    Node root = Node.newBlock(Token.BLOCK, null);
    Node assignmentNode = Node.newName("x", Token.NAME);
    Node valueNode = Node.newNumber(1, Token.NUMBER);
    Node printNode = Node.newName("print", Token.NAME);
    Node ifNode = Node.newIf(Token.IF, Node.newName("true", Token.NAME), null);

    Node assignment = Node.newAssign(
        Token.ASSIGN, Token.NAME, Node.newName("x", Token.NAME), valueNode);
    Node ifCondition = Node.newName("true", Token.NAME);

    root.addChild(ifNode);
    root.addChild(ifNode.getTrueBranch());
    root.addChild(assignment);
    root.addChild(Node.newName("x", Token.NAME)); // read
    root.addChild(ifNode.getFalseBranch());
    root.addChild(printNode);

    maybeReachingVariableUse.process(root);

    Collection<Node> uses = maybeReachingVariableUse.getUses("x", root);
    assertTrue(uses.isEmpty());
  }

  @Test
  public void testExceptionHandler() {
    Scope scope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    AbstractCompiler compiler = new AbstractCompiler(new CompilerOptions());

    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, scope, compiler);

    Node root = Node.newBlock(Token.BLOCK, null);
    Node tryNode = Node.newTry(Token.TRY, null);
    Node catchNode = Node.newCatch(
        Token.CATCH, Node.newName("ExceptionClass", Token.NAME), null);
    Node assignmentNode = Node.newName("x", Token.NAME);
    Node valueNode = Node.newNumber(1, Token.NUMBER);
    Node printNode = Node.newName("print", Token.NAME);
    Node ifNode = Node.newIf(Token.IF, Node.newName("true", Token.NAME), null);

    root.addChild(tryNode);
    root.addChild(tryNode.getTryBody());
    root.addChild(assignmentNode);
    root.addChild(Node.newName("x", Token.NAME)); // read
    root.addChild(ifNode);
    root.addChild(ifNode.getTrueBranch());
    root.addChild(printNode);
    root.addChild(tryNode.getExceptionHandler());
    root.addChild(assignmentNode);
    root.addChild(Node.newNumber(2, Token.NUMBER)); // assignment to x

    maybeReachingVariableUse.process(root);

    Collection<Node> uses = maybeReachingVariableUse.getUses("x", root);
    assertTrue(uses.contains(printNode)); // print statement before exception
  }
}