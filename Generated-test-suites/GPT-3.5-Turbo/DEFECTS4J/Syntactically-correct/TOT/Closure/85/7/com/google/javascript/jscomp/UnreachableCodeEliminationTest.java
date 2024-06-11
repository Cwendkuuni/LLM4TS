import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UnreachableCodeEliminationTest {

  private UnreachableCodeElimination uce;

  @Before
  public void setup() {
    uce = new UnreachableCodeElimination(new AbstractCompiler(), true);
  }

  @Test
  public void testEnterScope() {
    // Typical use case
    // Test if entering a scope correctly computes the control flow graph
    // and pushes it onto the stack
    // Also test if the reachability graph is computed correctly
    assertTrue(uce.cfgStack.isEmpty());
    assertNull(uce.curCfg);

    NodeTraversal t = new NodeTraversal(new AbstractCompiler(), new Node());
    uce.enterScope(t);

    assertEquals(1, uce.cfgStack.size());
    assertNotNull(uce.curCfg);
    assertNotNull(uce.curCfg.getEntry().getValue().getAnnotation());
  }

  @Test
  public void testExitScope() {
    // Typical use case
    // Test if exiting a scope correctly pops the control flow graph from the stack
    assertTrue(uce.cfgStack.isEmpty());
    assertNull(uce.curCfg);

    NodeTraversal t = new NodeTraversal(new AbstractCompiler(), new Node());
    uce.enterScope(t);

    assertEquals(1, uce.cfgStack.size());
    assertNotNull(uce.curCfg);

    uce.exitScope(t);

    assertTrue(uce.cfgStack.isEmpty());
    assertNull(uce.curCfg);
  }

  @Test
  public void testProcess() {
    // Typical use case
    // Test if process correctly traverses the AST and calls visit method
    // on each node
    assertEquals(0, uce.process(new Node(), new Node()));
  }

  @Test
  public void testVisit() {
    // Typical use case
    // Test if the visit method correctly handles different node types and
    // calls removeDeadExprStatementSafely method when necessary
    NodeTraversal t = new NodeTraversal(new AbstractCompiler(), new Node());
    Node n = new Node(Token.ADD);

    // Test when parent is null
    uce.visit(t, n, null);

    // Test when node type is FUNCTION
    n.setType(Token.FUNCTION);
    uce.visit(t, n, n.getParent());

    // Test when node type is SCRIPT
    n.setType(Token.SCRIPT);
    uce.visit(t, n, n.getParent());

    // Test when node not in CFG and does not have side effects
    n.setType(Token.FUNCTION);
    uce.visit(t, n, n.getParent());

    // Test when node not in CFG and has side effects
    n.setType(Token.ADD);
    uce.visit(t, n, n.getParent());

    // Test when node is in CFG and is not reachable
    n.setType(Token.RETURN);
    uce.visit(t, n, n.getParent());

    // Test when node is in CFG and is reachable
    n.setType(Token.ADD);
    uce.visit(t, n, n.getParent());
  }

  @Test
  public void testTryRemoveUnconditionalBranching() {
    // Edge case: empty node
    assertNull(uce.tryRemoveUnconditionalBranching(null));

    // Typical use case
    // Test if the tryRemoveUnconditionalBranching method correctly removes
    // unnecessary branches

    Node breakNode1 = new Node(Token.BREAK);
    Node breakNode2 = new Node(Token.BREAK);
    Node breakNode3 = new Node(Token.BREAK);
    Node returnNode = new Node(Token.RETURN);
    Node blockNode = new Node(Token.BLOCK);
    blockNode.addChildToBack(breakNode3);
    breakNode2.addChildToBack(blockNode);
    blockNode.addChildToBack(breakNode2);
    blockNode.addChildToBack(breakNode1);
    returnNode.addChildToFront(breakNode3);

    uce.tryRemoveUnconditionalBranching(returnNode);
    assertNull(returnNode.getNext());
  }

  @Test
  public void testComputeFollowing() {
    // Typical use case
    // Test if the computeFollowing method correctly returns the following node
    Node n = new Node(Token.ADD);
    assertNull(uce.computeFollowing(n));

    Node blockNode = new Node(Token.BLOCK);
    blockNode.addChildToFront(n);
    n.addChildToBack(new Node(Token.NAME));
    Node followingNode = new Node(Token.STRING);
    n.addChildToBack(followingNode);

    assertEquals(followingNode, uce.computeFollowing(blockNode));
  }

  @Test
  public void testRemoveDeadExprStatementSafely() {
    // Edge case: empty node
    uce.removeDeadExprStatementSafely(null);

    // Typical use case
    // Test if the removeDeadExprStatementSafely method correctly removes
    // dead expression statements
    Node parentNode = new Node(Token.ADD);
    Node childNode = new Node(Token.STRING);

    parentNode.addChildToBack(childNode);

    uce.removeDeadExprStatementSafely(childNode);
    assertNull(parentNode.getFirstChild());
  }

  @Test
  public void testProcessWithLogger() {
    // Test if process method correctly traverses the AST and calls visit method
    // on each node while logging the removal of nodes
    Logger logger = Logger.getLogger(UnreachableCodeElimination.class.getName());
    Level level = logger.getLevel();
    logger.setLevel(Level.FINE);

    assertEquals(0, uce.process(new Node(), new Node()));

    logger.setLevel(level);
  }

  @Test
  public void testProcessWithLoggerAndEmptyNode() {
    // Test if process method correctly traverses the AST and calls visit method
    // on each node while logging the removal of nodes
    Logger logger = Logger.getLogger(UnreachableCodeElimination.class.getName());
    Level level = logger.getLevel();
    logger.setLevel(Level.FINE);

    assertEquals(0, uce.process(new Node(), new Node()));

    logger.setLevel(level);
  }

}