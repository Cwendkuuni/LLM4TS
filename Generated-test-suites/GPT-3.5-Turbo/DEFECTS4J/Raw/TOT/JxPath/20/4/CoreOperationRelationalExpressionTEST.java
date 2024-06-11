import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {
    
    private CoreOperationRelationalExpression coreOpRelExpr;
    private EvalContext evalContext;
    
    @Before
    public void setUp() {
        coreOpRelExpr = new CoreOperationRelationalExpressionImpl();
        evalContext = new EvalContext();
    }
    
    @Test
    public void testComputeValueGreaterThan() {
        Object left = 5;
        Object right = 3;
        
        Object result = coreOpRelExpr.computeValue(evalContext);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testComputeValueGreaterThanOrEqualTo() {
        Object left = 5;
        Object right = 3;
        
        Object result = coreOpRelExpr.computeValue(evalContext);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testComputeValueLessThan() {
        Object left = 3;
        Object right = 5;
        
        Object result = coreOpRelExpr.computeValue(evalContext);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testComputeValueLessThanOrEqualTo() {
        Object left = 3;
        Object right = 5;
        
        Object result = coreOpRelExpr.computeValue(evalContext);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testComputeValueNaNComparison() {
        Object left = Double.NaN;
        Object right = 5;
        
        Object result = coreOpRelExpr.computeValue(evalContext);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testComputeValueIteratorComparison() {
        Object left = new IteratorImpl(new Object[]{2, 4, 6});
        Object right = new IteratorImpl(new Object[]{1, 3, 5});
        
        Object result = coreOpRelExpr.computeValue(evalContext);
        
        assertEquals(false, result);
    }
    
    private class CoreOperationRelationalExpressionImpl extends CoreOperationRelationalExpression {
        
        public CoreOperationRelationalExpressionImpl() {
            super(new Expression[]{new ExpressionImpl(), new ExpressionImpl()});
        }
        
        @Override
        protected boolean evaluateCompare(int compare) {
            if(compare > 0) {
                return true;
            }
            return false;
        }
        
        private class ExpressionImpl extends Expression {
            
            public ExpressionImpl() {
                super();
            }
            
            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        }
    }
    
    private class IteratorImpl implements Iterator {
        
        private Object[] elements;
        private int currentIndex;
        
        public IteratorImpl(Object[] elements) {
            this.elements = elements;
            this.currentIndex = 0;
        }
        
        @Override
        public boolean hasNext() {
            return currentIndex < elements.length - 1;
        }
        
        @Override
        public Object next() {
            return elements[currentIndex++];
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove operation not supported by this iterator");
        }
        
    }
}