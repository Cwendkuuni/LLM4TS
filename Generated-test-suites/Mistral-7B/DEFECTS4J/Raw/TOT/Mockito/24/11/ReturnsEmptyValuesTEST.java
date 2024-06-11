package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        Mockito<String> mockString = Mockito.mock(String.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(mockString.toString(invocation)).thenAnswer(new ReturnsEmptyValues());

        String mockedString = Mockito.invoke(mockString, invocation);
        String expectedToString = "Mock for " + String.class.getSimpleName() + ", hashCode: " + System.identityHashCode(mockString);
        assertEquals(expectedToString, mockedString);
    }

    @Test
    public void testCompareToMethod() {
        Mockito<Comparable> mockComparable = Mockito.mock(Comparable.class);
        Mockito.when(mockComparable.compareTo(ArgumentMatchers.any())).thenAnswer(new ReturnsEmptyValues());

        Comparable mockedComparable1 = Mockito.spy(mockComparable);
        Comparable mockedComparable2 = Mockito.spy(mockComparable);

        int comparisonResult = Mockito.invoke(mockedComparable1, "compareTo", mockedComparable2);
        int expectedComparisonResult = 1;
        assertEquals(expectedComparisonResult, comparisonResult);
    }

    @Test
    public void testBooleanReturningMethod() {
        Mockito<Boolean> mockBoolean = Mockito.mock(Boolean.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(mockBoolean.booleanValue()).thenAnswer(new ReturnsEmptyValues());

        Boolean mockedBoolean = Mockito.invoke(mockBoolean, invocation);
        assertNotNull(mockedBoolean);
        assertEquals(Boolean.FALSE, mockedBoolean);
    }

    @Test
    public void testByteReturningMethod() {
        Mockito<Byte> mockByte = Mockito.mock(Byte.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(mockByte.byteValue()).thenAnswer(new ReturnsEmptyValues());

        Byte mockedByte = Mockito.invoke(mockByte, invocation);
        assertNotNull(mockedByte);
        assertEquals((byte) 0, mockedByte.byteValue());
    }

    @Test
    public void testShortReturningMethod() {
        Mockito<Short> mockShort = Mockito.mock(Short.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(mockShort.shortValue()).thenAnswer(new ReturnsEmptyValues());

        Short mockedShort = Mockito.invoke(mockShort, invocation);
        assertNotNull(mockedShort);
        assertEquals((short) 0, mockedShort);
    }

    @Test
    public void testIntReturningMethod() {
        Mockito<Integer> mockInteger = Mockito.mock(Integer.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(mockInteger.intValue()).thenAnswer(new ReturnsEmptyValues());

        Integer mockedInteger = Mockito.invoke(mockInteger, invocation);
        assertNotNull(mockedInteger);
        assertEquals(0, mockedInteger.intValue());
    }

    @Test
    public void testLongReturningMethod() {
        Mockito<Long> mockLong = Mockito.mock(Long.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(mockLong.longValue()).thenAnswer(new ReturnsEmptyValues());

        Long mockedLong = Mockito.invoke(mockLong, invocation);
        assertNotNull(mockedLong);
        assertEquals(0L, mockedLong.longValue());
    }

    @Test
    public void testFloatReturningMethod() {
        Mockito<Float> mockFloat = Mockito.mock(Float.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(mockFloat.floatValue()).thenAnswer(new ReturnsEmptyValues());

        Float mockedFloat = Mockito.invoke(mockFloat, invocation);
        assertNotNull(mockedFloat);
        assertEquals(0f, mockedFloat.floatValue(), 0.01f);
    }

    @Test
    public void testDoubleReturningMethod() {
        Mockito<Double> mockDouble = Mockito.mock(Double.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(mockDouble.doubleValue()).thenAnswer(new ReturnsEmptyValues());

        Double mockedDouble = Mockito.invoke(mockDouble, invocation);
        assertNotNull(mockedDouble);
        assertEquals(0d, mockedDouble.doubleValue(), 0.01d);
    }

    @Test
    public void testCharReturningMethod() {
        Mockito<Character> mockCharacter = Mockito.mock(Character.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(mockCharacter.charValue()).thenAnswer(new ReturnsEmptyValues());

        Character mockedCharacter = Mockito.invoke(mockCharacter, invocation);
        assertNotNull(mockedCharacter);
        assertEquals('\u0000', mockedCharacter);
    }

    @Test
    public void testVoidReturningMethod() {
        Mockito<Void> mockVoid = Mockito.mock(Void.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.doNothing().when(mockVoid).thenAnswer(new ReturnsEmptyValues());

        mockVoid.voidMethod(invocation);
    }

    @Test
    public void testEmptyCollectionReturningMethod() {
        Mockito<Collection> mockCollection = Mockito.mock(Collection.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(mockCollection.isEmpty()).thenAnswer( Mockito.returns(true) );
        Mockito.when(mockCollection.size()).thenAnswer( Mockito.returns(0) );
        Mockito.when(mockCollection.iterator()).thenAnswer(new ReturnsEmptyValues());
        Mockito.when(mockCollection.toArray()).thenAnswer(new ReturnsEmptyValues());
        Mockito.when(mockCollection.clear()).thenAnswer(new ReturnsEmptyValues());

        Collection mockedCollection = Mockito.invoke(mockCollection, invocation);
        assertTrue(mockedCollection.isEmpty());
        assertEquals(0, mockedCollection.size());
        assertNotNull(mockedCollection.iterator());
        assertEquals(new Object[0], mockedCollection.toArray());
    }

    @Test
    public void testEmptySetReturningMethod() {
        Mockito<Set> mockSet = Mockito.mock(Set.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(mockSet.isEmpty()).thenAnswer( Mockito.returns(true) );
        Mockito.when(mockSet.size()).thenAnswer( Mockito.returns(0) );
        Mockito.when(mockSet.iterator()).thenAnswer(new ReturnsEmptyValues());
        Mockito.when(mockSet.add(ArgumentMatchers.any())).thenAnswer( Mockito.returns(true) );
        Mockito.when(mockSet.remove(ArgumentMatchers.any())).thenAnswer( Mockito.returns(false) );
        Mockito.when(mockSet.contains(ArgumentMatchers.any())).thenAnswer( Mockito.returns(false) );
        Mockito.when(mockSet.toArray()).thenAnswer(new ReturnsEmptyValues());

        Set mockedSet = Mockito.invoke(mockSet, invocation);
        assertTrue(mockedSet.isEmpty());
        assertEquals(0, mockedSet.size());
        assertNotNull(mockedSet.iterator());
        assertFalse(mockedSet.add(new Object()));
        assertFalse(mockedSet.remove(new Object()));
        assertFalse(mockedSet.contains(new Object()));
        assertNotNull(mockedSet.toArray());
    }

    @Test
    public void testEmptyMapReturningMethod() {
        Mockito<Map> mockMap = Mockito.mock(Map.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(mockMap.isEmpty()).thenAnswer( Mockito.returns(true) );
        Mockito.when(mockMap.size()).thenAnswer( Mockito.returns(0) );
        Mockito.when(mockMap.entrySet()).thenAnswer(new ReturnsEmptyValues());
        Mockito.when(mockMap.keySet()).thenAnswer(new ReturnsEmptyValues());
        Mockito.when(mockMap.values()).thenAnswer(new ReturnsEmptyValues());
        Mockito.when(mockMap.containsKey(ArgumentMatchers.any())).thenAnswer( Mockito.returns(false) );
        Mockito.when(mockMap.get(ArgumentMatchers.any())).thenAnswer( Mockito.returns(null) );
        Mockito.when(mockMap.put(ArgumentMatchers.any(), ArgumentMatchers.any())).thenAnswer( Mockito.returns(null) );
        Mockito.when(mockMap.remove(ArgumentMatchers.any())).thenAnswer( Mockito.returns(null) );

        Map mockedMap = Mockito.invoke(mockMap, invocation);
        assertTrue(mockedMap.isEmpty());
        assertEquals(0, mockedMap.size());
        assertNotNull(mockedMap.entrySet());
        assertNotNull(mockedMap.keySet());
        assertNotNull(mockedMap.values());
        assertFalse(mockedMap.containsKey(new Object()));
        assertNull(mockedMap.get(new Object()));
        assertNull(mockedMap.put(new Object(), new Object()));
        assertNull(mockedMap.remove(new Object()));
    }
}