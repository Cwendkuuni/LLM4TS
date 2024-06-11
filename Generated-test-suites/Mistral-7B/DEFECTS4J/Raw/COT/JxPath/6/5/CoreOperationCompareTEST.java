package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class CoreOperationCompareTest {

 private CoreOperationCompare compare;
 private EvalContext context;
 private Expression arg1;
 private Expression arg2;

 @Before
 public void setUp() {
 arg1 = new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return new Integer(1);
 }
 };
 arg2 = new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return new Integer(1);
 }
 };
 compare = new CoreOperationCompare(arg1, arg2);
 context = new EvalContext();
 }

 @Test
 public void testEqual() {
 assertTrue(compare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_Boolean() {
 Expression bool1 = new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return new Boolean(true);
 }
 };
 Expression bool2 = new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return new Boolean(true);
 }
 };
 compare = new CoreOperationCompare(bool1, bool2);
 assertTrue(compare.equal(context, bool1, bool2));
 }

 @Test
 public void testEqual_Number() {
 Expression num1 = new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return new Double(1.0);
 }
 };
 Expression num2 = new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return new Double(1.0);
 }
 };
 compare = new CoreOperationCompare(num1, num2);
 assertTrue(compare.equal(context, num1, num2));
 }

 @Test
 public void testEqual_String() {
 Expression str1 = new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return "test";
 }
 };
 Expression str2 = new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return "test";
 }
 };
 compare = new CoreOperationCompare(str1, str2);
 assertTrue(compare.equal(context, str1, str2));
 }

 @Test
 public void testEqual_Null() {
 Expression str1 = new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return null;
 }
 };
 Expression str2 = new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return null;
 }
 };
 compare = new CoreOperationCompare(str1, str2);
 assertTrue(compare.equal(context, str1, str2));
 }

 @Test
 public void testEqual_DifferentTypes() {
 Expression num = new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return new Integer(1);
 }
 };
 Expression str = new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return "test";
 }
 };
 compare = new CoreOperationCompare(num, str);
 assertFalse(compare.equal(context, num, str));
 }

 @Test
 public void testEqual_Collections() {
 Collection coll1 = new ArrayList(Arrays.asList(1, 2, 3));
 Collection coll2 = new ArrayList(Arrays.asList(1, 2, 3));
 compare = new CoreOperationCompare(new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return coll1;
 }
 }, new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return coll2;
 }
 });
 assertTrue(compare.equal(context, coll1, coll2));
 }

 @Test
 public void testEqual_CollectionsDifferentSize() {
 Collection coll1 = new ArrayList(Arrays.asList(1, 2, 3));
 Collection coll2 = new ArrayList(Arrays.asList(1, 2, 3, 4));
 compare = new CoreOperationCompare(new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return coll1;
 }
 }, new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return coll2;
 }
 });
 assertFalse(compare.equal(context, coll1, coll2));
 }

 @Test(expected = AssertionError.class)
 public void testEqual_Exception1() {
 Expression ex = new Expression() {
 @Override
 public Object compute(EvalContext context) {
 throw new RuntimeException();
 }
 };
 compare = new CoreOperationCompare(arg1, ex);
 }

 @Test(expected = AssertionError.class)
 public void testEqual_Exception2() {
 compare = new CoreOperationCompare(arg1, null);
 }

 @Test
 public void testContains_EmptyCollection() {
 Collection empty = new ArrayList();
 compare = new CoreOperationCompare(new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return new ArrayList(Arrays.asList(1, 2, 3));
 }
 }, new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return empty;
 }
 });
 assertFalse(compare.contains(context, arg1, empty));
 }

 @Test
 public void testContains() {
 Collection coll = new ArrayList(Arrays.asList(1, 2, 3));
 compare = new CoreOperationCompare(new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return coll;
 }
 }, new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return new Integer(2);
 }
 });
 assertTrue(compare.contains(context, coll, arg2));
 }

 @Test
 public void testFindMatch() {
 Collection coll1 = new ArrayList(Arrays.asList(1, 2, 3));
 Collection coll2 = new ArrayList(Arrays.asList(1, 2, 3, 4, 3));
 compare = new CoreOperationCompare(new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return coll1;
 }
 }, new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return coll2;
 }
 });
 assertTrue(compare.findMatch(coll1.iterator(), coll2.iterator()));
 }

 @Test
 public void testFindMatch_EmptyCollection() {
 Collection coll1 = new ArrayList();
 Collection coll2 = new ArrayList(Arrays.asList(1, 2, 3, 4, 3));
 compare = new CoreOperationCompare(new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return coll1;
 }
 }, new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return coll2;
 }
 });
 assertFalse(compare.findMatch(coll1.iterator(), coll2.iterator()));
 }

 @Test
 public void testFindMatch_CollectionsDifferentSize() {
 Collection coll1 = new ArrayList(Arrays.asList(1, 2, 3));
 Collection coll2 = new ArrayList(Arrays.asList(1, 2, 3, 4, 3));
 compare = new CoreOperationCompare(new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return coll1;
 }
 }, new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return coll2;
 }
 });
 assertFalse(compare.findMatch(coll1.iterator(), coll2.iterator()));
 }

}