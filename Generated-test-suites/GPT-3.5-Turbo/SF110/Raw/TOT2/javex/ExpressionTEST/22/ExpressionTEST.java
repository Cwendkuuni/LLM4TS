import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {
    
    @Test
    public void testGetExternalFunctions() {
        Expression expression = new ConcreteExpression("test");
        assertNotNull(expression.getExternalFunctions());
    }
    
    @Test
    public void testIsReservedFunction() {
        Expression expression = new ConcreteExpression("test");
        assertTrue(expression.isReservedFunction("and"));
        assertFalse(expression.isReservedFunction("notexists"));
    }
    
    @Test
    public void testIsLogicalFunction() {
        Expression expression = new ConcreteExpression("test");
        assertTrue(expression.isLogicalFunction("and"));
        assertFalse(expression.isLogicalFunction("abs"));
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new ConcreteExpression("test");
        assertTrue(expression.isMathFunction("divide"));
        assertFalse(expression.isMathFunction("tolower"));
    }
    
    @Test
    public void testIsDateFunction() {
        Expression expression = new ConcreteExpression("test");
        assertTrue(expression.isDateFunction("dateadd"));
        assertFalse(expression.isDateFunction("abs"));
    }
    
    @Test
    public void testIsStringFunction() {
        Expression expression = new ConcreteExpression("test");
        assertTrue(expression.isStringFunction("startswith"));
        assertFalse(expression.isStringFunction("abs"));
    }
    
    @Test
    public void testIsExternalFunction() {
        Expression expression = new ConcreteExpression("test");
        assertFalse(expression.isExternalFunction("test"));
    }
    
    @Test
    public void testIsFunction() {
        Expression expression = new ConcreteExpression("test");
        assertTrue(expression.isFunction("abs"));
        assertFalse(expression.isFunction("notexists"));
    }
    
    @Test
    public void testPrepare() {
        Expression expression = new ConcreteExpression("1 + 2");
        expression.prepare();
        assertNotNull(expression.evaluate());
    }
    
    @Test
    public void testEvaluate() {
        Expression expression = new ConcreteExpression("1 + 2");
        expression.prepare();
        assertEquals(3, expression.evaluate());
    }
}