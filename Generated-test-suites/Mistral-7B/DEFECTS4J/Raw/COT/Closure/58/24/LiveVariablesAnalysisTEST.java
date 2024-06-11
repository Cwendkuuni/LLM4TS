import static org.junit.Assert.*;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.BitSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

 private Scope jsScope;
 private ControlFlowGraph<Node> cfg;
 private LiveVariablesAnalysis liveVariablesAnalysis;

 @Before
 public void setUp() {
 // Initialize your test scope and control flow graph here
 // For the sake of this example, we will assume that the setup has been done.
 jsScope = new Scope();
 cfg = new ControlFlowGraph<>();
 }

 @Test
 public void testEmptyProgram() {
 LiveVariablesAnalysis emptyProgramAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);
 Set<Var> expectedEscapedVariables = Sets.newHashSet();
 assertEquals(expectedEscapedVariables, emptyProgramAnalysis.getEscapedLocals());
 }

 @Test
 public void testSimpleVariableAssignment() {
 Node assignmentNode = Node.newAssign(new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 5));
 LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);
 analysis.flowThrough(assignmentNode, analysis.createInitialEstimateLattice());
 Set<Var> expectedEscapedVariables = Sets.newHashSet();
 Var xVar = jsScope.getVar("x");
 assertTrue(analysis.isLive(xVar.index));
 assertEquals(expectedEscapedVariables, analysis.getEscapedLocals());
 }

 @Test
 public void testVariableAssignmentInConditionalBlock() {
 Node conditionNode = Node.newBinaryOp(Token.OP_COND, Node.newBoolean(true), Node.newAssign(Node.newName("x"), Node.newIntLiteral(5)));
 LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);
 analysis.flowThrough(conditionNode, analysis.createInitialEstimateLattice());
 Set<Var> expectedEscapedVariables = Sets.newHashSet();
 Var xVar = jsScope.getVar("x");
 assertTrue(analysis.isLive(xVar.index));
 assertEquals(expectedEscapedVariables, analysis.getEscapedLocals());
 }

 @Test
 public void testVariableAssignmentInLoop() {
 Node loopNode = Node.newWhile(Node.newName("condition"), Node.newBlock(Node.newAssign(Node.newName("x"), Node.newIntLiteral(5)), Node.newBlock(Node.newPrint("x"))));
 LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);
 analysis.flowThrough(loopNode, analysis.createInitialEstimateLattice());
 Set<Var> expectedEscapedVariables = Sets.newHashSet();
 Var xVar = jsScope.getVar("x");
 assertTrue(analysis.isLive(xVar.index));
 assertEquals(expectedEscapedVariables, analysis.getEscapedLocals());
 }

 @Test (expected = AssertionError.class)
 public void testVariableAssignmentInExceptionBlock() {
 Node exceptionNode = Node.newTryCatchBlock(Node.newAssign(Node.newName("x"), Node.newIntLiteral(5)), new Node(Token.THROW, Node.newName("e")), null, null);
 LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);
 analysis.flowThrough(exceptionNode, analysis.createInitialEstimateLattice());
 Set<Var> expectedEscapedVariables = Sets.newHashSet();
 Var xVar = jsScope.getVar("x");
 assertFalse(analysis.isLive(xVar.index));
 assertEquals(expectedEscapedVariables, analysis.getEscapedLocals());
 }

 @Test
 public void testArgumentsVariableAssignedInFunction() {
 Node functionNode = Node.newFunction(Node.newName("myFunction"), new Node[0], Node.newBlock(Node.newAssign(Node.newName("arguments")[0], Node.newIntLiteral(5)), Node.newReturn(Node.newName("arguments")[0])));
 LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);
 analysis.flowThrough(functionNode, analysis.createInitialEstimateLattice());
 Set<Var> expectedEscapedVariables = Sets.newHashSet(jsScope.getVar("arguments"));
 assertTrue(expectedEscapedVariables.contains(jsScope.getVar("arguments").getIndex()));
 assertEquals(expectedEscapedVariables, analysis.getEscapedLocals());
 }
}