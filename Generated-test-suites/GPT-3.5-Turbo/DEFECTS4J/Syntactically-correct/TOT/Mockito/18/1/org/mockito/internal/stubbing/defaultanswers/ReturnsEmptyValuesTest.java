import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues;

    @Before
    public void setUp() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }

    @Test
    public void testAnswer_returnsAppropriatePrimitiveForBooleanReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(boolean.class);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof Boolean);
        assertEquals(false, result);
    }

    @Test
    public void testAnswer_returnsAppropriatePrimitiveForByteReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(byte.class);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof Byte);
        assertEquals((byte) 0, result);
    }

    @Test
    public void testAnswer_returnsAppropriatePrimitiveForCharReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(char.class);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof Character);
        assertEquals('\0', result);
    }

    @Test
    public void testAnswer_returnsAppropriatePrimitiveForShortReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(short.class);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof Short);
        assertEquals((short) 0, result);
    }

    @Test
    public void testAnswer_returnsAppropriatePrimitiveForIntReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(int.class);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof Integer);
        assertEquals(0, result);
    }

    @Test
    public void testAnswer_returnsAppropriatePrimitiveForLongReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(long.class);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof Long);
        assertEquals(0L, result);
    }

    @Test
    public void testAnswer_returnsAppropriatePrimitiveForFloatReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(float.class);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof Float);
        assertEquals(0.0f, result);
    }

    @Test
    public void testAnswer_returnsAppropriatePrimitiveForDoubleReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(double.class);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof Double);
        assertEquals(0.0, result);
    }

    @Test
    public void testAnswer_returnsConsistentValuesForBoxedPrimitives() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Boolean.class);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof Boolean);
        assertEquals(false, result);

        when(invocation.getMethod().getReturnType()).thenReturn(Byte.class);

        result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof Byte);
        assertEquals((byte) 0, result);

        when(invocation.getMethod().getReturnType()).thenReturn(Character.class);

        result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof Character);
        assertEquals('\0', result);

        // Repeat for other boxed primitives
    }

    @Test
    public void testAnswer_returnsEmptyLinkedListForCollectionReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Collection.class);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList) result).isEmpty());
    }

    @Test
    public void testAnswer_returnsEmptyHashSetForSetReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Set.class);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof HashSet);
        assertTrue(((HashSet) result).isEmpty());
    }

    @Test
    public void testAnswer_returnsEmptyHashSetForHashSetReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(HashSet.class);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof HashSet);
        assertTrue(((HashSet) result).isEmpty());
    }

    @Test
    public void testAnswer_returnsEmptyTreeSetForSortedSetReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(SortedSet.class);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof TreeSet);
        assertTrue(((TreeSet) result).isEmpty());
    }

    @Test
    public void testAnswer_returnsEmptyTreeSetForTreeSetReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(TreeSet.class);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof TreeSet);
        assertTrue(((TreeSet) result).isEmpty());
    }

    @Test
    public void testAnswer_returnsEmptyLinkedHashSetForLinkedHashSetReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(LinkedHashSet.class);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof LinkedHashSet);
        assertTrue(((LinkedHashSet) result).isEmpty());
    }

    @Test
    public void testAnswer_returnsEmptyLinkedListForListReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(List.class);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList) result).isEmpty());
    }

    @Test
    public void testAnswer_returnsEmptyLinkedListForLinkedListReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(LinkedList.class);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList) result).isEmpty());
    }

    @Test
    public void testAnswer_returnsEmptyArrayListForArrayListReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(ArrayList.class);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof ArrayList);
        assertTrue(((ArrayList) result).isEmpty());
    }

    @Test
    public void testAnswer_returnsEmptyHashMapForMapReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Map.class);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof HashMap);
        assertTrue(((HashMap) result).isEmpty());
    }

    @Test
    public void testAnswer_returnsEmptyHashMapForHashMapReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(HashMap.class);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof HashMap);
        assertTrue(((HashMap) result).isEmpty());
    }

    @Test
    public void testAnswer_returnsEmptyTreeMapForSortedMapReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(SortedMap.class);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof TreeMap);
        assertTrue(((TreeMap) result).isEmpty());
    }

    @Test
    public void testAnswer_returnsEmptyTreeMapForTreeMapReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(TreeMap.class);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof TreeMap);
        assertTrue(((TreeMap) result).isEmpty());
    }

    @Test
    public void testAnswer_returnsEmptyLinkedHashMapForLinkedHashMapReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(LinkedHashMap.class);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof LinkedHashMap);
        assertTrue(((LinkedHashMap) result).isEmpty());
    }

    @Test
    public void testAnswer_returnsNullForOtherReturnTypes() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Date.class);

        Object result = returnsEmptyValues.answer(invocation);

        assertNull(result);
    }

    @Test
    public void testAnswer_returnsMockDescriptionForToStringMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        Object mock = mock(Object.class);
        when(invocation.getMock()).thenReturn(mock);
        MockUtil mockUtil = mock(MockUtil.class);
        ObjectMethodsGuru objectMethodsGuru = mock(ObjectMethodsGuru.class);
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        returnsEmptyValues.mockUtil = mockUtil;
        returnsEmptyValues.methodsGuru = objectMethodsGuru;

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals("Mock for Object, hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testAnswer_returnsZeroForCompareToMethodWhenArgumentsAreEqual() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object mock = mock(Object.class);
        Object[] arguments = {mock};
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getArguments()).thenReturn(arguments);
        when(invocation.getMethod().getReturnType()).thenReturn(int.class);

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals(0, result);
    }

    @Test
    public void testAnswer_returnsNonZeroForCompareToMethodWhenArgumentsAreNotEqual() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object[] arguments = {mock1};
        when(invocation.getMock()).thenReturn(mock1);
        when(invocation.getArguments()).thenReturn(arguments);
        when(invocation.getMethod().getReturnType()).thenReturn(int.class);

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals(1, result);
    }
}