import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

 private CoreOperationCompare operation;
 private Expression arg1;
 private Expression arg2;
 private EvalContext context;

 @Test
 public void testConstructor() {
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
 assertNotNull(coreOperationCompare);
 }

 @Test
 public void testGetPrecedence() {
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
 assertEquals(2, coreOperationCompare.getPrecedence());
 }

 @Test
 public void testIsSymmetric() {
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
 assertTrue(coreOperationCompare.isSymmetric());
 }

 @Test
 public void testEqualNullContext() {
 operation = new CoreOperationCompare(new ConstantExpression(null), new ConstantExpression(null));
 context = new EvalContext();
 assertFalse(operation.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualNullLeft() {
 operation = new CoreOperationCompare(new ConstantExpression(null), arg2);
 context = new EvalContext();
 assertFalse(operation.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualNullRight() {
 operation = new CoreOperationCompare(arg1, new ConstantExpression(null));
 context = new EvalContext();
 assertFalse(operation.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualBooleanTrueTrue() {
 operation = new CoreOperationCompare(new BooleanExpression(true), new BooleanExpression(true));
 context = new EvalContext();
 assertTrue(operation.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualBooleanTrueFalse() {
 operation = new CoreOperationCompare(new BooleanExpression(true), new BooleanExpression(false));
 context = new EvalContext();
 assertFalse(operation.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualBooleanFalseTrue() {
 operation = new CoreOperationCompare(new BooleanExpression(false), new BooleanExpression(true));
 context = new EvalContext();
 assertFalse(operation.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualBooleanFalseFalse() {
 operation = new CoreOperationCompare(new BooleanExpression(false), new BooleanExpression(false));
 context = new EvalContext();
 assertTrue(operation.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualNumberDouble() {
 operation = new CoreOperationCompare(new ConstantExpression(1.0), new ConstantExpression(1.0));
 context = new EvalContext();
 assertTrue(operation.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualNumberDoubleNotEqual() {
 operation = new CoreOperationCompare(new ConstantExpression(1.0), new ConstantExpression(2.0));
 context = new EvalContext();
 assertFalse(operation.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualString() {
 operation = new CoreOperationCompare(new ConstantExpression("Test"), new ConstantExpression("Test"));
 context = new EvalContext();
 assertTrue(operation.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualStringNotEqual() {
 operation = new CoreOperationCompare(new ConstantExpression("Test"), new ConstantExpression("Test2"));
 context = new EvalContext();
 assertFalse(operation.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualPointer() {
 Pointer l = new Pointer("Test");
 Pointer r = new Pointer("Test");
 operation = new CoreOperationCompare(l, r);
 context = new EvalContext();
 assertTrue(operation.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualPointerNotEqual() {
 Pointer l = new Pointer("Test");
 Pointer r = new Pointer("Test2");
 operation = new CoreOperationCompare(l, r);
 context = new EvalContext();
 assertFalse(operation.equal(context, arg1, arg2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualInvalidParameters() {
 operation = new CoreOperationCompare(arg1, null);
 context = new EvalContext();
 assertFalse(operation.equal(context, arg1, arg2));
 }

 @Test
 public void testContainsNullIterator() {
 operation = new CoreOperationCompare(new IteratorExpression(new HashSet<Object>().iterator()), null);
 context = new EvalContext();
 assertFalse(operation.contains(context, arg1, arg2));
 }

 @Test
 public void testContainsEmptyIterator() {
 operation = new CoreOperationCompare(new IteratorExpression(new HashSet<Object>().iterator()), new ConstantExpression(null));
 context = new EvalContext();
 assertFalse(operation.contains(context, arg1, arg2));
 }

 @Test
 public void testContainsEmptyRightIterator() {
 operation = new CoreOperationCompare(new ConstantExpression(null), new IteratorExpression(new HashSet<Object>().iterator()));
 context = new EvalContext();
 assertFalse(operation.contains(context, arg1, arg2));
 }

 @Test
 public void testContainsMatchingElements() {
 operation = new CoreOperationCompare(new IteratorExpression(new HashSet<Object>(Arrays.asList(new Integer(1), new Integer(1))).iterator()), new IteratorExpression(new HashSet<Object>(Arrays.asList(new Integer(1), new Integer(1))).iterator()));
 context = new EvalContext();
 assertTrue(operation.contains(context, arg1, arg2));
 }

 @Test
 public void testContainsNoMatchingElements() {
 operation = new CoreOperationCompare(new IteratorExpression(new HashSet<Object>(Arrays.asList(new Integer(1), new Integer(2))).iterator()), new IteratorExpression(new HashSet<Object>(Arrays.asList(new Integer(2), new Integer(2))).iterator()));
 context = new EvalContext();
 assertFalse(operation.contains(context, arg1, arg2));
 }

 @Test
 public void testFindMatch() {
 HashSet left = new HashSet(Arrays.asList(new Integer(1), new Integer(2), new Integer(3)));
 HashSet right = new HashSet(Arrays.asList(new Integer(2), new Integer(1), new Integer(3)));
 operation = new CoreOperationCompare(new IteratorExpression(left.iterator()), new IteratorExpression(right.iterator()));
 context = new EvalContext();
 assertTrue(operation.findMatch(context, arg1, arg2));
 }

 @Test
 public void testFindNoMatch() {
 HashSet left = new HashSet(Arrays.asList(new Integer(1), new Integer(2), new Integer(3)));
 HashSet right = new HashSet(Arrays.asList(new Integer(2), new Integer(4), new Integer(5)));
 operation = new CoreOperationCompare(new IteratorExpression(left.iterator()), new IteratorExpression(right.iterator()));
 context = new EvalContext();
 assertFalse(operation.findMatch(context, arg1, arg2));
 }
}