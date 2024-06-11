import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationCompareTest {

    private CoreOperationCompare operationCompare;
    private EvalContext contextMock;
    private Expression leftMock;
    private Expression rightMock;

    @Before
    public void setUp() {
        contextMock = mock(EvalContext.class);
        leftMock = mock(Expression.class);
        rightMock = mock(Expression.class);
        operationCompare = new CoreOperationCompare(leftMock, rightMock);
    }

    @Test
    public void testEqual() {
        Object l = "test";
        Object r = "test";
        when(leftMock.compute(contextMock)).thenReturn(l);
        when(rightMock.compute(contextMock)).thenReturn(r);
        boolean result = operationCompare.equal(contextMock, leftMock, rightMock);
        assertEquals(true, result);
    }

    @Test
    public void testContains() {
        Iterator itMock = mock(Iterator.class);
        Object value = "test";
        when(itMock.hasNext()).thenReturn(true, false);
        when(itMock.next()).thenReturn(value);
        boolean result = operationCompare.contains(itMock, value);
        assertEquals(true, result);
    }

    @Test
    public void testFindMatch() {
        Iterator litMock = mock(Iterator.class);
        Iterator ritMock = mock(Iterator.class);
        Object value = "test";
        HashSet leftMock = mock(HashSet.class);
        when(litMock.hasNext()).thenReturn(true, false);
        when(litMock.next()).thenReturn(value);
        when(ritMock.hasNext()).thenReturn(true, false);
        when(ritMock.next()).thenReturn(value);
        when(leftMock.iterator()).thenReturn(litMock);
        boolean result = operationCompare.findMatch(litMock, ritMock);
        assertEquals(true, result);
    }

    @Test
    public void testEqualPointers() {
        Pointer l = mock(Pointer.class);
        Pointer r = mock(Pointer.class);
        when(l.equals(r)).thenReturn(true);
        boolean result = operationCompare.equal(l, r);
        assertEquals(true, result);
    }

    @Test
    public void testEqualBoolean() {
        Object l = true;
        Object r = false;
        boolean result = operationCompare.equal(l, r);
        assertEquals(false, result);
    }

    @Test
    public void testEqualNumber() {
        Object l = 1;
        Object r = 1;
        boolean result = operationCompare.equal(l, r);
        assertEquals(true, result);
    }

    @Test
    public void testEqualString() {
        Object l = "test";
        Object r = "test";
        boolean result = operationCompare.equal(l, r);
        assertEquals(true, result);
    }

    @Test
    public void testEqualNull() {
        Object l = null;
        Object r = null;
        boolean result = operationCompare.equal(l, r);
        assertEquals(true, result);
    }

    @Test
    public void testEqualDifferentTypes() {
        Object l = "test";
        Object r = 1;
        boolean result = operationCompare.equal(l, r);
        assertEquals(false, result);
    }
}