import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.stubbing.Answer;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.*;

public class ReturnsEmptyValuesTest {
    
    private ReturnsEmptyValues returnsEmptyValues;
    
    @Before
    public void setup() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }
    
    @Test
    public void testAnswerToString_DefaultName() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[0]);
        Object mock = mock(Object.class);
        MockName name = mock(MockName.class);
        when(name.isDefault()).thenReturn(true);
        when(name.toString()).thenReturn("Mock for Object, hashCode: " + mock.hashCode());
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockName(mock)).thenReturn(name);
        when(mockUtil.getMockSettings(mock).getTypeToMock()).thenReturn(Object.class);
        returnsEmptyValues.methodsGuru = mock(ObjectMethodsGuru.class);
        returnsEmptyValues.mockUtil = mockUtil;
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertEquals("Mock for Object, hashCode: " + mock.hashCode(), result);
        verify(mockUtil).getMockName(mock);
        verify(mockUtil).getMockSettings(mock);
    }
    
    @Test
    public void testAnswerToString_CustomName() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[0]);
        Object mock = mock(Object.class);
        MockName name = mock(MockName.class);
        when(name.isDefault()).thenReturn(false);
        when(name.toString()).thenReturn("CustomName");
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockName(mock)).thenReturn(name);
        when(mockUtil.getMockSettings(mock).getTypeToMock()).thenReturn(Object.class);
        returnsEmptyValues.methodsGuru = mock(ObjectMethodsGuru.class);
        returnsEmptyValues.mockUtil = mockUtil;
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertEquals("CustomName", result);
        verify(mockUtil).getMockName(mock);
        verify(mockUtil).getMockSettings(mock);
    }
    
    @Test
    public void testAnswerCompareToMethod() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[1]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void testAnswerPrimitiveReturnType() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[2]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test
    public void testAnswerPrimitiveWrapperReturnType() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[3]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test
    public void testAnswerCollectionReturnType() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[4]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertEquals(new LinkedList<Object>(), result);
    }
    
    @Test
    public void testAnswerSetReturnType() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[5]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertEquals(new HashSet<Object>(), result);
    }
    
    @Test
    public void testAnswerHashSetReturnType() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[6]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertEquals(new HashSet<Object>(), result);
    }
    
    @Test
    public void testAnswerSortedSetReturnType() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[7]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertEquals(new TreeSet<Object>(), result);
    }
    
    @Test
    public void testAnswerTreeSetReturnType() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[8]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertEquals(new TreeSet<Object>(), result);
    }
    
    @Test
    public void testAnswerLinkedHashSetReturnType() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[9]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertEquals(new LinkedHashSet<Object>(), result);
    }
    
    @Test
    public void testAnswerListReturnType() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[10]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertEquals(new LinkedList<Object>(), result);
    }
    
    @Test
    public void testAnswerLinkedListReturnType() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[11]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertEquals(new LinkedList<Object>(), result);
    }
    
    @Test
    public void testAnswerArrayListReturnType() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[12]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertEquals(new ArrayList<Object>(), result);
    }
    
    @Test
    public void testAnswerMapReturnType() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[13]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertEquals(new HashMap<Object, Object>(), result);
    }
    
    @Test
    public void testAnswerHashMapReturnType() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[14]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertEquals(new HashMap<Object, Object>(), result);
    }
    
    @Test
    public void testAnswerSortedMapReturnType() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[15]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertEquals(new TreeMap<Object, Object>(), result);
    }
    
    @Test
    public void testAnswerTreeMapReturnType() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[16]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertEquals(new TreeMap<Object, Object>(), result);
    }
    
    @Test
    public void testAnswerLinkedHashMapReturnType() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[17]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertEquals(new LinkedHashMap<Object, Object>(), result);
    }
    
    @Test
    public void testAnswerOtherReturnType() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[18]);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertEquals(null, result);
    }
}