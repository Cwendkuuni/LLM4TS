import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Mock
    private ObjectMethodsGuru mockMethodsGuru;
    
    @Mock
    private MockUtil mockMockUtil;
    
    @InjectMocks
    private ReturnsEmptyValues returnsEmptyValues;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAnswer_toStringWithDefaultName_returnsMockDescription() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(mockMethodsGuru.isToString(invocation.getMethod())).thenReturn(true);
        Object mock = mock(Object.class);
        when(invocation.getMock()).thenReturn(mock);
        MockName name = mock(MockName.class);
        when(mockMockUtil.getMockName(mock)).thenReturn(name);
        when(name.isDefault()).thenReturn(true);
        when(mockMockUtil.getMockSettings(mock).getTypeToMock().getSimpleName()).thenReturn("TestClass");
        when(mock.hashCode()).thenReturn(100);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertEquals("Mock for TestClass, hashCode: 100", result);
    }

    @Test
    public void testAnswer_toStringWithCustomName_returnsNameString() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(mockMethodsGuru.isToString(invocation.getMethod())).thenReturn(true);
        Object mock = mock(Object.class);
        when(invocation.getMock()).thenReturn(mock);
        MockName name = mock(MockName.class);
        when(mockMockUtil.getMockName(mock)).thenReturn(name);
        when(name.isDefault()).thenReturn(false);
        when(name.toString()).thenReturn("CustomMockName");
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertEquals("CustomMockName", result);
    }

    @Test
    public void testAnswer_compareTo_sameReferences_returnsZero() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(mockMethodsGuru.isCompareToMethod(invocation.getMethod())).thenReturn(true);
        Object mock = mock(Object.class);
        when(invocation.getMock()).thenReturn(mock);
        Object[] arguments = new Object[] {mock};
        when(invocation.getArguments()).thenReturn(arguments);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertEquals(0, result);
    }

    @Test
    public void testAnswer_compareTo_differentReferences_returnsNonZero() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(mockMethodsGuru.isCompareToMethod(invocation.getMethod())).thenReturn(true);
        Object mock = mock(Object.class);
        when(invocation.getMock()).thenReturn(mock);
        Object[] arguments = new Object[] {new Object()};
        when(invocation.getArguments()).thenReturn(arguments);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertNotEquals(0, result);
    }

    @Test
    public void testAnswer_returnValueFor_primitiveType_returnsDefaultValue() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Class<?> type = int.class;
        when(invocation.getMethod()).thenReturn(type.getDeclaredMethods()[0]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertEquals(0, result);
    }

    @Test
    public void testAnswer_returnValueFor_primitiveWrapperType_returnsDefaultValue() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Class<?> type = Integer.class;
        when(invocation.getMethod()).thenReturn(type.getDeclaredMethods()[0]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertEquals(0, result);
    }

    @Test
    public void testAnswer_returnValueFor_CollectionType_returnsLinkedList() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Class<?> type = Collection.class;
        when(invocation.getMethod()).thenReturn(type.getDeclaredMethods()[0]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertTrue(result instanceof LinkedList);
    }

    @Test
    public void testAnswer_returnValueFor_SetType_returnsHashSet() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Class<?> type = Set.class;
        when(invocation.getMethod()).thenReturn(type.getDeclaredMethods()[0]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertTrue(result instanceof HashSet);
    }

    @Test
    public void testAnswer_returnValueFor_HashSetType_returnsHashSet() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Class<?> type = HashSet.class;
        when(invocation.getMethod()).thenReturn(type.getDeclaredMethods()[0]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertTrue(result instanceof HashSet);
    }

    @Test
    public void testAnswer_returnValueFor_SortedSetType_returnsTreeSet() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Class<?> type = SortedSet.class;
        when(invocation.getMethod()).thenReturn(type.getDeclaredMethods()[0]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertTrue(result instanceof TreeSet);
    }

    @Test
    public void testAnswer_returnValueFor_TreeSetType_returnsTreeSet() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Class<?> type = TreeSet.class;
        when(invocation.getMethod()).thenReturn(type.getDeclaredMethods()[0]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertTrue(result instanceof TreeSet);
    }

    @Test
    public void testAnswer_returnValueFor_LinkedHashSetType_returnsLinkedHashSet() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Class<?> type = LinkedHashSet.class;
        when(invocation.getMethod()).thenReturn(type.getDeclaredMethods()[0]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertTrue(result instanceof LinkedHashSet);
    }

    @Test
    public void testAnswer_returnValueFor_ListType_returnsLinkedList() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Class<?> type = List.class;
        when(invocation.getMethod()).thenReturn(type.getDeclaredMethods()[0]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertTrue(result instanceof LinkedList);
    }

    @Test
    public void testAnswer_returnValueFor_LinkedListType_returnsLinkedList() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Class<?> type = LinkedList.class;
        when(invocation.getMethod()).thenReturn(type.getDeclaredMethods()[0]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertTrue(result instanceof LinkedList);
    }

    @Test
    public void testAnswer_returnValueFor_ArrayListType_returnsArrayList() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Class<?> type = ArrayList.class;
        when(invocation.getMethod()).thenReturn(type.getDeclaredMethods()[0]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertTrue(result instanceof ArrayList);
    }

    @Test
    public void testAnswer_returnValueFor_MapType_returnsHashMap() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Class<?> type = Map.class;
        when(invocation.getMethod()).thenReturn(type.getDeclaredMethods()[0]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertTrue(result instanceof HashMap);
    }

    @Test
    public void testAnswer_returnValueFor_HashMapType_returnsHashMap() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Class<?> type = HashMap.class;
        when(invocation.getMethod()).thenReturn(type.getDeclaredMethods()[0]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertTrue(result instanceof HashMap);
    }

    @Test
    public void testAnswer_returnValueFor_SortedMapType_returnsTreeMap() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Class<?> type = SortedMap.class;
        when(invocation.getMethod()).thenReturn(type.getDeclaredMethods()[0]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertTrue(result instanceof TreeMap);
    }

    @Test
    public void testAnswer_returnValueFor_TreeMapType_returnsTreeMap() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Class<?> type = TreeMap.class;
        when(invocation.getMethod()).thenReturn(type.getDeclaredMethods()[0]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertTrue(result instanceof TreeMap);
    }

    @Test
    public void testAnswer_returnValueFor_LinkedHashMapType_returnsLinkedHashMap() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Class<?> type = LinkedHashMap.class;
        when(invocation.getMethod()).thenReturn(type.getDeclaredMethods()[0]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertTrue(result instanceof LinkedHashMap);
    }
    
    @Test
    public void testAnswer_returnValueFor_unsupportedType_returnsNull() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Class<?> type = StringBuilder.class;
        when(invocation.getMethod()).thenReturn(type.getDeclaredMethods()[0]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertNull(result);
    }

}