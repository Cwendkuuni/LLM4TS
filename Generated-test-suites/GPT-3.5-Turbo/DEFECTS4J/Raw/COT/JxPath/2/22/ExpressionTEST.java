import org.junit.Assert;
import org.junit.Test;

public class ExpressionTest {
    
    @Test
    public void testIsContextDependent() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return true;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }
            
            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        boolean isContextDependent = expression.isContextDependent();
        Assert.assertTrue(isContextDependent);
    }
    
    @Test
    public void testComputeValue() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return "result";
            }
            
            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        Object value = expression.computeValue(null);
        Assert.assertEquals("result", value);
    }
    
    @Test
    public void testCompute() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }
            
            @Override
            public Object compute(EvalContext context) {
                return "result";
            }
        };

        Object value = expression.compute(null);
        Assert.assertEquals("result", value);
    }
    
    @Test
    public void testIterate() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }
            
            @Override
            public Object compute(EvalContext context) {
                return new String[] {"value1", "value2"};
            }
        };

        Iterator iterator = expression.iterate(null);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("value1", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("value2", iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testIteratePointers() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }
            
            @Override
            public Object compute(EvalContext context) {
                return new String[] {"value1", "value2"};
            }
        };

        Iterator iterator = expression.iteratePointers(null);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("value1", iterator.next().getValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("value2", iterator.next().getValue());
        Assert.assertFalse(iterator.hasNext());
    }
}