import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class ExpressionTest {

    private Expression expression;
    
    @Before
    public void setUp() {
        expression = new ExpressionStub();
    }
    
    @Test
    public void testGetExternalFunctions() {
        List externalFunctions = expression.getExternalFunctions();
        assertNotNull(externalFunctions);
        assertEquals(0, externalFunctions.size());
    }
    
    @Test
    public void testExpressionConstructor() {
        Expression expression = new ExpressionStub("1 + 2");
        assertNotNull(expression);
        assertEquals("1 + 2", expression.expression);
    }
    
    @Test
    public void testDoEvaluationStack() {
        expression.stack = new ExpressionStack();
        expression.stack.add("1");
        expression.stack.add("+");
        expression.stack.add("2");
        
        expression.doEvaluationStack();
        
        ExpressionStack expectedStack = new ExpressionStack();
        expectedStack.add("1");
        expectedStack.add("2");
        expectedStack.add("+");
        
        assertEquals(expectedStack, expression.stack);
    }
    
    @Test
    public void testGetReservedFunctions() {
        FunctionExpression function = expression.getReservedFunctions("and");
        assertNotNull(function);
        assertTrue(function instanceof AndFunction);
    }
    
    @Test
    public void testIsReservedFunction() {
        boolean isReserved = expression.isReservedFunction("and");
        assertTrue(isReserved);
        
        isReserved = expression.isReservedFunction("not");
        assertTrue(isReserved);
        
        isReserved = expression.isReservedFunction("test");
        assertFalse(isReserved);
    }
    
    @Test
    public void testIsLogicalFunction() {
        boolean isLogical = expression.isLogicalFunction("and");
        assertTrue(isLogical);
        
        isLogical = expression.isLogicalFunction("function");
        assertFalse(isLogical);
    }
    
    @Test
    public void testIsMathFunction() {
        boolean isMath = expression.isMathFunction("abs");
        assertTrue(isMath);
        
        isMath = expression.isMathFunction("date");
        assertFalse(isMath);
    }
    
    @Test
    public void testIsDateFunction() {
        boolean isDate = expression.isDateFunction("dateadd");
        assertTrue(isDate);
        
        isDate = expression.isDateFunction("math");
        assertFalse(isDate);
    }
    
    @Test
    public void testIsStringFunction() {
        boolean isString = expression.isStringFunction("startswith");
        assertTrue(isString);
        
        isString = expression.isStringFunction("logical");
        assertFalse(isString);
    }
    
    @Test
    public void testGetExternalFunction() {
        FunctionExpression function = new FunctionExpressionStub("test");
        List<FunctionExpression> externalFunctions = new ArrayList<>();
        externalFunctions.add(function);
        
        expression.externalFunctions = externalFunctions;
        
        FunctionExpression result = expression.getExternalFunction("test");
        assertNotNull(result);
        assertEquals(function, result);
    }
    
    @Test
    public void testIsExternalFunction() {
        FunctionExpression function = new FunctionExpressionStub("test");
        List<FunctionExpression> externalFunctions = new ArrayList<>();
        externalFunctions.add(function);
        
        expression.externalFunctions = externalFunctions;
        
        boolean isExternal = expression.isExternalFunction("test");
        assertTrue(isExternal);
        
        isExternal = expression.isExternalFunction("function");
        assertFalse(isExternal);
    }
    
    @Test
    public void testGetFunction() {
        FunctionExpression function = expression.getFunction("and");
        assertNotNull(function);
        assertTrue(function instanceof AndFunction);
        
        function = expression.getFunction("test");
        assertNull(function);
    }
    
    @Test
    public void testIsFunction() {
        boolean isFunction = expression.isFunction("and");
        assertTrue(isFunction);
        
        isFunction = expression.isFunction("test");
        assertFalse(isFunction);
    }
    
    @Test
    public void testPrepare() {
        expression.expression = "1 + 2";
        expression.prepare();
        
        ExpressionStack expectedStack = new ExpressionStack();
        expectedStack.add("1");
        expectedStack.add("2");
        expectedStack.add("+");
        
        assertEquals(expectedStack, expression.stack);
        assertTrue(expression.prepared);
    }
    
    @Test
    public void testGetStackCopy() {
        expression.stack = new ExpressionStack();
        expression.stack.add("1");
        expression.stack.add("+");
        expression.stack.add("2");
        
        ExpressionStack copy = expression.getStackCopy();
        
        assertNotSame(expression.stack, copy);
        assertEquals(expression.stack, copy);
    }
    
    @Test
    public void testEvaluate() throws Exception {
        Object result = expression.evaluate();
        assertNotNull(result);
    }
    
    @Test
    public void testStack() {
        Stack<String> stack = new Stack<>();
        expression.stack("1", stack);
        
        assertFalse(stack.isEmpty());
        assertEquals("1", stack.peek());
        
        expression.stack("2", stack);
        assertEquals("12", stack.peek());
    }
    
    @Test
    public void testTokenize() {
        expression.expression = "1+2";
        expression.tokenize();
        
        ExpressionStack expectedStack = new ExpressionStack();
        expectedStack.add("1");
        expectedStack.add("+");
        expectedStack.add("2");
        
        assertEquals(expectedStack, expression.stack);
        
        expression.expression = "1 + 2";
        expression.tokenize();
        
        assertEquals(expectedStack, expression.stack);
        
        expression.expression = "1+2 + 3";
        expectedStack.add("+");
        expectedStack.add("3");
        
        expression.tokenize();
        
        assertEquals(expectedStack, expression.stack);
        
        expression.expression = "(1 + 2)";
        expression.tokenize();
        
        expectedStack.clear();
        expectedStack.add("(");
        expectedStack.add("1");
        expectedStack.add("+");
        expectedStack.add("2");
        expectedStack.add(")");
        
        assertEquals(expectedStack, expression.stack);
        
        expression.expression = "1 > 2";
        expression.tokenize();
        
        expectedStack.clear();
        expectedStack.add("1");
        expectedStack.add(">");
        expectedStack.add("2");
        
        assertEquals(expectedStack, expression.stack);
        
        expression.expression = "1 and 2";
        expression.tokenize();
        
        expectedStack.clear();
        expectedStack.add("1");
        expectedStack.add("and");
        expectedStack.add("2");
        
        assertEquals(expectedStack, expression.stack);
        
        expression.expression = "test";
        expression.tokenize();
        
        expectedStack.clear();
        expectedStack.add("test");
        
        assertEquals(expectedStack, expression.stack);
    }
}

class ExpressionStub extends Expression {
    
    public ExpressionStub() {
        super();
    }
    
    public ExpressionStub(String s) {
        super(s);
    }
    
    public Object evaluate() {
        return new Object();
    }
}

class FunctionExpressionStub extends FunctionExpression {
    
    public FunctionExpressionStub(String simbol) {
        super(simbol);
    }
    
    public Object evaluate() {
        return new Object();
    }
}