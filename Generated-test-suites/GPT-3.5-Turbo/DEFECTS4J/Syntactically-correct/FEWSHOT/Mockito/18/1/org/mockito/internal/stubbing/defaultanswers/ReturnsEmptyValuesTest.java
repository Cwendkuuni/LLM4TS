package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import static org.junit.Assert.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        // Test toString() method
        assertEquals("Mock for String, hashCode: 12345", answer.answer(mockitoInvocation("toString", String.class, new Class[]{}, new Object[]{})));
        assertEquals("Mock for Integer, hashCode: 12345", answer.answer(mockitoInvocation("toString", Integer.class, new Class[]{}, new Object[]{})));

        // Test compareTo() method
        assertEquals(0, answer.answer(mockitoInvocation("compareTo", Comparable.class, new Class[]{Comparable.class}, new Object[]{answer})));
        assertEquals(1, answer.answer(mockitoInvocation("compareTo", Comparable.class, new Class[]{Comparable.class}, new Object[]{new Object()})));

        // Test primitive return types
        assertEquals(0, answer.answer(mockitoInvocation("intValue", int.class, new Class[]{}, new Object[]{})));
        assertEquals(0L, answer.answer(mockitoInvocation("longValue", long.class, new Class[]{}, new Object[]{})));
        assertEquals(0.0f, answer.answer(mockitoInvocation("floatValue", float.class, new Class[]{}, new Object[]{})), 0.001);
        assertEquals(0.0, answer.answer(mockitoInvocation("doubleValue", double.class, new Class[]{}, new Object[]{})), 0.001);
        assertEquals('a', answer.answer(mockitoInvocation("charValue", char.class, new Class[]{}, new Object[]{})));
        assertEquals(false, answer.answer(mockitoInvocation("booleanValue", boolean.class, new Class[]{}, new Object[]{})));

        // Test primitive wrapper return types
        assertEquals(0, answer.answer(mockitoInvocation("intValue", Integer.class, new Class[]{}, new Object[]{})).intValue());
        assertEquals(0L, answer.answer(mockitoInvocation("longValue", Long.class, new Class[]{}, new Object[]{})).longValue());
        assertEquals(0.0f, answer.answer(mockitoInvocation("floatValue", Float.class, new Class[]{}, new Object[]{})).floatValue(), 0.001);
        assertEquals(0.0, answer.answer(mockitoInvocation("doubleValue", Double.class, new Class[]{}, new Object[]{})).doubleValue(), 0.001);
        assertEquals('a', answer.answer(mockitoInvocation("charValue", Character.class, new Class[]{}, new Object[]{})).charValue());
        assertEquals(false, answer.answer(mockitoInvocation("booleanValue", Boolean.class, new Class[]{}, new Object[]{})).booleanValue());

        // Test collection return types
        assertTrue(answer.answer(mockitoInvocation("returnValueFor", Class.class, new Class[]{Class.class}, new Object[]{Collection.class})) instanceof LinkedList);
        assertTrue(answer.answer(mockitoInvocation("returnValueFor", Class.class, new Class[]{Class.class}, new Object[]{Set.class})) instanceof HashSet);
        assertTrue(answer.answer(mockitoInvocation("returnValueFor", Class.class, new Class[]{Class.class}, new Object[]{HashSet.class})) instanceof HashSet);
        assertTrue(answer.answer(mockitoInvocation("returnValueFor", Class.class, new Class[]{Class.class}, new Object[]{SortedSet.class})) instanceof TreeSet);
        assertTrue(answer.answer(mockitoInvocation("returnValueFor", Class.class, new Class[]{Class.class}, new Object[]{TreeSet.class})) instanceof TreeSet);
        assertTrue(answer.answer(mockitoInvocation("returnValueFor", Class.class, new Class[]{Class.class}, new Object[]{LinkedHashSet.class})) instanceof LinkedHashSet);
        assertTrue(answer.answer(mockitoInvocation("returnValueFor", Class.class, new Class[]{Class.class}, new Object[]{List.class})) instanceof LinkedList);
        assertTrue(answer.answer(mockitoInvocation("returnValueFor", Class.class, new Class[]{Class.class}, new Object[]{LinkedList.class})) instanceof LinkedList);
        assertTrue(answer.answer(mockitoInvocation("returnValueFor", Class.class, new Class[]{Class.class}, new Object[]{ArrayList.class})) instanceof ArrayList);
        assertTrue(answer.answer(mockitoInvocation("returnValueFor", Class.class, new Class[]{Class.class}, new Object[]{Map.class})) instanceof HashMap);
        assertTrue(answer.answer(mockitoInvocation("returnValueFor", Class.class, new Class[]{Class.class}, new Object[]{HashMap.class})) instanceof HashMap);
        assertTrue(answer.answer(mockitoInvocation("returnValueFor", Class.class, new Class[]{Class.class}, new Object[]{SortedMap.class})) instanceof TreeMap);
        assertTrue(answer.answer(mockitoInvocation("returnValueFor", Class.class, new Class[]{Class.class}, new Object[]{TreeMap.class})) instanceof TreeMap);
        assertTrue(answer.answer(mockitoInvocation("returnValueFor", Class.class, new Class[]{Class.class}, new Object[]{LinkedHashMap.class})) instanceof LinkedHashMap);
    }

    private InvocationOnMock mockitoInvocation(String methodName, Class<?> returnType, Class<?>[] parameterTypes, Object[] arguments) {
        try {
            Class<?> clazz = org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValuesTest.class;
            Object instance = clazz.newInstance();
            return new org.mockito.internal.invocation.MockitoMethodInvocation("Test", instance.getClass().getMethod(methodName, parameterTypes), instance, arguments, org.mockito.mock.MockCreationSettings.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}