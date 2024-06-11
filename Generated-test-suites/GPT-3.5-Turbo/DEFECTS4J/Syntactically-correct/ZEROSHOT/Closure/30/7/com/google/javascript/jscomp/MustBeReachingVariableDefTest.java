import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MustBeReachingVariableDefTest {
  
  private MustBeReachingVariableDef mustBeReachingVariableDef;
  
  @Before
  public void setUp() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(); // create a mock control flow graph
    Scope jsScope = new Scope(); // create a mock scope
    AbstractCompiler compiler = new AbstractCompiler(); // create a mock compiler
    mustBeReachingVariableDef = new MustBeReachingVariableDef(cfg, jsScope, compiler);
  }
  
  @Test
  public void testGetDef() {
    // create a mock node and useNode
    Node node = new Node(Token.NAME, "name");
    Node useNode = new Node(Token.USE_NAME, "use");
    
    // call the getDef method
    Node result = mustBeReachingVariableDef.getDef("name", useNode);
    
    // assert the result
    assertNotNull(result);
  }
  
  @Test
  public void testDependsOnOuterScopeVars() {
    // create a mock node and useNode
    Node node = new Node(Token.NAME, "name");
    Node useNode = new Node(Token.USE_NAME, "use");
    
    // call the dependsOnOuterScopeVars method
    boolean result = mustBeReachingVariableDef.dependsOnOuterScopeVars("name", useNode);
    
    // assert the result
    assertFalse(result);
  }
}