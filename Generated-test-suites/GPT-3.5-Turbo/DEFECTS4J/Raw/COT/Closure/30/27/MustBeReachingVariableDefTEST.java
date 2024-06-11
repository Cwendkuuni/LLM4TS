
import org.junit.Test;
import static org.junit.Assert.*;

public class MustBeReachingVariableDefTest {

  @Test
  public void testGetDef() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef();
    
    // Test case 1: Variable with a single definition
    Node def1 = new Node(Token.VAR, new Node(Token.NAME, new Node(Token.STRING, "x")), new Node(Token.NUMBER, 10));
    Node use1 = new Node(Token.NAME, new Node(Token.STRING, "x"));
    Node use2 = new Node(Token.ASSIGN, new Node(Token.NAME, new Node(Token.STRING, "x")), new Node(Token.STRING, "Hello"));
    analysis.flowThrough(def1, MustDef.createEntryLattice());
    Node result1 = analysis.getDef("x", use1);
    assertEquals(def1, result1);
    Node result2 = analysis.getDef("x", use2);
    assertEquals(def1, result2);
    
    // Test case 2: Variable with multiple definitions
    Node def2_1 = new Node(Token.VAR, new Node(Token.NAME, new Node(Token.STRING, "y")), new Node(Token.NUMBER, 20));
    Node def2_2 = new Node(Token.VAR, new Node(Token.NAME, new Node(Token.STRING, "y")), new Node(Token.NUMBER, 30));
    Node use3 = new Node(Token.NAME, new Node(Token.STRING, "y"));
    analysis.flowThrough(def2_1, MustDef.createEntryLattice());
    analysis.flowThrough(def2_2, analysis.flowThrough(def2_1, MustDef.createEntryLattice()));
    Node result3 = analysis.getDef("y", use3);
    assertEquals(def2_2, result3);
  }
  
  @Test
  public void testDependsOnOuterScopeVars() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef();
    
    // Test case 1: Variable that does not depend on outer scope variables
    Node def1 = new Node(Token.VAR, new Node(Token.NAME, new Node(Token.STRING, "a")), new Node(Token.NUMBER, 10));
    analysis.flowThrough(def1, MustDef.createEntryLattice());
    boolean result1 = analysis.dependsOnOuterScopeVars("a", def1);
    assertFalse(result1);
    
    // Test case 2: Variable that depends on outer scope variables
    Node def2 = new Node(Token.VAR, new Node(Token.NAME, new Node(Token.STRING, "b")), new Node(Token.NAME, new Node(Token.STRING, "x")));
    analysis.flowThrough(def2, MustDef.createEntryLattice());
    boolean result2 = analysis.dependsOnOuterScopeVars("b", def2);
    assertTrue(result2);
  }

}
