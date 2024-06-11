import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationNotEqualTest {
  
  @Test
  public void testComputeValue() {
    Expression arg1 = new Expression();
    Expression arg2 = new Expression();
    CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
    
    EvalContext context = new EvalContext();
    Object result = operation.computeValue(context);
    
    assertTrue(result instanceof Boolean);
  }
  
  @Test
  public void testGetSymbol() {
    Expression arg1 = new Expression();
    Expression arg2 = new Expression();
    CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
    
    String symbol = operation.getSymbol();
    
    assertEquals("!=", symbol);
  }

}