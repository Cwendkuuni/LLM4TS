
import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    @Test
    public void createMockFor_WithMockAnnotation_ShouldReturnNull() {
        // Arrange
        SpyAnnotationEngine spy = new SpyAnnotationEngine();
        Mock mockAnnotation = mock(Mock.class);
        Field field = getFieldByName("mockField");
        
        // Act
        Object result = spy.createMockFor(mockAnnotation, field);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void process_WithSpyAnnotationAndNullInstance_ShouldThrowMockitoException() {
        // Arrange
        SpyAnnotationEngine spy = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();
        
        // Act & Assert
        assertThrows(MockitoException.class, () -> spy.process(context, testClass));
    }
    
    @Test
    public void process_WithSpyAnnotationAndExistingMock_ShouldResetMock() throws IllegalAccessException {
        // Arrange
        SpyAnnotationEngine spy = new SpyAnnotationEngine();
        MockUtil mockUtilMock = mock(MockUtil.class);
        Field field = getFieldByName("mockField");
        Object instance = new Object();
        field.set(instance, Mockito.mock(List.class));
        
        // Mock the MockUtil class behavior
        when(mockUtilMock.isMock(any())).thenReturn(true);
        
        // Replace the MockUtil instance in the SpyAnnotationEngine class with the mock
        setFieldValue(spy, "mockUtil", mockUtilMock);
        
        // Act
        spy.process(SpyAnnotationEngine.class, instance);
        
        // Assert
        verify(mockUtilMock, times(1)).isMock(any());
        verify(field.get(instance), times(1)).reset();
    }
    
    @Test
    public void process_WithSpyAnnotationAndNonMockInstance_ShouldSpyInstance() throws IllegalAccessException {
        // Arrange
        SpyAnnotationEngine spy = new SpyAnnotationEngine();
        MockUtil mockUtilMock = mock(MockUtil.class);
        Field field = getFieldByName("mockField");
        Object instance = new Object();
        field.set(instance, new LinkedList<>());
        
        // Mock the MockUtil class behavior
        when(mockUtilMock.isMock(any())).thenReturn(false);
        
        // Replace the MockUtil instance in the SpyAnnotationEngine class with the mock
        setFieldValue(spy, "mockUtil", mockUtilMock);
        
        // Act
        spy.process(SpyAnnotationEngine.class, instance);
        
        // Assert
        verify(mockUtilMock, times(1)).isMock(any());
        verify(field, times(1)).set(eq(instance), any());
    }

    @Test
    public void process_WithAccessibleField_ShouldSetAccessibleToTrue() throws IllegalAccessException {
        // Arrange
        SpyAnnotationEngine spy = new SpyAnnotationEngine();
        Field field = getFieldByName("mockField");
        Boolean wasAccessible = field.isAccessible();
        Object instance = new Object();
        field.set(instance, new LinkedList<>());

        // Act
        spy.process(SpyAnnotationEngine.class, instance);

        // Assert
        assertTrue(field.isAccessible());

        // Reset field accessibility
        field.setAccessible(wasAccessible);
    }

    @Test
    public void process_WithInaccessibleField_ShouldSetAccessibleToTrueAfterProcessing() throws IllegalAccessException {
        // Arrange
        SpyAnnotationEngine spy = new SpyAnnotationEngine();
        Field field = getFieldByName("mockField");
        Boolean wasAccessible = field.isAccessible();
        field.setAccessible(false);
        Object instance = new Object();
        field.set(instance, new LinkedList<>());

        // Act
        spy.process(SpyAnnotationEngine.class, instance);

        // Assert
        assertTrue(field.isAccessible());

        // Reset field accessibility
        field.setAccessible(wasAccessible);
    }

    @Test
    public void process_WithUnsupportedCombinationOfAnnotations_ShouldThrowMockitoException() throws IllegalAccessException {
        // Arrange
        SpyAnnotationEngine spy = new SpyAnnotationEngine();
        Field field = getFieldByName("mockField");
        Object instance = new Object();
        field.set(instance, new LinkedList<>());
        Captor captorAnnotation = mock(Captor.class);
        field.setAnnotation(captorAnnotation);

        // Act & Assert
        assertThrows(MockitoException.class, () -> spy.process(SpyAnnotationEngine.class, instance));
    }

    private Field getFieldByName(String fieldName) {
        try {
            return SpyAnnotationEngine.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setFieldValue(Object obj, String fieldName, Object fieldValue) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, fieldValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
