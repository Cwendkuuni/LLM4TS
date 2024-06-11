import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {
    
    private DefaultInjectionEngine defaultInjectionEngine;
    
    @Before
    public void setup() {
        defaultInjectionEngine = new DefaultInjectionEngine();
    }
    
    @Test
    public void testInjectMocksOnFields() {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();
        
        Field field1 = mock(Field.class);
        injectMocksFields.add(field1);
        
        Field field2 = mock(Field.class);
        injectMocksFields.add(field2);
        
        Field field3 = mock(Field.class);
        injectMocksFields.add(field3);
        
        FieldInitializer fieldInitializer = mock(FieldInitializer.class);
        when(fieldInitializer.initialize()).thenReturn(null);
        
        doThrow(new MockitoException()).when(new Reporter()).cannotInitializeForInjectMocksAnnotation(anyString(), any(MockitoException.class));
        
        Field[] declaredFields = {field1, field2, field3};
        when(field1.getType()).thenReturn(Object.class);
        when(field2.getType()).thenReturn(Object.class);
        when(field3.getType()).thenReturn(Object.class);
        
        when(field3.getSuperclass()).thenReturn(Object.class);
        when(field2.getSuperclass()).thenReturn(Object.class);
        when(field1.getSuperclass()).thenReturn(Object.class);
        
        Comparator<Field> supertypesLastComparator = mock(Comparator.class);
        when(supertypesLastComparator.compare(any(Field.class), any(Field.class))).thenReturn(1);
        
        when(defaultInjectionEngine.orderedInstanceFieldsFrom(any(Class.class))).thenReturn(declaredFields);
        when(defaultInjectionEngine.injectMockCandidate(any(Class.class), any(Set.class), any(Object.class))).thenCallRealMethod();
        
        // Act
        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        
        // Assert
        verify(fieldInitializer).initialize();
        verify(field3).getSuperclass();
        verify(field2).getSuperclass();
        verify(field1).getSuperclass();
        verify(defaultInjectionEngine).orderedInstanceFieldsFrom(Object.class);
        verify(defaultInjectionEngine).injectMockCandidate(Object.class, mocks, null);
    }
    
    @Test
    public void testInjectMockCandidate() throws Exception {
        // Arrange
        Class<?> awaitingInjectionClazz = Object.class;
        Set<Object> mocks = new HashSet<>();
        Object fieldInstance = new Object();
        
        Field field = mock(Field.class);
        when(field.getType()).thenReturn(Object.class);
        
        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
        when(mockCandidateFilter.filterCandidate(any(Set.class), any(Field.class), any(Object.class))).thenReturn(mockCandidateFilter);
        when(mockCandidateFilter.thenInject()).thenReturn(mockCandidateFilter);
        
        // Act
        defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);
        
        // Assert
        verify(mockCandidateFilter).filterCandidate(mocks, field, fieldInstance);
        verify(mockCandidateFilter).thenInject();
    }
    
    @Test
    public void testOrderedInstanceFieldsFrom() throws Exception {
        // Arrange
        Class<?> awaitingInjectionClazz = Object.class;
        
        Field field1 = mock(Field.class);
        Field field2 = mock(Field.class);
        Field field3 = mock(Field.class);
        
        Field[] declaredFields = {field1, field2, field3};
        
        Comparator<Field> supertypesLastComparator = mock(Comparator.class);
        
        // Act
        Field[] result = defaultInjectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);
        
        // Assert
        assertEquals(3, result.length);
        assertEquals(field1, result[0]);
        assertEquals(field2, result[1]);
        assertEquals(field3, result[2]);
        verify(supertypesLastComparator).compare(field1, field2);
        verify(supertypesLastComparator).compare(field2, field3);
        verify(supertypesLastComparator).compare(field1, field3);
    }
}