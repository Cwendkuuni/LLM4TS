import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

 @Test
 public void testEquals() {
 CoreOperationCompare testee = new CoreOperationCompare(
 new ConstantExpression(new Integer(1)),
 new ConstantExpression(new Integer(1))
 );
 EvalContext context = new EvalContext();
 assertTrue(testee.evaluateToBoolean(context));
 }

 @Test
 public void testNotEquals() {
 CoreOperationCompare testee = new CoreOperationCompare(
 new ConstantExpression(new Integer(1)),
 new ConstantExpression(new Integer(2))
 );
 EvalContext context = new EvalContext();
 assertFalse(testee.evaluateToBoolean(context));
 }

 @Test
 public void testEqualNulls() {
 CoreOperationCompare testee = new CoreOperationCompare(
 new ConstantExpression(null),
 new ConstantExpression(null)
 );
 EvalContext context = new EvalContext();
 assertTrue(testee.evaluateToBoolean(context));
 }

 @Test
 public void testNotEqualNullsAndNonNull() {
 CoreOperationCompare testee = new CoreOperationCompare(
 new ConstantExpression(null),
 new ConstantExpression(new Integer(1))
 );
 EvalContext context = new EvalContext();
 assertFalse(testee.evaluateToBoolean(context));
 }

 @Test
 public void testEqualStrings() {
 CoreOperationCompare testee = new CoreOperationCompare(
 new ConstantExpression("Hello"),
 new ConstantExpression("Hello")
 );
 EvalContext context = new EvalContext();
 assertTrue(testee.evaluateToBoolean(context));
 }

 @Test
 public void testNotEqualStrings() {
 CoreOperationCompare testee = new CoreOperationCompare(
 new ConstantExpression("Hello"),
 new ConstantExpression("World")
 );
 EvalContext context = new EvalContext();
 assertFalse(testee.evaluateToBoolean(context));
 }

 @Test
 public void testEqualCollections() {
 CoreOperationCompare testee = new CoreOperationCompare(
 new ConstantExpression(new ArrayList<Integer>() {{ add(1); add(2); }}),
 new ConstantExpression(new ArrayList<Integer>() {{ add(1); add(2); }})
 );
 EvalContext context = new EvalContext();
 assertTrue(testee.evaluateToBoolean(context));
 }

 @Test
 public void testNotEqualCollections() {
 CoreOperationCompare testee = new CoreOperationCompare(
 new ConstantExpression(new ArrayList<Integer>() {{ add(1); add(2); }}),
 new ConstantExpression(new ArrayList<Integer>() {{ add(2); add(3); }})
 );
 EvalContext context = new EvalContext();
 assertFalse(testee.evaluateToBoolean(context));
 }

 @Test
 public void testEqualIterators() {
 CoreOperationCompare testee = new CoreOperationCompare(
 new ConstantExpression(new ArrayList<Integer>() {{ add(1); add(2); }}.iterator()),
 new ConstantExpression(new ArrayList<Integer>() {{ add(1); add(2); }}.iterator())
 );
 EvalContext context = new EvalContext();
 assertTrue(testee.evaluateToBoolean(context));
 }

 @Test
 public void testNotEqualIteratorsDifferentSize() {
 CoreOperationCompare testee = new CoreOperationCompare(
 new ConstantExpression(new ArrayList<Integer>() {{ add(1); add(2); }}.iterator()),
 new ConstantExpression(new ArrayList<Integer>() {{ add(1); add(2); add(3); }}.iterator())
 );
 EvalContext context = new EvalContext();
 assertFalse(testee.evaluateToBoolean(context));
 }

 @Test(expected = ClassCastException.class)
 public void testEqualMixedTypes() {
 CoreOperationCompare testee = new CoreOperationCompare(
 new ConstantExpression("Hello"),
 new ConstantExpression(new Integer(1))
 );
 EvalContext context = new EvalContext();
 // this test should fail with a ClassCastException
 testee.evaluateToBoolean(context);
 }

 @Test
 public void testEqualsContext() {
 CoreOperationCompare testee = new CoreOperationCompare(
 new ConstantExpression(new InitialContext()),
 new ConstantExpression(new InitialContext())
 );
 EvalContext context = new EvalContext();
 context.setSingleNodePointer(new Pointer("//node()"));
 assertTrue(testee.evaluateToBoolean(context));
 }

 @Test
 public void testNotEqualsContext() {
 CoreOperationCompare testee = new CoreOperationCompare(
 new ConstantExpression(new InitialContext()),
 new ConstantExpression(new SelfContext())
 );
 EvalContext context = new EvalContext();
 context.setSingleNodePointer(new Pointer("//node()"));
 assertFalse(testee.evaluateToBoolean(context));
 }
}