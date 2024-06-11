package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.functions.FunctionContext;

public class CoreOperationEqualTest {

  @org.junit.Before
  public void setUp() {
  }

  @org.junit.After
  public void tearDown() {
  }

  /**
   * Test constructor.
   */
  @org.junit.Test
  public void testCoreOperationEqualConstruction() {
    Expression arg1 = new CoreConstantExpression(1);
    Expression arg2 = new CoreConstantExpression(1);
    CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    assertNotNull(coreOperationEqual);
  }

  /**
   * Test constructor with different types of expressions.
   */
  @org.junit.Test
  public void testCoreOperationEqualWithDifferentTypes() {
    Expression arg1 = new CoreConstantExpression(1);
    Expression arg2 = new CoreFunctionCallExpression(new CoreFunctionName("string:concat"), new Path(""), new Path("")) {
      @Override
      public Object computeValue(EvalContext context) {
        return "test";
      }
    };
    CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    assertNotNull(coreOperationEqual);
  }

  /**
   * Test getSymbol method.
   */
  @org.junit.Test
  public void testGetSymbol() {
    Expression arg1 = new CoreConstantExpression(1);
    Expression arg2 = new CoreConstantExpression(1);
    CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    String symbol = coreOperationEqual.getSymbol();
    assertEquals("=", symbol);
  }

  /**
   * Test computeValue method with boolean values.
   */
  @org.junit.Test
  public void testComputeValueWithBooleanValues() {
    Expression arg1 = new CoreConstantExpression(Boolean.TRUE);
    Expression arg2 = new CoreConstantExpression(Boolean.TRUE);
    CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    EvalContext context = new EvalContext();
    Object result = coreOperationEqual.computeValue(context);
    assertTrue(result instanceof Boolean);
    assertEquals(Boolean.TRUE, result);

    arg1 = new CoreConstantExpression(Boolean.TRUE);
    arg2 = new CoreConstantExpression(Boolean.FALSE);
    coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    context = new EvalContext();
    result = coreOperationEqual.computeValue(context);
    assertFalse(result instanceof Boolean);
    assertNull(result);
  }

  /**
   * Test computeValue method with integer values.
   */
  @org.ujava.Test
  public void testComputeValueWithIntegerValues() {
    Expression arg1 = new CoreConstantExpression(1);
    Expression arg2 = new CoreConstantExpression(1);
    CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    EvalContext context = new EvalContext();
    Object result = coreOperationEqual.computeValue(context);
    assertTrue(result instanceof Boolean);
    assertEquals(Boolean.TRUE, result);

    arg1 = new CoreConstantExpression(1);
    arg2 = new CoreConstantExpression(2);
    coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    context = new EvalContext();
    result = coreOperationEqual.computeValue(context);
    assertFalse(result instanceof Boolean);
    assertNull(result);
  }

  /**
   * Test computeValue method with string values.
   */
  @org.junit.Test
  public void testComputeValueWithStringValues() {
    Expression arg1 = new CoreConstantExpression("test");
    Expression arg2 = new CoreConstantExpression("test");
    CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    EvalContext context = new EvalContext();
    Object result = coreOperationEqual.computeValue(context);
    assertTrue(result instanceof Boolean);
    assertEquals(Boolean.TRUE, result);

    arg1 = new CoreConstantExpression("test");
    arg2 = new CoreConstantExpression("otherTest");
    coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    context = new EvalContext();
    result = coreOperationEqual.computeValue(context);
    assertFalse(result instanceof Boolean);
    assertNull(result);
  }

  /**
   * Test computeValue method with different types of values.
   */
  @org.junit.Test
  public void testComputeValueWithDifferentTypes() {
    Expression arg1 = new CoreConstantExpression(1);
    Expression arg2 = new CoreConstantExpression("test");
    CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    EvalContext context = new EvalContext();
    Object result = coreOperationEqual.computeValue(context);
    assertFalse(result instanceof Boolean);
    assertNull(result);
  }

  /**
   * Test computeValue method with function calls.
   */
  @org.junit.Test
  public void testComputeValueWithFunctionCalls() {
    Expression arg1 = new CoreFunctionCallExpression(new CoreFunctionName("string:length"), new CoreFunctionCallExpression(new CoreFunctionName("string:concat"), new Path(""), new Path("test"))) {
      @Override
      public Object computeValue(EvalContext context) {
        FunctionContext functionContext = (FunctionContext) context;
        return functionContext.getValue(0).toString().length();
      }
    };
    Expression arg2 = new CoreConstantExpression(5);
    CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    EvalContext context = new EvalContext();
    Object result = coreOperationEqual.computeValue(context);
    assertTrue(result instanceof Boolean);
    assertEquals(Boolean.TRUE, result);

    arg1 = new CoreFunctionCallExpression(new CoreFunctionName("string:length"), new CoreFunctionCallExpression(new CoreFunctionName("string:concat"), new Path(""), new Path("test"))) {
      @Override
      public Object computeValue(EvalContext context) {
        FunctionContext functionContext = (FunctionContext) context;
        return functionContext.getValue(0).toString().length();
      }
    };
    arg2 = new CoreFunctionCallExpression(new CoreFunctionName("string:length"), new CoreFunctionCallExpression(new CoreFunctionName("string:concat"), new Path(""), new Path("otherTest"))) {
      @Override
      public Object computeValue(EvalContext context) {
        FunctionContext functionContext = (FunctionContext) context;
        return functionContext.getValue(0).toString().length();
      }
    };
    coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    context = new EvalContext();
    result = coreOperationEqual.computeValue(context);
    assertFalse(result instanceof Boolean);
    assertNull(result);
  }
}