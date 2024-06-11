import org.apache.commons.jxpath.axes.InitialContext;
import org.apache.commons.jxpath.axes.SelfContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest {

 @Test
 public void testCoreOperationCompareConstructor() {
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new CoreExpressionTest().createExpression("'a'"),
 new CoreExpressionTest().createExpression("'a'")
 );
 assertNotNull(coreOperationCompare);
 }

 @Test
 public void testEqualMethodWithTwoNullArguments() {
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new CoreExpressionTest().createExpression(null),
 new CoreExpressionTest().createExpression(null)
 );
 assertTrue(coreOperationCompare.equal(new EvalContext(), null, null));
 }

 @Test
 public void testEqualMethodWithTwoEqualObjects() {
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new CoreExpressionTest().createExpression("'a'"),
 new CoreExpressionTest().createExpression("'a'")
 );
 assertTrue(coreOperationCompare.equal(new EvalContext(), null, null));
 }

 @Test
 public void testEqualMethodWithTwoDifferentObjects() {
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new CoreExpressionTest().createExpression("'a'"),
 new CoreExpressionTest().createExpression("'b'")
 );
 assertFalse(coreOperationCompare.equal(new EvalContext(), null, null));
 }

 @Test
 public void testEqualMethodWithInitialContextLeftAndSelfContextRight() {
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new CoreExpressionTest().createExpression(new InitialContext(new Pointer(""))),
 new CoreOperationCompare(
 new CoreExpressionTest().createExpression(new SelfContext(new Pointer(""))),
 new CoreExpressionTest().createExpression(null)
 )
 );
 assertFalse(coreOperationCompare.equal(new EvalContext(), null, null));
 }

 @Test
 public void testEqualMethodWithSelfContextLeftAndInitialContextRight() {
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new CoreExpressionTest().createExpression(new SelfContext(new Pointer(""))),
 new CoreOperationCompare(
 new CoreExpressionTest().createExpression(new InitialContext(new Pointer(""))),
 new CoreExpressionTest().createExpression(null)
 )
 );
 assertFalse(coreOperationCompare.equal(new EvalContext(), null, null));
 }

 @Test
 public void testEqualMethodWithTwoCollections() {
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new CoreExpressionTest().createExpression(Collections.singletonList(1)),
 new CoreExpressionTest().createExpression(Collections.singletonList(1))
 );
 assertTrue(coreOperationCompare.equal(new EvalContext(), null, null));
 }

 @Test
 public void testEqualMethodWithTwoDifferentCollections() {
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new CoreExpressionTest().createExpression(Collections.singletonList(1)),
 new CoreExpressionTest().createExpression(Collections.singletonList(2))
 );
 assertFalse(coreOperationCompare.equal(new EvalContext(), null, null));
 }

 @Test
 public void testEqualMethodWithTwoIterators() {
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new CoreExpressionTest().createExpression(new CoreIteratorTest().createIterator(new ArrayList<Integer>(List.of(1, 2)))),
 new CoreExpressionTest().createExpression(new CoreIteratorTest().createIterator(new ArrayList<Integer>(List.of(1, 2))))
 );
 assertTrue(coreOperationCompare.equal(new EvalContext(), null, null));
 }

 @Test
 public void testEqualMethodWithTwoDifferentIterators() {
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new CoreExpressionTest().createExpression(new CoreIteratorTest().createIterator(new ArrayList<Integer>(List.of(1, 2)))),
 new CoreExpressionTest().createExpression(new CoreIteratorTest().createIterator(new ArrayList<Integer>(List.of(1, 3))))
 );
 assertFalse(coreOperationCompare.equal(new EvalContext(), null, null));
 }

 @Test( expected = IllegalArgumentException.class)
 public void testEqualMethodWithMismatchedTypes() {
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new CoreExpressionTest().createExpression("'a'"),
 new CoreExpressionTest().createExpression(1)
 );
 // The test should throw an IllegalArgumentException
 }

 @Test
 public void testContainsMethodWithNullIteratorAndNullValue() {
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new CoreExpressionTest().createExpression(new CoreIteratorTest().createIterator(null)),
 new CoreExpressionTest().createExpression(null)
 );
 assertFalse(coreOperationCompare.contains(new EvalContext(), null, null));
 }

 @Test
 public void testContainsMethodWithNullIteratorAndNonNullValue() {
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new CoreExpressionTest().createExpression(new CoreIteratorTest().createIterator(null)),
 new CoreExpressionTest().createExpression("'a'")
 );
 assertFalse(coreOperationCompare.contains(new EvalContext(), null, null));
 }

 @Test
 public void testContainsMethodWithNullValueAndNonNullIterator() {
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new CoreExpressionTest().createExpression(new CoreIteratorTest().createIterator(new ArrayList<Integer>(List.of(1)))),
 new CoreExpressionTest().createExpression(null)
 );
 assertFalse(coreOperationCompare.contains(new EvalContext(), null, null));
 }

 @Test
 public void testContainsMethodWithTwoMatchingValues() {
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new CoreExpressionTest().createExpression(new CoreIteratorTest().createIterator(new ArrayList<Integer>(List.of(1, 2)))),
 new CoreExpressionTest().createExpression(1)
 );
 assertTrue(coreOperationCompare.contains(new EvalContext(), null, null));
 }

 @Test
 public void testContainsMethodWithTwoNonMatchingValues() {
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new CoreExpressionTest().createExpression(new CoreIteratorTest().createIterator(new ArrayList<Integer>(List.of(1, 2)))),
 new CoreExpressionTest().createExpression(3)
 );
 assertFalse(coreOperationCompare.contains(new EvalContext(), null, null));
 }

 @Test
 public void testFindMatchMethodWithNullLeftIteratorAndNullRightIterator() {
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new CoreExpressionTest().createExpression(new CoreIteratorTest().createIterator(null)),
 new CoreExpressionTest().createExpression(new CoreIteratorTest().createIterator(null))
 );
 assertFalse(coreOperationCompare.findMatch(new EvalContext(), null, null));
 }

 @Test
 public void testFindMatchMethodWithNullLeftIteratorAndNonNullRightIterator() {
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new CoreExpressionTest().createExpression(new CoreIteratorTest().createIterator(null)),
 new CoreExpressionTest().createExpression(new CoreIteratorTest().createIterator(new ArrayList<Integer>(List.of(1, 2))))
 );
 assertFalse(coreOperationCompare.findMatch(new EvalContext(), null, null));
 }

 @Test
 public void testFindMatchMethodWithNullRightIteratorAndNonNullLeftIterator() {
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new CoreExpressionTest().createExpression(new CoreIteratorTest().createIterator(new ArrayList<Integer>(List.of(1, 2)))),
 new CoreExpressionTest().createExpression(new CoreIteratorTest().createIterator(null))
 );
 assertFalse(coreOperationCompare.findMatch(new EvalContext(), null, null));
 }

 @Test
 public void testFindMatchMethodWithTwoMatchingIterators() {
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new CoreExpressionTest().createExpression(new CoreIteratorTest().createIterator(new ArrayList<Integer>(List.of(1, 2)))),
 new CoreExpressionTest().createExpression(new CoreIteratorTest().createIterator(new ArrayList<Integer>(List.of(1, 2))))
 );
 assertTrue(coreOperationCompare.findMatch(new EvalContext(), null, null));
 }

 @Test
 public void testFindMatchMethodWithTwoNonMatchingIterators() {
 CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
 new CoreExpressionTest().createExpression(new CoreIteratorTest().createIterator(new ArrayList<Integer>(List.of(1, 2)))),
 new CoreExpressionTest().createExpression(new CoreIteratorTest().createIterator(new ArrayList<Integer>(List.of(1, 3))))
 );
 assertFalse(coreOperationCompare.findMatch(new EvalContext(), null, null));
 }

}