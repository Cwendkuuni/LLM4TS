import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues;

    @Before
    public void setup() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }

    @Test
    public void testReturnValueFor_PrimitiveType() {
        assertEquals(0, returnsEmptyValues.returnValueFor(int.class));
        assertEquals(0L, returnsEmptyValues.returnValueFor(long.class));
        assertEquals(0.0f, returnsEmptyValues.returnValueFor(float.class), 0.01);
        assertEquals(0.0, returnsEmptyValues.returnValueFor(double.class), 0.01);
        assertEquals((short) 0, returnsEmptyValues.returnValueFor(short.class));
        assertEquals((byte) 0, returnsEmptyValues.returnValueFor(byte.class));
        assertEquals('\u0000', returnsEmptyValues.returnValueFor(char.class));
        assertEquals(false, returnsEmptyValues.returnValueFor(boolean.class));
        assertEquals(0, (Integer) returnsEmptyValues.returnValueFor(Integer.class));
        assertEquals(0L, (Long) returnsEmptyValues.returnValueFor(Long.class));
        assertEquals(0.0f, (Float) returnsEmptyValues.returnValueFor(Float.class), 0.01);
        assertEquals(0.0, (Double) returnsEmptyValues.returnValueFor(Double.class), 0.01);
        assertEquals((short) 0, (Short) returnsEmptyValues.returnValueFor(Short.class));
        assertEquals((byte) 0, (Byte) returnsEmptyValues.returnValueFor(Byte.class));
        assertEquals('\u0000', (Character) returnsEmptyValues.returnValueFor(Character.class));
        assertEquals(false, (Boolean) returnsEmptyValues.returnValueFor(Boolean.class));
    }

    @Test
    public void testReturnValueFor_CollectionType() {
        assertEquals(new LinkedList<Object>(), returnsEmptyValues.returnValueFor(Collection.class));
        assertEquals(new HashSet<Object>(), returnsEmptyValues.returnValueFor(Set.class));
        assertEquals(new HashSet<Object>(), returnsEmptyValues.returnValueFor(HashSet.class));
        assertEquals(new TreeSet<Object>(), returnsEmptyValues.returnValueFor(SortedSet.class));
        assertEquals(new TreeSet<Object>(), returnsEmptyValues.returnValueFor(TreeSet.class));
        assertEquals(new LinkedHashSet<Object>(), returnsEmptyValues.returnValueFor(LinkedHashSet.class));
        assertEquals(new LinkedList<Object>(), returnsEmptyValues.returnValueFor(List.class));
        assertEquals(new LinkedList<Object>(), returnsEmptyValues.returnValueFor(LinkedList.class));
        assertEquals(new ArrayList<Object>(), returnsEmptyValues.returnValueFor(ArrayList.class));
    }

    @Test
    public void testReturnValueFor_MapType() {
        assertEquals(new HashMap<Object, Object>(), returnsEmptyValues.returnValueFor(Map.class));
        assertEquals(new HashMap<Object, Object>(), returnsEmptyValues.returnValueFor(HashMap.class));
        assertEquals(new TreeMap<Object, Object>(), returnsEmptyValues.returnValueFor(SortedMap.class));
        assertEquals(new TreeMap<Object, Object>(), returnsEmptyValues.returnValueFor(TreeMap.class));
        assertEquals(new LinkedHashMap<Object, Object>(), returnsEmptyValues.returnValueFor(LinkedHashMap.class));
    }

    @Test
    public void testAnswer_ToStringMethod_DefaultName() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(invocation.getMock()).thenReturn(mock(Object.class));

        String result = (String) returnsEmptyValues.answer(invocation);

        assertEquals("Mock for Object, hashCode: " + invocation.getMock().hashCode(), result);
    }

    @Test
    public void testAnswer_ToStringMethod_CustomName() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMock()).thenAnswer(invocationOnMock -> "CustomMockName");

        String result = (String) returnsEmptyValues.answer(invocation);

        assertEquals("CustomMockName", result);
    }

    @Test
    public void testAnswer_CompareToMethod_SameReference() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Comparable.class.getDeclaredMethods()[0]);
        when(invocation.getMock()).thenReturn(mock(Comparable.class));
        when(invocation.getArguments()).thenReturn(new Object[]{invocation.getMock()});

        int result = (int) returnsEmptyValues.answer(invocation);

        assertEquals(0, result);
    }

    @Test
    public void testAnswer_CompareToMethod_DifferentReference() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Comparable.class.getDeclaredMethods()[0]);
        when(invocation.getMock()).thenReturn(mock(Comparable.class));
        when(invocation.getArguments()).thenReturn(new Object[]{new Object()});

        int result = (int) returnsEmptyValues.answer(invocation);

        assertEquals(1, result);
    }

    @Test
    public void testAnswer_OtherMethod_ReturnsNull() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[1]);

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals(null, result);
    }

    @Test
    public void testAnswer_AllMethods() {
        InvocationOnMock toStringInvocation = mock(InvocationOnMock.class);
        when(toStringInvocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(toStringInvocation.getMock()).thenReturn(mock(Object.class));
        when(toStringInvocation.getMock()).thenAnswer(invocationOnMock -> "CustomMockName");

        InvocationOnMock compareToSameRefInvocation = mock(InvocationOnMock.class);
        when(compareToSameRefInvocation.getMethod()).thenReturn(Comparable.class.getDeclaredMethods()[0]);
        when(compareToSameRefInvocation.getMock()).thenReturn(mock(Comparable.class));
        when(compareToSameRefInvocation.getArguments()).thenReturn(new Object[]{compareToSameRefInvocation.getMock()});

        InvocationOnMock compareToDiffRefInvocation = mock(InvocationOnMock.class);
        when(compareToDiffRefInvocation.getMethod()).thenReturn(Comparable.class.getDeclaredMethods()[0]);
        when(compareToDiffRefInvocation.getMock()).thenReturn(mock(Comparable.class));
        when(compareToDiffRefInvocation.getArguments()).thenReturn(new Object[]{new Object()});

        InvocationOnMock otherMethodInvocation = mock(InvocationOnMock.class);
        when(otherMethodInvocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[1]);

        assertEquals("Mock for Object, hashCode: " + toStringInvocation.getMock().hashCode(),
                (String) returnsEmptyValues.answer(toStringInvocation));

        assertEquals("CustomMockName", (String) returnsEmptyValues.answer(toStringInvocation));

        assertEquals(0, (int) returnsEmptyValues.answer(compareToSameRefInvocation));

        assertEquals(1, (int) returnsEmptyValues.answer(compareToDiffRefInvocation));

        assertEquals(null, returnsEmptyValues.answer(otherMethodInvocation));
    }
}