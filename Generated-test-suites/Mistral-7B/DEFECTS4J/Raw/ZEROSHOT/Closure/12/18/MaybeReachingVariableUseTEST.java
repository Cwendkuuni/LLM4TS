import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MaybeReachingVariableUse.*;

import org.junit.Before;
import org.junit.Test;

public class MaybeReachingVariableUseTest {

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;

  @Before
  public void setUp() {
    // Initialize ControlFlowGraph and Scope for testing
    // Replace this with actual initialization logic for your tests
    cfg = new ControlFlowGraph<>();
    jsScope = new Scope(new AbstractCompiler(null));
  }

  @Test
  public void testBasicFlow() {
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    Node entryNode = new Node(Token.BLOCK);
    Node definitionNode = new Node(Token.ASSIGN, entryNode, new Node(Token.NAME, "A"), new Node(Token.INT_LITERAL, 1));
    Node useNode = new Node(Token.PRINT, new Node(Token.NAME, "A"));
    Node exitNode = new Node(Token.RETURN, null);

    cfg.addEdge(entryNode, definitionNode);
    cfg.addEdge(definitionNode, useNode);
    cfg.addEdge(useNode, exitNode);

    analysis.computeMayBeReachingVariables();

    Collection<Node> uses = analysis.getUses("A", definitionNode);
    assertFalse(uses.isEmpty());
    assertTrue(uses.contains(useNode));
  }

  @Test
  public void testConditionalFlow() {
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    Node entryNode = new Node(Token.BLOCK);
    Node definitionNode = new Node(Token.ASSIGN, entryNode, new Node(Token.NAME, "A"), new Node(Token.INT_LITERAL, 1));
    Node ifNode = new Node(Token.IF, new Node(Token.NAME, "x"), new Node(Token.BLOCK));
    Node useNodeIfTrue = new Node(Token.PRINT, new Node(Token.NAME, "A"));
    Node useNodeIfFalse = new Node(Token.PRINT, new Node(Token.INT_LITERAL, 2));
    Node exitNode = new Node(Token.RETURN, null);

    cfg.addEdge(entryNode, definitionNode);
    cfg.addEdge(definitionNode, ifNode);
    cfg.addEdge(ifNode, useNodeIfTrue);
    cfg.addEdge(ifNode, useNodeIfFalse);
    cfg.addEdge(useNodeIfTrue, exitNode);
    cfg.addEdge(useNodeIfFalse, exitNode);

    analysis.computeMayBeReachingVariables();

    Collection<Node> usesIfTrue = analysis.getUses("A", definitionNode);
    assertTrue(usesIfTrue.contains(useNodeIfTrue));
    Collection<Node> usesIfFalse = analysis.getUses("A", definitionNode);
    assertTrue(usesIfFalse.isEmpty());
  }

  @Test
  public void testForLoop() {
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    Node entryNode = new Node(Token.BLOCK);
    Node varNode = new Node(Token.VAR, null, new Node(Token.NAME, "i"), null);
    Node forNode = new Node(Token.FOR, null, varNode, null, new Node(Token.NAME, "j"), null);
    Node definitionNode = new Node(Token.ASSIGN, null, new Node(Token.NAME, "i"), new Node(Token.INT_LITERAL, 0));
    Node incrementNode = new Node(Token.ASSIGN, null, new Node(Token.NAME, "i"), new Node(Token.INCREMENT, new Node(Token.NAME, "i")));
    Node bodyNode = new Node(Token.BLOCK);
    Node useNode = new Node(Token.PRINT, new Node(Token.NAME, "i"));
    Node exitNode = new Node(Token.RETURN, null);

    cfg.addEdge(entryNode, forNode);
    cfg.addEdge(forNode, definitionNode);
    cfg.addEdge(definitionNode, incrementNode);
    cfg.addEdge(incrementNode, bodyNode);
    cfg.addEdge(bodyNode, useNode);
    cfg.addEdge(useNode, bodyNode);
    cfg.addEdge(bodyNode, forNode);
    cfg.addEdge(forNode, exitNode);

    analysis.computeMayBeReachingVariables();

    Collection<Node> uses = analysis.getUses("i", definitionNode);
    assertTrue(uses.contains(useNode));
  }
}