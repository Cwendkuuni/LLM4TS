
package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.*;

public class ReturnsEmptyValuesTest {
    @Test
    public void testAnswer() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // Test toString() method
        String expectedToString = "Mock for " + HashMap.class.getSimpleName() + ", hashCode: " + returnsEmptyValues.hashCode();
        assertEquals(expectedToString, returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class)));

        // Test compareTo() method
        assertEquals(0, returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, additionalArguments)));

        // Test primitive and wrapper classes
        assertNull(returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, additionalArguments)));
        assertEquals(0, returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, additionalArguments)));
        
        // Test Collection and List
        LinkedList<Object> expectedLinkedList = new LinkedList<Object>();
        assertEquals(expectedLinkedList.getClass(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, additionalArguments)));
        assertEquals(expectedLinkedList.getClass(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, additionalArguments)));

        // Test Set
        HashSet<Object> expectedHashSet = new HashSet<Object>();
        assertEquals(expectedHashSet.getClass(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, additionalArguments)));
        assertEquals(expectedHashSet.getClass(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, additionalArguments)));

        // Test SortedSet and TreeSet
        TreeSet<Object> expectedTreeSet = new TreeSet<Object>();
        assertEquals(expectedTreeSet.getClass(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, additionalArguments)));
        assertEquals(expectedTreeSet.getClass(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, additionalArguments)));

        // Test LinkedHashSet
        LinkedHashSet<Object> expectedLinkedHashSet = new LinkedHashSet<Object>();
        assertEquals(expectedLinkedHashSet.getClass(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, additionalArguments)));
        assertEquals(expectedLinkedHashSet.getClass(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, additionalArguments)));

        // Test Map
        HashMap<Object, Object> expectedHashMap = new HashMap<Object, Object>();
        assertEquals(expectedHashMap.getClass(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, additionalArguments)));
        assertEquals(expectedHashMap.getClass(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, additionalArguments)));

        // Test SortedMap and TreeMap
        TreeMap<Object, Object> expectedTreeMap = new TreeMap<Object, Object>();
        assertEquals(expectedTreeMap.getClass(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, additionalArguments)));
        assertEquals(expectedTreeMap.getClass(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, additionalArguments)));

        // Test LinkedHashMap
        LinkedHashMap<Object, Object> expectedLinkedHashMap = new LinkedHashMap<Object, Object>();
        assertEquals(expectedLinkedHashMap.getClass(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, additionalArguments)));

        // Test default case
        assertNull(returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, additionalArguments)));
    }}
    