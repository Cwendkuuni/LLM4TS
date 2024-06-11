package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationCompareTest {
    private CoreOperationCompare coreOperationCompare;
    private Expression left;
    private Expression right;

    @Before
    public void setup() {
        left = mock(Expression.class);
        right = mock(Expression.class);
        coreOperationCompare = new CoreOperationCompare(left, right) {

            @Override
            protected boolean equal(EvalContext context, Expression left, Expression right) {
                return false;
            }

        };
    }

    @Test
    public void testGetPrecedence() {
        assertEquals(2, coreOperationCompare.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        assertTrue(coreOperationCompare.isSymmetric());
    }

    @Test
    public void testEqual_LeftEvalNull_ReturnFalse() {
        assertFalse(coreOperationCompare.equal(null, left, right));
    }

    @Test
    public void testEqual_LeftEvalInitialContextEvalNull_ReturnFalse() {
        assertFalse(coreOperationCompare.equal(new EvalContext(null, null, null), left, right));
    }

    @Test
    public void testEqual_LeftEvalInitialContextEvalSingleNodePointer_ReturnFalse() {
        InitialContext initialContext = new InitialContext(mock(EvalContext.class));
        initialContext.setNode(mock(Object.class));
        EvalContext evalContext = new EvalContext(null, null, null);
        evalContext.setSingleNodePointer(initialContext);
        assertFalse(coreOperationCompare.equal(evalContext, left, right));
    }

    @Test
    public void testEqual_LeftEvalCollection_ReturnFalse() {
        Collection<Object> collection = new ArrayList<>();
        assertFalse(coreOperationCompare.equal(collection, left, right));
    }

    @Test
    public void testEqual_LeftEvalIteratorAndRightEvalIterator_ReturnFalse() {
        Iterator<Object> leftIterator = mock(Iterator.class);
        Iterator<Object> rightIterator = mock(Iterator.class);
        assertFalse(coreOperationCompare.equal(leftIterator, rightIterator));
    }

    @Test
    public void testEqual_LeftEvalIterator_ReturnFalse() {
        Iterator<Object> iterator = mock(Iterator.class);
        assertFalse(coreOperationCompare.equal(iterator, left, right));
    }

    @Test
    public void testEqual_RightEvalIterator_ReturnFalse() {
        Iterator<Object> iterator = mock(Iterator.class);
        assertFalse(coreOperationCompare.equal(left, iterator, right));
    }

    @Test
    public void testEqual_EqualEvalObjects_ReturnFalse() {
        Object l = new Object();
        Object r = new Object();
        assertFalse(coreOperationCompare.equal(l, r));
    }

    @Test
    public void testEqual_LeftEvalPointerAndRightEvalPointer_ReturnFalse() {
        Object l = mock(Pointer.class);
        Object r = mock(Pointer.class);
        assertFalse(coreOperationCompare.equal(l, r));
    }

    @Test
    public void testEqual_LeftEvalPointer_ReturnFalse() {
        Object l = mock(Pointer.class);
        assertFalse(coreOperationCompare.equal(l, right));
    }

    @Test
    public void testEqual_RightEvalPointer_ReturnFalse() {
        Object r = mock(Pointer.class);
        assertFalse(coreOperationCompare.equal(left, r));
    }

    @Test
    public void testEqual_EqualEvalBoolean_ReturnFalse() {
        Object l = true;
        Object r = false;
        assertFalse(coreOperationCompare.equal(l, r));
    }

    @Test
    public void testEqual_EqualEvalNumbers_ReturnFalse() {
        Object l = 1;
        Object r = 1D;
        assertFalse(coreOperationCompare.equal(l, r));
    }

    @Test
    public void testEqual_EqualEvalStrings_ReturnFalse() {
        Object l = "foo";
        Object r = "bar";
        assertFalse(coreOperationCompare.equal(l, r));
    }

    @Test
    public void testEqual_NotEqualEvalObjects_ReturnFalse() {
        Object l = new Object();
        Object r = null;
        assertFalse(coreOperationCompare.equal(l, r));
    }

    @Test
    public void testContains_NonEmptyIteratorAndEqualValue_ReturnTrue() {
        List<Object> list = new ArrayList<>();
        list.add("foo");
        Iterator<Object> iterator = list.iterator();
        Object value = "foo";
        assertTrue(coreOperationCompare.contains(iterator, value));
    }

    @Test
    public void testContains_EmptyIterator_ReturnFalse() {
        List<Object> list = new ArrayList<>();
        Iterator<Object> iterator = list.iterator();
        Object value = "foo";
        assertFalse(coreOperationCompare.contains(iterator, value));
    }

    @Test
    public void testFindMatch_NonEmptyIterators_ReturnTrue() {
        List<Object> leftList = new ArrayList<>();
        leftList.add("foo");
        List<Object> rightList = new ArrayList<>();
        rightList.add("foo");
        Iterator<Object> leftIterator = leftList.iterator();
        Iterator<Object> rightIterator = rightList.iterator();
        assertTrue(coreOperationCompare.findMatch(leftIterator, rightIterator));
    }

    @Test
    public void testFindMatch_EmptyIterator_ReturnFalse() {
        List<Object> leftList = new ArrayList<>();
        List<Object> rightList = new ArrayList<>();
        Iterator<Object> leftIterator = leftList.iterator();
        Iterator<Object> rightIterator = rightList.iterator();
        assertFalse(coreOperationCompare.findMatch(leftIterator, rightIterator));
    }

    @Test
    public void testEqualAndString_NullAndNotNull_ReturnFalse() {
        Object l = null;
        Object r = new Object();
        assertFalse(InfoSetUtil.stringValue(l).equals(InfoSetUtil.stringValue(r)));
    }

    @Test
    public void testEqualAndString_NotNullAndNull_ReturnFalse() {
        Object l = new Object();
        Object r = null;
        assertFalse(InfoSetUtil.stringValue(l).equals(InfoSetUtil.stringValue(r)));
    }

    @Test
    public void testEqualAndString_NotNullAndNotNull_ReturnFalse() {
        Object l = new Object();
        Object r = new Object();
        assertFalse(InfoSetUtil.stringValue(l).equals(InfoSetUtil.stringValue(r)));
    }

    @Test
    public void testEqualAndString_NullAndNull_ReturnTrue() {
        Object l = null;
        Object r = null;
        assertTrue(InfoSetUtil.stringValue(l).equals(InfoSetUtil.stringValue(r)));
    }

    @Test
    public void testEqual_NaN_ReturnTrue() {
        Object l = Double.NaN;
        Object r = Double.NaN;
        assertTrue(InfoSetUtil.doubleValue(l) == InfoSetUtil.doubleValue(r));
    }

    @Test
    public void testEqual_primitiveNumbers_ReturnTrue() {
        assertTrue(coreOperationCompare.equal(1, 1));
        assertTrue(coreOperationCompare.equal(1D, 1D));
        assertTrue(coreOperationCompare.equal(1F, 1F));
    }

    @Test
    public void testEqualBothTrue_ReturnTrue() {
        assertTrue(coreOperationCompare.equal(true, true));
    }

    @Test
    public void testEqualBothFalse_ReturnTrue() {
        assertTrue(coreOperationCompare.equal(false, false));
    }

    @Test
    public void testEqual_OtherCases_ReturnFalse() {
        assertFalse(coreOperationCompare.equal("foo", 1));
        assertFalse(coreOperationCompare.equal(1, "foo"));
        assertFalse(coreOperationCompare.equal(1, 1D));
        assertFalse(coreOperationCompare.equal(1, 1L));
        assertFalse(coreOperationCompare.equal(1, 1F));
        assertFalse(coreOperationCompare.equal(1, true));
    }
}