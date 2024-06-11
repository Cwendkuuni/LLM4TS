import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.Mockito;
import org.mockito.mock.MockName;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import java.io.Serializable;
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
    private ObjectMethodsGuru objectMethodsGuru;
    private MockUtil mockUtil;
    
    @Before
    public void setup() {
        returnsEmptyValues = new ReturnsEmptyValues();
        objectMethodsGuru = new ObjectMethodsGuru();
        mockUtil = new MockUtil();
    }
    
    @Test
    public void testAnswer_toString_defaultMock() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(objectMethodsGuru.isToString(Mockito.any())).thenReturn(true);
        
        Object mock = Mockito.mock(Object.class);
        Mockito.when(invocation.getMock()).thenReturn(mock);
        
        MockName name = Mockito.mock(MockName.class);
        Mockito.when(mockUtil.getMockName(mock)).thenReturn(name);
        Mockito.when(name.isDefault()).thenReturn(true);
        
        String result = (String) returnsEmptyValues.answer(invocation);
        
        assertEquals("Mock for Object, hashCode: " + mock.hashCode(), result);
    }
    
    @Test
    public void testAnswer_toString_customMock() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(objectMethodsGuru.isToString(Mockito.any())).thenReturn(true);
        
        Object mock = Mockito.mock(Object.class);
        Mockito.when(invocation.getMock()).thenReturn(mock);
        
        MockName name = Mockito.mock(MockName.class);
        Mockito.when(mockUtil.getMockName(mock)).thenReturn(name);
        Mockito.when(name.isDefault()).thenReturn(false);
        Mockito.when(name.toString()).thenReturn("CustomMock");
        
        String result = (String) returnsEmptyValues.answer(invocation);
        
        assertEquals("CustomMock", result);
    }
    
    @Test
    public void testAnswer_compareTo() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(objectMethodsGuru.isCompareToMethod(Mockito.any())).thenReturn(true);
        
        Integer result = (Integer) returnsEmptyValues.answer(invocation);
        
        assertEquals(1, result.intValue());
    }
    
    @Test
    public void testAnswer_primitiveReturnType_int() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(objectMethodsGuru.isToString(Mockito.any())).thenReturn(false);
        Mockito.when(objectMethodsGuru.isCompareToMethod(Mockito.any())).thenReturn(false);
        Mockito.when(invocation.getMethod()).thenReturn(getMethod("getInt"));
        
        int result = (int) returnsEmptyValues.answer(invocation);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testAnswer_primitiveReturnType_char() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(objectMethodsGuru.isToString(Mockito.any())).thenReturn(false);
        Mockito.when(objectMethodsGuru.isCompareToMethod(Mockito.any())).thenReturn(false);
        Mockito.when(invocation.getMethod()).thenReturn(getMethod("getChar"));
        
        char result = (char) returnsEmptyValues.answer(invocation);
        
        assertEquals('\u0000', result);
    }
    
    @Test
    public void testAnswer_primitiveWrapperReturnType() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(objectMethodsGuru.isToString(Mockito.any())).thenReturn(false);
        Mockito.when(objectMethodsGuru.isCompareToMethod(Mockito.any())).thenReturn(false);
        Mockito.when(invocation.getMethod()).thenReturn(getMethod("getInteger"));
        
        Integer result = (Integer) returnsEmptyValues.answer(invocation);
        
        assertEquals(0, result.intValue());
    }
    
    @Test
    public void testAnswer_collectionReturnType_LinkedList() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(objectMethodsGuru.isToString(Mockito.any())).thenReturn(false);
        Mockito.when(objectMethodsGuru.isCompareToMethod(Mockito.any())).thenReturn(false);
        Mockito.when(invocation.getMethod()).thenReturn(getMethod("getLinkedList"));
        
        Collection<?> result = (Collection<?>) returnsEmptyValues.answer(invocation);
        
        assertTrue(result instanceof LinkedList);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswer_collectionReturnType_Set() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(objectMethodsGuru.isToString(Mockito.any())).thenReturn(false);
        Mockito.when(objectMethodsGuru.isCompareToMethod(Mockito.any())).thenReturn(false);
        Mockito.when(invocation.getMethod()).thenReturn(getMethod("getSet"));
        
        Collection<?> result = (Collection<?>) returnsEmptyValues.answer(invocation);
        
        assertTrue(result instanceof HashSet);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswer_collectionReturnType_HashSet() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(objectMethodsGuru.isToString(Mockito.any())).thenReturn(false);
        Mockito.when(objectMethodsGuru.isCompareToMethod(Mockito.any())).thenReturn(false);
        Mockito.when(invocation.getMethod()).thenReturn(getMethod("getHashSet"));
        
        Collection<?> result = (Collection<?>) returnsEmptyValues.answer(invocation);
        
        assertTrue(result instanceof HashSet);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswer_collectionReturnType_SortedSet() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(objectMethodsGuru.isToString(Mockito.any())).thenReturn(false);
        Mockito.when(objectMethodsGuru.isCompareToMethod(Mockito.any())).thenReturn(false);
        Mockito.when(invocation.getMethod()).thenReturn(getMethod("getSortedSet"));
        
        Collection<?> result = (Collection<?>) returnsEmptyValues.answer(invocation);
        
        assertTrue(result instanceof TreeSet);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswer_collectionReturnType_TreeSet() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(objectMethodsGuru.isToString(Mockito.any())).thenReturn(false);
        Mockito.when(objectMethodsGuru.isCompareToMethod(Mockito.any())).thenReturn(false);
        Mockito.when(invocation.getMethod()).thenReturn(getMethod("getTreeSet"));
        
        Collection<?> result = (Collection<?>) returnsEmptyValues.answer(invocation);
        
        assertTrue(result instanceof TreeSet);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswer_collectionReturnType_LinkedHashSet() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(objectMethodsGuru.isToString(Mockito.any())).thenReturn(false);
        Mockito.when(objectMethodsGuru.isCompareToMethod(Mockito.any())).thenReturn(false);
        Mockito.when(invocation.getMethod()).thenReturn(getMethod("getLinkedHashSet"));
        
        Collection<?> result = (Collection<?>) returnsEmptyValues.answer(invocation);
        
        assertTrue(result instanceof LinkedHashSet);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswer_collectionReturnType_List() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(objectMethodsGuru.isToString(Mockito.any())).thenReturn(false);
        Mockito.when(objectMethodsGuru.isCompareToMethod(Mockito.any())).thenReturn(false);
        Mockito.when(invocation.getMethod()).thenReturn(getMethod("getList"));
        
        Collection<?> result = (Collection<?>) returnsEmptyValues.answer(invocation);
        
        assertTrue(result instanceof LinkedList);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswer_collectionReturnType_LinkedList() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(objectMethodsGuru.isToString(Mockito.any())).thenReturn(false);
        Mockito.when(objectMethodsGuru.isCompareToMethod(Mockito.any())).thenReturn(false);
        Mockito.when(invocation.getMethod()).thenReturn(getMethod("getLinkedList"));
        
        Collection<?> result = (Collection<?>) returnsEmptyValues.answer(invocation);
        
        assertTrue(result instanceof LinkedList);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswer_collectionReturnType_ArrayList() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(objectMethodsGuru.isToString(Mockito.any())).thenReturn(false);
        Mockito.when(objectMethodsGuru.isCompareToMethod(Mockito.any())).thenReturn(false);
        Mockito.when(invocation.getMethod()).thenReturn(getMethod("getArrayList"));
        
        Collection<?> result = (Collection<?>) returnsEmptyValues.answer(invocation);
        
        assertTrue(result instanceof ArrayList);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswer_collectionReturnType_Map() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(objectMethodsGuru.isToString(Mockito.any())).thenReturn(false);
        Mockito.when(objectMethodsGuru.isCompareToMethod(Mockito.any())).thenReturn(false);
        Mockito.when(invocation.getMethod()).thenReturn(getMethod("getMap"));
        
        Map<?, ?> result = (Map<?, ?>) returnsEmptyValues.answer(invocation);
        
        assertTrue(result instanceof HashMap);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswer_collectionReturnType_HashMap() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(objectMethodsGuru.isToString(Mockito.any())).thenReturn(false);
        Mockito.when(objectMethodsGuru.isCompareToMethod(Mockito.any())).thenReturn(false);
        Mockito.when(invocation.getMethod()).thenReturn(getMethod("getHashMap"));
        
        Map<?, ?> result = (Map<?, ?>) returnsEmptyValues.answer(invocation);
        
        assertTrue(result instanceof HashMap);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswer_collectionReturnType_SortedMap() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(objectMethodsGuru.isToString(Mockito.any())).thenReturn(false);
        Mockito.when(objectMethodsGuru.isCompareToMethod(Mockito.any())).thenReturn(false);
        Mockito.when(invocation.getMethod()).thenReturn(getMethod("getSortedMap"));
        
        Map<?, ?> result = (Map<?, ?>) returnsEmptyValues.answer(invocation);
        
        assertTrue(result instanceof TreeMap);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswer_collectionReturnType_TreeMap() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(objectMethodsGuru.isToString(Mockito.any())).thenReturn(false);
        Mockito.when(objectMethodsGuru.isCompareToMethod(Mockito.any())).thenReturn(false);
        Mockito.when(invocation.getMethod()).thenReturn(getMethod("getTreeMap"));
        
        Map<?, ?> result = (Map<?, ?>) returnsEmptyValues.answer(invocation);
        
        assertTrue(result instanceof TreeMap);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswer_collectionReturnType_LinkedHashMap() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(objectMethodsGuru.isToString(Mockito.any())).thenReturn(false);
        Mockito.when(objectMethodsGuru.isCompareToMethod(Mockito.any())).thenReturn(false);
        Mockito.when(invocation.getMethod()).thenReturn(getMethod("getLinkedHashMap"));
        
        Map<?, ?> result = (Map<?, ?>) returnsEmptyValues.answer(invocation);
        
        assertTrue(result instanceof LinkedHashMap);
        assertTrue(result.isEmpty());
    }
    
    private java.lang.reflect.Method getMethod(String methodName) {
        try {
            return ReturnsEmptyValues.class.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}