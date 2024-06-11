package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.TestBase;
import org.junit.*;

public class CoreOperationGreaterThanTest extends TestBase {


@BeforeClass
public static void setup() {
    setContext(createEvaluationContext());
}

@Test
public void testComputeValue() {
    Expression arg1 = new LiteralExpression(5.5, getType(Double.class));
    Expression arg2 = new LiteralExpression(3.0, getType(Double.class));
    CoreOperationGreaterThan expression = new CoreOperationGreaterThan(arg1, arg2);
    Object result = expression.computeValue(ctx);
    assertTrue(result.equals(Boolean.TRUE));
}

@Test
public void testComputeValueWithNonNumericValues() {
    Expression arg1 = new StringLiteralExpression("Hello", getType(String.class));
    Expression arg2 = new StringLiteralExpression("World", getType(String.class));
    CoreOperationGreaterThan expression = new CoreOperationGreaterThan(arg1, arg2);
    EvaluationException exception = assertThrows(EvaluationException.class, () -> expression.computeValue(ctx));
    assertEquals("Error executing CoreOperationGreaterThan: Cannot convert String to number", exception.getMessage());
}

@Test
public void testComputeValueWithEqualValues() {
    Expression arg1 = new LiteralExpression(5.5, getType(Double.class));
    Expression arg2 = new LiteralExpression(5.5, getType(Double.class));
    CoreOperationGreaterThan expression = new CoreOperationGreaterThan(arg1, arg2);
    Object result = expression.computeValue(ctx);
    assertFalse(result.equals(Boolean.TRUE));
}

@Test
public void testComputeValueWithNegativeNumbers() {
    Expression arg1 = new LiteralExpression(-5.5, getType(Double.class));
    Expression arg2 = new LiteralExpression(3.0, getType(Double.class));
    CoreOperationGreaterThan expression = new CoreOperationGreaterThan(arg1, arg2);
    Object result = expression.computeValue(ctx);
    assertTrue(result.equals(Boolean.FALSE));
}

@Test(expected = NullPointerException.class)
public void testComputeValueWithNullArguments() {
    Expression arg1 = null;
    Expression arg2 = new LiteralExpression(3.0, getType(Double.class));
    CoreOperationGreaterThan expression = new CoreOperationGreaterThan(arg1, arg2);
    assertNull(expression.computeValue(ctx));
}

@Test(expected = EvaluationException.class)
public void testComputeValueWithInvalidArguments() {
    Expression arg1 = new LiteralExpression("InvalidInput");
    Expression arg2 = new LiteralExpression(3.0, getType(Double.class));
    CoreOperationGreaterThan expression = new CoreOperationGreaterThan(arg1, arg2);
    assertThrows(EvaluationException.class, () -> expression.computeValue(ctx));
}


}