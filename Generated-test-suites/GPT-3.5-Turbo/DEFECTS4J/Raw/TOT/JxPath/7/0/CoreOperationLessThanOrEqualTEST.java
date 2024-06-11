import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.compiler.*;

public class CoreOperationLessThanOrEqualTest {
  
  @Test
  public void testComputeValue() {
    EvalContext context = new EvalContext();
    
    CoreOperationLessThanOrEqual coreOp1 = new CoreOperationLessThanOrEqual(new CoreOperationLiteral("10"), new CoreOperationLiteral("5"));
    assertEquals(Boolean.FALSE, coreOp1.computeValue(context));
    
    CoreOperationLessThanOrEqual coreOp2 = new CoreOperationLessThanOrEqual(new CoreOperationLiteral("5"), new CoreOperationLiteral("10"));
    assertEquals(Boolean.TRUE, coreOp2.computeValue(context));
    
    CoreOperationLessThanOrEqual coreOp3 = new CoreOperationLessThanOrEqual(new CoreOperationLiteral("5"), new CoreOperationLiteral("5"));
    assertEquals(Boolean.TRUE, coreOp3.computeValue(context));
    
    CoreOperationLessThanOrEqual coreOp4 = new CoreOperationLessThanOrEqual(new CoreOperationLiteral("-2.5"), new CoreOperationLiteral("-1.5"));
    assertEquals(Boolean.TRUE, coreOp4.computeValue(context));
    
    CoreOperationLessThanOrEqual coreOp5 = new CoreOperationLessThanOrEqual(new CoreOperationLiteral("-5.5"), new CoreOperationLiteral("-14"));
    assertEquals(Boolean.FALSE, coreOp5.computeValue(context));
  }
  
  @Test
  public void testGetSymbol() {
    CoreOperationLessThanOrEqual coreOp = new CoreOperationLessThanOrEqual(new CoreOperationLiteral("5"), new CoreOperationLiteral("10"));
    assertEquals("<=", coreOp.getSymbol());
  }
}