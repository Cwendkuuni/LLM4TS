import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();

        // Test toString() method
        Object mock = mock(Object.class);
        mock.hashCode();
        String expectedToString = "Mock for Object, hashCode: " + mock.hashCode();
        assertEquals(expectedToString, values.answer(invocationOnMock(methodsGuru.isToString(mock.getClass().getMethod("toString")))));

        // Test compareTo() method
        assertEquals(1, values.answer(invocationOnMock(methodsGuru.isCompareToMethod(mock.getClass().getMethod("compareTo()")))));

        // Test primitive and wrapper classes
        assertEquals(false, values.answer(invocationOnMock(mock.getClass().getMethod("isBoolean"))));
        assertEquals((byte) 0, values.answer(invocationOnMock(mock.getClass().getMethod("getByte"))));
        assertEquals((short) 0, values.answer(invocationOnMock(mock.getClass().getMethod("getShort"))));
        assertEquals(0, values.answer(invocationOnMock(mock.getClass().getMethod("getInt"))));
        assertEquals(0L, values.answer(invocationOnMock(mock.getClass().getMethod("getLong"))));
        assertEquals(0.0f, values.answer(invocationOnMock(mock.getClass().getMethod("getFloat"))), 0);
        assertEquals(0.0, values.answer(invocationOnMock(mock.getClass().getMethod("getDouble"))), 0);
        assertEquals('\u0000', values.answer(invocationOnMock(mock.getClass().getMethod("getChar"))));
        assertNull(values.answer(invocationOnMock(mock.getClass().getMethod("getObject"))));

        // Test collections
        assertEquals(new LinkedList<>(), values.answer(invocationOnMock(mock.getClass().getMethod("getLinkedList"))));
        assertEquals(new HashSet<>(), values.answer(invocationOnMock(mock.getClass().getMethod("getHashSet"))));
        assertEquals(new TreeSet<>(), values.answer(invocationOnMock(mock.getClass().getMethod("getTreeSet"))));
        assertEquals(new LinkedHashSet<>(), values.answer(invocationOnMock(mock.getClass().getMethod("getLinkedHashSet"))));
        assertEquals(new ArrayList<>(), values.answer(invocationOnMock(mock.getClass().getMethod("getArrayList"))));
        assertEquals(new HashMap<>(), values.answer(invocationOnMock(mock.getClass().getMethod("getHashMap"))));
        assertEquals(new TreeMap<>(), values.answer(invocationOnMock(mock.getClass().getMethod("getTreeMap"))));
        assertEquals(new LinkedHashMap<>(), values.answer(invocationOnMock(mock.getClass().getMethod("getLinkedHashMap"))));
        
        // TODO: Test empty Iterable
    }

    private InvocationOnMock invocationOnMock(Method method) {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(method);
        return invocation;
    }

    private ObjectMethodsGuru methodsGuru = new ObjectMethodsGuru();
}