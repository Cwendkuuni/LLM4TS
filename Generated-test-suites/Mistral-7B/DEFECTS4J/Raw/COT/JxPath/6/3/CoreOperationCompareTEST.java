package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.EvaluationContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationConstant;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNumber;
import org.apache.commons.jxpath.ri.compiler.CoreOperationString;
import org.apache.commons.jxpath.ri.compiler.StaticContext;
import org.apache.commons.jxpath.ri.iter.ArrayArrayIter;
import org.apache.commons.jxpath.ri.iter.ArrayIter;
import org.apache.commons.jxpath.ri.iter.SingletonIter;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {

    private CoreOperationCompare compare;
    private CoreOperationNumber leftNum;
    private CoreOperationNumber rightNum;
    private CoreOperationString leftStr;
    private CoreOperationString rightStr;
    private CoreOperationConstant leftConstant;
    private CoreOperationConstant rightConstant;
    private EvaluationContext context;

    @Before
    public void setUp() {
        compare = new CoreOperationCompare(leftNum = new CoreOperationNumber(1), rightNum = new CoreOperationNumber(1));
        context = new StaticContext(new JXPathContext(), new ArrayList<>());
    }

    @Test
    public void testEqual() {
        assertTrue(compare.compare(context));
    }

    @Test
    public void testNotEqual() {
        compare = new CoreOperationCompare(leftNum = new CoreOperationNumber(1), rightNum = new CoreOperationNumber(2));
        assertFalse(compare.compare(context));
    }

    @Test
    public void testEqualWithSelfContext() {
        leftNum = new CoreOperationNumber(1, new SelfContext());
        rightNum = new CoreOperationNumber(1, new SelfContext());
        assertTrue(compare.compare(context));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArguments() {
        compare = new CoreOperationCompare(null, rightNum);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullRightArgument() {
        compare = new CoreOperationCompare(leftNum, null);
    }

    @Test
    public void testEqualWithIterators() {
        List<Integer> left = Arrays.asList(1, 2, 3);
        List<Integer> right = Arrays.asList(3, 2, 1);
        leftNum = new CoreOperationNumber(0, new ArrayIter(left.iterator()));
        rightNum = new CoreOperationNumber(0, new ArrayIter(right.iterator()));
        assertTrue(compare.compare(context));
    }

    @Test
    public void testNotEqualWithIterators() {
        List<Integer> left = Arrays.asList(1, 2, 3);
        List<Integer> right = Arrays.asList(3, 2, 4);
        leftNum = new CoreOperationNumber(0, new ArrayIter(left.iterator()));
        rightNum = new CoreOperationNumber(0, new ArrayIter(right.iterator()));
        assertFalse(compare.compare(context));
    }

    @Test
    public void testEqualWithSingletonIterators() {
        leftConstant = new CoreOperationConstant(new SingletonIter(new Integer(1)));
        rightConstant = new CoreOperationConstant(new SingletonIter(new Integer(1)));
        compare = new CoreOperationCompare(leftConstant, rightConstant);
        assertTrue(compare.compare(context));
    }

    @Test
    public void testNotEqualWithSingletonIterators() {
        leftConstant = new CoreOperationConstant(new SingletonIter(new Integer(1)));
        rightConstant = new CoreOperationConstant(new SingletonIter(new Integer(2)));
        compare = new CoreOperationCompare(leftConstant, rightConstant);
        assertFalse(compare.compare(context));
    }

    @Test
    public void testEqualWithStrings() {
        leftStr = new CoreOperationString("hello");
        rightStr = new CoreOperationString("hello");
        assertTrue(compare.compare(context));
    }

    @Test
    public void testNotEqualWithStrings() {
        leftStr = new CoreOperationString("hello");
        rightStr = new CoreOperationString("world");
        assertFalse(compare.compare(context));
    }

    @Test
    public void testEqualWithBoolean() {
        leftNum = new CoreOperationNumber(1);
        rightNum = new CoreOperationNumber(true);
        leftNum.setType(CoreOperation.BOOLEAN);
        rightNum.setType(CoreOperation.BOOLEAN);
        assertTrue(compare.compare(context));
    }

    @Test
    public void testNotEqualWithBooleans() {
        leftNum = new CoreOperationNumber(1);
        rightNum = new CoreOperationNumber(true);
        leftNum.setType(CoreOperation.BOOLEAN);
        rightNum.setType(CoreOperation.BOOLEAN);
        compare = new CoreOperationCompare(leftNum, rightNum);
        assertFalse(compare.compare(context));
    }

    @Test
    public void testEqualWithNumbers() {
        leftNum = new CoreOperationNumber(1.0);
        rightNum = new CoreOperationNumber(1.0);
        assertTrue(compare.compare(context));
    }

    @Test
    public void testNotEqualWithNumbers() {
        leftNum = new CoreOperationNumber(1.0);
        rightNum = new CoreOperationNumber(2.0);
        assertFalse(compare.compare(context));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareWithNonComparableTypes() {
        leftNum = new CoreOperationNumber(1);
        rightStr = new CoreOperationString("world");
        compare = new CoreOperationCompare(leftNum, rightStr);
    }
}