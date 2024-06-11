package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

  @Test
  public void testIsContextDependent() {
    // Test isContextDependent() method returns the correct value
    Expression expression = new SampleExpression();
    assertFalse(expression.isContextDependent());
  }

  @Test
  public void testComputeValue() {
    // Test computeValue() method returns the correct value
    Expression expression = new SampleExpression();
    EvalContext context = new EvalContext();
    Object result = expression.computeValue(context);
    assertEquals("Expected result", result);
  }

  @Test
  public void testCompute() {
    // Test compute() method returns the correct value
    Expression expression = new SampleExpression();
    EvalContext context = new EvalContext();
    Object result = expression.compute(context);
    assertEquals("Expected result", result);
  }

  @Test
  public void testIterate() {
    // Test iterate() method returns the correct Iterator
    Expression expression = new SampleExpression();
    EvalContext context = new EvalContext();
    Iterator iterator = expression.iterate(context);
    assertNotNull(iterator);
  }

  @Test
  public void testIteratePointers() {
    // Test iteratePointers() method returns the correct Iterator
    Expression expression = new SampleExpression();
    EvalContext context = new EvalContext();
    Iterator iterator = expression.iteratePointers(context);
    assertNotNull(iterator);
  }

  @Test
  public void testPointerIterator() {
    // Test PointerIterator behaves as expected
    Iterator iterator = new Expression.PointerIterator(Collections.singletonList(new Pointer()).iterator(), new QName(), Locale.ENGLISH);
    assertTrue(iterator.hasNext());
    assertNotNull(iterator.next());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testPointerIteratorRemove() {
    // Test PointerIterator's remove() throws UnsupportedOperationException
    Iterator iterator = new Expression.PointerIterator(Collections.singletonList(new Pointer()).iterator(), new QName(), Locale.ENGLISH);
    iterator.remove();
  }

  @Test
  public void testValueIterator() {
    // Test ValueIterator behaves as expected
    Iterator iterator = new Expression.ValueIterator(Collections.singletonList("value").iterator());
    assertTrue(iterator.hasNext());
    assertNotNull(iterator.next());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testValueIteratorRemove() {
    // Test ValueIterator's remove() throws UnsupportedOperationException
    Iterator iterator = new Expression.ValueIterator(Collections.singletonList("value").iterator());
    iterator.remove();
  }
}