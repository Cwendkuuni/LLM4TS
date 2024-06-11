import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationCompareTest {

 private CoreOperationCompare op;
 private Expression arg1;
 private Expression arg2;
 private EvalContext context;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 this.arg1 = new ExpressionMock();
 this.arg2 = new ExpressionMock();
 this.context = new EvalContextMock();
 this.op = new CoreOperationCompare(this.arg1, this.arg2);
 }

 @Test
 public void testCoreOperationCompareConstructor() {
 assertNotNull(op);
 }

 @Test
 public void testEqualNull() {
 assertFalse(op.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualBooleanTrueTrue() {
 Boolean t1 = Boolean.TRUE;
 Boolean t2 = Boolean.TRUE;
 context.setSingleNodePointer(t1);
 context.setSingleNodePointer(t2);
 assertTrue(op.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualBooleanTrueFalse() {
 Boolean t1 = Boolean.TRUE;
 Boolean t2 = Boolean.FALSE;
 context.setSingleNodePointer(t1);
 context.setSingleNodePointer(t2);
 assertFalse(op.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualBooleanFalseTrue() {
 Boolean t1 = Boolean.FALSE;
 Boolean t2 = Boolean.TRUE;
 context.setSingleNodePointer(t1);
 context.setSingleNodePointer(t2);
 assertFalse(op.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualBooleanFalseFalse() {
 Boolean t1 = Boolean.FALSE;
 Boolean t2 = Boolean.FALSE;
 context.setSingleNodePointer(t1);
 context.setSingleNodePointer(t2);
 assertTrue(op.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualNumberDoubleZeroZero() {
 Double d1 = 0.0;
 Double d2 = 0.0;
 context.setSingleNodePointer(d1);
 context.setSingleNodePointer(d2);
 assertTrue(op.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualNumberDoubleZeroOne() {
 Double d1 = 0.0;
 Double d2 = 1.0;
 context.setSingleNodePointer(d1);
 context.setSingleNodePointer(d2);
 assertFalse(op.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualNumberDoubleOneZero() {
 Double d1 = 1.0;
 Double d2 = 0.0;
 context.setSingleNodePointer(d1);
 context.setSingleNodePointer(d2);
 assertFalse(op.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualNumberDoubleOneOne() {
 Double d1 = 1.0;
 Double d2 = 1.0;
 context.setSingleNodePointer(d1);
 context.setSingleNodePointer(d2);
 assertTrue(op.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualStringEmptyStringEmptyString() {
 String s1 = "";
 String s2 = "";
 context.setSingleNodePointer(s1);
 context.setSingleNodePointer(s2);
 assertTrue(op.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualStringEmptyStringNonEmptyString() {
 String s1 = "";
 String s2 = "test";
 context.setSingleNodePointer(s1);
 context.setSingleNodePointer(s2);
 assertFalse(op.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualStringNonEmptyStringEmptyString() {
 String s1 = "test";
 String s2 = "";
 context.setSingleNodePointer(s1);
 context.setSingleNodePointer(s2);
 assertFalse(op.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualStringNonEmptyStringNonEmptyString() {
 String s1 = "test";
 String s2 = "test";
 context.setSingleNodePointer(s1);
 context.setSingleNodePointer(s2);
 assertTrue(op.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualPointerNullNull() {
 assertFalse(op.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualPointerNullPointer() {
 assertFalse(op.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualPointerPointerNull() {
 assertFalse(op.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualPointerPointerEqual() {
 Pointer p1 = new PointerMock();
 Pointer p2 = new PointerMock();
 context.setSingleNodePointer(p1);
 context.setSingleNodePointer(p2);
 p1.setValue(p2.getValue());
 assertTrue(op.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualPointerPointerNotEqual() {
 Pointer p1 = new PointerMock();
 Pointer p2 = new PointerMock();
 context.setSingleNodePointer(p1);
 context.setSingleNodePointer(p2);
 p1.setValue("test1");
 p2.setValue("test2");
 assertFalse(op.equal(context, arg1, arg2));
 }

 @Test
 public void testContainsIteratorEmptyCollection() {
 Collection c = new HashSet();
 Iterator it = c.iterator();
 assertFalse(op.contains(it, arg1));
 }

 @Test
 public void testContainsIteratorSingleElement() {
 Collection c = new HashSet();
 c.add(arg1);
 Iterator it = c.iterator();
 assertTrue(op.contains(it, arg1));
 }

 @Test
 public void testContainsIteratorDifferentElements() {
 Collection c = new HashSet();
 c.add(arg1);
 Iterator it = new IteratorMock();
 it.next = () -> arg2;
 assertFalse(op.contains(it, arg1));
 }

 @Test
 public void testFindMatchIteratorEmptyCollection() {
 Collection l = new HashSet();
 Collection r = new HashSet();
 Iterator lit = l.iterator();
 Iterator rit = r.iterator();
 assertFalse(op.findMatch(lit, rit));
 }

 @Test
 public void testFindMatchIteratorSingleElementLeft() {
 Collection l = new HashSet();
 l.add(arg1);
 Collection r = new HashSet();
 Iterator lit = l.iterator();
 Iterator rit = r.iterator();
 assertFalse(op.findMatch(lit, rit));
 }

 @Test
 public void testFindMatchIteratorSingleElementRight() {
 Collection l = new HashSet();
 Iterator lit = new IteratorMock();
 lit.next = () -> arg1;
 Collection r = new HashSet();
 r.add(arg2);
 Iterator rit = r.iterator();
 assertFalse(op.findMatch(lit, rit));
 }

 @Test
 public void testFindMatchIteratorMultipleElementsBoth() {
 Collection l = new HashSet();
 l.add(arg1);
 l.add(arg1);
 Collection r = new HashSet();
 r.add(arg2);
 r.add(arg2);
 Iterator lit = l.iterator();
 Iterator rit = r.iterator();
 assertTrue(op.findMatch(lit, rit));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFindMatchIteratorSingleElementLeftThrowsException() {
 Collection l = new HashSet();
 l.add(arg1);
 Collection r = new HashSet();
 Iterator lit = new IteratorMock();
 lit.hasNext = () -> false;
 Iterator rit = r.iterator();
 op.findMatch(lit, rit);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFindMatchIteratorSingleElementRightThrowsException() {
 Collection l = new HashSet();
 Iterator lit = l.iterator();
 lit.next = () -> arg1;
 Collection r = new HashSet();
 Iterator rit = new IteratorMock();
 rit.hasNext = () -> false;
 op.findMatch(lit, rit);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFindMatchIteratorEmptyLeftThrowsException() {
 Collection l = new HashSet();
 Collection r = new HashSet();
 Iterator lit = r.iterator();
 Iterator rit = r.iterator();
 op.findMatch(lit, rit);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFindMatchIteratorEmptyRightThrowsException() {
 Collection l = new HashSet();
 Collection r = new HashSet();
 Iterator lit = l.iterator();
 Iterator rit = new IteratorMock();
 rit.hasNext = () -> false;
 op.findMatch(lit, rit);
 }
}