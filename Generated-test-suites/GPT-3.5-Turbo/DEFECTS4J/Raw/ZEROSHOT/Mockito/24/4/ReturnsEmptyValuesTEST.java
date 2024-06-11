import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
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

public class ReturnsEmptyValuesTest {
    
    private ReturnsEmptyValues returnsEmptyValues;
    
    @Before
    public void setUp() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }
    
    @Test
    public void testAnswer_toString() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        Object result = returnsEmptyValues.answer(invocation);
        assertTrue(result instanceof String);
    }
    
    @Test
    public void testAnswer_compareTo() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Comparable.class.getMethod("compareTo", Object.class));
        Object result = returnsEmptyValues.answer(invocation);
        assertEquals(1, result);
    }
    
    @Test
    public void testAnswer_primitiveReturnType() throws NoSuchMethodException {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(Boolean.TYPE);
        Object result = returnsEmptyValues.answer(invocation);
        assertEquals(false, result);
    }
    
    @Test
    public void testAnswer_returnValueFor_Collection() throws NoSuchMethodException {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(Collection.class);
        Object result = returnsEmptyValues.answer(invocation);
        assertTrue(result instanceof LinkedList);
    }
    
    @Test
    public void testAnswer_returnValueFor_Set() throws NoSuchMethodException {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(Set.class);
        Object result = returnsEmptyValues.answer(invocation);
        assertTrue(result instanceof HashSet);
    }
    
    @Test
    public void testAnswer_returnValueFor_HashSet() throws NoSuchMethodException {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(HashSet.class);
        Object result = returnsEmptyValues.answer(invocation);
        assertTrue(result instanceof HashSet);
    }
    
    @Test
    public void testAnswer_returnValueFor_SortedSet() throws NoSuchMethodException {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(SortedSet.class);
        Object result = returnsEmptyValues.answer(invocation);
        assertTrue(result instanceof TreeSet);
    }
    
    @Test
    public void testAnswer_returnValueFor_TreeSet() throws NoSuchMethodException {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(TreeSet.class);
        Object result = returnsEmptyValues.answer(invocation);
        assertTrue(result instanceof TreeSet);
    }
    
    @Test
    public void testAnswer_returnValueFor_LinkedHashSet() throws NoSuchMethodException {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(LinkedHashSet.class);
        Object result = returnsEmptyValues.answer(invocation);
        assertTrue(result instanceof LinkedHashSet);
    }
    
    @Test
    public void testAnswer_returnValueFor_List() throws NoSuchMethodException {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(List.class);
        Object result = returnsEmptyValues.answer(invocation);
        assertTrue(result instanceof LinkedList);
    }
    
    @Test
    public void testAnswer_returnValueFor_LinkedList() throws NoSuchMethodException {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(LinkedList.class);
        Object result = returnsEmptyValues.answer(invocation);
        assertTrue(result instanceof LinkedList);
    }
    
    @Test
    public void testAnswer_returnValueFor_ArrayList() throws NoSuchMethodException {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(ArrayList.class);
        Object result = returnsEmptyValues.answer(invocation);
        assertTrue(result instanceof ArrayList);
    }
    
    @Test
    public void testAnswer_returnValueFor_Map() throws NoSuchMethodException {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(Map.class);
        Object result = returnsEmptyValues.answer(invocation);
        assertTrue(result instanceof HashMap);
    }
    
    @Test
    public void testAnswer_returnValueFor_HashMap() throws NoSuchMethodException {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(HashMap.class);
        Object result = returnsEmptyValues.answer(invocation);
        assertTrue(result instanceof HashMap);
    }
    
    @Test
    public void testAnswer_returnValueFor_SortedMap() throws NoSuchMethodException {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(SortedMap.class);
        Object result = returnsEmptyValues.answer(invocation);
        assertTrue(result instanceof TreeMap);
    }
    
    @Test
    public void testAnswer_returnValueFor_TreeMap() throws NoSuchMethodException {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(TreeMap.class);
        Object result = returnsEmptyValues.answer(invocation);
        assertTrue(result instanceof TreeMap);
    }
    
    @Test
    public void testAnswer_returnValueFor_LinkedHashMap() throws NoSuchMethodException {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(LinkedHashMap.class);
        Object result = returnsEmptyValues.answer(invocation);
        assertTrue(result instanceof LinkedHashMap);
    }
    
    @Test
    public void testAnswer_returnValueFor_otherTypes() throws NoSuchMethodException {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        Object result = returnsEmptyValues.answer(invocation);
        assertNull(result);
    }
}