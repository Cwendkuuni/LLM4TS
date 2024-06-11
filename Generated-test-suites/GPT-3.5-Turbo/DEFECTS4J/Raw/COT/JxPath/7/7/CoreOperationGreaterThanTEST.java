import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

  @Test
  public void testComputeValue() {
    CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());
    
    // Test case 1
    double arg1 = 5.0;
    double arg2 = 3.0;
    EvalContext context1 = new EvalContext();
    context1.setVariable("arg1", arg1);
    context1.setVariable("arg2", arg2);
    Object expected1 = true;
    Object actual1 = operation.computeValue(context1);
    assertEquals(expected1, actual1);
    
    // Test case 2
    double arg3 = 5.0;
    double arg4 = 7.0;
    EvalContext context2 = new EvalContext();
    context2.setVariable("arg3", arg3);
    context2.setVariable("arg4", arg4);
    Object expected2 = false;
    Object actual2 = operation.computeValue(context2);
    assertEquals(expected2, actual2);
  }
  
  @Test
  public void testGetSymbol() {
    CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());
    String expected = ">";
    String actual = operation.getSymbol();
    assertEquals(expected, actual);
  }
}