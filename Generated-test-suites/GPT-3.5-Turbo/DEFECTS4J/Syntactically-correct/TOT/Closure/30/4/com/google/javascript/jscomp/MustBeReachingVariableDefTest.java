import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

  @Test
  public void testGetDefWithValidVariable() {
    MustBeReachingVariableDef mbvd = createMustBeReachingVariableDef();
    Node def = mbvd.getDef("x", new Node(Token.NAME));
    assertEquals(new Node(Token.VAR, new Node(Token.NAME, "x"), 
        new Node(Token.STRING, "Hello")), def);
  }

  @Test
  public void testGetDefWithInvalidVariable() {
    MustBeReachingVariableDef mbvd = createMustBeReachingVariableDef();
    Node def = mbvd.getDef("y", new Node(Token.NAME));
    assertNull(def);
  }

  @Test
  public void testDependsOnOuterScopeVarsWithValidVariable() {
    MustBeReachingVariableDef mbvd = createMustBeReachingVariableDef();
    boolean dependsOnOuter = mbvd.dependsOnOuterScopeVars("x", new Node(Token.NAME));
    assertFalse(dependsOnOuter);
  }

  @Test
  public void testDependsOnOuterScopeVarsWithInvalidVariable() {
    MustBeReachingVariableDef mbvd = createMustBeReachingVariableDef();
    boolean dependsOnOuter = mbvd.dependsOnOuterScopeVars("y", new Node(Token.NAME));
    assertFalse(dependsOnOuter);
  }

  private MustBeReachingVariableDef createMustBeReachingVariableDef() {
    // Create a Mock ControlFlowGraph
    ControlFlowGraph<Node> cfg = createMock(ControlFlowGraph.class);
    // Create a Mock Scope
    Scope jsScope = createMock(Scope.class);
    // Create a Mock AbstractCompiler
    AbstractCompiler compiler = createMock(AbstractCompiler.class);

    return new MustBeReachingVariableDef(cfg, jsScope, compiler);
  }
}