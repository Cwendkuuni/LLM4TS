import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {
  
  private CoreOperationGreaterThanOrEqual operation;

  @Before
  public void setUp() {
    operation = new CoreOperationGreaterThanOrEqual(expression1, expression2);
  }

  @Test
  public void testComputeValue_withValidExpressions_returnsTrue() {
    EvalContext context = new EvalContext();
    context.setContextNode(expression1ContextNode);
    
    when(expression1.computeValue(context)).thenReturn(5.0);
    when(expression2.computeValue(context)).thenReturn(3.0);
    
    Object result = operation.computeValue(context);
    
    assertTrue((boolean) result);
  }

  @Test
  public void testComputeValue_withValidExpressions_returnsFalse() {
    EvalContext context = new EvalContext();
    context.setContextNode(expression1ContextNode);
    
    when(expression1.computeValue(context)).thenReturn(3.0);
    when(expression2.computeValue(context)).thenReturn(5.0);
    
    Object result = operation.computeValue(context);
    
    assertFalse((boolean) result);
  }

  @Test(expected = NullPointerException.class)
  public void testComputeValue_withNullContext_throwsNullPointerException() {
    operation.computeValue(null);
  }

  @Test
  public void testGetSymbol_returnsGreaterThanOrEqualToSymbol() {
    String symbol = operation.getSymbol();
    
    assertEquals(">=", symbol);
  }

}