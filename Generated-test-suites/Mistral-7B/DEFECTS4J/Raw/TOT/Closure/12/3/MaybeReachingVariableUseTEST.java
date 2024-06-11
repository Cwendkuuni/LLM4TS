import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import static com.google.common.truth.Truth.assertThat;

public class MaybeReachingVariableUseTest {

 private MaybeReachingVariableUse analysis;
 private ControlFlowGraph<Node> controlFlowGraph;
 private Scope jsScope;

 @Before
 public void setUp() {
 controlFlowGraph = new ControlFlowGraph<Node>(null);
 jsScope = new Scope(null, null, null);
 analysis = new MaybeReachingVariableUse(controlFlowGraph, jsScope, null);
 }

 @Test
 public void testEmptyProgram() {
 // Empty program test
 analysis.compute();
 Collection<Node> uses = analysis.getUses("A", analysis.getCfg().getRoot());
 assertThat(uses).isEmpty();
 }

 @Test
 public void testLocalVariableUse() {
 // Test program with local variable use
 Node root = Node.newBlock(Token.BLOCK, null);
 Node assignA = Node.newName("A", null);
 Node assignB = Node.newName("B", null);
 Node printA = Node.newName("print", null, Node.newName("A", null), null);
 Node printB = Node.newName("print", null, Node.newName("B", null), null);
 Node assignC = Node.newName("C", null);
 Node printC = Node.newName("print", null, Node.newName("C", null), null);

 Node block = Node.newBlock(Token.BLOCK, null);
 block.addChild(assignA);
 block.addChild(assignB);
 block.addChild(printA);
 block.addChild(printB);
 block.addChild(assignC);
 block.addChild(printC);
 root.addChild(block);

 analysis.setEntryNode(root);
 analysis.compute();

 Collection<Node> usesA = analysis.getUses("A", root);
 Collection<Node> usesB = analysis.getUsages("B");
 Collection<Node> usesC = analysis.getUses("C", root);

 // Test local variable use
 assertThat(usesA).containsExactly(printA, assignA);
 assertThat(usesB).isEmpty();
 assertThat(usesC).isEmpty();
 }

 @Test
 public void testEscapedVariableUse() {
 // Test program with escaped variable use
 Node root = Node.newBlock(Token.BLOCK, null);
 Node assignA = Node.newName("A", null);
 Node assignB = Node.newName("B", null);
 Node printA = Node.newName("print", null, Node.newName("A", null), null);
 Node printB = Node.newName("print", null, Node.newName("B", null), null);
 Node assignC = Node.newName("C", null);
 Node printC = Node.newName("print", null, Node.newName("C", null), null);
 Node function = Node.newFunction("function", "foo", Node.newBlock(Token.BLOCK, null), null);
 Node callFunction = Node.newCall(Node.newName("foo", null), null);

 Node block = Node.newBlock(Token.BLOCK, null);
 block.addChild(assignA);
 block.addChild(assignB);
 block.addChild(printA);
 block.addChild(assignC);
 block.addChild(printC);
 block.addChild(function);
 block.addChild(callFunction);
 block.addChild(printB);
 root.addChild(block);

 analysis.setEntryNode(root);
 analysis.compute();

 Collection<Node> usesA = analysis.getUses("A", root);
 Collection<Node> usesB = analysis.getUsages("B");
 Collection<Node> usesC = analysis.getUses("C", root);

 // Test escaped variable use
 Var escapedVar = jsScope.getVar("B");
 Preconditions.checkState(escapedVar != null && escapedVar.scope == jsScope);
 Preconditions.checkState(!escaped.contains(escapedVar));
 escaped.add(escapedVar);
 analysis = new MaybeReachingVariableUse(controlFlowGraph, jsScope, null);
 analysis.compute();
 usesB = analysis.getUses("B", root);

 // Check that B is not in the usesB list
 assertThat(usesB).isEmpty();
 }

 @Test
 public void testConditionalBranch() {
 // Test program with conditional branch
 Node root = Node.newBlock(Token.BLOCK, null);
 Node condition = Node.newBinaryOp(Token.IF, Node.newName("x", null), Node.newNumber(0), null);
 Node assignA = Node.newName("A", null);
 Node printA = Node.newName("print", null, Node.newName("A", null), null);
 Node assignB = Node.newName("B", null);
 Node printB = Node.newName("print", null, Node.newName("B", null), null);

 Node thenBlock = Node.newBlock(Token.BLOCK, null);
 thenBlock.addChild(assignA);
 thenBlock.addChild(printA);
 Node elseBlock = Node.newBlock(Token.BLOCK, null);
 elseBlock.addChild(assignB);
 elseBlock.addChild(printB);

 Node ifStatement = Node.newIfStatement(condition, thenBlock, elseBlock, null);
 root.addChild(ifStatement);

 analysis.setEntryNode(root);
 analysis.compute();

 Collection<Node> usesA = analysis.getUses("A", root);
 Collection<Node> usesB = analysis.getUsages("B");

 // Test conditional branch
 int x = 1;
 // When x is true, A is used
 assertThat(usesA).containsExactly(printA);

 // When x is false, B is used
 x = 0;
 analysis.jsScope.setLocal("x", Node.newNumber(x));
 usesB = analysis.getUses("B", root);
 assertThat(usesB).containsExactly(printB);
 }
}