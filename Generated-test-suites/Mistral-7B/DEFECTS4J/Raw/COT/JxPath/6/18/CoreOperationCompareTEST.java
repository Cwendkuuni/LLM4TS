import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

 private CoreOperationCompare operationCompare;
 private Expression arg1;
 private Expression arg2;
 private EvalContext context;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 arg1 = new ExpressionMock();
 arg2 = new ExpressionMock();
 context = new EvalContext();
 operationCompare = new CoreOperationCompare(arg1, arg2);
 }

 @Test
 public void testEqual() {
 Object l = "testString";
 Object r = "testString";
 when(arg1.compute(context)).thenReturn(l);
 when(arg2.compute(context)).thenReturn(r);
 assertTrue(operationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_Boolean() {
 Object l = true;
 Object r = true;
 when(arg1.compute(context)).thenReturn(l);
 when(arg2.compute(context)).thenReturn(r);
 assertTrue(operationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_Number() {
 Object l = 1.0;
 Object r = 1.0;
 when(arg1.compute(context)).thenReturn(l);
 when(arg2.compute(context)).thenReturn(r);
 assertTrue(operationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_Null() {
 Object l = null;
 Object r = null;
 when(arg1.compute(context)).thenReturn(l);
 when(arg2.compute(context)).thenReturn(r);
 assertTrue(operationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_DifferentTypes() {
 Object l = "testString";
 Object r = 1.0;
 when(arg1.compute(context)).thenReturn(l);
 when(arg2.compute(context)).thenReturn(r);
 assertFalse(operationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_DifferentValues() {
 Object l = "testString";
 Object r = "differentString";
 when(arg1.compute(context)).thenReturn(l);
 when(arg2.compute(context)).thenReturn(r);
 assertFalse(operationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_Iterator_SameValues() {
 Object iter1 = new IteratorMock(new Object[] { "testString" });
 Object iter2 = new IteratorMock(new Object[] { "testString" });
 when(arg1.compute(context)).thenReturn(iter1);
 when(arg2.compute(context)).thenReturn(iter2);
 assertTrue(operationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_Iterator_DifferentValues() {
 Object iter1 = new IteratorMock(new Object[] { "testString" });
 Object iter2 = new IteratorMock(new Object[] { "differentString" });
 when(arg1.compute(context)).thenReturn(iter1);
 when(arg2.compute(context)).thenReturn(iter2);
 assertFalse(operationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_Iterator_SameIterator() {
 Object iter = new IteratorMock(new Object[] { "testString" });
 when(arg1.compute(context)).thenReturn(iter);
 when(arg2.compute(context)).thenReturn(iter);
 assertTrue(operationCompare.equal(context, arg1, arg2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqual_NullArgument() {
 Object l = null;
 Object r = "testString";
 when(arg1.compute(context)).thenReturn(l);
 when(arg2.compute(context)).thenReturn(r);
 assertFalse(operationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testContains() {
 Object iter = new IteratorMock(new Object[] { "testString", "testString" });
 when(arg1.compute(context)).thenReturn(iter);
 Object value = "testString";
 assertTrue(operationCompare.contains(iter, value));
 }

 @Test
 public void testContains_NullValue() {
 Object iter = new IteratorMock(new Object[] { "testString", "testString" });
 when(arg1.compute(context)).thenReturn(iter);
 Object value = null;
 assertFalse(operationCompare.contains(iter, value));
 }

 @Test
 public void testFindMatch() {
 Object iter1 = new IteratorMock(new Object[] { "testString1", "testString2" });
 Object iter2 = new IteratorMock(new Object[] { "testString2", "testString3" });
 assertTrue(operationCompare.findMatch(iter1, iter2));
 }

 @Test
 public void testFindMatch_EmptyIterators() {
 Object iter1 = new IteratorMock(new Object[0]);
 Object iter2 = new IteratorMock(new Object[0]);
 assertTrue(operationCompare.findMatch(iter1, iter2));
 }

 @Test
 public void testFindMatch_NoMatch() {
 Object iter1 = new IteratorMock(new Object[] { "testString1", "testString2" });
 Object iter2 = new IteratorMock(new Object[] { "testString3", "testString4" });
 assertFalse(operationCompare.findMatch(iter1, iter2));
 }

 @Test
 public void testFindMatch_SelfIterator() {
 Object iter1 = new IteratorMock(new Object[] { "testString1" });
 Object iter2 = iter1;
 assertTrue(operationCompare.findMatch(iter1, iter2));
 }

 @Test
 public void testFindMatch_DifferentIteratorsSize() {
 Object iter1 = new IteratorMock(new Object[] { "testString1", "testString2" });
 Object iter2 = new IteratorMock(new Object[] { "testString1" });
 assertFalse(operationCompare.findMatch(iter1, iter2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindMatch_NullIterator() {
 Object iter1 = null;
 Object iter2 = new IteratorMock(new Object[] { "testString1" });
 assertFalse(operationCompare.findMatch(iter1, iter2));
 }

}