import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqualExpression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.util.ExpressionUtil;
import org.junit.Test;
import org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testNotEqual_Boolean_Boolean() {
        Expression arg1 = ExpressionUtil.literalExpression(true);
        Expression arg2 = ExpressionUtil.literalExpression(false);
        CoreOperationNotEqual op = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testNotEqual_Boolean_Boolean_Equal() {
        Expression arg1 = ExpressionUtil.literalExpression(true);
        Expression arg2 = ExpressionUtil.literalExpression(true);
        CoreOperationNotEqual op = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEqual_Number_Number_WrongTypes() {
        Expression arg1 = ExpressionUtil.literalExpression(1.0);
        Expression arg2 = ExpressionUtil.literalExpression("str");
        CoreOperationNotEqual op = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        op.computeValue(context); // should throw an exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEqual_Null_Expression_NullArg() {
        Expression arg1 = ExpressionUtil.literalExpression(null);
        Expression arg2 = new CoreOperationNumber("1.0");
        CoreOperationNotEqual op = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        op.computeValue(context); // should throw an exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEqual_Expression_Null_NullArg() {
        Expression arg1 = new CoreOperationNumber("1.0");
        Expression arg2 = ExpressionUtil.literalExpression(null);
        CoreOperationNotEqual op = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        op.computeValue(context); // should throw an exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEqual_Expression_Number_WrongTypes() {
        Expression arg1 = new CoreOperationString("str");
        Expression arg2 = ExpressionUtil.literalExpression(1.0);
        CoreOperationNotEqual op = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        op.computeValue(context); // should throw an exception
    }

    @Test
    public void testNotEqual_Number_Number() {
        Expression arg1 = ExpressionUtil.literalExpression(1.0);
        Expression arg2 = ExpressionUtil.literalExpression(2.0);
        CoreOperationNotEqual op = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testNotEqual_String_String() {
        Expression arg1 = ExpressionUtil.literalExpression("str1");
        Expression arg2 = ExpressionUtil.literalExpression("str2");
        CoreOperationNotEqual op = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testNotEqual_String_String_Equal() {
        Expression arg1 = ExpressionUtil.literalExpression("str1");
        Expression arg2 = ExpressionUtil.literalExpression("str1");
        CoreOperationNotEqual op = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }
}