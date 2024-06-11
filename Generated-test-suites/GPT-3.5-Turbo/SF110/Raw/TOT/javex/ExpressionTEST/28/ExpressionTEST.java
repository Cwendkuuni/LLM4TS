import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class ExpressionTest {
    
    private Expression expression;
    
    @Before
    public void setup() {
        expression = new TestExpression("5 + 3");
        expression.prepare();
    }
    
    @Test
    public void testGetExternalFunctions() {
        List<FunctionExpression> externalFunctions = expression.getExternalFunctions();
        assertNotNull(externalFunctions);
        assertEquals(0, externalFunctions.size());
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(expression);
        assertEquals("5 + 3", expression.expression);
        assertNotNull(expression.stack);
        assertEquals(0, expression.stack.size());
        assertNull(expression.externalFunctions);
        assertNull(expression.reservedFunctions);
        assertFalse(expression.prepared);
    }
    
    @Test
    public void testDoEvaluationStack() {
        expression.doEvaluationStack();
        
        assertEquals(5, expression.stack.size());
        assertEquals("5", expression.stack.get(0));
        assertEquals("3", expression.stack.get(1));
        assertEquals("+", expression.stack.get(2));
        assertEquals("+", expression.stack.get(3));
        assertEquals("3", expression.stack.get(4));
    }
    
    @Test
    public void testPutFunction() {
        Map<FunctionExpression, FunctionExpression> map = new TreeMap<>();
        
        FunctionExpression f = new TestFunctionExpression("square", FunctionExpression.MATH);
        
        Expression.putFunction(f, map);
        
        assertTrue(map.containsKey(f));
    }
    
    @Test
    public void testGetReservedFunctions() {
        Expression.putFunction(new TestFunctionExpression("square", FunctionExpression.MATH), Expression.reservedFunctions);
        
        FunctionExpression f = Expression.getReservedFunctions("square");
        assertNotNull(f);
        assertEquals("square", f.getSymbol());
        assertEquals(FunctionExpression.MATH, f.getType());
    }
    
    @Test
    public void testIsReservedFunction() {
        Expression.putFunction(new TestFunctionExpression("isTrue", FunctionExpression.LOGICAL), Expression.reservedFunctions);
        
        assertTrue(expression.isReservedFunction("isTrue"));
        assertFalse(expression.isReservedFunction("isFalse"));
    }
    
    @Test
    public void testIsLogicalFunction() {
        Expression.putFunction(new TestFunctionExpression("isTrue", FunctionExpression.LOGICAL), Expression.reservedFunctions);
        
        assertTrue(expression.isLogicalFunction("isTrue"));
        assertFalse(expression.isLogicalFunction("isFalse"));
    }
    
    @Test
    public void testIsMathFunction() {
        Expression.putFunction(new TestFunctionExpression("square", FunctionExpression.MATH), Expression.reservedFunctions);
        
        assertTrue(expression.isMathFunction("square"));
        assertFalse(expression.isMathFunction("cube"));
    }
    
    @Test
    public void testIsDateFunction() {
        Expression.putFunction(new TestFunctionExpression("dateAdd", FunctionExpression.DATE), Expression.reservedFunctions);
        
        assertTrue(expression.isDateFunction("dateAdd"));
        assertFalse(expression.isDateFunction("dateSubtract"));
    }
    
    @Test
    public void testIsStringFunction() {
        Expression.putFunction(new TestFunctionExpression("concat", FunctionExpression.STRING), Expression.reservedFunctions);
        
        assertTrue(expression.isStringFunction("concat"));
        assertFalse(expression.isStringFunction("split"));
    }
    
    @Test
    public void testGetExternalFunction() {
        List<FunctionExpression> externalFunctions = new ArrayList<>();
        externalFunctions.add(new TestFunctionExpression("external", FunctionExpression.LOGICAL));
        expression.externalFunctions = externalFunctions;
        
        FunctionExpression external = expression.getExternalFunction("external");
        assertNotNull(external);
        assertEquals("external", external.getSimbol());
    }
    
    @Test
    public void testIsExternalFunction() {
        List<FunctionExpression> externalFunctions = new ArrayList<>();
        externalFunctions.add(new TestFunctionExpression("external", FunctionExpression.LOGICAL));
        expression.externalFunctions = externalFunctions;
        
        assertTrue(expression.isExternalFunction("external"));
        assertFalse(expression.isExternalFunction("internal"));
    }
    
    @Test
    public void testGetFunction() {
        Expression.putFunction(new TestFunctionExpression("isTrue", FunctionExpression.LOGICAL), Expression.reservedFunctions);
        
        FunctionExpression reserved = expression.getFunction("isTrue");
        assertNotNull(reserved);
        assertEquals("isTrue", reserved.getSymbol());
        assertEquals(FunctionExpression.LOGICAL, reserved.getType());
        
        FunctionExpression external = new TestFunctionExpression("external", FunctionExpression.MATH);
        expression.externalFunctions = Arrays.asList(external);
        
        FunctionExpression externalFn = expression.getFunction("external");
        assertNotNull(externalFn);
        assertEquals("external", externalFn.getSimbol());
        assertEquals(FunctionExpression.MATH, externalFn.getType());
    }
    
    @Test
    public void testIsFunction() {
        Expression.putFunction(new TestFunctionExpression("isTrue", FunctionExpression.LOGICAL), Expression.reservedFunctions);
        
        assertTrue(expression.isFunction("isTrue"));
        assertFalse(expression.isFunction("isFalse"));
    }
    
    @Test
    public void testPrepare() {
        expression.prepare();
        
        assertEquals(3, expression.stack.size());
        assertEquals("5", expression.stack.get(0));
        assertEquals("3", expression.stack.get(1));
        assertEquals("+", expression.stack.get(2));
        
        assertTrue(expression.prepared);
    }
    
    @Test
    public void testEvaluate() {
        try {
            Object result = expression.evaluate();
            assertEquals(8, result);
        } catch (Exception e) {
            fail("Exception was thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetStackCopy() {
        ExpressionStack originalStack = expression.getStackCopy();
        
        assertEquals(expression.stack.size(), originalStack.size());
        for (int i = 0; i < expression.stack.size(); i++) {
            assertEquals(expression.stack.get(i), originalStack.get(i));
        }
    }
    
    private class TestExpression extends Expression {
        public TestExpression(String s) {
            super(s);
        }

        @Override
        public Object evaluate() throws Exception {
            return null;
        }
    }
    
    private class TestFunctionExpression extends FunctionExpression {
        public TestFunctionExpression(String symbol, int type) {
            super(symbol, type);
        }

        @Override
        public Object evaluate(List<Object> args) throws Exception {
            return null;
        }
    }
}