import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {
    
    private CoreOperationRelationalExpression testObj;
    
    @Before
    public void setUp() {
        testObj = new CoreOperationRelationalExpressionTestImpl();
    }
    
    @Test
    public void testComputeValue_whenLeftIsSmallerThanRight_shouldReturnTrue() {
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(1);
        
        Object result = testObj.computeValue(context);
        
        assertTrue((Boolean)result);
    }
    
    @Test
    public void testComputeValue_whenLeftIsEqualToRight_shouldReturnFalse() {
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(2);
        
        Object result = testObj.computeValue(context);
        
        assertFalse((Boolean)result);
    }
    
    @Test
    public void testComputeValue_whenLeftIsGreaterThanRight_shouldReturnFalse() {
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(3);
        
        Object result = testObj.computeValue(context);
        
        assertFalse((Boolean)result);
    }
    
    @Test
    public void testComputeValue_whenLeftIsIteratorAndRightIsIteratorWithNoMatch_shouldReturnFalse() {
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(Arrays.asList(1, 2, 3).iterator());
        
        Object result = testObj.computeValue(context);
        
        assertFalse((Boolean)result);
    }
    
    @Test
    public void testComputeValue_whenLeftIsIteratorAndRightIsIteratorWithMatch_shouldReturnTrue() {
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(Arrays.asList(1, 2, 3).iterator());
        
        Object result = testObj.computeValue(context);
        
        assertTrue((Boolean)result);
    }
    
    @Test
    public void testComputeValue_whenLeftIsIteratorAndRightIsElementWithMatch_shouldReturnTrue() {
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(Arrays.asList(1, 2, 3).iterator());
        
        Object result = testObj.computeValue(context);
        
        assertTrue((Boolean)result);
    }
    
    @Test
    public void testComputeValue_whenLeftIsIteratorAndRightIsElementWithNoMatch_shouldReturnFalse() {
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(Arrays.asList(1, 2, 3).iterator());
        
        Object result = testObj.computeValue(context);
        
        assertFalse((Boolean)result);
    }
    
    @Test
    public void testComputeValue_whenLeftIsCollectionAndRightIsElementWithMatch_shouldReturnTrue() {
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(Arrays.asList(1, 2, 3));
        
        Object result = testObj.computeValue(context);
        
        assertTrue((Boolean)result);
    }
    
    @Test
    public void testComputeValue_whenLeftIsCollectionAndRightIsElementWithNoMatch_shouldReturnFalse() {
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(Arrays.asList(1, 2, 3));
        
        Object result = testObj.computeValue(context);
        
        assertFalse((Boolean)result);
    }
    
    @Test
    public void testComputeValue_whenLeftIsInitialContextAndRightIsInitialContext_shouldReturnFalse() {
        EvalContext left = new EvalContext();
        left.setSingleNodePointer(new InitialContext());
        
        EvalContext right = new EvalContext();
        right.setSingleNodePointer(new InitialContext());
        
        Object result = testObj.computeValue(left);
        
        assertFalse((Boolean)result);
    }
    
    @Test
    public void testComputeValue_whenLeftIsGreaterThanRightWithNullValues_shouldReturnFalse() {
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(3.5);
        
        Object result = testObj.computeValue(context);
        
        assertFalse((Boolean)result);
    }
    
    private class CoreOperationRelationalExpressionTestImpl extends CoreOperationRelationalExpression {
        
        public CoreOperationRelationalExpressionTestImpl() {
            super(new Expression[]{new Expression(), new Expression()});
        }
        
        @Override
        protected boolean evaluateCompare(int compare) {
            return compare > 0;
        }
    }
}