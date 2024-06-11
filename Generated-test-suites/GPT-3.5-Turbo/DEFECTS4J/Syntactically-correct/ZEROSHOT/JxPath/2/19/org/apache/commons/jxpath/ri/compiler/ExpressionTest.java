import org.junit.Assert;
import org.junit.Test;

public class ExpressionTest {
  
  @Test
  public void testIsContextDependent() {
    Expression expression = new Expression() {
        public boolean computeContextDependent() {
            return true;
        }

        public Object computeValue(EvalContext context) {
            return null;
        }

        public Object compute(EvalContext context) {
            return null;
        }
    };
    
    boolean contextDependent = expression.isContextDependent();
    Assert.assertTrue(contextDependent);
  }
  
  @Test
  public void testComputeValue() {
    Expression expression = new Expression() {
        public boolean computeContextDependent() {
            return false;
        }

        public Object computeValue(EvalContext context) {
            return "Test";
        }

        public Object compute(EvalContext context) {
            return null;
        }
    };
    
    Object value = expression.computeValue(null);
    Assert.assertEquals("Test", value);
  }
  
  @Test
  public void testCompute() {
    Expression expression = new Expression() {
        public boolean computeContextDependent() {
            return false;
        }

        public Object computeValue(EvalContext context) {
            return null;
        }

        public Object compute(EvalContext context) {
            return 10;
        }
    };
    
    Object value = expression.compute(null);
    Assert.assertEquals(10, value);
  }
  
  @Test
  public void testIterate() {
    Expression expression = new Expression() {
        public boolean computeContextDependent() {
            return false;
        }

        public Object computeValue(EvalContext context) {
            return null;
        }

        public Object compute(EvalContext context) {
            return new String[]{"Test1", "Test2"};
        }
    };
    
    Iterator iterator = expression.iterate(null);
    Assert.assertEquals("Test1", iterator.next());
    Assert.assertEquals("Test2", iterator.next());
    Assert.assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testIteratePointers() {
    Expression expression = new Expression() {
        public boolean computeContextDependent() {
            return false;
        }

        public Object computeValue(EvalContext context) {
            return null;
        }

        public Object compute(EvalContext context) {
            return new String[]{"Test1", "Test2"};
        }
    };
    
    Iterator iterator = expression.iteratePointers(null);
    Assert.assertEquals("Test1", iterator.next());
    Assert.assertEquals("Test2", iterator.next());
    Assert.assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testPointerIterator() {
    Iterator iterator = new Expression.PointerIterator(null, null, null);
    Assert.assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testValueIterator() {
    Iterator iterator = new Expression.ValueIterator(null);
    Assert.assertFalse(iterator.hasNext());
  }
  
}