import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import org.junit.Assert;
import org.junit.Test;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

public class LiveVariablesAnalysisTest {

    @Test
    public void testCreateEntryLattice() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
        Assert.assertFalse(entryLattice.isLive(0));
        for (int i = 1; i < jsScope.getVarCount(); i++) {
            Assert.assertFalse(entryLattice.isLive(i));
        }
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        LiveVariableLattice initialEstimateLattice = liveVariablesAnalysis.createInitialEstimateLattice();
        for (int i = 0; i < jsScope.getVarCount(); i++) {
            Assert.assertTrue(initialEstimateLattice.isLive(i));
        }
    }

    @Test
    public void testFlowThroughSimpleAssignment() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        Node assignmentNode = Node.newLiteral(Token.ASSIGN, Node.newIntLiteral(1));
        Node lhs = Node.newName("x");
        Node rhs = Node.newName("y");
        Node statementNode = Node.newName("a").addChildren(lhs, assignmentNode, rhs);
        LiveVariableLattice inputLattice = new LiveVariableLattice(jsScope.getVarCount());
        inputLattice.liveSet.set(0);
        LiveVariableLattice outputLattice = liveVariablesAnalysis.flowThrough(statementNode, inputLattice);
        Assert.assertTrue(outputLattice.isLive(0));
        Assert.assertTrue(outputLattice.isLive(1));
    }

    @Test
    public void testFlowThroughConditionalAssignment() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        Node condition = Node.newBinaryOp(Token.LOGICAL_OR,
                Node.newName("a"), Node.newLiteral(Token.BOOL, true));
        Node assignmentNode = Node.newLiteral(Token.ASSIGN, Node.newIntLiteral(1));
        Node lhs = Node.newName("x");
        Node statementNode1 = Node.newName("if").addChildren(condition, lhs, assignmentNode);
        Node statementNode2 = Node.newName("else").addChildren(Node.newName("x"));
        LiveVariableLattice inputLattice = new LiveVariableLattice(jsScope.getVarCount());
        inputLattice.liveSet.set(0);
        LiveVariableLattice outputLattice1 = liveVariablesAnalysis.flowThrough(statementNode1, inputLattice);
        LiveVariableLattice outputLattice2 = liveVariablesAnalysis.flowThrough(statementNode2, inputLattice);
        Assert.assertTrue(outputLattice1.isLive(0));
        Assert.assertTrue(outputLattice2.isLive(0));
    }

    @Test
    public void testFlowThroughForLoop() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        Node initializationNode = Node.newName("x").addChildren(Node.newName("i").addChildren(Node.newName("="), Node.newName("0")));
        Node conditionNode = Node.newName("i").addChildren(Node.newBinaryOp(Token.LESS_THAN, Node.newName("10")));
        Node incrementNode = Node.newName("i").addChildren(Node.newBinaryOp(Token.PLUS_EQUALS, Node.newLiteral(Token.INT_ONE)));
        Node loopBodyNode = Node.newName("x").addChildren(Node.newName("="), Node.newName("i"));
        Node forNode = Node.newName("for").addChildren(initializationNode, conditionNode, incrementNode, loopBodyNode);
        LiveVariableLattice inputLattice = new LiveVariableLattice(jsScope.getVarCount());
        inputLattice.liveSet.set(0);
        LiveVariableLattice outputLattice = liveVariablesAnalysis.flowThrough(forNode, inputLattice);
        Assert.assertTrue(outputLattice.isLive(0));
        Assert.assertTrue(outputLattice.isLive(1));
    }

    @Test(expected = RuntimeException.class)
    public void testFlowThroughInvalidNode() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        Node invalidNode = Node.newLiteral(Token.INVALID, null);
        LiveVariableLattice inputLattice = new LiveVariableLattice(jsScope.getVarCount());
        LiveVariableLattice outputLattice = liveVariablesAnalysis.flowThrough(invalidNode, inputLattice);
    }

    @Test
    public void testComputeGenKill() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        Node assignmentNode1 = Node.newLiteral(Token.ASSIGN, Node.newName("x"));
        Node conditionNode = Node.newName("a").addChildren(Node.newName(">"), Node.newLiteral(Token.INT, 10));
        Node assignmentNode2 = Node.newLiteral(Token.ASSIGN, Node.newName("x"));
        Node statementNode = Node.newName("if").addChildren(conditionNode, assignmentNode1, Node.newName("else").addChildren(assignmentNode2));
        BitSet genSet = new BitSet(jsScope.getVarCount());
        BitSet killSet = new BitSet(jsScope.getVarCount());
        liveVariablesAnalysis.computeGenKill(statementNode, genSet, killSet, false);
        Assert.assertTrue(genSet.get(0));
        Assert.assertFalse(killSet.get(0));
    }
}