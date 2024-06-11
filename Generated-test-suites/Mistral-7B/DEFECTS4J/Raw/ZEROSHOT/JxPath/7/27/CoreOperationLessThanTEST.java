package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.test.ExpressionTestCase;

public class CoreOperationLessThanTest extends ExpressionTestCase {

    public CoreOperationLessThanTest() {
        super("CoreOperationLessThan");
    }

    @Override
    protected Class<? extends CoreOperationLessThan> getExpressionClass() {
        return CoreOperationLessThan.class;
    }

    public void testCoreOperationLessThan_BothArgsDouble_ReturnsCorrectBooleanValue() {
        // Given
        CoreOperationLessThan expression = new CoreOperationLessThan(new ConstantExpression(1.5), new ConstantExpression(2.2));
         // When
         Boolean result = (Boolean) expression.computeValue(new EvalContext());
         // Then
         assertTrue(result);
    }

    public void testCoreOperationLessThan_FirstArgDouble_SecondArgDouble_ReturnsCorrectBooleanValue() {
        // Given
        CoreOperationLessThan expression = new CoreOperationLessThan(new ConstantExpression(1.5), new ConstantExpression(2.5));
         // When
         Boolean result = (Boolean) expression.computeValue(new EvalContext());
         // Then
         assertFalse(result);
    }

    public void testCoreOperationLessThan_FirstArgDouble_SecondArgInt() {
        // Given
        CoreOperationLessThan expression = new CoreOperationLessThan(new ConstantExpression(1.5), new ConstantExpression(2));
         // When
         Boolean result = (Boolean) expression.computeValue(new EvalContext());
         // Then
         assertTrue(result);
    }

    public void testCoreOperationLessThan_FirstArgInt_SecondArgDouble() {
        // Given
        CoreOperationLessThan expression = new CoreOperationLessThan(new ConstantExpression(2), new ConstantExpression(1.5));
         // When
         Boolean result = (Boolean) expression.computeValue(new EvalContext());
         // Then
         assertFalse(result);
    }

    public void testCoreOperationLessThan_FirstArgInt_SecondArgInt() {
        // Given
        CoreOperationLessThan expression = new CoreOperationLessThan(new ConstantExpression(2), new ConstantExpression(3));
         // When
         Boolean result = (Boolean) expression.computeValue(new EvalContext());
         // Then
         assertFalse(result);
    }

    public void testCoreOperationLessThan_FirstArgNull_SecondArgNull() {
        // Given
        CoreOperationLessThan expression = new CoreOperationLessThan(null, null);
         // When
         try {
             expression.computeValue(new EvalContext());
         } catch (NullPointerException e) {
             // Then
             assertEquals("args[0] is marked not null in CoreOperationLessThan constructor", e.getMessage());
         }
    }

    public void testCoreOperationLessThan_FirstArgNull_SecondArgNotNull() {
        // Given
        CoreOperationLessThan expression = new CoreOperationLessThan(null, new ConstantExpression(1));
         // When
         try {
             expression.computeValue(new EvalContext());
         } catch (NullPointerException e) {
             // Then
             assertEquals("args[0] is marked not null in CoreOperationLessThan constructor", e.getMessage());
         }
    }

    public void testCoreOperationLessThan_FirstArgNotNull_SecondArgNull() {
        // Given
        CoreOperationLessThan expression = new CoreOperationLessThan(new ConstantExpression(1), null);
         // When
         try {
             expression.computeValue(new EvalContext());
         } catch (NullPointerException e) {
             // Then
             assertEquals("args[1] is marked not null in CoreOperationLessThan constructor", e.getMessage());
         }
    }
}