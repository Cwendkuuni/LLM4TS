package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

class CoreOperationLessThanOrEqualTest {

  @Test
  public void testConstructor() {
    Expression arg1 = new ConstantExpression(1.0);
    Expression arg2 = new ConstantExpression(2.0);
    CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
    // No exception is thrown, so the test passes
  }

  @Test
  public void testComputeValue() {
    EvalContext context = new EvalContext();
    Expression arg1 = new ConstantExpression(1.0);
    Expression arg2 = new ConstantExpression(2.0);
    CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
    boolean result = coreOperationLessThanOrEqual.computeValue(context).booleanValue();
    assertTrue(result);
  }

  @Test
  public void testEdgeCasesAndExceptions() {
    EvalContext context = new EvalContext();
    // Test for negative numbers
    Expression arg1 = new ConstantExpression(-1.0);
    Expression arg2 = new ConstantExpression(1.0);
    CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
    assertFalse(coreOperationLessThanOrEqual.computeValue(context).booleanValue());

    // Test for equal numbers
    arg1 = new ConstantExpression(1.0);
    arg2 = new ConstantExpression(1.0);
    coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
    assertFalse(coreOperationLessThanOrEqual.computeValue(context).booleanValue());

    // Test for one argument being null
    Expression arg1Null = null;
    assertThrows(NullPointerException.class, () -> new CoreOperationLessThanOrEqual(arg1Null, arg2));

    // Test for both arguments being null
    Expression arg2Null = null;
    assertThrows(NullPointerException.class, () -> new CoreOperationLessThanOrEqual(arg1, arg2Null));

    // Test for invalid arguments (non-numeric values)
    Expression arg1Invalid = new StringExpression("invalid value");
    assertThrows(ClassCastException.class, () -> new CoreOperationLessThanOrEqual(arg1Invalid, arg2));
  }
}