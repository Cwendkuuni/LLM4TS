import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MaybeReachingVariableUse.*;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

public class MaybeReachingVariableUseTest {

  private AbstractCompiler compiler;
  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;

  @Before
  public void setUp() {
    // Initialize your compiler, control flow graph, and scope here.
    // For simplicity, we assume they are initialized in your constructor.
  }

  @Test
  public void testMaybeReachingVariableUse() {
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    ReachingUses initial = analysis.computeInitialEstimate(NodeUtil.entryNode(cfg));

    // Test getUses method
    Var varA = jsScope.getVar("A");
    Node defA = Node.newName("A");
    Node useA = Node.newName("useA");
    Node assignmentA = Node.newAssignment(Node.newName("A"), Node.newInt(1));

    // Test forward analysis
    Node entry = NodeUtil.entryNode(cfg);
    Node entryNode = NodeUtil.makeNode(cfg, entry, Token.BLOCK, null);
    entryNode.addChild(assignmentA);
    entryNode.addChild(useA);

    ReachingUses result = analysis.computeResult(entryNode, initial);
    Set<Node> uses = analysis.getUses("A", defA);
    assertEquals(Sets.newHashSet(useA), uses);

    // Test backward analysis
    Node useB = Node.newName("useB");
    Node assignmentB = Node.newAssignment(Node.newName("A"), Node.newInt(2));
    Node conditionalB = Node.newConditional(Node.newBooleanLiteral(true), assignmentB, useB);

    Node blockB = Node.newBlock(null, null);
    blockB.addChild(NodeUtil.makeNode(cfg, null, Token.BLOCK, null));
    blockB.addChild(conditionalB);

    ReachingUses initialB = analysis.createEntryLattice();
    ReachingUses resultB = analysis.computeResult(blockB, initialB);
    Set<Node> usesB = analysis.getUses("A", defA);
    assertTrue(usesB.isEmpty());

    // Test escaping variables
    Var varEscaped = jsScope.getVar("escaped");
    Node defEscaped = Node.newName("escaped");
    Node useEscaped = Node.newName("useEscaped");
    Node assignmentEscaped = Node.newAssignment(defEscaped, Node.newName("escapedValue"));
    Node escapingBlock = Node.newBlock(null, null);

    escapingBlock.addChild(NodeUtil.makeNode(cfg, null, Token.BLOCK, null));
    escapingBlock.addChild(assignmentEscaped);
    escapingBlock.addChild(useEscaped);

    analysis.escaped = Sets.newHashSet(varEscaped);
    ReachingUses resultEscaped = analysis.computeResult(escapingBlock, initialB);
    Set<Node> usesEscaped = analysis.getUses("escaped", defEscaped);
    assertTrue(usesEscaped.isEmpty());
  }
}