import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        // create mock objects
        EvalContext context = mock(EvalContext.class);
        Expression left = mock(Expression.class);
        Expression right = mock(Expression.class);

        // define the expected behavior of the mock objects
        Object l = new Object();
        Object r = new Object();
        when(left.compute(context)).thenReturn(l);
        when(right.compute(context)).thenReturn(r);

        // create an instance of CoreOperationCompare
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);

        // execute the method to be tested
        boolean result = coreOperationCompare.equal(context, left, right);

        // verify the result
        assertFalse(result);
    }

    @Test
    public void testContains() {
        // create mock objects
        Iterator it = mock(Iterator.class);
        Object value = new Object();

        // define the expected behavior of the mock objects
        when(it.hasNext()).thenReturn(true, false);
        when(it.next()).thenReturn(value);

        // create an instance of CoreOperationCompare
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);

        // execute the method to be tested
        boolean result = coreOperationCompare.contains(it, value);

        // verify the result
        assertTrue(result);
    }

    @Test
    public void testFindMatch() {
        // create mock objects
        Iterator lit = mock(Iterator.class);
        Iterator rit = mock(Iterator.class);

        // define the expected behavior of the mock objects
        Object value = new Object();
        when(lit.hasNext()).thenReturn(true, false);
        when(lit.next()).thenReturn(value);
        when(rit.hasNext()).thenReturn(true, false);
        when(rit.next()).thenReturn(value);

        // create an instance of CoreOperationCompare
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);

        // execute the method to be tested
        boolean result = coreOperationCompare.findMatch(lit, rit);

        // verify the result
        assertTrue(result);
    }

    @Test
    public void testEqualWithPointers() {
        // create mock objects
        Pointer lPointer = mock(Pointer.class);
        Pointer rPointer = mock(Pointer.class);
        Pointer valuePointer = mock(Pointer.class);

        // define the expected behavior of the mock objects
        Object l = lPointer;
        Object r = rPointer;
        when(lPointer.getValue()).thenReturn(valuePointer);
        when(rPointer.getValue()).thenReturn(valuePointer);

        // create an instance of CoreOperationCompare
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);

        // execute the method to be tested
        boolean result = coreOperationCompare.equal(l, r);

        // verify the result
        assertTrue(result);
    }

    @Test
    public void testEqualWithBoolean() {
        // create mock objects
        Object l = Boolean.TRUE;
        Object r = Boolean.FALSE;

        // create an instance of CoreOperationCompare
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);

        // execute the method to be tested
        boolean result = coreOperationCompare.equal(l, r);

        // verify the result
        assertFalse(result);
    }

    @Test
    public void testEqualWithNumber() {
        // create mock objects
        Object l = 1;
        Object r = 1.0;

        // create an instance of CoreOperationCompare
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);

        // execute the method to be tested
        boolean result = coreOperationCompare.equal(l, r);

        // verify the result
        assertTrue(result);
    }

    @Test
    public void testEqualWithString() {
        // create mock objects
        Object l = "test";
        Object r = "test";

        // create an instance of CoreOperationCompare
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);

        // execute the method to be tested
        boolean result = coreOperationCompare.equal(l, r);

        // verify the result
        assertTrue(result);
    }

    @Test
    public void testEqualWithNull() {
        // create mock objects
        Object l = null;
        Object r = null;

        // create an instance of CoreOperationCompare
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);

        // execute the method to be tested
        boolean result = coreOperationCompare.equal(l, r);

        // verify the result
        assertTrue(result);
    }

}