
import org.junit.jupiter.api.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.mock.MockSettings;
import org.mockito.stubbing.Answer;

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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsEmptyValuesTest {
    
    @Test
    void testAnswerToString() {
        MockUtil mockUtil = mock(MockUtil.class);
        ObjectMethodsGuru methodsGuru = mock(ObjectMethodsGuru.class);
        MockSettings mockSettings = mock(MockSettings.class);
        
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        answer.methodsGuru = methodsGuru;
        answer.mockUtil = mockUtil;
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethods()[0]);
        
        Object mock = mock(Object.class);
        when(invocation.getMock()).thenReturn(mock);
        
        MockName mockName = mock(MockName.class);
        when(mockUtil.getMockName(mock)).thenReturn(mockName);
        when(mockUtil.getMockSettings(mock)).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);
        when(mockName.isDefault()).thenReturn(true);
        
        assertEquals("Mock for Object, hashCode: " + mock.hashCode(), answer.answer(invocation));
        
        verify(methodsGuru).isToString(invocation.getMethod());
        verify(mockUtil).getMockName(mock);
        verify(mockUtil).getMockSettings(mock);
    }
    
    @Test
    void testAnswerCompareToMethod() {
        ObjectMethodsGuru methodsGuru = mock(ObjectMethodsGuru.class);
        
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        answer.methodsGuru = methodsGuru;
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Comparable.class.getMethods()[0]);
        
        assertEquals(1, answer.answer(invocation));
        
        verify(methodsGuru).isCompareToMethod(invocation.getMethod());
    }
    
    @Test
    void testAnswerPrimitiveReturn() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Integer.class.getMethods()[0]);
        
        assertEquals(0, answer.answer(invocation));
    }
    
    @Test
    void testAnswerCollectionReturn() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Collection.class.getMethods()[0]);
        
        assertTrue(answer.answer(invocation) instanceof LinkedList);
    }
    
    @Test
    void testAnswerSetReturn() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Set.class.getMethods()[0]);
        
        assertTrue(answer.answer(invocation) instanceof HashSet);
    }
    
    @Test
    void testAnswerHashSetReturn() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(HashSet.class.getMethods()[0]);
        
        assertTrue(answer.answer(invocation) instanceof HashSet);
    }
    
    @Test
    void testAnswerSortedSetReturn() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(SortedSet.class.getMethods()[0]);
        
        assertTrue(answer.answer(invocation) instanceof TreeSet);
    }
    
    @Test
    void testAnswerTreeSetReturn() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TreeSet.class.getMethods()[0]);
        
        assertTrue(answer.answer(invocation) instanceof TreeSet);
    }
    
    @Test
    void testAnswerLinkedHashSetReturn() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(LinkedHashSet.class.getMethods()[0]);
        
        assertTrue(answer.answer(invocation) instanceof LinkedHashSet);
    }
    
    @Test
    void testAnswerListReturn() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(List.class.getMethods()[0]);
        
        assertTrue(answer.answer(invocation) instanceof LinkedList);
    }
    
    @Test
    void testAnswerLinkedListReturn() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(LinkedList.class.getMethods()[0]);
        
        assertTrue(answer.answer(invocation) instanceof LinkedList);
    }
    
    @Test
    void testAnswerArrayListReturn() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ArrayList.class.getMethods()[0]);
        
        assertTrue(answer.answer(invocation) instanceof ArrayList);
    }
    
    @Test
    void testAnswerMapReturn() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Map.class.getMethods()[0]);
        
        assertTrue(answer.answer(invocation) instanceof HashMap);
    }
    
    @Test
    void testAnswerHashMapReturn() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(HashMap.class.getMethods()[0]);
        
        assertTrue(answer.answer(invocation) instanceof HashMap);
    }
    
    @Test
    void testAnswerSortedMapReturn() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(SortedMap.class.getMethods()[0]);
        
        assertTrue(answer.answer(invocation) instanceof TreeMap);
    }
    
    @Test
    void testAnswerTreeMapReturn() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TreeMap.class.getMethods()[0]);
        
        assertTrue(answer.answer(invocation) instanceof TreeMap);
    }
    
    @Test
    void testAnswerLinkedHashMapReturn() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(LinkedHashMap.class.getMethods()[0]);
        
        assertTrue(answer.answer(invocation) instanceof LinkedHashMap);
    }
    
    @Test
    void testAnswerDefault() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(null);
        
        assertNull(answer.answer(invocation));
    }
    
    @Test
    void testReturnValueForPrimitive() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        
        assertEquals(0, answer.returnValueFor(int.class));
        assertEquals(0L, answer.returnValueFor(long.class));
        assertEquals((short) 0, answer.returnValueFor(short.class));
        assertEquals(0.0f, answer.returnValueFor(float.class));
        assertEquals(0.0, answer.returnValueFor(double.class));
        assertEquals((byte) 0, answer.returnValueFor(byte.class));
        assertEquals('\u0000', answer.returnValueFor(char.class));
        assertFalse((boolean) answer.returnValueFor(boolean.class));
        
        assertEquals(new Integer(0), answer.returnValueFor(Integer.class));
        assertEquals(new Long(0), answer.returnValueFor(Long.class));
        assertEquals(new Short((short) 0), answer.returnValueFor(Short.class));
        assertEquals(new Float(0.0f), answer.returnValueFor(Float.class));
        assertEquals(new Double(0.0), answer.returnValueFor(Double.class));
        assertEquals(new Byte((byte) 0), answer.returnValueFor(Byte.class));
        assertEquals(new Character('\u0000'), answer.returnValueFor(Character.class));
        assertFalse((Boolean) answer.returnValueFor(Boolean.class));
    }
    
}
