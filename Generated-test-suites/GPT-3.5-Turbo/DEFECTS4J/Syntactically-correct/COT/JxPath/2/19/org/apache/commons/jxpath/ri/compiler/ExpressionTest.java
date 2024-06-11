import org.junit.Test;
import static org.junit.Assert.*;

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
    
    assertTrue(expression.isContextDependent());
  }
  
  @Test
  public void testComputeValue() {
    Expression expression = new Expression() {
      public boolean computeContextDependent() {
        return false;
      }
  
      public Object computeValue(EvalContext context) {
        return "value";
      }
  
      public Object compute(EvalContext context) {
        return null;
      }
    };
    
    assertEquals("value", expression.computeValue(null));
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
        return "result";
      }
    };
    
    assertEquals("result", expression.compute(null));
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
        return new String[]{"a", "b", "c"};
      }
    };
    
    Iterator iterator = expression.iterate(null);
    assertTrue(iterator.hasNext());
    assertEquals("a", iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals("b", iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals("c", iterator.next());
    assertFalse(iterator.hasNext());
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
        return new String[]{"a", "b", "c"};
      }
    };
    
    Iterator iterator = expression.iteratePointers(null);
    assertTrue(iterator.hasNext());
    assertEquals("a", ((Pointer) iterator.next()).getValue());
    assertTrue(iterator.hasNext());
    assertEquals("b", ((Pointer) iterator.next()).getValue());
    assertTrue(iterator.hasNext());
    assertEquals("c", ((Pointer) iterator.next()).getValue());
    assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testPointerIterator() {
    Expression.PointerIterator pointerIterator = new Expression.PointerIterator(null, null, null);
    assertFalse(pointerIterator.hasNext());
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testPointerIteratorRemove() {
    Expression.PointerIterator pointerIterator = new Expression.PointerIterator(null, null, null);
    pointerIterator.remove();
  }
  
  @Test
  public void testValueIterator() {
    Expression.ValueIterator valueIterator = new Expression.ValueIterator(null);
    assertFalse(valueIterator.hasNext());
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testValueIteratorRemove() {
    Expression.ValueIterator valueIterator = new Expression.ValueIterator(null);
    valueIterator.remove();
  }
  
}