package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.stubbing.answer.Answer;
import org.mockito.internal.stubbing.answer.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerForPrimitiveTypes() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        Answer answer = emptyValues;

        Class<?> intType = int.class;
        InvocationOnMock invocation = MockitoTestUtils.createMockInvocation(intType);
        Object result = answer.answer(invocation);

        assertEquals(0, result);

        byte byteType = byte.class;
        result = answer.answer(MockitoTestUtils.createMockInvocation(byteType));
        assertEquals((byte) 0, result);

        short shortType = short.class;
        result = answer.answer(MockitoTestUtils.createMockInvocation(shortType));
        assertEquals((short) 0, result);

        char charType = char.class;
        result = answer.answer(MockitoTestUtils.createMockInvocation(charType));
        assertEquals('\u0000', result);

        boolean booleanType = boolean.class;
        result = answer.answer(MockitoTestUtils.createMockInvocation(booleanType));
        assertTrue(result);
    }

    @Test
    public void testAnswerForPrimitiveWrapperTypes() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        Answer answer = emptyValues;

        Class<?> intWrapperType = Integer.class;
        InvocationOnMock invocation = MockitoTestUtils.createMockInvocation(intWrapperType);
        Object result = answer.answer(invocation);

        assertEquals(0, result);

        Class<?> booleanWrapperType = Boolean.class;
        result = answer.answer(MockitoTestUtils.createMockInvocation(booleanWrapperType));
        assertTrue(result);
    }

    @Test
    public void testAnswerForCollectionTypes() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        Answer answer = emptyValues;

        Collection<String> collection = answer.answer(MockitoTestUtils.createMockInvocation(Collection.class));
        assertTrue(collection.isEmpty());

        List<String> list = (List) answer.answer(MockitoTestUtils.createMockInvocation(List.class));
        assertTrue(list.isEmpty());

        Set<String> set = (Set) answer.answer(MockitoTestUtils.createMockInvocation(Set.class));
        assertTrue(set.isEmpty());

        SortedSet<String> sortedSet = (SortedSet) answer.answer(MockitoTestUtils.createMockInvocation(SortedSet.class));
        assertTrue(sortedSet.isEmpty());

        LinkedHashSet<String> linkedHashSet = (LinkedHashSet) answer.answer(MockitoTestUtils.createMockInvocation(LinkedHashSet.class));
        assertTrue(linkedHashSet.isEmpty());

        HashSet<String> hashSet = (HashSet) answer.answer(MockitoTestUtils.createMockInvocation(HashSet.class));
        assertTrue(hashSet.isEmpty());

        TreeSet<String> treeSet = (TreeSet) answer.answer(MockitoTestUtils.createMockInvocation(TreeSet.class));
        assertTrue(treeSet.isEmpty());
    }

    @Test
    public void testAnswerForMapTypes() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        Answer answer = emptyValues;

        Map<String, String> map = answer.answer(MockitoTestUtils.createMockInvocation(Map.class));
        assertTrue(map.isEmpty());

        HashMap<String, String> hashMap = (HashMap) answer.answer(MockitoTestUtils.createMockInvocation(HashMap.class));
        assertTrue(hashMap.isEmpty());

        SortedMap<String, String> sortedMap = (SortedMap) answer.answer(MockitoTestUtils.createMockInvocation(SortedMap.class));
        assertTrue(sortedMap.isEmpty());

        LinkedHashMap<String, String> linkedHashMap = (LinkedHashMap) answer.answer(MockitoTestUtils.createMockInvocation(LinkedHashMap.class));
        assertTrue(linkedHashMap.isEmpty());

        TreeMap<String, String> treeMap = (TreeMap) answer.answer(MockitoTestUtils.createMockInvocation(TreeMap.class));
        assertTrue(treeMap.isEmpty());
    }

    @Test
    public void testToString() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        Answer answer = emptyValues;

        Object mock = MockitoTestUtils.createMock(String.class);
        MockName name = MockitoTestUtils.createMockName("mockName");
        InvocationOnMock invocation = MockitoTestUtils.createMockInvocation(String.class, "toString");

        if (name.isDefault()) {
            String result = (String) answer.answer(invocation);
            assertEquals("Mock for " + MockitoTestUtils.getTypeToMock(mock).getSimpleName() + ", hashCode: " + mock.hashCode(), result);
        } else {
            String result = (String) answer.answer(invocation);
            assertEquals(name.toString(), result);
        }
    }

    @Test
    public void testCompareToZeroIfEqual() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        Answer answer = emptyValues;

        Object mock1 = MockitoTestUtils.createMock(Comparable.class);
        Object mock2 = MockitoTestUtils.createMock(Comparable.class);
        InvocationOnMock invocation = MockitoTestUtils.createMockInvocation(Comparable.class, "compareTo", mock2);

        int result = (Integer) answer.answer(invocation);
        assertTrue(mock1 == mock2 ? result == 0 : result != 0);
    }

    @Test
    public void testCompareToNonZeroIfNotEqual() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        Answer answer = emptyValues;

        Object mock1 = MockitoTestUtils.createMock(Comparable.class);
        Object mock2 = MockitoTestUtils.createMock(Comparable.class);
        InvocationOnMock invocation = MockitoTestUtils.createMockInvocation(Comparable.class, "compareTo", mock2);

        int result = (Integer) answer.answer(invocation);
        assertFalse(mock1 == mock2 ? result == 0 : result == 0);
    }

    private static InvocationOnMock createMockInvocation(Class<?> type, String methodName, Object... arguments) {
        return new InvocationOnMock(MockitoTestUtils.createMock(type), methodName, arguments);
    }

    private static InvocationOnMock createMockInvocation(Class<?> type) {
        return createMockInvocation(type, null);
    }

    private static Class<?> getTypeToMock(Object mock) {
        return mock.getClass();
    }

    private static MockName createMockName(String name) {
        return new MockName(name);
    }

    static class MockitoTestUtils {
        static InvocationOnMock createMockInvocation(Class<?> type, String methodName, Object... arguments) {
            Object mock = Mockito.mock(type);
            return new InvocationOnMock(mock, methodName, arguments);
        }

        static InvocationOnMock createMockInvocation(Class<?> type) {
            return createMockInvocation(type, null);
        }

        static Object getMockName(Object mock) {
            return Mockito.mockName(mock);
        }

        static Class<?> getTypeToMock(Object mock) {
            return mock.getClass();
        }
    }
}