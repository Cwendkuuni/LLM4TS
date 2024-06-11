package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.EvaluationContext;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.Axis;
import org.apache.commons.jxpath.ri.compiled.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiled.CoreOperationTest;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare.*; //for accessing protected methods
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * CoreOperationCompareTest class contains test methods for testing CoreOperationCompare class.
 */
public class CoreOperationCompareTest extends CoreOperationTest {

 /**
  * Test for {@code equal(EvaluationContext, Expression, Expression)} method in {@code CoreOperationCompare} class.
  */
 @Test
 public void testEqual() {
 // Test with null values
 Expression leftNull = new Const(null);
 Expression rightNull = new Const(null);
 CoreOperationCompare coreOperationCompare = new Eq(leftNull, rightNull);
 assertTrue(coreOperationCompare.evaluate(context));

 // Test with non-null and equal values
 Expression leftInt = new Const(1);
 Expression rightInt = new Const(1);
 CoreOperationCompare coreOperationCompareInt = new Eq(leftInt, rightInt);
 assertTrue(coreOperationCompareInt.evaluate(context));

 // Test with non-null and unequal values
 Expression leftString = new Const("John");
 Expression rightString = new Const("Mike");
 CoreOperationCompare coreOperationCompareString = new Eq(leftString, rightString);
 assertFalse(coreOperationCompareString.evaluate(context));

 // Test with empty collections
 Expression leftList = new List(Axis.DESCENDANT, new Const(Collections.emptyList()));
 Expression rightList = new List(Axis.DESCENDANT, new Const(Collections.emptyList()));
 CoreOperationCompare coreOperationCompareEmptyList = new Eq(leftList, rightList);
 assertTrue(coreOperationCompareEmptyList.evaluate(context));

 // Test with non-empty and equal collections
 Expression leftList1 = new List(Axis.DESCENDANT, Arrays.asList(new Const(1), new Const(2)));
 Expression rightList1 = new List(Axis.DESCENDANT, Arrays.asList(new Const(1), new Const(2)));
 CoreOperationCompare coreOperationCompareList1 = new Eq(leftList1, rightList1);
 assertTrue(coreOperationCompareList1.evaluate(context));

 // Test with non-empty and unequal collections
 Expression leftList2 = new List(Axis.DESCENDANT, Arrays.asList(new Const(1), new Const(2)));
 Expression rightList2 = new List(Axis.DESCENDANT, Arrays.asList(new Const(2), new Const(1)));
 CoreOperationCompare coreOperationCompareList2 = new Eq(leftList2, rightList2);
 assertFalse(coreOperationCompareList2.evaluate(context));
 }

 /**
  * Test for {@code contains(Iterator, Object)} method in {@code CoreOperationCompare} class.
  */
 @Test
 public void testContains() {
 // Test with empty iterator and non-empty collection
 Expression leftEmptyIterator = new List(Axis.CHILD, Collections.emptyList());
 Expression rightList = new List(Axis.DESCENDANT, Arrays.asList(new Const(1), new Const(2)));
 CoreOperationCompare coreOperationCompareContainsEmpty = new Eq(leftEmptyIterator, rightList);
 assertFalse(coreOperationCompareContainsEmpty.evaluate(context));

 // Test with non-empty iterator and non-matching value
 Iterator leftIterator = Arrays.asList(new Const(1), new Const(2), new Const(3))
 .iterator();
 Expression rightValue = new Const(4);
 CoreOperationCompare coreOperationCompareContainsNotMatch = new Eq(leftIterator, rightValue);
 assertFalse(coreOperationCompareContainsNotMatch.evaluate(context));

 // Test with non-empty iterator and matching value
 Iterator leftIteratorMatch = Arrays.asList(new Const(1), new Const(2), new Const(3))
 .iterator();
 Expression rightValueMatch = new Const(2);
 CoreOperationCompare coreOperationCompareContainsMatch = new Eq(leftIteratorMatch, rightValueMatch);
 assertTrue(coreOperationCompareContainsMatch.evaluate(context));
 }

 /**
  * Test for {@code findMatch(Iterator, Iterator)} method in {@code CoreOperationCompare} class.
  */
 @Test
 public void testFindMatch() {
 // Test with empty iterators
 Iterator leftEmptyIterator = Collections.emptyIterator();
 Iterator rightEmptyIterator = Collections.emptyIterator();
 CoreOperationCompare coreOperationCompareFindMatchEmpty = new Eq(leftEmptyIterator, rightEmptyIterator);
 assertTrue(coreOperationCompareFindMatchEmpty.evaluate(context));

 // Test with non-empty and matching iterators
 Iterator leftIterator1 = Arrays.asList(new Const(1), new Const(2), new Const(3))
 .iterator();
 Iterator rightIterator1 = Arrays.asList(new Const(1), new Const(2), new Const(3))
 .iterator();
 CoreOperationCompare coreOperationCompareFindMatchMatch = new Eq(leftIterator1, rightIterator1);
 assertTrue(coreOperationCompareFindMatchMatch.evaluate(context));

 // Test with non-empty and non-matching iterators
 Iterator leftIterator2 = Arrays.asList(new Const(1), new Const(2), new Const(3))
 .iterator();
 Iterator rightIterator2 = Arrays.asList(new Const(3), new Const(2), new Const(1))
 .iterator();
 CoreOperationCompare coreOperationCompareFindMatchNotMatch = new Eq(leftIterator2, rightIterator2);
 assertFalse(coreOperationCompareFindMatchNotMatch.evaluate(context));
 }
}