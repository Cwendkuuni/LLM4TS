import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collection;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;

public class MaybeReachingVariableUseTest {

  @Test
  public void testComputeMayUse_SimpleAssignment() {
    // Arrange
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    
    Node rootNode = new Node(Token.NAME, "rootNode");
    MaybeReachingVariableUse testObj = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    MaybeReachingVariableUse.ReachingUses expected = new MaybeReachingVariableUse.ReachingUses();
    expected.mayUseMap.put(jsScope.getVar("A"), rootNode);
  
    // Act
    testObj.computeMayUse(rootNode, rootNode, testObj.createEntryLattice(), false);
    MaybeReachingVariableUse.ReachingUses actual = testObj.flowThrough(rootNode, testObj.createEntryLattice());
  
    // Assert
    assertEquals(expected.mayUseMap.keySet(), actual.mayUseMap.keySet());
    Collection<Node> expectedUses = expected.mayUseMap.get(jsScope.getVar("A"));
    Collection<Node> actualUses = actual.mayUseMap.get(jsScope.getVar("A"));
    assertEquals(expectedUses.size(), actualUses.size());
    assertTrue(expectedUses.containsAll(actualUses));
  }

  @Test
  public void testComputeMayUse_ConditionalAssignment() {
    // Arrange
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();

    Node rootNode = new Node(Token.NAME, "rootNode");
    MaybeReachingVariableUse testObj = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    MaybeReachingVariableUse.ReachingUses expected = new MaybeReachingVariableUse.ReachingUses();
    expected.mayUseMap.put(jsScope.getVar("A"), rootNode);
  
    // Act
    testObj.computeMayUse(rootNode, rootNode, testObj.createEntryLattice(), true);
    MaybeReachingVariableUse.ReachingUses actual = testObj.flowThrough(rootNode, testObj.createEntryLattice());
  
    // Assert
    assertEquals(expected.mayUseMap.keySet(), actual.mayUseMap.keySet());
    Collection<Node> expectedUses = expected.mayUseMap.get(jsScope.getVar("A"));
    Collection<Node> actualUses = actual.mayUseMap.get(jsScope.getVar("A"));
    assertEquals(expectedUses.size(), actualUses.size());
    assertTrue(expectedUses.containsAll(actualUses));
  }

  @Test
  public void testGetUses_SingleUse() {
    // Arrange
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();

    Node rootNode = new Node(Token.NAME, "rootNode");
    MaybeReachingVariableUse testObj = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    MaybeReachingVariableUse.ReachingUses uses = new MaybeReachingVariableUse.ReachingUses();
    uses.mayUseMap.put(jsScope.getVar("A"), rootNode);
    Node defNode = new Node(Token.NAME, "defNode");
    
    // Act
    Collection<Node> actual = testObj.getUses("A", defNode);
    
    // Assert
    assertEquals(uses.mayUseMap.get(jsScope.getVar("A")), actual);
  }

  @Test
  public void testGetUses_NoUse() {
    // Arrange
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();

    MaybeReachingVariableUse testObj = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    MaybeReachingVariableUse.ReachingUses uses = new MaybeReachingVariableUse.ReachingUses();
    Node defNode = new Node(Token.NAME, "defNode");
    
    // Act
    Collection<Node> actual = testObj.getUses("A", defNode);
    
    // Assert
    assertNull(actual);
  }
}