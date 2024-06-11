import static org.junit.Assert.*;
import org.junit.Test;

public class ExpressionTest {
    
    @Test
    public void testGetExternalFunctions() {
        Expression expression = new SampleExpression("test");
        assertNotNull(expression.getExternalFunctions());
    }
    
    @Test
    public void testGetReservedFunctions() {
        assertNotNull(Expression.getReservedFunctions("and"));
        assertNull(Expression.getReservedFunctions("random"));
    }
    
    @Test
    public void testIsReservedFunction() {
        Expression expression = new SampleExpression("test");
        assertTrue(expression.isReservedFunction("and"));
        assertFalse(expression.isReservedFunction("random"));
    }
    
    @Test
    public void testIsLogicalFunction() {
        Expression expression = new SampleExpression("test");
        assertTrue(expression.isLogicalFunction("and"));
        assertFalse(expression.isLogicalFunction("abs"));
    }
    
    @Test
    public void testIsMathFunction() {
        Expression expression = new SampleExpression("test");
        assertTrue(expression.isMathFunction("sqrt"));
        assertFalse(expression.isMathFunction("and"));
    }
    
    @Test
    public void testIsDateFunction() {
        Expression expression = new SampleExpression("test");
        assertTrue(expression.isDateFunction("dateAdd"));
        assertFalse(expression.isDateFunction("and"));
    }
    
    @Test
    public void testIsStringFunction() {
        Expression expression = new SampleExpression("test");
        assertTrue(expression.isStringFunction("length"));
        assertFalse(expression.isStringFunction("and"));
    }
    
    @Test
    public void testIsExternalFunction() {
        Expression expression = new SampleExpression("test");
        assertFalse(expression.isExternalFunction("testFunction"));
    }
    
    @Test
    public void testIsFunction() {
        Expression expression = new SampleExpression("test");
        assertTrue(expression.isFunction("abs"));
        assertFalse(expression.isFunction("random"));
    }
    
    @Test
    public void testEvaluate() {
        Expression expression = new SampleExpression("1 + 1");
        assertNotNull(expression.evaluate());
    }
    
    @Test
    public void testPrepare() {
        Expression expression = new SampleExpression("1 + 1");
        expression.prepare();
        assertTrue(expression.isPrepared());
    }
    
}

class SampleExpression extends Expression {
    public SampleExpression(String s) {
        super(s);
    }
    
    public Object evaluate() throws Exception {
        return 2;
    }
    
    public boolean isPrepared() {
        return prepared;
    }
}