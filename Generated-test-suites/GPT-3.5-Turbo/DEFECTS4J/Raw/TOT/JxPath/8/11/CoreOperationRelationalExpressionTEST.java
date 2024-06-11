import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue_Typical() {
        // Typical use case
        Expression[] args = new Expression[2];
        args[0] = new Expression("value1");
        args[1] = new Expression("value2");
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        
        EvalContext context = new EvalContext();
        context.setVariable("value1", 10);
        context.setVariable("value2", 5);
        
        Object result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValue_Edge() {
        // Edge case
        Expression[] args = new Expression[2];
        args[0] = new Expression("value1");
        args[1] = new Expression("value2");
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        
        EvalContext context = new EvalContext();
        context.setVariable("value1", 0);
        context.setVariable("value2", 0);
        
        Object result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValue_Error() {
        // Error scenario
        Expression[] args = new Expression[2];
        args[0] = new Expression("value1");
        args[1] = new Expression("value2");
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        
        EvalContext context = new EvalContext();
        context.setVariable("value1", "abc");
        context.setVariable("value2", 5);
        
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new Expression("value1");
        args[1] = new Expression("value2");
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }
    
    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new Expression("value1");
        args[1] = new Expression("value2");
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        
        boolean isSymmetric = expression.isSymmetric();
        assertEquals(false, isSymmetric);
    }
    
    @Test
    public void testEvaluateCompare() {
        Expression[] args = new Expression[2];
        args[0] = new Expression("value1");
        args[1] = new Expression("value2");
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        
        boolean result = expression.evaluateCompare(0);
        assertEquals(true, result);
    }
    
    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        args[0] = new Expression("value1");
        args[1] = new Expression("value2");
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        
        Object o = expression.reduce(5);
        assertEquals(5, o);
    }
    
    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        args[0] = new Expression("value");
        args[1] = new Expression("value");
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        
        Iterator it = new Iterator() {
                private boolean hasNext = true;
                
                @Override
                public boolean hasNext() {
                    return hasNext;
                }

                @Override
                public Object next() {
                    hasNext = false;
                    return "value";
                }
                
                @Override
                public void remove() {}
            };
        
        boolean result = expression.containsMatch(it, "value");
        assertEquals(true, result);
    }
    
    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        args[0] = new Expression("value");
        args[1] = new Expression("value");
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        
        Iterator it1 = new Iterator() {
                private boolean hasNext = true;
                
                @Override
                public boolean hasNext() {
                    return hasNext;
                }

                @Override
                public Object next() {
                    hasNext = false;
                    return "value";
                }
                
                @Override
                public void remove() {}
            };
        
        Iterator it2 = new Iterator() {
                private boolean hasNext = true;
                
                @Override
                public boolean hasNext() {
                    return hasNext;
                }

                @Override
                public Object next() {
                    hasNext = false;
                    return "value";
                }
                
                @Override
                public void remove() {}
            };
        
        boolean result = expression.findMatch(it1, it2);
        assertEquals(true, result);
    }
}