package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompareTest;

/**
 * Test for CoreOperationCompare class.
 */
public class CoreOperationCompareTest extends CoreOperationTest {

 private CoreOperationCompare op;

 @SuppressWarnings("unchecked")
 @Test
 public void testCoreOperationCompare_objects() {
 op = new CoreOperationCompare(new ConstantExpression(new Integer(1)),
 new ConstantExpression(new Integer(1)));
 assertTrue(op.compute(new EvalContext()));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testCoreOperationCompare_differentObjects() {
 op = new CoreOperationCompare(new ConstantExpression(new Integer(1)),
 new ConstantExpression(new Integer(2)));
 assertFalse(op.compute(new EvalContext()));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testCoreOperationCompare_booleans() {
 op = new CoreOperationCompare(new ConstantExpression(Boolean.TRUE),
 new ConstantExpression(Boolean.TRUE));
 assertTrue(op.compute(new EvalContext()));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testCoreOperationCompare_differentBooleans() {
 op = new CoreOperationCompare(new ConstantExpression(Boolean.TRUE),
 new ConstantExpression(Boolean.FALSE));
 assertFalse(op.compute(new EvalContext()));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testCoreOperationCompare_doubles() {
 op = new CoreOperationCompare(new ConstantExpression(Double.valueOf(1.0)),
 new ConstantExpression(Double.valueOf(1.0)));
 assertTrue(op.compute(new EvalContext()));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testCoreOperationCompare_differentDoubles() {
 op = new CoreOperationCompare(new ConstantExpression(Double.valueOf(1.0)),
 new ConstantExpression(Double.valueOf(2.0)));
 assertFalse(op.compute(new EvalContext()));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testCoreOperationCompare_strings() {
 op = new CoreOperationCompare(new ConstantExpression("abc"),
 new ConstantExpression("abc"));
 assertTrue(op.compute(new EvalContext()));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testCoreOperationCompare_differentStrings() {
 op = new CoreOperationCompare(new ConstantExpression("abc"),
 new ConstantExpression("def"));
 assertFalse(op.compute(new EvalContext()));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testCoreOperationCompare_pointers() {
 Pointer l = new Pointer(new SelfContext(), new Integer(1));
 Pointer r = new Pointer(new SelfContext(), new Integer(1));
 op = new CoreOperationCompare(l, r);
 assertTrue(op.compute(new EvalContext()));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testCoreOperationCompare_differentPointers() {
 Pointer l = new Pointer(new SelfContext(), new Integer(1));
 Pointer r = new Pointer(new SelfContext(), new Integer(2));
 op = new CoreOperationCompare(l, r);
 assertFalse(op.compute(new EvalContext()));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testCoreOperationCompare_iterators() {
 List<Integer> list1 = new ArrayList();
 list1.add(1);
 list1.add(2);
 List<Integer> list2 = new ArrayList();
 list2.add(1);
 list2.add(2);
 op = new CoreOperationCompare(new IteratorExpression(list1.iterator()),
 new IteratorExpression(list2.iterator()));
 assertTrue(op.compute(new EvalContext()));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testCoreOperationCompare_differentIterators() {
 List<Integer> list1 = new ArrayList();
 list1.add(1);
 List<Integer> list2 = new ArrayList();
 list2.add(2);
 op = new CoreOperationCompare(new IteratorExpression(list1.iterator()),
 new IteratorExpression(list2.iterator()));
 assertFalse(op.compute(new EvalContext()));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testCoreOperationCompare_iterators_equalValues() {
 List<Integer> list1 = new ArrayList();
 list1.add(1);
 list1.add(2);
 List<Integer> list2 = new ArrayList();
 list2.add(2);
 list2.add(1);
 op = new CoreOperationCompare(new IteratorExpression(list1.iterator()),
 new IteratorExpression(list2.iterator()));
 assertTrue(op.compute(new EvalContext()));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testCoreOperationCompare_iterators_noMatch() {
 List<Integer> list1 = new ArrayList();
 list1.add(1);
 List<Integer> list2 = new ArrayList();
 list2.add(2);
 op = new CoreOperationCompare(new IteratorExpression(list1.iterator()),
 new IteratorExpression(list2.iterator()));
 assertFalse(op.compute(new EvalContext()));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testCoreOperationCompare_iterators_oneEmpty() {
 List<Integer> list1 = new ArrayList();
 op = new CoreOperationCompare(new IteratorExpression(Collections.emptyIterator()),
 new IteratorExpression(new ArrayList<Integer>().iterator()));
 assertFalse(op.compute(new EvalContext()));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testCoreOperationCompare_iterators_otherEmpty() {
 List<Integer> list1 = new ArrayList();
 op = new CoreOperationCompare(new IteratorExpression(new ArrayList<Integer>().iterator()),
 new IteratorExpression(Collections.emptyIterator()));
 assertFalse(op.compute(new EvalContext()));
 }

 @Test
 public void testCoreOperationCompare_nullValue() {
 op = new CoreOperationCompare(new ConstantExpression(null),
 new ConstantExpression(new Integer(1)));
 assertFalse(op.compute(new EvalContext()));
 }

 @Test
 public void testCoreOperationCompare_nullValue2() {
 op = new CoreOperationCompare(new ConstantExpression(new Integer(1)),
 new ConstantExpression(null));
 assertFalse(op.compute(new EvalContext()));
 }

 @Test
 public void testCoreOperationCompare_nullValue3() {
 op = new CoreOperationCompare(new ConstantExpression(null),
 new ConstantExpression(null));
 assertTrue(op.compute(new EvalContext()));
 }

}