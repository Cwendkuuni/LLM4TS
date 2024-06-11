import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new ExpressionImpl("");
        List externalFunctions = expression.getExternalFunctions();
        Assert.assertNotNull(externalFunctions);
    }

    @Test
    public void testConstructor() {
        Expression expression = new ExpressionImpl("1 + 2");
        Assert.assertNotNull(expression);
    }

    @Test
    public void testDoEvaluationStack() {
        Expression expression = new ExpressionImpl("1 + 2");
        expression.doEvaluationStack();
        // Add asserts
    }

    @Test
    public void testGetReservedFunctions() {
        FunctionExpression functionExpression = Expression.getReservedFunctions("+");
        Assert.assertNotNull(functionExpression);
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new ExpressionImpl("");
        boolean result = expression.isReservedFunction("+");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new ExpressionImpl("");
        boolean result = expression.isLogicalFunction("and");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new ExpressionImpl("");
        boolean result = expression.isMathFunction("+");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new ExpressionImpl("");
        boolean result = expression.isDateFunction("date");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new ExpressionImpl("");
        boolean result = expression.isStringFunction("substring");
        Assert.assertTrue(result);
    }

    @Test
    public void testGetExternalFunction() {
        Expression expression = new ExpressionImpl("");
        FunctionExpression functionExpression = expression.getExternalFunction("function");
        Assert.assertNotNull(functionExpression);
    }

    @Test
    public void testIsExternalFunction() {
        Expression expression = new ExpressionImpl("");
        boolean result = expression.isExternalFunction("function");
        Assert.assertTrue(result);
    }

    @Test
    public void testGetFunction() {
        Expression expression = new ExpressionImpl("");
        FunctionExpression functionExpression = expression.getFunction("+");
        Assert.assertNotNull(functionExpression);
    }

    @Test
    public void testIsFunction() {
        Expression expression = new ExpressionImpl("");
        boolean result = expression.isFunction("+");
        Assert.assertTrue(result);
    }

    @Test
    public void testConstructor2() {
        Expression expression = new ExpressionImpl();
        Assert.assertNotNull(expression);
    }

    @Test
    public void testPrepare() {
        Expression expression = new ExpressionImpl("");
        expression.prepare();
        // Add asserts
    }

    @Test
    public void testGetStackCopy() {
        Expression expression = new ExpressionImpl("");
        ExpressionStack stack = expression.getStackCopy();
        Assert.assertNotNull(stack);
    }

    @Test
    public void testEvaluate() throws Exception {
        Expression expression = new ExpressionImpl("");
        Object result = expression.evaluate();
        // Add asserts
    }

    @Test
    public void testTokenize() {
        Expression expression = new ExpressionImpl("");
        expression.tokenize();
        // Add asserts
    }

}