import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class ExpressionTest {

    Expression expression;

    @Before
    public void setUp() {
        expression = new Expression("") {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
    }

    @Test
    public void testGetExternalFunctions() {
        List expected = new ArrayList();
        List result = expression.getExternalFunctions();
        assertEquals(expected, result);
    }

    @Test
    public void testExpression() {
        Expression expression = new Expression("2 + 3") {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        assertEquals("2 + 3", expression.expression);
        assertNotNull(expression.stack);
        assertEquals(false, expression.prepared);
    }

    @Test
    public void testDoEvaluationStack() {
        expression.stack.add("2");
        expression.stack.add("+");
        expression.stack.add("3");
        expression.doEvaluationStack();
        ExpressionStack expected = new ExpressionStack();
        expected.add("2");
        expected.add("3");
        expected.add("+");
        assertEquals(expected, expression.stack);
    }

    @Test
    public void testPutFunction() {
        Map map = new HashMap<>();
        FunctionExpression f = new AndFunction();
        Expression.putFunction(f, map);
        FunctionExpression result = (FunctionExpression) map.get(f);
        assertEquals(f, result);
    }

    @Test
    public void testGetReservedFunctions() {
        String simbol = "and";
        FunctionExpression expected = new AndFunction();
        FunctionExpression result = Expression.getReservedFunctions(simbol);
        assertEquals(expected, result);
    }

    @Test
    public void testIsReservedFunction() {
        String simbol = "between";
        boolean result = expression.isReservedFunction(simbol);
        assertTrue(result);
    }

    @Test
    public void testIsLogicalFunction() {
        String simbol = "and";
        boolean result = expression.isLogicalFunction(simbol);
        assertTrue(result);
    }

    @Test
    public void testIsMathFunction() {
        String simbol = "sqrt";
        boolean result = expression.isMathFunction(simbol);
        assertTrue(result);
    }

    @Test
    public void testIsDateFunction() {
        String simbol = "dateadd";
        boolean result = expression.isDateFunction(simbol);
        assertTrue(result);
    }

    @Test
    public void testIsStringFunction() {
        String simbol = "tolower";
        boolean result = expression.isStringFunction(simbol);
        assertTrue(result);
    }

    @Test
    public void testGetExternalFunction() {
        List externalFunctions = new ArrayList<>();
        externalFunctions.add(new EqualsFunction());
        expression.externalFunctions = externalFunctions;
        String simbol = "equals";
        FunctionExpression expected = new EqualsFunction();
        FunctionExpression result = expression.getExternalFunction(simbol);
        assertEquals(expected, result);
    }

    @Test
    public void testIsExternalFunction() {
        List externalFunctions = new ArrayList<>();
        externalFunctions.add(new StartsWithFunction());
        expression.externalFunctions = externalFunctions;
        String simbol = "startswith";
        boolean result = expression.isExternalFunction(simbol);
        assertTrue(result);
    }

    @Test
    public void testGetFunction() {
        String simbol = "and";
        FunctionExpression expected = new AndFunction();
        FunctionExpression result = expression.getFunction(simbol);
        assertEquals(expected, result);
    }

    @Test
    public void testIsFunction() {
        String simbol = "isempty";
        boolean result = expression.isFunction(simbol);
        assertTrue(result);
    }

    @Test
    public void testExpression2() {
        expression.prepared = true;
        expression.stack.push("(");
        expression.stack.push("2");
        expression.stack.push("+");
        expression.stack.push("3");
        expression.stack.push(")");
        expression.stack.push("-");
        expression.stack.push("4");
        expression.prepare();
        ExpressionStack expected = new ExpressionStack();
        expected.push("2");
        expected.push("3");
        expected.push("+");
        expected.push("4");
        expected.push("-");
        expected.push("(");
        assertEquals(expected, expression.stack);
        assertTrue(expression.prepared);
    }

    @Test
    public void testAdd() {
        String c = "+";
        Stack stack = new Stack();
        stack.push("2");
        expression.stack(c, stack);
        Stack expected = new Stack();
        expected.push("2+");
        assertEquals(expected, stack);
    }

    @Test
    public void testPrepare() {
        expression.expression = "2 + 3";
        expression.prepare();
        ExpressionStack expected = new ExpressionStack();
        expected.push("2");
        expected.push("3");
        expected.push("+");
        assertEquals(expected, expression.stack);
        assertTrue(expression.prepared);
    }

    @Test
    public void testGetStackCopy() {
        expression.stack.push("2");
        expression.stack.push("+");
        ExpressionStack expected = new ExpressionStack();
        expected.push("2");
        expected.push("+");
        ExpressionStack result = expression.getStackCopy();
        assertEquals(expected, result);
    }

    @Test
    public void testEvaluate() throws Exception {
        Object result = expression.evaluate();
        assertNull(result);
    }

    @Test
    public void testTokenize() {
        expression.expression = "2   +  3";
        expression.tokenize();
        ExpressionStack expected = new ExpressionStack();
        expected.push("2");
        expected.push("+");
        expected.push("3");
        assertEquals(expected, expression.stack);
    }

    @After
    public void tearDown() {
        expression = null;
    }
}