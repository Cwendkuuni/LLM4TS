import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.stubbing.Answer;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    private ObjectMethodsGuru methodsGuru = new ObjectMethodsGuru();
    private MockUtil mockUtil = new MockUtil();

    @Test
    public void testAnswerToStringMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(methodsGuru.isToString(invocation.getMethod())).thenReturn(true);

        Object mock = mock(Object.class);
        MockName name = mockUtil.getMockName(mock);
        when(name.isDefault()).thenReturn(true);
        when(mockUtil.getMockSettings(mock).getTypeToMock()).thenReturn(Object.class);

        String expected = "Mock for Object, hashCode: " + mock.hashCode();
        assertEquals(expected, returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testAnswerCompareToMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(methodsGuru.isCompareToMethod(invocation.getMethod())).thenReturn(true);

        assertEquals(1, returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testReturnValueForPrimitiveType() {
        assertEquals(0, returnsEmptyValues.returnValueFor(int.class));
    }

    @Test
    public void testReturnValueForCollectionType() {
        Collection<Object> expected = new LinkedList<>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(Collection.class));
    }

    @Test
    public void testReturnValueForSetType() {
        HashSet<Object> expected = new HashSet<>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(Set.class));
    }

    @Test
    public void testReturnValueForListType() {
        LinkedList<Object> expected = new LinkedList<>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(List.class));
    }

    @Test
    public void testReturnValueForMapType() {
        HashMap<Object, Object> expected = new HashMap<>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(Map.class));
    }

    @Test
    public void testReturnValueForSortedMapType() {
        TreeMap<Object, Object> expected = new TreeMap<>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(SortedMap.class));
    }

    @Test
    public void testReturnValueForUnknownType() {
        assertNull(returnsEmptyValues.returnValueFor(Object.class));
    }
}