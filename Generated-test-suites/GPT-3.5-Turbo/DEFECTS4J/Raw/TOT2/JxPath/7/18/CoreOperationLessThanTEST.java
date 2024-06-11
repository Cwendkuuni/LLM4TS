import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanTest {
  
  @Test
  public void testComputeValue() {
    CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(2), new Constant(5));
    EvalContext context = new EvalContext();
    
    assertTrue((boolean)operation.computeValue(context));
  }
  
  @Test
  public void testGetSymbol() {
    CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(2), new Constant(5));
    
    assertEquals("<", operation.getSymbol());
  }
  
  @Test
  public void testComputeValueWithNegativeNumbers() {
    CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(-2), new Constant(-5));
    EvalContext context = new EvalContext();
    
    assertFalse((boolean)operation.computeValue(context));
  }
  
  @Test
  public void testComputeValueWithDoubleValues() {
    CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(2.5), new Constant(5.7));
    EvalContext context = new EvalContext();
    
    assertTrue((boolean)operation.computeValue(context));
  }
  
  @Test
  public void testComputeValueEqualNumbers() {
    CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(5), new Constant(5));
    EvalContext context = new EvalContext();
    
    assertFalse((boolean)operation.computeValue(context));
  }
  
  @Test
  public void testComputeValueWithNullValues() {
    CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(null), new Constant(5));
    EvalContext context = new EvalContext();
    
    assertFalse((boolean)operation.computeValue(context));
  }
  
}