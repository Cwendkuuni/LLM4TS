
import org.junit.Test;

public class MustBeReachingVariableDefTest {

  @Test
  public void testGetDef() {
    // Test getting definition of a local variable at a specific use node
    Node useNode = new Node(Token.NAME, "x");
    Node defNode = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NUMBER, "10"));
    MustBeReachingVariableDef mustDef = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node result = mustDef.getDef("x", useNode);
    assertEquals(defNode, result);
  }

  @Test
  public void testDependsOnOuterScopeVars() {
    // Test checking if a variable depends on outer scope variables
    Node useNode = new Node(Token.NAME, "x");
    MustBeReachingVariableDef mustDef = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    boolean result = mustDef.dependsOnOuterScopeVars("x", useNode);
    assertTrue(result);
  }
}
