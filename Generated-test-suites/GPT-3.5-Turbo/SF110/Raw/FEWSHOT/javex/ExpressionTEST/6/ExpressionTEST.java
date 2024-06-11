package org.javex;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class ExpressionTest {

    private Expression expression;

    @Before
    public void setUp() {
        expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
    }

    @Test
    public void testGetExternalFunctions() {
        assertNull(expression.getExternalFunctions());
    }

    @Test
    public void testIsReservedFunction() {
        assertFalse(expression.isReservedFunction("+"));
        assertFalse(expression.isReservedFunction("-"));
        assertFalse(expression.isReservedFunction("*"));
        assertFalse(expression.isReservedFunction("/"));
        assertFalse(expression.isReservedFunction("("));
        assertFalse(expression.isReservedFunction(")"));
    }

    @Test
    public void testIsLogicalFunction() {
        assertFalse(expression.isLogicalFunction("+"));
        assertFalse(expression.isLogicalFunction("-"));
        assertFalse(expression.isLogicalFunction("*"));
        assertFalse(expression.isLogicalFunction("/"));
        assertFalse(expression.isLogicalFunction("("));
        assertFalse(expression.isLogicalFunction(")"));
    }

    @Test
    public void testIsMathFunction() {
        assertFalse(expression.isMathFunction("and"));
        assertFalse(expression.isMathFunction("or"));
        assertFalse(expression.isMathFunction("between"));
        assertFalse(expression.isMathFunction("contains"));
        assertFalse(expression.isMathFunction("endswith"));
        assertFalse(expression.isMathFunction("equals"));
        assertFalse(expression.isMathFunction("greatereqthan"));
        assertFalse(expression.isMathFunction("greaterthan"));
        assertFalse(expression.isMathFunction("isempty"));
        assertFalse(expression.isMathFunction("is"));
        assertFalse(expression.isMathFunction("isnull"));
        assertFalse(expression.isMathFunction("lesseqthan"));
        assertFalse(expression.isMathFunction("lessthan"));
        assertFalse(expression.isMathFunction("notequals"));
        assertFalse(expression.isMathFunction("not"));
        assertFalse(expression.isMathFunction("notchar"));
        assertFalse(expression.isMathFunction("startswith"));
        assertFalse(expression.isMathFunction("matches"));
        assertFalse(expression.isMathFunction("inlist"));
        assertFalse(expression.isMathFunction("abs"));
        assertFalse(expression.isMathFunction("divide"));
        assertFalse(expression.isMathFunction("multiply"));
        assertFalse(expression.isMathFunction("plus"));
        assertFalse(expression.isMathFunction("sqrt"));
        assertFalse(expression.isMathFunction("substract"));
        assertFalse(expression.isMathFunction("power"));
        assertFalse(expression.isMathFunction("powerchar"));
        assertFalse(expression.isMathFunction("dateadd"));
        assertFalse(expression.isMathFunction("datediff"));
        assertFalse(expression.isMathFunction("length"));
        assertFalse(expression.isMathFunction("substring"));
        assertFalse(expression.isMathFunction("tolower"));
        assertFalse(expression.isMathFunction("toupper"));
    }

    @Test
    public void testIsDateFunction() {
        assertFalse(expression.isDateFunction("+"));
        assertFalse(expression.isDateFunction("-"));
        assertFalse(expression.isDateFunction("*"));
        assertFalse(expression.isDateFunction("/"));
        assertFalse(expression.isDateFunction("("));
        assertFalse(expression.isDateFunction(")"));
    }

    @Test
    public void testIsStringFunction() {
        assertFalse(expression.isStringFunction("+"));
        assertFalse(expression.isStringFunction("-"));
        assertFalse(expression.isStringFunction("*"));
        assertFalse(expression.isStringFunction("/"));
        assertFalse(expression.isStringFunction("("));
        assertFalse(expression.isStringFunction(")"));
    }

    @Test
    public void testGetExternalFunction() {
        assertNull(expression.getExternalFunction("+"));
        assertNull(expression.getExternalFunction("-"));
        assertNull(expression.getExternalFunction("*"));
        assertNull(expression.getExternalFunction("/"));
        assertNull(expression.getExternalFunction("("));
        assertNull(expression.getExternalFunction(")"));
    }

    @Test
    public void testIsExternalFunction() {
        assertFalse(expression.isExternalFunction("+"));
        assertFalse(expression.isExternalFunction("-"));
        assertFalse(expression.isExternalFunction("*"));
        assertFalse(expression.isExternalFunction("/"));
        assertFalse(expression.isExternalFunction("("));
        assertFalse(expression.isExternalFunction(")"));
    }

    @Test
    public void testGetFunction() {
        assertNull(expression.getFunction("+"));
        assertNull(expression.getFunction("-"));
        assertNull(expression.getFunction("*"));
        assertNull(expression.getFunction("/"));
        assertNull(expression.getFunction("("));
        assertNull(expression.getFunction(")"));
    }

    @Test
    public void testIsFunction() {
        assertFalse(expression.isFunction("+"));
        assertFalse(expression.isFunction("-"));
        assertFalse(expression.isFunction("*"));
        assertFalse(expression.isFunction("/"));
        assertFalse(expression.isFunction("("));
        assertFalse(expression.isFunction(")"));
    }

    @Test
    public void testPrepare() {
        expression.prepare();
        assertTrue(expression.prepared);
    }

    @Test
    public void testEvaluate() {
        try {
            assertNull(expression.evaluate());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTokenize() {
        expression.expression = "+-*/()";
        expression.tokenize();
        assertEquals(expression.stack.size(), 7);
        assertEquals(expression.stack.get(0), "+");
        assertEquals(expression.stack.get(1), "-");
        assertEquals(expression.stack.get(2), "*");
        assertEquals(expression.stack.get(3), "/");
        assertEquals(expression.stack.get(4), "(");
        assertEquals(expression.stack.get(5), ")");
    }
}