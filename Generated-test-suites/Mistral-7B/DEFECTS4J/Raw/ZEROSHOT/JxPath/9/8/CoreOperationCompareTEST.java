import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

 @Test
 public void testCoreOperationCompare() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new LiteralExpression(new Integer(1)),
 new LiteralExpression(new Integer(1))
 );
 EvalContext context = new EvalContext();
 assertTrue(compare.equal(context, null, null));
 assertTrue(compare.equal(context, compare, null));
 assertTrue(compare.equal(context, compare, compare));

 Object result = compare.compute(context);
 assertNull(result);
 }

 @Test
 public void testCoreOperationCompareDifferentTypes() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new LiteralExpression(new Integer(1)),
 new LiteralExpression(new String("1"))
 );
 EvalContext context = new EvalContext();
 assertFalse(compare.equal(context, null, null));
 assertFalse(compare.equal(context, compare, null));
 assertFalse(compare.equal(context, compare, compare));

 Object result = compare.compute(context);
 assertNull(result);
 }

 @Test
 public void testCoreOperationCompareBooleanValues() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new LiteralExpression(Boolean.TRUE),
 new LiteralExpression(Boolean.TRUE)
 );
 EvalContext context = new EvalContext();
 assertTrue(compare.equal(context, null, null));
 assertTrue(compare.equal(context, compare, null));
 assertTrue(compare.equal(context, compare, compare));

 Object result = compare.compute(context);
 assertNull(result);
 }

 @Test
 public void testCoreOperationCompareBooleanValuesDifferent() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new LiteralExpression(Boolean.TRUE),
 new LiteralExpression(Boolean.FALSE)
 );
 EvalContext context = new EvalContext();
 assertFalse(compare.equal(context, null, null));
 assertFalse(compare.equal(context, compare, null));
 assertFalse(compare.equal(context, compare, compare));

 Object result = compare.compute(context);
 assertNull(result);
 }

 @Test
 public void testCoreOperationCompareNumberValues() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new LiteralExpression(new Double(1.0)),
 new LiteralExpression(new Double(1.0))
 );
 EvalContext context = new EvalContext();
 assertTrue(compare.equal(context, null, null));
 assertTrue(compare.equal(context, compare, null));
 assertTrue(compare.equal(context, compare, compare));

 Object result = compare.compute(context);
 assertNull(result);
 }

 @Test
 public void testCoreOperationCompareNumberValuesDifferent() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new LiteralExpression(new Double(1.0)),
 new LiteralExpression(new Double(2.0))
 );
 EvalContext context = new EvalContext();
 assertFalse(compare.equal(context, null, null));
 assertFalse(compare.equal(context, compare, null));
 assertFalse(compare.equal(context, compare, compare));

 Object result = compare.compute(context);
 assertNull(result);
 }

 @Test
 public void testCoreOperationCompareStringValues() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new LiteralExpression("1"),
 new LiteralExpression("1")
 );
 EvalContext context = new EvalContext();
 assertTrue(compare.equal(context, null, null));
 assertTrue(compare.equal(context, compare, null));
 assertTrue(compare.equal(context, compare, compare));

 Object result = compare.compute(context);
 assertNull(result);
 }

 @Test
 public void testCoreOperationCompareStringValuesDifferent() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new LiteralExpression("1"),
 new LiteralExpression("2")
 );
 EvalContext context = new EvalContext();
 assertFalse(compare.equal(context, null, null));
 assertFalse(compare.equal(context, compare, null));
 assertFalse(compare.equal(context, compare, compare));

 Object result = compare.compute(context);
 assertNull(result);
 }

 @Test
 public void testCoreOperationCompareCollections() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new CollectionExpression(new ArrayList<Object>() {{ add(new Integer(1)); }}),
 new CollectionExpression(new ArrayList<Object>() {{ add(new Integer(1)); }})
 );
 EvalContext context = new EvalContext();
 assertTrue(compare.equal(context, null, null));
 assertTrue(compare.equal(context, compare, null));
 assertTrue(compare.equal(context, compare, compare));

 Object result = compare.compute(context);
 assertNull(result);
 }

 @Test
 public void testCoreOperationCompareCollectionsDifferent() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new CollectionExpression(new ArrayList<Object>() {{ add(new Integer(1)); }}),
 new CollectionExpression(new ArrayList<Object>() {{ add(new Integer(2)); }})
 );
 EvalContext context = new EvalContext();
 assertFalse(compare.equal(context, null, null));
 assertFalse(compare.equal(context, compare, null));
 assertFalse(compare.equal(context, compare, compare));

 Object result = compare.compute(context);
 assertNull(result);
 }

 @Test
 public void testCoreOperationCompareIterators() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new CollectionExpression(new ArrayList<Object>() {{ add(new Integer(1)); add(new Integer(2)); }}),
 new CollectionExpression(new ArrayList<Object>() {{ add(new Integer(1)); }})
 );
 EvalContext context = new EvalContext();
 assertFalse(compare.equal(context, null, null));
 assertFalse(compare.equal(context, compare, null));
 assertFalse(compare.equal(context, compare, compare));

 Object result = compare.compute(context);
 assertNull(result);
 }

 @Test
 public void testCoreOperationCompareIteratorsMatch() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new CollectionExpression(new ArrayList<Object>() {{ add(new Integer(1)); add(new Integer(1)); }}),
 new CollectionExpression(new ArrayList<Object>() {{ add(new Integer(1)); }})
 );
 EvalContext context = new EvalContext();
 assertTrue(compare.equal(context, null, null));
 assertTrue(compare.equal(context, compare, null));
 assertTrue(compare.equal(context, compare, compare));

 Object result = compare.compute(context);
 assertNull(result);
 }
}