import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class MaybeReachingVariableUseTest {
  private ControlFlowGraph<Node> cfg;
  private MaybeReachingVariableUse analyzer;
  private Scope jsScope;
  private Set<Scope.Var> escaped;
  
  @Before
  public void setUp() {
    // Create control flow graph
    cfg = new ControlFlowGraph<>();
  
    // Create jsScope
    jsScope = new Scope();
  
    // Create escaped variables set
    escaped = new HashSet<>();
  
    // Create analyzer object
    analyzer = new MaybeReachingVariableUse(cfg, jsScope, null);
  }
  
  @Test
  public void testFlowThrough() {
    // Create input lattice element
    MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
  
    // Create expected output lattice element
    MaybeReachingVariableUse.ReachingUses expectedOutput = new MaybeReachingVariableUse.ReachingUses(input);
  
    // Create test node
    Node n = new Node(Token.NAME);
    n.setString("test");
  
    // Call flowThrough method
    MaybeReachingVariableUse.ReachingUses actualOutput = analyzer.flowThrough(n, input);
  
    // Assert equality of actual and expected output
    assertEquals(expectedOutput, actualOutput);
  }
  
  @Test
  public void testComputeMayUse() {
    // Create output lattice element
    MaybeReachingVariableUse.ReachingUses output = new MaybeReachingVariableUse.ReachingUses();
  
    // Create test node
    Node n = new Node(Token.VAR);
    Node varName = new Node(Token.NAME);
    varName.setString("test");
    n.addChildToFront(varName);
  
    // Expected output
    MaybeReachingVariableUse.ReachingUses expectedOutput = new MaybeReachingVariableUse.ReachingUses(output);
    addToUseIfLocal("test", n, expectedOutput);
  
    // Call computeMayUse method
    analyzer.computeMayUse(n, n, output, false);
  
    // Assert equality of actual and expected output
    assertEquals(expectedOutput, output);
  }
  
  @Test
  public void testAddToUseIfLocal() {
    // Create test lattice element
    MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();
  
    // Create test node
    Node node = new Node(Token.NAME);
    node.setString("test");
  
    // Create expected output
    MaybeReachingVariableUse.ReachingUses expectedUse = new MaybeReachingVariableUse.ReachingUses(use);
    expectedUse.mayUseMap.put(jsScope.getVar("test"), node);
  
    // Call addToUseIfLocal method
    addToUseIfLocal("test", node, use);
  
    // Assert equality of actual and expected use
    assertEquals(expectedUse, use);
  }
  
  @Test
  public void testRemoveFromUseIfLocal() {
    // Create test lattice element
    MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();
    use.mayUseMap.put(jsScope.getVar("test"), new Node(Token.NAME));
  
    // Create expected output
    MaybeReachingVariableUse.ReachingUses expectedUse = new MaybeReachingVariableUse.ReachingUses(use);
    expectedUse.mayUseMap.removeAll(jsScope.getVar("test"));
  
    // Call removeFromUseIfLocal method
    removeFromUseIfLocal("test", use);
  
    // Assert equality of actual and expected use
    assertEquals(expectedUse, use);
  }
  
  @Test
  public void testGetUses() {
    // Create test name and defNode
    String name = "test";
    Node defNode = new Node(Token.NAME);
    defNode.setString(name);
  
    // Call getUses method
    Collection<Node> uses = analyzer.getUses(name, defNode);
  
    // Assert that uses is not null
    assertTrue(uses != null);
  }
  
  /**
   * Sets the variable for the given name to the node value in the upward
   * exposed lattice. Do nothing if the variable name is one of the escaped
   * variable.
   */
  private void addToUseIfLocal(String name, Node node, MaybeReachingVariableUse.ReachingUses use) {
    Scope.Var var = jsScope.getVar(name);
    if (var == null || var.scope != jsScope) {
      return;
    }
    if (!escaped.contains(var)) {
      use.mayUseMap.put(var, node);
    }
  }

  /**
   * Removes the variable for the given name from the node value in the upward
   * exposed lattice. Do nothing if the variable name is one of the escaped
   * variable.
   */
  private void removeFromUseIfLocal(String name, MaybeReachingVariableUse.ReachingUses use) {
    Scope.Var var = jsScope.getVar(name);
    if (var == null || var.scope != jsScope) {
      return;
    }
    if (!escaped.contains(var)) {
      use.mayUseMap.removeAll(var);
    }
  }
}