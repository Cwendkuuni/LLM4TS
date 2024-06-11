import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest {

 private Expression getExpression(String path, Object value) {
 return new Constant(value);
 }

 @Test
 public void testEqualWithSameValues() {
 CoreOperationCompare compare = new CoreOperationCompare(
 getExpression("/test[1]", 1),
 getExpression("/test[1]", 1)
 );
 JXPathContext context = new JXPathContext();
 context.setNodes(new Object[] { new Integer[] { 1, 2, 3 } });
 assertTrue(compare.evaluateToBoolean(context));
 }

 @Test
 public void testEqualWithDifferentValues() {
 CoreOperationCompare compare = new CoreOperationCompare(
 getExpression("/test[1]", 1),
 getExpression("/test[1]", 2)
 );
 JXPathContext context = new JXPathContext();
 context.setNodes(new Object[] { new Integer[] { 1, 2, 3 } });
 assertFalse(compare.evaluateToBoolean(context));
 }

 @Test
 public void testEqualWithNullValues() {
 CoreOperationCompare compare = new CoreOperationCompare(
 getExpression("/test[1]", null),
 getExpression("/test[1]", null)
 );
 JXPathContext context = new JXPathContext();
 context.setNodes(new Object[] { new Integer[] { 1, 2, 3 } });
 assertTrue(compare.evaluateToBoolean(context));
 }

 @Test
 public void testEqualWithNullAndValue() {
 CoreOperationCompare compare = new CoreOperationCompare(
 getExpression("/test[1]", null),
 getExpression("/test[1]", 1)
 );
 JXPathContext context = new JXPathContext();
 context.setNodes(new Object[] { new Integer[] { 1, 2, 3 } });
 assertFalse(compare.evaluateToBoolean(context));
 }

 @Test
 public void testEqualWithValueAndNull() {
 CoreOperationCompare compare = new CoreOperationCompare(
 getExpression("/test[1]", 1),
 getExpression("/test[1]", null)
 );
 JXPathContext context = new JXPathContext();
 context.setNodes(new Object[] { new Integer[] { 1, 2, 3 } });
 assertFalse(compare.evaluateToBoolean(context));
 }

 @Test
 public void testEqualWithCollectionAndValue() {
 CoreOperationCompare compare = new CoreOperationCompare(
 getExpression("/test[1]/descendant::*[1]", new Integer[] { 1, 2, 3 }),
 getExpression("/test[1]/descendant::*[1]", 1)
 );
 JXPathContext context = new JXPathContext();
 context.setNodes(new Object[] { new Integer[] { 1, 2, 3 } });
 assertTrue(compare.evaluateToBoolean(context));
 }

 @Test
 public void testEqualWithValueAndCollection() {
 CoreOperationCompare compare = new CoreOperationCompare(
 getExpression("/test[1]", 1),
 getExpression("/test[1]/descendant::*[1]", new Integer[] { 1, 2, 3 })
 );
 JXPathContext context = new JXPathContext();
 context.setNodes(new Object[] { new Integer[] { 1, 2, 3 } });
 assertFalse(compare.evaluateToBoolean(context));
 }

 @Test
 public void testEqualWithCollectionAndCollection() {
 CoreOperationCompare compare = new CoreOperationCompare(
 getExpression("/test[1]/descendant::*[1]", new Integer[] { 1, 2, 3 }),
 getExpression("/test[1]/descendant::*[1]", new Integer[] { 1, 2, 3 })
 );
 JXPathContext context = new JXPathContext();
 context.setNodes(new Object[] { new Integer[] { 1, 2, 3 } });
 assertTrue(compare.evaluateToBoolean(context));
 }

 @Test
 public void testFindMatch() {
 CoreOperationCompare compare = new CoreOperationCompare(
 getExpression("/test[1]/descendant::*[1]", new Integer[] { 1, 2, 3 }),
 getExpression("/test[1]/descendant::*[1]", new Integer[] { 2, 3, 4 })
 );
 JXPathContext context = new JXPathContext();
 context.setNodes(new Object[] { new Integer[] { 1, 2, 3, 2, 3, 4 } });
 assertTrue(compare.evaluateToBoolean(context));
 }

 @Test
 public void testFindNoMatch() {
 CoreOperationCompare compare = new CoreOperationCompare(
 getExpression("/test[1]/descendant::*[1]", new Integer[] { 1, 2, 3 }),
 getExpression("/test[1]/descendant::*[1]", new Integer[] { 5, 6, 7 })
 );
 JXPathContext context = new JXPathContext();
 context.setNodes(new Object[] { new Integer[] { 1, 2, 3, 5, 6, 7 } });
 assertFalse(compare.evaluateToBoolean(context));
 }

 @Test
 public void testNotEqualWithSameValues() {
 CoreOperationCompare compare = new CoreOperationCompare(
 getExpression("/test[1]", 1),
 getExpression("/test[1]", 1)
 );
 JXPathContext context = new JXPathContext();
 context.setNodes(new Object[] { new Integer[] { 1, 2, 3 } });
 assertFalse(compare.evaluateToBoolean(context));
 }

 @Test
 public void testNotEqualWithDifferentValues() {
 CoreOperationCompare compare = new CoreOperationCompare(
 getExpression("/test[1]", 1),
 getExpression("/test[1]", 2)
 );
 JXPathContext context = new JXPathContext();
 context.setNodes(new Object[] { new Integer[] { 1, 2, 3 } });
 assertTrue(compare.evaluateToBoolean(context));
 }

 @Test
 public void testNotEqualWithNullValues() {
 CoreOperationCompare compare = new CoreOperationCompare(
 getExpression("/test[1]", null),
 getExpression("/test[1]", null)
 );
 assertNull(compare);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEqualWithNullAndValue() {
 CoreOperationCompare compare = new CoreOperationCompare(
 getExpression("/test[1]", null),
 getExpression("/test[1]", 1)
 );
 JXPathContext context = new JXPathContext();
 context.setNodes(new Object[] { new Integer[] { 1, 2, 3 } });
 // This test should throw an IllegalArgumentException
 compare.evaluateToBoolean(context);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEqualWithValueAndNull() {
 CoreOperationCompare compare = new CoreOperationCompare(
 getExpression("/test[1]", 1),
 getExpression("/test[1]", null)
 );
 JXPathContext context = new JXPathContext();
 context.setNodes(new Object[] { new Integer[] { 1, 2, 3 } });
 // This test should throw an IllegalArgumentException
 compare.evaluateToBoolean(context);
 }
}