import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
  
  ClosureReverseAbstractInterpreter interpreter;
  
  @Before
  public void setUp() {
    interpreter = new ClosureReverseAbstractInterpreter();
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_WhenConditionIsCallAndOutcomeIsTrue_ReturnsNotNull() {
    //Arrange
    Node condition = createTestNode();
    FlowScope blindScope = createTestFlowScope();
    boolean outcome = true;
    
    //Act
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    
    //Assert
    assertNotNull(result);
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_WhenConditionIsCallAndOutcomeIsFalse_ReturnsNull() {
    //Arrange
    Node condition = createTestNode();
    FlowScope blindScope = createTestFlowScope();
    boolean outcome = false;
    
    //Act
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    
    //Assert
    assertNull(result);
  }
  
  private Node createTestNode() {
    //Create a test Node object
    return new Node();
  }
  
  private FlowScope createTestFlowScope() {
    //Create a test FlowScope object
    return new FlowScope();
  }
}