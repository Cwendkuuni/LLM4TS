import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class SpyAnnotationEngineTest {
    
    SpyAnnotationEngine engine = new SpyAnnotationEngine();
    
    @Test
    public void testCreateMockFor_WhenAnnotationIsSpyAndFieldIsNull_ReturnsNull() {
        // Arrange
        Spy annotation = mock(Spy.class);
        Field field = mock(Field.class);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.get(any())).thenReturn(null);
        
        // Act
        Object result = engine.createMockFor(annotation, field);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testCreateMockFor_WhenAnnotationIsMock_ReturnsNull() {
        // Arrange
        Mock annotation = mock(Mock.class);
        Field field = mock(Field.class);
        
        // Act
        Object result = engine.createMockFor(annotation, field);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testCreateMockFor_WhenAnnotationIsMockitoAnnotationsMock_ReturnsNull() {
        // Arrange
        org.mockito.MockitoAnnotations.Mock annotation = 
            mock(org.mockito.MockitoAnnotations.Mock.class);
        Field field = mock(Field.class);
        
        // Act
        Object result = engine.createMockFor(annotation, field);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testCreateMockFor_WhenAnnotationIsCaptor_ReturnsNull() {
        // Arrange
        Captor annotation = mock(Captor.class);
        Field field = mock(Field.class);
        
        // Act
        Object result = engine.createMockFor(annotation, field);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testProcess_WhenFieldHasSpyAnnotationAndInstanceIsNull_ThrowsException() {
        // Arrange
        Class<?> context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field[] fields = new Field[1];
        Field field = mock(Field.class);
        fields[0] = field;
        when(context.getDeclaredFields()).thenReturn(fields);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.getName()).thenReturn("testField");
        when(field.get(any())).thenReturn(null);
        
        // Act & Assert
        try {
            engine.process(context, testClass);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException ex) {
            assertEquals("Cannot create a @Spy for 'testField' field because the *instance* is missing\n" +
            		 "The instance must be created *before* initMocks();\n" +
                     "Example of correct usage of @Spy:\n" +
                     "   @Spy List mock = new LinkedList();\n" +
                     "   //also, don't forget about MockitoAnnotations.initMocks();", ex.getMessage());
        }
    }
    
    @Test
    public void testProcess_WhenFieldHasSpyAnnotationAndInstanceIsMock_ResetsTheMock() throws IllegalAccessException {
        // Arrange
        Class<?> context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field[] fields = new Field[1];
        Field field = mock(Field.class);
        fields[0] = field;
        when(context.getDeclaredFields()).thenReturn(fields);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.get(any())).thenReturn(mock(List.class));
        
        // Act
        engine.process(context, testClass);
        
        // Assert
        verify(field, times(1)).setAccessible(true);
        verify(field, times(1)).set(any(), any());
        verify(field, times(1)).setAccessible(false);
        verifyZeroInteractions(Mockito.class);
    }
    
    @Test
    public void testProcess_WhenFieldHasSpyAnnotationAndInstanceIsNotMock_SpiesTheInstance() throws IllegalAccessException {
        // Arrange
        Class<?> context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field[] fields = new Field[1];
        Field field = mock(Field.class);
        fields[0] = field;
        when(context.getDeclaredFields()).thenReturn(fields);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        Object instance = new ArrayList();
        when(field.get(any())).thenReturn(instance);
        
        // Act
        engine.process(context, testClass);
        
        // Assert
        verify(field, times(1)).setAccessible(true);
        verify(field, times(1)).set(any(), any());
        verify(field, times(1)).setAccessible(false);
        verifyZeroInteractions(Mockito.class);
    }
    
    @Test
    public void testProcess_WhenFieldDoesNotHaveSpyAnnotation_DoesNothing() {
        // Arrange
        Class<?> context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field[] fields = new Field[1];
        Field field = mock(Field.class);
        fields[0] = field;
        when(context.getDeclaredFields()).thenReturn(fields);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(false);
        
        // Act
        engine.process(context, testClass);
        
        // Assert
        verifyZeroInteractions(field);
        verifyZeroInteractions(Mockito.class);
    }
    
    @Test
    public void testProcess_WhenFieldHasSpyAnnotationAndFieldIsInaccessible_ThrowsException() {
        // Arrange
        Class<?> context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field[] fields = new Field[1];
        Field field = mock(Field.class);
        fields[0] = field;
        when(context.getDeclaredFields()).thenReturn(fields);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.isAccessible()).thenReturn(false);
        
        // Act & Assert
        try {
            engine.process(context, testClass);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException ex) {
            assertEquals("Problems initiating spied field " + field.getName(), ex.getMessage());
        }
    }
    
    @Test
    public void testProcess_WhenFieldHasSpyAnnotationAndFieldIsAccessible_CallsResetOnMockito() 
            throws IllegalAccessException {
        // Arrange
        Class<?> context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field[] fields = new Field[1];
        Field field = mock(Field.class);
        fields[0] = field;
        when(context.getDeclaredFields()).thenReturn(fields);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.isAccessible()).thenReturn(true);
        Object instance = mock(List.class);
        when(field.get(any())).thenReturn(instance);
        
        // Act
        engine.process(context, testClass);
        
        // Assert
        verify(field, times(1)).setAccessible(true);
        verify(field, times(1)).get(testClass);
        verify(Mockito, times(1)).reset(instance);
        verify(field, times(1)).setAccessible(false);
    }
    
    @Test
    public void testAssertNoAnnotations_WhenFieldHasUndesiredAnnotation_ThrowsException() {
        // Arrange
        Field field = mock(Field.class);
        when(field.isAnnotationPresent(Mock.class)).thenReturn(true);
        
        // Act & Assert
        try {
            engine.assertNoAnnotations(Spy.class, field, Mock.class);
            fail("Expected Reporter.unsupportedCombinationOfAnnotations to be called");
        } catch (Exception ex) {
            // Verify that the correct method was called
            verify(new Reporter(), times(1)).unsupportedCombinationOfAnnotations(eq("Spy"), eq("Spy"));
        }
    }
    
    @Test
    public void testAssertNoAnnotations_WhenFieldDoesNotHaveUndesiredAnnotation_DoesNothing() {
        // Arrange
        Field field = mock(Field.class);
        
        // Act
        engine.assertNoAnnotations(Spy.class, field, Mock.class);
        
        // Assert
        verifyZeroInteractions(new Reporter());
    }
    
}