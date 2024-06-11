package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.Primitives.isPrimitiveOrWrapper;
import static org.mockito.internal.util.Primitives.wrap;

import java.util.ArrayList;
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
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerForNonNullInvokeWithStringReturnType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        String returnValue = (String) emptyValues.answer(createInvokeWithStringReturnType());

        assertEquals("", returnValue);
    }

    @Test
    public void testAnswerForNonNullInvokeWithIntegerReturnType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        int returnValue = (int) emptyValues.answer(createInvokeWithIntegerReturnType());

        assertEquals(0, returnValue);
    }

    @Test
    public void testAnswerForNonNullInvokeWithIntegerWrapperReturnType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        Integer returnValue = (Integer) emptyValues.answer(createInvokeWithIntegerWrapperReturnType());

        assertEquals(0, returnValue.intValue());
    }

    @Test
    public void testAnswerForNonNullInvokeWithBooleanReturnType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        boolean returnValue = (boolean) emptyValues.answer(createInvokeWithBooleanReturnType());

        assertFalse(returnValue);
    }

    @Test
    public void testAnswerForNonNullInvokeWithBooleanWrapperReturnType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        Boolean returnValue = (Boolean) emptyValues.answer(createInvokeWithBooleanWrapperReturnType());

        assertFalse(returnValue.booleanValue());
    }

    @Test
    public void testAnswerForNonNullInvokeWithCharacterReturnType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        char returnValue = (char) emptyValues.answer(createInvokeWithCharacterReturnType());

        assertEquals('\u0000', returnValue);
    }

    @Test
    public void testAnswerForNonNullInvokeWithCharacterWrapperReturnType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        Character returnValue = (Character) emptyValues.answer(createInvokeWithCharacterWrapperReturnType());

        assertEquals('\u0000', returnValue.charValue());
    }

    @Test
    public void testAnswerForNonNullInvokeWithCollectionReturnType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        Collection<Object> returnValue = (Collection<Object>) emptyValues.answer(createInvokeWithCollectionReturnType());

        assertNotNull(returnValue);
        assertEquals(0, returnValue.size());
    }

    @Test
    public void testAnswerForNonNullInvokeWithSetReturnType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        Set<Object> returnValue = (Set<Object>) emptyValues.answer(createInvokeWithSetReturnType());

        assertNotNull(returnValue);
        assertEquals(0, returnValue.size());
    }

    @Test
    public void testAnswerForNonNullInvokeWithHashSetReturnType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        Set<Object> returnValue = (Set<Object>) emptyValues.answer(createInvokeWithHashSetReturnType());

        assertNotNull(returnValue);
        assertEquals(0, returnValue.size());
    }

    @Test
    public void testAnswerForNonNullInvokeWithSortedSetReturnType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        SortedSet<Object> returnValue = (SortedSet<Object>) emptyValues.answer(createInvokeWithSortedSetReturnType());

        assertNotNull(returnValue);
        assertEquals(0, returnValue.size());
    }

    @Test
    public void testAnswerForNonNullInvokeWithTreeSetReturnType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        SortedSet<Object> returnValue = (SortedSet<Object>) emptyValues.answer(createInvokeWithTreeSetReturnType());

        assertNotNull(returnValue);
        assertEquals(0, returnValue.size());
    }

    @Test
    public void testAnswerForNonNullInvokeWithLinkedHashSetReturnType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        Set<Object> returnValue = (Set<Object>) emptyValues.answer(createInvokeWithLinkedHashSetReturnType());

        assertNotNull(returnValue);
        assertEquals(0, returnValue.size());
    }

    @Test
    public void testAnswerForNonNullInvokeWithListReturnType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        List<Object> returnValue = (List<Object>) emptyValues.answer(createInvokeWithListReturnType());

        assertNotNull(returnValue);
        assertEquals(0, returnValue.size());
    }

    @Test
    public void testAnswerForNonNullInvokeWithLinkedListReturnType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        List<Object> returnValue = (List<Object>) emptyValues.answer(createInvokeWithLinkedListReturnType());

        assertNotNull(returnValue);
        assertEquals(0, returnValue.size());
    }

    @Test
    public void testAnswerForNonNullInvokeWithArrayListReturnType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        List<Object> returnValue = (List<Object>) emptyValues.answer(createInvokeWithArrayListReturnType());

        assertNotNull(returnValue);
        assertEquals(0, returnValue.size());
    }

    @Test
    public void testAnswerForNonNullInvokeWithMapReturnType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        Map<Object, Object> returnValue = (Map<Object, Object>) emptyValues.answer(createInvokeWithMapReturnType());

        assertNotNull(returnValue);
        assertEquals(0, returnValue.size());
    }

    @Test
    public void testAnswerForNonNullInvokeWithHashMapReturnType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        Map<Object, Object> returnValue = (Map<Object, Object>) emptyValues.answer(createInvokeWithHashMapReturnType());

        assertNotNull(returnValue);
        assertEquals(0, returnValue.size());
    }

    @Test
    public void testAnswerForNonNullInvokeWithSortedMapReturnType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        SortedMap<Object, Object> returnValue = (SortedMap<Object, Object>) emptyValues.answer(createInvokeWithSortedMapReturnType());

        assertNotNull(returnValue);
        assertEquals(0, returnValue.size());
    }

    @Test
    public void testAnswerForNonNullInvokeWithTreeMapReturnType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        SortedMap<Object, Object> returnValue = (SortedMap<Object, Object>) emptyValues.answer(createInvokeWithTreeMapReturnType());

        assertNotNull(returnValue);
        assertEquals(0, returnValue.size());
    }

    @Test
    public void testAnswerForNonNullInvokeWithLinkedHashMapReturnType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        Map<Object, Object> returnValue = (Map<Object, Object>) emptyValues.answer(createInvokeWithLinkedHashMapReturnType());

        assertNotNull(returnValue);
        assertEquals(0, returnValue.size());
    }

    @Test
    public void testAnswerForInvokeWithNullReturnType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        Object returnValue = emptyValues.answer(createInvokeWithNullReturnType());

        assertNull(returnValue);
    }

    private InvocationOnMock createInvokeWithStringReturnType() {
        return new MockitoInvocation("testMethodWithStringReturnType", new Object[0], String.class);
    }

    private InvocationOnMock createInvokeWithIntegerReturnType() {
        return new MockitoInvocation("testMethodWithIntegerReturnType", new Object[0], int.class);
    }

    private InvocationOnMock createInvokeWithIntegerWrapperReturnType() {
        return new MockitoInvocation("testMethodWithIntegerWrapperReturnType", new Object[0], Integer.TYPE);
    }

    private InvocationOnMock createInvokeWithBooleanReturnType() {
        return new MockitoInvocation("testMethodWithBooleanReturnType", new Object[0], boolean.class);
    }

    private InvocationOnMock createInvokeWithBooleanWrapperReturnType() {
        return new MockitoInvocation("testMethodWithBooleanWrapperReturnType", new Object[0], Boolean.TYPE);
    }

    private InvocationOnMock createInvokeWithCharacterReturnType() {
        return new MockitoInvocation("testMethodWithCharacterReturnType", new Object[0], char.class);
    }

    private InvocationOnMock createInvokeWithCharacterWrapperReturnType() {
        return new MockitoInvocation("testMethodWithCharacterWrapperReturnType", new Object[0], Character.TYPE);
    }

    private InvocationOnMock createInvokeWithCollectionReturnType() {
        return new MockitoInvocation("testMethodWithCollectionReturnType", new Object[0], Collection.class);
    }

    private InvocationOnMock createInvokeWithSetReturnType() {
        return new MockitoInvocation("testMethodWithSetReturnType", new Object[0], Set.class);
    }

    private InvocationOnMock createInvokeWithHashSetReturnType() {
        return new MockitoInvocation("testMethodWithHashSetReturnType", new Object[0], HashSet.class);
    }

    private InvocationOnMock createInvokeWithSortedSetReturnType() {
        return new MockitoInvocation("testMethodWithSortedSetReturnType", new Object[0], SortedSet.class);
    }

    private InvocationOnMock createInvokeWithTreeSetReturnType() {
        return new MockitoInvocation("testMethodWithTreeSetReturnType", new Object[0], TreeSet.class);
    }

    private InvocationOnMock createInvokeWithLinkedHashSetReturnType() {
        return new MockitoInvocation("testMethodWithLinkedHashSetReturnType", new Object[0], LinkedHashSet.class);
    }

    private InvocationOnMock createInvokeWithListReturnType() {
        return new MockitoInvocation("testMethodWithListReturnType", new Object[0], List.class);
    }

    private InvocationOnMock createInvokeWithLinkedListReturnType() {
        return new MockitoInvocation("testMethodWithLinkedListReturnType", new Object[0], LinkedList.class);
    }

    private InvocationOnMock createInvokeWithArrayListReturnType() {
        return new MockitoInvocation("testMethodWithArrayListReturnType", new Object[0], ArrayList.class);
    }

    private InvocationOnMock createInvokeWithMapReturnType() {
        return new MockitoInvocation("testMethodWithMapReturnType", new Object[0], Map.class);
    }

    private InvocationOnMock createInvokeWithHashMapReturnType() {
        return new MockitoInvocation("testMethodWithHashMapReturnType", new Object[0], HashMap.class);
    }

    private InvocationOnMock createInvokeWithSortedMapReturnType() {
        return new MockitoInvocation("testMethodWithSortedMapReturnType", new Object[0], SortedMap.class);
    }

    private InvocationOnMock createInvokeWithTreeMapReturnType() {
        return new MockitoInvocation("testMethodWithTreeMapReturnType", new Object[0], TreeMap.class);
    }

    private InvocationOnMock createInvokeWithLinkedHashMapReturnType() {
        return new MockitoInvocation("testMethodWithLinkedHashMapReturnType", new Object[0], LinkedHashMap.class);
    }

    private InvocationOnMock createInvokeWithNullReturnType() {
        return new MockitoInvocation("testMethodWithNullReturnType", new Object[0], null);
    }

    static class MockitoInvocation {
        private final String methodName;
        private final Object[] arguments;
        private final Class<?> returnType;

        MockitoInvocation(String methodName, Object[] arguments, Class<?> returnType) {
            this.methodName = methodName;
            this.arguments = arguments;
            this.returnType = returnType;
        }
    }
}