import static org.junit.Assert.*;
import org.junit.*;
import org.mockito.invocation.*;
import org.mockito.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToStringDefaultName() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(String.class.getMethod("toString"));
        Mockito.when(invocation.getMock()).thenReturn(Mockito.mock(Object.class));
        String result = (String)values.answer(invocation);
        
        String expected = "Mock for Object, hashCode: " + invocation.getMock().hashCode();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testAnswerToStringCustomName() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(String.class.getMethod("toString"));
        Mockito.when(invocation.getMock()).thenReturn(Mockito.mock(Object.class));
        Mockito.when(invocation.getMock().toString()).thenReturn("CustomName");
        String result = (String)values.answer(invocation);
        
        String expected = "CustomName";
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testAnswerCompareToSameReference() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Comparable.class.getMethod("compareTo", Object.class));
        Object[] arguments = new Object[1];
        arguments[0] = invocation.getMock();
        Mockito.when(invocation.getArguments()).thenReturn(arguments);
        Mockito.when(invocation.getMock()).thenReturn(Mockito.mock(Comparable.class));
        Object result = values.answer(invocation);
        
        int expected = 0;
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testAnswerCompareToDifferentReference() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Comparable.class.getMethod("compareTo", Object.class));
        Object[] arguments = new Object[1];
        arguments[0] = Mockito.mock(Object.class);
        Mockito.when(invocation.getArguments()).thenReturn(arguments);
        Mockito.when(invocation.getMock()).thenReturn(Mockito.mock(Comparable.class));
        Object result = values.answer(invocation);
        
        int expected = 1;
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testAnswerReturnValueForPrimitiveInt() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Foo.class.getMethod("getInt"));
        Object result = values.answer(invocation);
        
        int expected = 0;
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testAnswerReturnValueForWrapperInteger() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Foo.class.getMethod("getInteger"));
        Object result = values.answer(invocation);
        
        Integer expected = 0;
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testAnswerReturnValueForCollection() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Foo.class.getMethod("getCollection"));
        Object result = values.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof Collection);
        assertTrue(((Collection<?>) result).isEmpty());
    }
    
    @Test
    public void testAnswerReturnValueForSet() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Foo.class.getMethod("getSet"));
        Object result = values.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof Set);
        assertTrue(((Set<?>) result).isEmpty());
    }
    
    @Test
    public void testAnswerReturnValueForHashSet() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Foo.class.getMethod("getHashSet"));
        Object result = values.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof HashSet);
        assertTrue(((HashSet<?>) result).isEmpty());
    }
    
    @Test
    public void testAnswerReturnValueForSortedSet() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Foo.class.getMethod("getSortedSet"));
        Object result = values.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof SortedSet);
        assertTrue(((SortedSet<?>) result).isEmpty());
    }
    
    @Test
    public void testAnswerReturnValueForTreeSet() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Foo.class.getMethod("getTreeSet"));
        Object result = values.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof TreeSet);
        assertTrue(((TreeSet<?>) result).isEmpty());
    }
    
    @Test
    public void testAnswerReturnValueForLinkedHashSet() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Foo.class.getMethod("getLinkedHashSet"));
        Object result = values.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof LinkedHashSet);
        assertTrue(((LinkedHashSet<?>) result).isEmpty());
    }
    
    @Test
    public void testAnswerReturnValueForList() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Foo.class.getMethod("getList"));
        Object result = values.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof List);
        assertTrue(((List<?>) result).isEmpty());
    }
    
    @Test
    public void testAnswerReturnValueForLinkedList() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Foo.class.getMethod("getLinkedList"));
        Object result = values.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList<?>) result).isEmpty());
    }
    
    @Test
    public void testAnswerReturnValueForArrayList() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Foo.class.getMethod("getArrayList"));
        Object result = values.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof ArrayList);
        assertTrue(((ArrayList<?>) result).isEmpty());
    }
    
    @Test
    public void testAnswerReturnValueForMap() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Foo.class.getMethod("getMap"));
        Object result = values.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof Map);
        assertTrue(((Map<?, ?>) result).isEmpty());
    }
    
    @Test
    public void testAnswerReturnValueForHashMap() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Foo.class.getMethod("getHashMap"));
        Object result = values.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof HashMap);
        assertTrue(((HashMap<?, ?>) result).isEmpty());
    }
    
    @Test
    public void testAnswerReturnValueForSortedMap() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Foo.class.getMethod("getSortedMap"));
        Object result = values.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof SortedMap);
        assertTrue(((SortedMap<?, ?>) result).isEmpty());
    }
    
    @Test
    public void testAnswerReturnValueForTreeMap() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Foo.class.getMethod("getTreeMap"));
        Object result = values.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof TreeMap);
        assertTrue(((TreeMap<?, ?>) result).isEmpty());
    }
    
    @Test
    public void testAnswerReturnValueForLinkedHashMap() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Foo.class.getMethod("getLinkedHashMap"));
        Object result = values.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof LinkedHashMap);
        assertTrue(((LinkedHashMap<?, ?>) result).isEmpty());
    }
    
    @Test
    public void testAnswerReturnValueForOtherTypes() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Foo.class.getMethod("getOtherType"));
        Object result = values.answer(invocation);
        
        assertNull(result);
    }
    
    @SuppressWarnings("rawtypes")
    private interface Foo {
        int getInt();
        Integer getInteger();
        Collection getCollection();
        Set getSet();
        HashSet getHashSet();
        SortedSet getSortedSet();
        TreeSet getTreeSet();
        LinkedHashSet getLinkedHashSet();
        List getList();
        LinkedList getLinkedList();
        ArrayList getArrayList();
        Map getMap();
        HashMap getHashMap();
        SortedMap getSortedMap();
        TreeMap getTreeMap();
        LinkedHashMap getLinkedHashMap();
        Object getOtherType();
    }
}