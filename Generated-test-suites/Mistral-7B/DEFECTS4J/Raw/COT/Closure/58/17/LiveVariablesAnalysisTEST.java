import static org.junit.Assert.*;
import static com.google.javascript.jscomp.LiveVariablesAnalysis.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest {
  private Scope jsScope;
  private LiveVariablesAnalysis analysis;

  @Before
  public void setUp() {
    // Replace this with the actual setup code.
    jsScope = new Scope();
  }

  @Test
  public void testGetVarIndex() {
    int varIndex = analysis.getVarIndex("x");
    assertEquals(0, varIndex);
  }

  @Test
  public void testFlowThroughSimpleExpression() {
    Node node = Node.newIntLiteral(1);
    LiveVariableLattice inputLattice = new LiveVariableLattice(jsScope.getVarCount());
    LiveVariableLattice outputLattice = analysis.flowThrough(node, inputLattice);
    assertTrue(outputLattice.liveSet.isEmpty());
  }

  @Test
  public void testFlowThroughAssignment() {
    Node node = NodeFactory.createBinaryExpression(
        NodeFactory.createName("x"), NodeFactory.createToken(Token.ASSIGN),
        NodeFactory.createName("y"));

    LiveVariableLattice inputLattice = new LiveVariableLattice(jsScope.getVarCount());
    LiveVariableLattice outputLattice = analysis.flowThrough(node, inputLattice);

    Var x = jsScope.getVar("x");
    Var y = jsScope.getVar("y");

    outputLattice.liveSet.set(x.index);
    outputLattice.liveSet.set(y.index);

    assertEquals(inputLattice, outputLattice);
  }

  @Test
  public void testFlowThroughIfStatement() {
    Node condition = NodeFactory.createBinaryExpression(
        NodeFactory.createName("x"), NodeFactory.createToken(Token.LT),
        NodeFactory.createIntLiteral(5));

    Node thenBranch = NodeFactory.createBlockStatement(
        NodeFactory.createName("y") // Assign new value to 'y'
    );

    Node elseBranch = NodeFactory.createBlockStatement();

    Node ifStatement = NodeFactory.createIfStatement(condition, thenBranch, elseBranch);

    LiveVariableLattice inputLattice = new LiveVariableLattice(jsScope.getVarCount());
    LiveVariableLattice outputLattice = analysis.flowThrough(ifStatement, inputLattice);

    Var x = jsScope.getVar("x");
    Var y = jsScope.getVar("y");

    outputLattice.liveSet.set(x.index); // x is live because of the condition
    outputLattice.liveSet.set(y.index); // y is live because it is assigned a value

    assertEquals(inputLattice, outputLattice);
  }

  @Test
  public void testFlowThroughWhileLoop() {
    Node condition = NodeFactory.createName("x");
    Node loopBody = NodeFactory.createBlockStatement(
        NodeFactory.createAssignStatement(
            NodeFactory.createName("x"), NodeFactory.createUnaryMinus(
                NodeFactory.createName("x"))));

    Node whileLoop = NodeFactory.createWhileStatement(condition, loopBody);

    LiveVariableLattice inputLattice = new LiveVariableLattice(jsScope.getVarCount());
    LiveVariableLattice outputLattice = analysis.flowThrough(whileLoop, inputLattice);

    Var x = jsScope.getVar("x");

    outputLattice.liveSet.set(x.index); // x is live because of the condition

    assertEquals(inputLattice, outputLattice);
  }

  @Test
  public void testFlowThroughFunction() {
    Node functionBody = NodeFactory.createBlockStatement();
    Node function = NodeFactory.createFunctionDeclaration(
        "myFunction", NodeFactory.createList(), functionBody);

    LiveVariableLattice inputLattice = new LiveVariableLattice(jsScope.getVarCount());
    LiveVariableLattice outputLattice = analysis.flowThrough(function, inputLattice);

    assertTrue(outputLattice.liveSet.isEmpty());
  }

  @Test
  public void testComputeGenKillForSimpleExpression() {
    Node node = Node.newIntLiteral(1);

    BitSet genSet = new BitSet();
    BitSet killSet = new BitSet();

    analysis.computeGenKill(node, genSet, killSet, false);

    assertTrue(genSet.isEmpty());
    assertTrue(killSet.isEmpty());
  }

  @Test
  public void testComputeGenKillForAssignment() {
    Node lhs = NodeFactory.createName("x");
    Node rhs = NodeFactory.createName("y");
    Node assignment = NodeFactory.createBinaryExpression(
        lhs, NodeFactory.createToken(Token.ASSIGN), rhs);

    BitSet genSet = new BitSet();
    BitSet killSet = new BitSet();

    analysis.computeGenKill(assignment, genSet, killSet, false);

    Var x = jsScope.getVar("x");
    Var y = jsScope.getVar("y");

    genSet.set(x.index);
    genSet.set(y.index);

    killSet.set(x.index);

    assertEquals(genSet, analysis.getGenSet(assignment));
    assertEquals(killSet, analysis.getKillSet(assignment));
  }

  @Test
  public void testComputeGenKillForIfStatement() {
    Node condition = NodeFactory.createBinaryExpression(
        NodeFactory.createName("x"), NodeFactory.createToken(Token.LT),
        NodeFactory.createIntLiteral(5));

    Node thenBranch = NodeFactory.createBlockStatement(
        NodeFactory.createName("y") // Assign new value to 'y'
    );

    Node elseBranch = NodeFactory.createBlockStatement();

    Node ifStatement = NodeFactory.createIfStatement(condition, thenBranch, elseBranch);

    BitSet genSet = new BitSet();
    BitSet killSet = new BitSet();

    analysis.computeGenKill(ifStatement, genSet, killSet, false);

    Var x = jsScope.getVar("x");
    Var y = jsScope.getVar("y");

    genSet.set(x.index); // x is live because of the condition
    genSet.set(y.index); // y is live because it is assigned a value

    killSet.set(x.index); // x is killed because it is not assigned a value in the else branch

    assertEquals(genSet, analysis.getGenSet(ifStatement));
    assertEquals(killSet, analysis.getKillSet(ifStatement));
  }

  @Test
  public void testComputeGenKillForWhileLoop() {
    Node condition = NodeFactory.createName("x");
    Node loopBody = NodeFactory.createBlockStatement(
        NodeFactory.createAssignStatement(
            NodeFactory.createName("x"), NodeFactory.createUnaryMinus(
                NodeFactory.createName("x"))));

    Node whileLoop = NodeFactory.createWhileStatement(condition, loopBody);

    BitSet genSet = new BitSet();
    BitSet killSet = new BitSet();

    analysis.computeGenKill(whileLoop, genSet, killSet, false);

    Var x = jsScope.getVar("x");

    genSet.set(x.index); // x is live because of the condition

    assertEquals(genSet, analysis.getGenSet(whileLoop));
    assertEquals(killSet, analysis.getKillSet(whileLoop));
  }

  @Test
  public void testComputeGenKillForFunction() {
    Node functionBody = NodeFactory.createBlockStatement();
    Node function = NodeFactory.createFunctionDeclaration(
        "myFunction", NodeFactory.createList(), functionBody);

    BitSet genSet = new BitSet();
    BitSet killSet = new BitSet();

    analysis.computeGenKill(function, genSet, killSet, false);

    assertTrue(genSet.isEmpty());
    assertTrue(killSet.isEmpty());
  }

  @Test
  public void testGetEscapedLocals() {
    Node rootNode = NodeFactory.createBlockStatement();

    Node lp = rootNode.addChild(NodeFactory.createToken(Token.LPAREN));
    Node arg1 = rootNode.addChild(NodeFactory.createName("arg1"));
    Node comma = rootNode.addChild(NodeFactory.createToken(Token.COMMA));
    Node arg2 = rootNode.addChild(NodeFactory.createName("arg2"));
    Node rp = rootNode.addChild(NodeFactory.createToken(Token.RPAREN));

    Node body = NodeFactory.createBlockStatement();

    Node function = NodeFactory.createFunctionDeclaration(
        NodeFactory.createName("myFunction"),
        NodeFactory.createList(lp, arg1, arg2, rp), body);

    analysis = new LiveVariablesAnalysis(null, jsScope, null);

    Set<Var> escapedLocals = analysis.getEscapedLocals();

    Var arg1Var = jsScope.getVar("arg1");
    Var arg2Var = jsScope.getVar("arg2");

    assertTrue(escapedLocals.contains(arg1Var));
    assertTrue(escapedLocals.contains(arg2Var));
  }

  @Test(expected = AssertionError.class)
  public void testComputeGenKillForNullNode() {
    BitSet genSet = new BitSet();
    BitSet killSet = new BitSet();

    analysis.computeGenKill(null, genSet, killSet, false);

    fail("This should throw an AssertionError");
  }
}