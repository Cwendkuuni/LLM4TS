import org.junit.Test;
import static org.junit.Assert.*;

public class ReturnsEmptyValuesTest {
    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    
    @Test
    public void testAnswer_ToStringMethod_ReturnsDescriptionOfMock() {
        // Arrange
        String expected = "Mock for TypeToMock, hashCode: " + mock.hashCode();
        
        // Act
        String actual = returnsEmptyValues.answer(getInvocationMock()).toString();
        
        // Assert
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAnswer_CompareToMethod_ReferencesAreEqual_ReturnsZero() {
        // Arrange
        Object mock = new Object();
        Object argument = mock;
        Object invocationMock = getInvocationMock(mock, argument);
        
        // Act
        int actual = (int)returnsEmptyValues.answer(invocationMock);
        
        // Assert
        assertEquals(0, actual);
    }
    
    @Test
    public void testAnswer_CompareToMethod_ReferencesAreNotEqual_ReturnsNonZero() {
        // Arrange
        Object mock = new Object();
        Object argument = new Object();
        Object invocationMock = getInvocationMock(mock, argument);
        
        // Act
        int actual = (int)returnsEmptyValues.answer(invocationMock);
        
        // Assert
        assertNotEquals(0, actual);
    }
    
    @Test
    public void testAnswer_ReturnsEmptyCollection() {
        // Arrange
        Object invocationMock = getInvocationMock();
        
        // Act
        Object actual = returnsEmptyValues.answer(invocationMock);
        
        // Assert
        assertTrue(actual instanceof Collection);
        assertTrue(((Collection)actual).isEmpty());
    }
    
    @Test
    public void testAnswer_ReturnsEmptySet() {
        // Arrange
        Object invocationMock = getInvocationMock();
        
        // Act
        Object actual = returnsEmptyValues.answer(invocationMock);
        
        // Assert
        assertTrue(actual instanceof Set);
        assertTrue(((Set)actual).isEmpty());
    }
    
    @Test
    public void testAnswer_ReturnsEmptyHashSet() {
        // Arrange
        Object invocationMock = getInvocationMock();
        
        // Act
        Object actual = returnsEmptyValues.answer(invocationMock);
        
        // Assert
        assertTrue(actual instanceof HashSet);
        assertTrue(((HashSet)actual).isEmpty());
    }
    
    @Test
    public void testAnswer_ReturnsEmptySortedSet() {
        // Arrange
        Object invocationMock = getInvocationMock();
        
        // Act
        Object actual = returnsEmptyValues.answer(invocationMock);
        
        // Assert
        assertTrue(actual instanceof SortedSet);
        assertTrue(((SortedSet)actual).isEmpty());
    }
    
    @Test
    public void testAnswer_ReturnsEmptyTreeSet() {
        // Arrange
        Object invocationMock = getInvocationMock();
        
        // Act
        Object actual = returnsEmptyValues.answer(invocationMock);
        
        // Assert
        assertTrue(actual instanceof TreeSet);
        assertTrue(((TreeSet)actual).isEmpty());
    }
    
    @Test
    public void testAnswer_ReturnsEmptyLinkedHashSet() {
        // Arrange
        Object invocationMock = getInvocationMock();
        
        // Act
        Object actual = returnsEmptyValues.answer(invocationMock);
        
        // Assert
        assertTrue(actual instanceof LinkedHashSet);
        assertTrue(((LinkedHashSet)actual).isEmpty());
    }
    
    @Test
    public void testAnswer_ReturnsEmptyList() {
        // Arrange
        Object invocationMock = getInvocationMock();
        
        // Act
        Object actual = returnsEmptyValues.answer(invocationMock);
        
        // Assert
        assertTrue(actual instanceof List);
        assertTrue(((List)actual).isEmpty());
    }
    
    @Test
    public void testAnswer_ReturnsEmptyLinkedList() {
        // Arrange
        Object invocationMock = getInvocationMock();
        
        // Act
        Object actual = returnsEmptyValues.answer(invocationMock);
        
        // Assert
        assertTrue(actual instanceof LinkedList);
        assertTrue(((LinkedList)actual).isEmpty());
    }
    
    @Test
    public void testAnswer_ReturnsEmptyArrayList() {
        // Arrange
        Object invocationMock = getInvocationMock();
        
        // Act
        Object actual = returnsEmptyValues.answer(invocationMock);
        
        // Assert
        assertTrue(actual instanceof ArrayList);
        assertTrue(((ArrayList)actual).isEmpty());
    }
    
    @Test
    public void testAnswer_ReturnsEmptyMap() {
        // Arrange
        Object invocationMock = getInvocationMock();
        
        // Act
        Object actual = returnsEmptyValues.answer(invocationMock);
        
        // Assert
        assertTrue(actual instanceof Map);
        assertTrue(((Map)actual).isEmpty());
    }
    
    @Test
    public void testAnswer_ReturnsEmptyHashMap() {
        // Arrange
        Object invocationMock = getInvocationMock();
        
        // Act
        Object actual = returnsEmptyValues.answer(invocationMock);
        
        // Assert
        assertTrue(actual instanceof HashMap);
        assertTrue(((HashMap)actual).isEmpty());
    }
    
    @Test
    public void testAnswer_ReturnsEmptySortedMap() {
        // Arrange
        Object invocationMock = getInvocationMock();
        
        // Act
        Object actual = returnsEmptyValues.answer(invocationMock);
        
        // Assert
        assertTrue(actual instanceof SortedMap);
        assertTrue(((SortedMap)actual).isEmpty());
    }
    
    @Test
    public void testAnswer_ReturnsEmptyTreeMap() {
        // Arrange
        Object invocationMock = getInvocationMock();
        
        // Act
        Object actual = returnsEmptyValues.answer(invocationMock);
        
        // Assert
        assertTrue(actual instanceof TreeMap);
        assertTrue(((TreeMap)actual).isEmpty());
    }
    
    @Test
    public void testAnswer_ReturnsEmptyLinkedHashMap() {
        // Arrange
        Object invocationMock = getInvocationMock();
        
        // Act
        Object actual = returnsEmptyValues.answer(invocationMock);
        
        // Assert
        assertTrue(actual instanceof LinkedHashMap);
        assertTrue(((LinkedHashMap)actual).isEmpty());
    }
    
    // Helper methods for creating test setup
    
    private InvocationOnMock getInvocationMock() {
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(getMockitoMethod());
        return invocationMock;
    }
    
    private InvocationOnMock getInvocationMock(Object mock, Object argument) {
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(getCompareToMethod());
        when(invocationMock.getMock()).thenReturn(mock);
        when(invocationMock.getArguments()).thenReturn(new Object[]{argument});
        return invocationMock;
    }
    
    private Method getMockitoMethod() throws NoSuchMethodException {
        return Mockito.class.getMethod("toString");
    }
    
    private Method getCompareToMethod() throws NoSuchMethodException {
        return Comparable.class.getMethod("compareTo", Object.class);
    }
}