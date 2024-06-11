package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.test.ExpressionEvalTestBase;

public class CoreOperationLessThanOrEqualTest extends ExpressionEvalTestBase {

    @Override
    protected String getExpressionClassName() {
        return CoreOperationLessThanOrEqual.class.getName();
    }

    @Test
    public void testComputeValue_boolean_boolean() {
        boolean arg1 = false;
        boolean arg2 = true;
        EvalContext context = createContext();

        Object result = new CoreOperationLessThanOrEqual(
                new ConstantExpression(arg1), new ConstantExpression(arg2)).computeValue(context);
         assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_double_double_less() {
        double arg1 = 2.0;
        double arg2 = 3.0;
        EvalContext context = createContext();

        Object result = new CoreOperationLessThanOrEqual(
                new ConstantExpression(arg1), new ConstantExpression(arg2)).computeValue(context);
         assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_double_double_equal() {
        double arg1 = 2.0;
        double arg2 = 2.0;
        EvalContext context = createContext();

        Object result = new CoreOperationLessThanOrEqual(
                new ConstantExpression(arg1), new ConstantExpression(arg2)).computeValue(context);
         assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_double_double_greater() {
        double arg1 = 3.0;
        double arg2 = 2.0;
        EvalContext context = createContext();

        Object result = new CoreOperationLessThanOrEqual(
                new ConstantExpression(arg1), new ConstantExpression(arg2)).computeValue(context);
         assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_null_double() {
        double arg2 = 2.0;
        EvalContext context = createContext();

        Object result = new CoreOperationLessThanOrEqual(
                new ConstantExpression(null), new ConstantExpression(arg2)).computeValue(context);
         assertNull(result);
    }

    @Test
    public void testComputeValue_double_null() {
        double arg1 = 2.0;
        EvalContext context = createContext();

        Object result = new CoreOperationLessThanOrEqual(
                new ConstantExpression(arg1), new ConstantExpression(null)).computeValue(context);
         assertNull(result);
    }

    @Test
    public void testComputeValue_int_int_less() {
        int arg1 = 2;
        int arg2 = 3;
        EvalContext context = createContext();

        Object result = new CoreOperationLessThanOrEqual(
                new ConstantExpression(arg1), new ConstantExpression(arg2)).computeValue(context);
         assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_int_int_equal() {
        int arg1 = 2;
        int arg2 = 2;
        EvalContext context = createContext();

        Object result = new CoreOperationLessThanOrEqual(
                new ConstantExpression(arg1), new ConstantExpression(arg2)).computeValue(context);
         assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_int_int_greater() {
        int arg1 = 3;
        int arg2 = 2;
        EvalContext context = createContext();

        Object result = new CoreOperationLessThanOrEqual(
                new ConstantExpression(arg1), new ConstantExpression(arg2)).computeValue(context);
         assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_String_String() {
        String arg1 = "3.14";
        String arg2 = "2.718";
        EvalContext context = createContext();

        Object result = new CoreOperationLessThanOrEqual(
                new ConstantStringExpression(arg1), new ConstantStringExpression(arg2)).computeValue(context);
         assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_String_String_less() {
        String arg1 = "1.2";
        String arg2 = "2.3";
        EvalContext context = createContext();

        Object result = new CoreOperationLessThanOrEqual(
                new ConstantStringExpression(arg1), new ConstantStringExpression(arg2)).computeValue(context);
         assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_String_String_equal() {
        String arg1 = "1.2";
        String arg2 = "1.2";
        EvalContext context = createContext();

        Object result = new CoreOperationLessThanOrEqual(
                new ConstantStringExpression(arg1), new ConstantStringExpression(arg2)).computeValue(context);
         assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_String_String_greater() {
        String arg1 = "2.3";
        String arg2 = "1.2";
        EvalContext context = createContext();

        Object result = new CoreOperationLessThanOrEqual(
                new ConstantStringExpression(arg1), new ConstantStringExpression(arg2)).computeValue(context);
         assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_String_String_invalid() {
        String arg1 = "foo";
        String arg2 = "bar";
        EvalContext context = createContext();

        Object result = new CoreOperationLessThanOrEqual(
                new ConstantStringExpression(arg1), new ConstantStringExpression(arg2)).computeValue(context);
         assertNull(result);
    }

    @Test
    public void testComputeValue_String_number() {
        String arg1 = "3.14";
        double arg2 = 2.718;
        EvalContext context = createContext();

        Object result = new CoreOperationLessThanOrEqual(
                new ConstantStringExpression(arg1), new ConstantExpression(arg2)).computeValue(context);
         assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_number_String() {
        double arg1 = 3.14;
        String arg2 = "2.718";
        EvalContext context = createContext();

        Object result = new CoreOperationLessThanOrEqual(
                new ConstantExpression(arg1), new ConstantStringExpression(arg2)).computeValue(context);
         assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_number_number() {
        double arg1 = 3.14;
        double arg2 = 2.718;
        EvalContext context = createContext();

        Object result = new CoreOperationLessThanOrEqual(
                new ConstantExpression(arg1), new ConstantExpression(arg2)).computeValue(context);
         assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_number_null() {
        double arg1 = 3.14;
        EvalContext context = createContext();

        Object result = new CoreOperationLessThanOrEqual(
                new ConstantExpression(arg1), new ConstantExpression(null)).computeValue(context);
         assertNull(result);
    }

    @Test
    public void testComputeValue_null_number() {
        double arg2 = 3.14;
        EvalContext context = createContext();

        Object result = new CoreOperationLessThanOrEqual(
                new ConstantExpression(null), new ConstantExpression(arg2)).computeValue(context);
         assertNull(result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                new ConstantExpression(3), new ConstantExpression(5));
        assertEquals("<=", op.getSymbol());
    }

}