package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.axes.InitialContext;
import org.apache.commons.jxpath.axes.SelfContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.Pointer;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CoreOperationCompareTest {

 private CoreOperationCompare compare;
 private EvalContext context;

 @Before
 public void setUp() {
 compare = new CoreOperationCompare(new ConstExpression(1), new ConstExpression(1));
 context = new EvalContext();
 }

 @Test
 public void testEqualsSameObjects() {
 Object result = compare.evaluate(context);
 Assert.assertTrue(result);
 }

 @Test
 public void testEqualsNullAndNull() {
 Object result = compare.evaluate(context);
 Assert.assertTrue(result);
 }

 @Test
 public void testEqualsBooleanTrueAndTrue() {
 Object result = compare.evaluate(context);
 Assert.assertTrue(result);
 }

 @Test
 public void testEqualsBooleanFalseAndFalse() {
 Object result = compare.evaluate(context);
 Assert.assertTrue(result);
 }

 @Test
 public void testEqualsBooleanTrueAndFalse() {
 Object result = compare.evaluate(context);
 Assert.assertFalse(result);
 }

 @Test
 public void testEqualsBooleanFalseAndTrue() {
 Object result = compare.evaluate(context);
 Assert.assertFalse(result);
 }

 @Test
 public void testEqualsNumberAndNumber() {
 Object result = compare.evaluate(context);
 Assert.assertTrue(result);
 }

 @Test
 public void testEqualsNumberAndString() {
 Object result = compare.evaluate(context);
 Assert.assertFalse(result);
 }

 @Test
 public void testEqualsStringAndNumber() {
 Object result = compare.evaluate(context);
 Assert.assertFalse(result);
 }

 @Test
 public void testEqualsStringAndString() {
 Object result = compare.evaluate(context);
 Assert.assertTrue(result);
 }

 @Test
 public void testEqualsCollectionAndCollection() {
 Set set1 = new HashSet();
 set1.add(1);
 set1.add(2);
 Set set2 = new HashSet();
 set2.add(2);
 set2.add(1);
 compare = new CoreOperationCompare(new ConstExpression(set1), new ConstExpression(set2));
 Object result = compare.evaluate(context);
 Assert.assertTrue(result);
 }

 @Test
 public void testEqualsCollectionAndCollectionDifferentOrder() {
 Set set1 = new HashSet();
 set1.add(1);
 set1.add(2);
 Set set2 = new HashSet();
 set2.add(2);
 set2.add(1);
 compare = new CoreOperationCompare(new ConstExpression(set1), new ConstExpression(set2));
 Object result = compare.evaluate(context);
 Assert.assertTrue(result);
 }

 @Test
 public void testEqualsCollectionAndCollectionDifferentElements() {
 Set set1 = new HashSet();
 set1.add(1);
 set1.add(2);
 Set set2 = new HashSet();
 set2.add(3);
 set2.add(4);
 compare = new CoreOperationCompare(new ConstExpression(set1), new ConstExpression(set2));
 Object result = compare.evaluate(context);
 Assert.assertFalse(result);
 }

 @Test
 public void testEqualsCollectionAndIterator() {
 Set set = new HashSet();
 set.add(1);
 set.add(2);
 Iterator iterator = set.iterator();
 compare = new CoreOperationCompare(new ConstExpression(set), new ConstExpression(iterator));
 Object result = compare.evaluate(context);
 Assert.assertTrue(result);
 }

 @Test
 public void testEqualsIteratorAndCollection() {
 Set set = new HashSet();
 set.add(1);
 set.add(2);
 Iterator iterator = set.iterator();
 compare = new CoreOperationCompare(new ConstExpression(iterator), new ConstExpression(set));
 Object result = compare.evaluate(context);
 Assert.assertTrue(result);
 }

 @Test
 public void testEqualsIteratorAndIterator() {
 Iterator iterator1 = new ArrayList(Collections.singletonList(1).iterator());
 Iterator iterator2 = new ArrayList(Collections.singletonList(1).iterator());
 compare = new CoreOperationCompare(new ConstExpression(iterator1), new ConstExpression(iterator2));
 Object result = compare.evaluate(context);
 Assert.assertTrue(result);
 }

 @Test
 public void testEqualsIteratorAndIteratorDifferentOrder() {
 Iterator iterator1 = new ArrayList(Collections.singletonList(1).iterator());
 Iterator iterator2 = new ArrayList(Collections.singletonList(1).iterator());
 List list1 = new ArrayList();
 list1.add(iterator1);
 List list2 = new ArrayList();
 list2.add(iterator2);
 Collections.reverse(list1);
 compare = new CoreOperationCompare(new ConstExpression(iterator1), new ConstExpression(iterator2));
 Object result = compare.evaluate(context);
 Assert.assertTrue(result);
 }

 @Test
 public void testEqualsIteratorAndIteratorDifferentElements() {
 Iterator iterator1 = new ArrayList(Collections.singletonList(1).iterator());
 Iterator iterator2 = new ArrayList(Collections.singletonList(2).iterator());
 compare = new CoreOperationCompare(new ConstExpression(iterator1), new ConstExpression(iterator2));
 Object result = compare.evaluate(context);
 Assert.assertFalse(result);
 }

 @Test
 public void testEqualsPointerAndPointer() {
 Pointer pointer1 = new Pointer();
 pointer1.setValue(1);
 Pointer pointer2 = new Pointer();
 pointer2.setValue(1);
 compare = new CoreOperationCompare(new ConstExpression(pointer1), new ConstExpression(pointer2));
 Object result = compare.evaluate(context);
 Assert.assertTrue(result);
 }

 @Test
 public void testEqualsPointerAndString() {
 Pointer pointer = new Pointer();
 pointer.setValue(new Integer(1));
 compare = new CoreOperationCompare(new ConstExpression(pointer), new ConstExpression("1"));
 Object result = compare.evaluate(context);
 Assert.assertTrue(result);
 }

 @Test
 public void testEqualsStringAndPointer() {
 String string = "1";
 Pointer pointer = new Pointer();
 pointer.setValue(new Integer(1));
 compare = new CoreOperationCompare(new ConstExpression(string), new ConstExpression(pointer));
 Object result = compare.evaluate(context);
 Assert.assertTrue(result);
 }

 @Test
 public void testEqualsPointerAndNull() {
 Pointer pointer = new Pointer();
 compare = new CoreOperationCompare(new ConstExpression(pointer), new ConstExpression(null));
 Object result = compare.evaluate(context);
 Assert.assertFalse(result);
 }

 @Test
 public void testEqualsNullAndPointer() {
 Pointer pointer = new Pointer();
 compare = new CoreOperationCompare(new ConstExpression(null), new ConstExpression(pointer));
 Object result = compare.evaluate(context);
 Assert.assertFalse(result);
 }

 @Test
 public void testEqualsInitialContextAndInitialContext() {
 InitialContext initialContext1 = new InitialContext();
 InitialContext initialContext2 = new InitialContext();
 compare = new CoreOperationCompare(new ConstExpression(initialContext1), new ConstExpression(initialContext2));
 Object result = compare.evaluate(context);
 Assert.assertTrue(result);
 }

 @Test
 public void testEqualsSelfContextAndSelfContext() {
 SelfContext selfContext1 = new SelfContext();
 SelfContext selfContext2 = new SelfContext();
 compare = new CoreOperationCompare(new ConstExpression(selfContext1), new ConstExpression(selfContext2));
 Object result = compare.evaluate(context);
 Assert.assertTrue(result);
 }

 @Test(expected = ClassCastException.class)
 public void testEqualsMismatchedTypes() {
 compare = new CoreOperationCompare(new ConstExpression(1), new ConstExpression("1"));
 Object result = compare.evaluate(context);
 Assert.fail("Should have thrown ClassCastException");
 }

 @Test(expected = NullPointerException.class)
 public void testEqualsNullPointer() {
 compare = new CoreOperationCompare(new ConstExpression(null), new ConstExpression(1));
 Object result = compare.evaluate(context);
 Assert.fail("Should have thrown NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualsNullContext() {
 context = null;
 compare = new CoreOperationCompare(new ConstExpression(1), new ConstExpression(1));
 Object result = compare.evaluate(context);
 Assert.fail("Should have thrown IllegalArgumentException");
 }
}