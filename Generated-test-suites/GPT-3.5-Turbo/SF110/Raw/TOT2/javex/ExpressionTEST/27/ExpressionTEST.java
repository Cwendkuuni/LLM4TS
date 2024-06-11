import org.javex.Expression;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.math.DivideFunction;
import org.javex.functions.date.DateAddFunction;
import org.javex.functions.string.ContainsFunction;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ExpressionTest {

    private Expression expression;

    @Before
    public void setUp() {
        expression = new Expression("3 + 2");
    }

    @Test
    public void testGetExternalFunctions() {
        List externalFunctions = new ArrayList<>();
        externalFunctions.add(new DivideFunction());
        expression.externalFunctions = externalFunctions;
        
        assertEquals(externalFunctions, expression.getExternalFunctions());
    }

    @Test
    public void testIsReservedFunction() {
        assertTrue(expression.isReservedFunction("+"));
        assertFalse(expression.isReservedFunction("$"));
    }

    @Test
    public void testIsLogicalFunction() {
        assertTrue(expression.isLogicalFunction("and"));
        assertFalse(expression.isLogicalFunction("+"));
    }

    @Test
    public void testIsMathFunction() {
        assertTrue(expression.isMathFunction("+"));
        assertFalse(expression.isMathFunction("and"));
    }

    @Test
    public void testIsDateFunction() {
        assertTrue(expression.isDateFunction("dateAdd"));
        assertFalse(expression.isDateFunction("+"));
    }

    @Test
    public void testIsStringFunction() {
        assertTrue(expression.isStringFunction("contains"));
        assertFalse(expression.isStringFunction("+"));
    }

    @Test
    public void testIsExternalFunction() {
        List externalFunctions = new ArrayList<>();
        externalFunctions.add(new ContainsFunction());
        expression.externalFunctions = externalFunctions;
        
        assertTrue(expression.isExternalFunction("contains"));
        assertFalse(expression.isExternalFunction("startsWith"));
    }

    @Test
    public void testGetFunction() {
        assertNotNull(expression.getFunction("dateAdd"));
        assertNull(expression.getFunction("doesNotExist"));
    }

    @Test
    public void testIsFunction() {
        assertTrue(expression.isFunction("dateAdd"));
        assertFalse(expression.isFunction("doesNotExist"));
    }

    @Test
    public void testPrepare() {
        expression.prepare();
        assertTrue(expression.prepared);
    }

    @Test
    public void testEvaluate() {
        expression = new Expression("5 * 2");
        try {
            Object result = expression.evaluate();
            assertEquals(10, result);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

}