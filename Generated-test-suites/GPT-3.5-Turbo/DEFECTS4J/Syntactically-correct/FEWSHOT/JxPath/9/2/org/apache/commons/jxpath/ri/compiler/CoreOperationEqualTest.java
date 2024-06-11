package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

  @Test
  public void testComputeValue() {
    Expression arg1 = new Expression();
    Expression arg2 = new Expression();
    CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
  
    EvalContext context = new EvalContext();
    context.setVariable("arg1", arg1);
    context.setVariable("arg2", arg2);
  
    assertTrue(operationEqual.computeValue(context));
  }
  
  @Test
  public void testGetSymbol() {
    Expression arg1 = new Expression();
    Expression arg2 = new Expression();
    CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
  
    assertEquals("=", operationEqual.getSymbol());
  }
  
}