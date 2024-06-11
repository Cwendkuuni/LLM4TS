import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashSet;
import java.util.ArrayList;

public class CoreOperationCompareTest {

    private CoreOperationCompare coreOperationCompare;
    private Expression arg1;
    private Expression arg2;

    @Before
    public void setUp() {
        arg1 = new ExpressionMock();
        arg2 = new ExpressionMock();
        coreOperationCompare = new CoreOperationCompare(arg1, arg2);
    }

    @Test
    public void testGetPrecedence() {
        int precedence = coreOperationCompare.getPrecedence();
        assertEquals(2, precedence);
    }

    @Test
    public void testIsSymmetric() {
        boolean symmetric = coreOperationCompare.isSymmetric();
        assertTrue(symmetric);
    }

    @Test
    public void testEqual_BothEvalContext() {
        EvalContext evalContext1 = new EvalContext();
        EvalContext evalContext2 = new EvalContext();
        boolean equals = coreOperationCompare.equal(evalContext1, evalContext2, evalContext2);
        assertTrue(equals);
    }

    @Test
    public void testEqual_OneEvalContextAndOneSingleNodePointer() {
        EvalContext evalContext = new EvalContext();
        SelfContext selfContext = new SelfContext();
        Pointer singleNodePointer = new Pointer();
        selfContext.setSingleNodePointer(singleNodePointer);
        boolean equals = coreOperationCompare.equal(evalContext, selfContext, selfContext);
        assertTrue(equals);
    }

    @Test
    public void testEqual_BothSingleNodePointer() {
        SelfContext selfContext = new SelfContext();
        Pointer singleNodePointer1 = new Pointer();
        Pointer singleNodePointer2 = new Pointer();
        selfContext.setSingleNodePointer(singleNodePointer1);
        boolean equals = coreOperationCompare.equal(singleNodePointer1, singleNodePointer2);
        assertTrue(equals);
    }

    @Test
    public void testEqual_BothCollection() {
        Collection<Object> collection1 = new ArrayList<>();
        Collection<Object> collection2 = new ArrayList<>();
        boolean equals = coreOperationCompare.equal(collection1, collection2);
        assertTrue(equals);
    }

    @Test
    public void testEqual_BothIterator() {
        Iterator<Object> iterator1 = new IteratorMock();
        Iterator<Object> iterator2 = new IteratorMock();
        boolean equals = coreOperationCompare.equal(iterator1, iterator2);
        assertTrue(equals);
    }

    @Test
    public void testEqual_OneIteratorAndOneElement() {
        Iterator<Object> iterator = new IteratorMock();
        Object element = new Object();
        boolean equals = coreOperationCompare.equal(iterator, element);
        assertFalse(equals);
    }

    @Test
    public void testEqual_FindMatch() {
        Iterator<Object> lit = new IteratorMock();
        Iterator<Object> rit = new IteratorMock();
        boolean match = coreOperationCompare.findMatch(lit, rit);
        assertTrue(match);
    }

    @Test
    public void testEqual_FindMatch_NoMatch() {
        Iterator<Object> lit = new IteratorMock();
        Iterator<Object> rit = new IteratorMock(false);
        boolean match = coreOperationCompare.findMatch(lit, rit);
        assertFalse(match);
    }

    @Test
    public void testEqual_Contains() {
        Iterator<Object> it = new IteratorMock();
        Object value = new Object();
        boolean contains = coreOperationCompare.contains(it, value);
        assertTrue(contains);
    }

    @Test
    public void testEqual_Contains_NotFound() {
        Iterator<Object> it = new IteratorMock(false);
        Object value = new Object();
        boolean contains = coreOperationCompare.contains(it, value);
        assertFalse(contains);
    }

    @Test
    public void testEqual_PointerEquals() {
        Pointer pointer1 = new Pointer();
        Pointer pointer2 = pointer1;
        boolean pointerEquals = coreOperationCompare.equal(pointer1, pointer2);
        assertTrue(pointerEquals);
    }

    @Test
    public void testEqual_PointerNotEquals() {
        Pointer pointer1 = new Pointer();
        Pointer pointer2 = new Pointer();
        boolean pointerEquals = coreOperationCompare.equal(pointer1, pointer2);
        assertFalse(pointerEquals);
    }

    @Test
    public void testEqual_BooleanEquals() {
        Boolean bool1 = true;
        Boolean bool2 = true;
        boolean booleanEquals = coreOperationCompare.equal(bool1, bool2);
        assertTrue(booleanEquals);
    }

    @Test
    public void testEqual_BooleanNotEquals() {
        Boolean bool1 = true;
        Boolean bool2 = false;
        boolean booleanEquals = coreOperationCompare.equal(bool1, bool2);
        assertFalse(booleanEquals);
    }

    @Test
    public void testEqual_NumberEquals() {
        Number number1 = 10;
        Number number2 = 10;
        boolean numberEquals = coreOperationCompare.equal(number1, number2);
        assertTrue(numberEquals);
    }

    @Test
    public void testEqual_NumberNotEquals() {
        Number number1 = 10;
        Number number2 = 20;
        boolean numberEquals = coreOperationCompare.equal(number1, number2);
        assertFalse(numberEquals);
    }

    @Test
    public void testEqual_StringEquals() {
        String str1 = "hello";
        String str2 = "hello";
        boolean stringEquals = coreOperationCompare.equal(str1, str2);
        assertTrue(stringEquals);
    }

    @Test
    public void testEqual_StringNotEquals() {
        String str1 = "hello";
        String str2 = "world";
        boolean stringEquals = coreOperationCompare.equal(str1, str2);
        assertFalse(stringEquals);
    }

    @Test
    public void testEqual_NullNotEquals() {
        Object l = null;
        Object r = new Object();
        boolean nullEquals = coreOperationCompare.equal(l, r);
        assertFalse(nullEquals);
    }

    @Test
    public void testEqual_NullIsNull() {
        Object l = null;
        Object r = null;
        boolean nullEquals = coreOperationCompare.equal(l, r);
        assertTrue(nullEquals);
    }

    @Test
    public void testEqual_NullAndObjectNotEquals() {
        Object l = null;
        Object r = new Object();
        boolean nullEquals = coreOperationCompare.equal(l, r);
        assertFalse(nullEquals);
    }

}