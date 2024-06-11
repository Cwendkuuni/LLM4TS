import static org.junit.Assert.*;
import org.junit.Test;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression exp = new Expression("1 + 2");
        assertEquals(null, exp.getExternalFunctions());
    }

    @Test
    public void testExpression() {
        Expression exp = new Expression("1 + 2");
        assertEquals(false, exp.prepared);
        assertEquals("1 + 2", exp.expression);
        assertEquals(null, exp.stack);
        assertEquals(null, exp.externalFunctions);
    }

    @Test
    public void testDoEvaluationStack() {
        Expression exp = new Expression("1 + 2");
        exp.stack = new ExpressionStack();
        exp.stack.add("1");
        exp.stack.add("+");
        exp.stack.add("2");
        exp.doEvaluationStack();
        assertEquals("1 2 +", exp.stack.toString());
    }

    @Test
    public void testPutFunction() {
        Expression exp = new Expression();
        Map map = new TreeMap();
        FunctionExpression f = new AndFunction();
        exp.putFunction(f, map);
        assertEquals(f, map.get(f));
    }

    @Test
    public void testGetReservedFunctions() {
        assertEquals(null, Expression.getReservedFunctions("invalid"));
        assertEquals(true, Expression.getReservedFunctions("and") instanceof AndFunction);
    }

    @Test
    public void testIsReservedFunction() {
        Expression exp = new Expression("1 + 2");
        assertEquals(false, exp.isReservedFunction("invalid"));
        assertEquals(true, exp.isReservedFunction("and"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression exp = new Expression("1 + 2");
        assertEquals(false, exp.isLogicalFunction("invalid"));
        assertEquals(true, exp.isLogicalFunction("and"));
    }

    @Test
    public void testIsMathFunction() {
        Expression exp = new Expression("1 + 2");
        assertEquals(false, exp.isMathFunction("invalid"));
        assertEquals(true, exp.isMathFunction("abs"));
    }

    @Test
    public void testIsDateFunction() {
        Expression exp = new Expression("1 + 2");
        assertEquals(false, exp.isDateFunction("invalid"));
        assertEquals(true, exp.isDateFunction("dateadd"));
    }

    @Test
    public void testIsStringFunction() {
        Expression exp = new Expression("1 + 2");
        assertEquals(false, exp.isStringFunction("invalid"));
        assertEquals(true, exp.isStringFunction("contains"));
    }

    @Test
    public void testGetExternalFunction() {
        Expression exp = new Expression("1 + 2");
        exp.externalFunctions = new ArrayList<FunctionExpression>();
        FunctionExpression f1 = new FunctionExpression();
        f1.setSymbol("symbol1");
        FunctionExpression f2 = new FunctionExpression();
        f2.setSymbol("symbol2");
        exp.externalFunctions.add(f1);
        exp.externalFunctions.add(f2);
        assertEquals(f1, exp.getExternalFunction("symbol1"));
        assertEquals(null, exp.getExternalFunction("symbol3"));
    }

    @Test
    public void testIsExternalFunction() {
        Expression exp = new Expression("1 + 2");
        exp.externalFunctions = new ArrayList<FunctionExpression>();
        FunctionExpression f1 = new FunctionExpression();
        f1.setSymbol("symbol1");
        FunctionExpression f2 = new FunctionExpression();
        f2.setSymbol("symbol2");
        exp.externalFunctions.add(f1);
        exp.externalFunctions.add(f2);
        assertEquals(true, exp.isExternalFunction("symbol1"));
        assertEquals(false, exp.isExternalFunction("symbol3"));
    }

    @Test
    public void testGetFunction() {
        Expression exp = new Expression("1 + 2");
        assertEquals(null, exp.getFunction("invalid"));
        assertEquals(true, exp.getFunction("and") instanceof AndFunction);
    }

    @Test
    public void testIsFunction() {
        Expression exp = new Expression("1 + 2");
        assertEquals(false, exp.isFunction("invalid"));
        assertEquals(true, exp.isFunction("and"));
    }

    @Test
    public void testExpression() {
        Expression exp = new Expression("1 + 2");
        assertEquals(false, exp.prepared);
    }

    @Test
    public void testStack() {
        Expression exp = new Expression("1 + 2");
        Stack stack = new Stack();
        exp.stack("1", stack);
        assertEquals("1", stack.pop());
        exp.stack("2", stack);
        assertEquals("2", stack.pop());
    }

    @Test
    public void testPrepare() {
        Expression exp = new Expression("1 + 2");
        exp.prepare();
        assertEquals(true, exp.prepared);
    }

    @Test
    public void testGetStackCopy() {
        Expression exp = new Expression("1 + 2");
        exp.stack = new ExpressionStack();
        exp.stack.add("1");
        ExpressionStack copy = exp.getStackCopy();
        assertEquals(exp.stack.toString(), copy.toString());
    }

    @Test
    public void testEvaluate() throws Exception {
        Expression exp = new Expression("1 + 2");
        exp.prepare();
        assertEquals(3, exp.evaluate());
    }

    @Test
    public void testTokenize() {
        Expression exp = new Expression("1 + 2");
        exp.stack = new ExpressionStack();
        exp.tokenize();
        assertEquals("1 + 2", exp.stack.toString());
    }
}