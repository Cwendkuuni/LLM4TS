
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;

import java.util.Collection;
import java.util.List;

public class MaybeReachingVariableUseTest {

  MaybeReachingVariableUse reachingVariableUse;
  ControlFlowGraph<Node> cfg;
  Scope jsScope;
  AbstractCompiler compiler;

  @Before
  public void setUp() {
    cfg = new ControlFlowGraph<>();
    jsScope = new Scope();
    compiler = new AbstractCompiler();
    reachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);
  }

  @Test
  public void testComputeMayUse_WithNameNode_AddToUseIfLocal() {
    Node node = new Node(Token.NAME);
    node.setString("x");
    Var var = new Var("x", node);
    jsScope.addLocal(var);
    
    MaybeReachingVariableUse.ReachingUses uses = reachingVariableUse.createInitialEstimateLattice();
    reachingVariableUse.computeMayUse(node, node, uses, false);
    Collection<Node> mayUse = uses.mayUseMap.get(var);
    
    assertEquals(1, mayUse.size());
    assertTrue(mayUse.contains(node));
  }
  
  @Test
  public void testComputeMayUse_WithNameNode_RemoveFromUseIfLocal() {
    Node node = new Node(Token.NAME);
    node.setString("x");
    Var var = new Var("x", node);
    jsScope.addLocal(var);
    
    MaybeReachingVariableUse.ReachingUses uses = reachingVariableUse.createInitialEstimateLattice();
    
    uses.mayUseMap.put(var, node);
    reachingVariableUse.removeFromUseIfLocal("x", uses);
    Collection<Node> mayUse = uses.mayUseMap.get(var);
    
    assertEquals(0, mayUse.size());
  }
  
  @Test
  public void testFlowThrough_WithAssignment_AddToUseIfLocal() {
    Node node = new Node(Token.NAME);
    node.setString("x");
    Var var = new Var("x", node);
    jsScope.addLocal(var);
    
    MaybeReachingVariableUse.ReachingUses input = reachingVariableUse.createInitialEstimateLattice();
    MaybeReachingVariableUse.ReachingUses output = reachingVariableUse.flowThrough(node, input);
    Collection<Node> mayUse = output.mayUseMap.get(var);
    
    assertEquals(1, mayUse.size());
    assertTrue(mayUse.contains(node));
  }
  
  @Test
  public void testFlowThrough_WithAssignment_RemoveFromUseIfLocal() {
    Node node = new Node(Token.NAME);
    node.setString("x");
    Var var = new Var("x", node);
    jsScope.addLocal(var);
    
    MaybeReachingVariableUse.ReachingUses input = reachingVariableUse.createInitialEstimateLattice();
    input.mayUseMap.put(var, node);
    
    MaybeReachingVariableUse.ReachingUses output = reachingVariableUse.flowThrough(node, input);
    Collection<Node> mayUse = output.mayUseMap.get(var);
    
    assertEquals(0, mayUse.size());
  }
  
  @Test
  public void testGetUses_WithUpwardExposedUse_ReturnsListOfNodes() {
    Node defNode = new Node(Token.NAME);
    defNode.setString("x");
    
    Node useNode1 = new Node(Token.NAME);
    useNode1.setString("x");
    
    Node useNode2 = new Node(Token.NAME);
    useNode2.setString("x");
    
    Var var = new Var("x", defNode);
    jsScope.addLocal(var);
    
    MaybeReachingVariableUse.ReachingUses uses = reachingVariableUse.createInitialEstimateLattice();
    uses.mayUseMap.put(var, useNode1);
    uses.mayUseMap.put(var, useNode2);
    
    Collection<Node> useList = reachingVariableUse.getUses("x", defNode);
    
    assertEquals(2, useList.size());
    assertTrue(useList.contains(useNode1));
    assertTrue(useList.contains(useNode2));
  }
  
  @Test
  public void testGetUses_WithNoUpwardExposedUse_ReturnsEmptyList() {
    Node defNode = new Node(Token.NAME);
    defNode.setString("x");
    
    Var var = new Var("x", defNode);
    jsScope.addLocal(var);
    
    MaybeReachingVariableUse.ReachingUses uses = reachingVariableUse.createInitialEstimateLattice();
    
    Collection<Node> useList = reachingVariableUse.getUses("x", defNode);
    
    assertEquals(0, useList.size());
  }
}

