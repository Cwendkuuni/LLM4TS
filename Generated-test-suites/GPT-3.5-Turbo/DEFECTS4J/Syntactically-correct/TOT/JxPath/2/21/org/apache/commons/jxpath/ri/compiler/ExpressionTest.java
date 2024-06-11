import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;

public class ExpressionTest {

  @Test
  public void testIsContextDependent() {
    Expression exp = new Expression() {
      public boolean computeContextDependent() {
        return false;
      }

      public Object computeValue(EvalContext context) {
        return null;
      }

      public Object compute(EvalContext context) {
        return null;
      }
    };
    Assert.assertFalse(exp.isContextDependent());
  }

  @Test
  public void testComputeValue() {
    Expression exp = new Expression() {
      public boolean computeContextDependent() {
        return false;
      }

      public Object computeValue(EvalContext context) {
        return "Test Value";
      }

      public Object compute(EvalContext context) {
        return null;
      }
    };
    EvalContext context = new EvalContext();
    Assert.assertEquals("Test Value", exp.computeValue(context));
  }

  @Test
  public void testCompute() {
    Expression exp = new Expression() {
      public boolean computeContextDependent() {
        return false;
      }

      public Object computeValue(EvalContext context) {
        return null;
      }

      public Object compute(EvalContext context) {
        return "Test Compute";
      }
    };
    EvalContext context = new EvalContext();
    Assert.assertEquals("Test Compute", exp.compute(context));
  }

  @Test
  public void testIterate() {
    Expression exp = new Expression() {
      public boolean computeContextDependent() {
        return false;
      }

      public Object computeValue(EvalContext context) {
        return null;
      }

      public Object compute(EvalContext context) {
        return new String[]{"Test1", "Test2", "Test3"};
      }
    };
    EvalContext context = new EvalContext();
    Assert.assertTrue(exp.iterate(context).hasNext());
    Assert.assertEquals("Test1", exp.iterate(context).next());
  }

  @Test
  public void testIteratePointers() {
    Expression exp = new Expression() {
      public boolean computeContextDependent() {
        return false;
      }

      public Object computeValue(EvalContext context) {
        return null;
      }

      public Object compute(EvalContext context) {
        return new String[]{"Test1", "Test2", "Test3"};
      }
    };
    EvalContext context = new EvalContext();
    Assert.assertTrue(exp.iteratePointers(context).hasNext());
    Assert.assertEquals("Test1", exp.iteratePointers(context).next());
  }

  @Test
  public void testPointerIterator() {
    Expression.PointerIterator ptrIter = new Expression.PointerIterator(new String[]{"Test1", "Test2", "Test3"}.iterator(), new QName(null, "value"), new Locale("en"));
    Assert.assertTrue(ptrIter.hasNext());
    Assert.assertEquals("Test1", ptrIter.next());
  }

  @Test
  public void testValueIterator() {
    Expression.ValueIterator valIter = new Expression.ValueIterator(new String[]{"Test1", "Test2", "Test3"}.iterator());
    Assert.assertTrue(valIter.hasNext());
    Assert.assertEquals("Test1", valIter.next());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRemovePointerIterator() {
    Expression.PointerIterator ptrIter = new Expression.PointerIterator(new String[]{"Test1", "Test2", "Test3"}.iterator(), new QName(null, "value"), new Locale("en"));
    ptrIter.remove();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRemoveValueIterator() {
    Expression.ValueIterator valIter = new Expression.ValueIterator(new String[]{"Test1", "Test2", "Test3"}.iterator());
    valIter.remove();
  }

}