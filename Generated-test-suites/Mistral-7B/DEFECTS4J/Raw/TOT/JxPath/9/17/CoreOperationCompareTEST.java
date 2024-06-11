import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.axes.JXPathConstants;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.Constant;
import org.apache.commons.jxpath.ri.compiler.NumberValue;
import org.apache.commons.jxpath.ri.compiler.StringValue;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest {

 @Test
 public void testEqual_boolean_boolean() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(true), new Constant(true));
 JXPathContext ctx = new JXPathContext();
 assertTrue(op.evaluate(ctx, null)[0]);
 }

 @Test
 public void testEqual_boolean_false() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(true), new Constant(false));
 JXPathContext ctx = new JXPathContext();
 assertFalse(op.evaluate(ctx, null)[0]);
 }

 @Test
 public void testEqual_boolean_null() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(true), new Constant(null));
 JXPathContext ctx = new JXPathContext();
 assertNull(op.evaluate(ctx, null));
 }

 @Test
 public void testEqual_boolean_Other() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(true), new StringValue("Other"));
 JXPathContext ctx = new JXPathContext();
 assertNull(op.evaluate(ctx, null));
 }

 @Test
 public void testEqual_null_boolean() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(null), new Constant(true));
 JXPathContext ctx = new JXPathContext();
 assertNull(op.evaluate(ctx, null));
 }

 @Test
 public void testEqual_null_false() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(null), new Constant(false));
 JXPathContext ctx = new JXPathContext();
 assertNull(op.evaluate(ctx, null));
 }

 @Test
 public void testEqual_null_null() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(null), new Constant(null));
 JXPathContext ctx = new JXPathContext();
 assertTrue(op.evaluate(ctx, null)[0]);
 }

 @Test
 public void testEqual_number_number() {
 CoreOperationCompare op = new CoreOperationCompare(
 new NumberValue(1), new NumberValue(1));
 JXPathContext ctx = new JXPathContext();
 assertTrue(op.evaluate(ctx, null)[0]);
 }

 @Test
 public void testEqual_number_number_notEqual() {
 CoreOperationCompare op = new CoreOperationCompare(
 new NumberValue(1), new NumberValue(2));
 JXPathContext ctx = new JXPathContext();
 assertFalse(op.evaluate(ctx, null)[0]);
 }

 @Test
 public void testEqual_number_null() {
 CoreOperationCompare op = new CoreOperationCompare(
 new NumberValue(1), new Constant(null));
 JXPathContext ctx = new JXPathContext();
 assertNull(op.evaluate(ctx, null));
 }

 @Test
 public void testEqual_number_Other() {
 CoreOperationCompare op = new CoreOperationCompare(
 new NumberValue(1), new StringValue("Other"));
 JXPathContext ctx = new JXPathContext();
 assertNull(op.evaluate(ctx, null));
 }

 @Test
 public void testEqual_String_String() {
 CoreOperationCompare op = new CoreOperationCompare(
 new StringValue("String1"), new StringValue("String1"));
 JXPathContext ctx = new JXPathContext();
 assertTrue(op.evaluate(ctx, null)[0]);
 }

 @Test
 public void testEqual_String_String_notEqual() {
 CoreOperationCompare op = new CoreOperationCompare(
 new StringValue("String1"), new StringValue("String2"));
 JXPathContext ctx = new JXPathContext();
 assertFalse(op.evaluate(ctx, null)[0]);
 }

 @Test
 public void testEqual_String_null() {
 CoreOperationCompare op = new CoreOperationCompare(
 new StringValue("String1"), new Constant(null));
 JXPathContext ctx = new JXPathContext();
 assertNull(op.evaluate(ctx, null));
 }

 @Test
 public void testEqual_String_Other() {
 CoreOperationCompare op = new CoreOperationCompare(
 new StringValue("String1"), new StringValue("Other"));
 JXPathContext ctx = new JXPathContext();
 assertNull(op.evaluate(ctx, null));
 }

 @Test
 public void testEqual_iterators() {
 CoreOperationCompare op = new CoreOperationCompare(
 new IteratorValue(new Object[] { 1, 2, 3 }),
 new IteratorValue(new Object[] { 1, 2, 3 }));
 JXPathContext ctx = new JXPathContext();
 assertTrue(op.evaluate(ctx, null)[0]);
 }

 @Test
 public void testEqual_iterators_notMatching() {
 CoreOperationCompare op = new CoreOperationCompare(
 new IteratorValue(new Object[] { 1, 2, 3 }),
 new IteratorValue(new Object[] { 1, 2, 4 }));
 JXPathContext ctx = new JXPathContext();
 assertFalse(op.evaluate(ctx, null)[0]);
 }

 @Test
 public void testEqual_iterators_null() {
 CoreOperationCompare op = new CoreOperationCompare(
 new IteratorValue(new Object[] { 1, 2, 3 }),
 new Constant(null));
 JXPathContext ctx = new JXPathContext();
 assertNull(op.evaluate(ctx, null));
 }

 @Test
 public void testEqual_iterators_Other() {
 CoreOperationCompare op = new CoreOperationCompare(
 new IteratorValue(new Object[] { 1, 2, 3 }),
 new IteratorValue(new Object[] { 4, 5, 6 }));
 JXPathContext ctx = new JXPathContext();
 assertNull(op.evaluate(ctx, null));
 }

 @Test
 public void testNotEqual_boolean_boolean() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(true), new Constant(true));
 JXPathContext ctx = new JXPathContext();
 assertFalse(op.evaluate(ctx, null)[0]);
 }

 @Test
 public void testNotEqual_boolean_false() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(true), new Constant(false));
 JXPathContext ctx = new JXPathContext();
 assertTrue(op.evaluate(ctx, null)[0]);
 }

 @Test
 public void testNotEqual_boolean_null() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(true), new Constant(null));
 JXPathContext ctx = new JXPathContext();
 assertFalse(op.evaluate(ctx, null)[0]);
 }

 @Test
 public void testNotEqual_boolean_Other() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(true), new StringValue("Other"));
 JXPathContext ctx = new JXPathContext();
 assertNull(op.evaluate(ctx, null));
 }

 @Test
 public void testNotEqual_null_boolean() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(null), new Constant(true));
 JXPathContext ctx = new JXPathContext();
 assertFalse(op.evaluate(ctx, null)[0]);
 }

 @Test
 public void testNotEqual_null_false() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(null), new Constant(false));
 JXPathContext ctx = new JXPathContext();
 assertFalse(op.evaluate(ctx, null)[0]);
 }

 @Test
 public void testNotEqual_null_null() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(null), new Constant(null));
 JXPathContext ctx = new JXPathContext();
 assertTrue(op.evaluate(ctx, null)[0]);
 }

 @Test
 public void testNotEqual_number_number() {
 CoreOperationCompare op = new CoreOperationCompare(
 new NumberValue(1), new NumberValue(1));
 JXPathContext ctx = new JXPathContext();
 assertTrue(op.evaluate(ctx, null)[0]);
 }

 @Test
 public void testNotEqual_number_number_notEqual() {
 CoreOperationCompare op = new CoreOperationCompare(
 new NumberValue(1), new NumberValue(2));
 JXPathContext ctx = new JXPathContext();
 assertFalse(op.evaluate(ctx, null)[0]);
 }

 @Test
 public void testNotEqual_number_null() {
 CoreOperationCompare op = new CoreOperationCompare(
 new NumberValue(1), new Constant(null));
 JXPathContext ctx = new JXPathContext();
 assertFalse(op.evaluate(ctx, null)[0]);
 }

 @Test
 public void testNotEqual_number_Other() {
 CoreOperationCompare op = new CoreOperationCompare(
 new NumberValue(1), new StringValue("Other"));
 JXPathContext ctx = new JXPathContext();
 assertNull(op.evaluate(ctx, null));
 }

 @Test
 public void testNotEqual_String_String() {
 CoreOperationCompare op = new CoreOperationCompare(
 new StringValue("String1"), new StringValue("String1"));
 JXPathContext ctx = new JXPathContext();
 assertTrue(op.evaluate(ctx, null)[0]);
 }

 @Test
 public void testNotEqual_String_String_notEqual() {
 CoreOperationCompare op = new CoreOperationCompare(
 new StringValue("String1"), new StringValue("String2"));
 JXPathContext ctx = new JXPathContext();
 assertFalse(op.evaluate(ctx, null)[0]);
 }

 @Test
 public void testNotEqual_String_null() {
 CoreOperationCompare op = new CoreOperationCompare(
 new StringValue("String1"), new Constant(null));
 JXPathContext ctx = new JXPathContext();
 assertFalse(op.evaluate(ctx, null)[0]);
 }

 @Test
 public void testNotEqual_String_Other() {
 CoreOperationCompare op = new CoreOperationCompare(
 new StringValue("String1"), new StringValue("Other"));
 JXPathContext ctx = new JXPathContext();
 assertNull(op.evaluate(ctx, null));
 }

 @Test
 public void testNotEqual_iterators() {
 CoreOperationCompare op = new CoreOperationCompare(
 new IteratorValue(new Object[] { 1, 2, 3 }),
 new IteratorValue(new Object[] { 1, 2, 3 }));
 JXPathContext ctx = new JXPathContext();
 assertTrue(op.evaluate(ctx, null)[0]);
 }

 @Test
 public void testNotEqual_iterators_notMatching() {
 CoreOperationCompare op = new CoreOperationCompare(
 new IteratorValue(new Object[] { 1, 2, 3 }),
 new IteratorValue(new Object[] { 1, 2, 4 }));
 JXPathContext ctx = new JXPathContext();
 assertFalse(op.evaluate(ctx, null)[0]);
 }

 @Test
 public void testNotEqual_iterators_null() {
 CoreOperationCompare op = new CoreOperationCompare(
 new IteratorValue(new Object[] { 1, 2, 3 }),
 new Constant(null));
 JXPathContext ctx = new JXPathContext();
 assertFalse(op.evaluate(ctx, null)[0]);
 }

 @Test
 public void testNotEqual_iterators_Other() {
 CoreOperationCompare op = new CoreOperationCompare(
 new IteratorValue(new Object[] { 1, 2, 3 }),
 new IteratorValue(new Object[] { 4, 5, 6 }));
 JXPathContext ctx = new JXPathContext();
 assertNull(op.evaluate(ctx, null));
 }
}