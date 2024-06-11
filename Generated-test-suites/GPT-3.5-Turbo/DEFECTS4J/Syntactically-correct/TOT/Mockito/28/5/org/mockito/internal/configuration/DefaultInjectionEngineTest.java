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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {
    
    private DefaultInjectionEngine defaultInjectionEngine;
    private MockCandidateFilter mockCandidateFilter;
    private Comparator<Field> supertypesLast;
    
    @Before
    public void setup() {
        defaultInjectionEngine = new DefaultInjectionEngine();
        mockCandidateFilter = mock(MockCandidateFilter.class);
        supertypesLast = mock(Comparator.class);
        defaultInjectionEngine.mockCandidateFilter = mockCandidateFilter;
        defaultInjectionEngine.supertypesLast = supertypesLast;
    }
    
    @Test
    public void testInjectMocksOnFields_validMocksAndInstance_successfullyInjectMocks() {
        // Arrange
        Field field = mock(Field.class);
        Object injectMocksFieldInstance = new Object();
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(field);
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(Object.class));
        Object testClassInstance = new Object();
        try {
            whenNew(FieldInitializer.class).withArguments(testClassInstance, field)
                    .thenReturn(mock(FieldInitializer.class));
            when(mockCandidateFilter.filterCandidate(anySet(), any(Field.class), any(Object.class)))
                    .thenReturn(mock(MockCandidateFilter.class));
    
            // Act
            defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

            // Assert
            verify(mockCandidateFilter, times(1)).filterCandidate(anySet(), any(Field.class), any(Object.class));
    
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }
    
    @Test
    public void testInjectMocksOnFields_exceptionWhenInitializingField_throwException() {
        // Arrange
        Field field = mock(Field.class);
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(field);
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(Object.class));
        Object testClassInstance = new Object();
        
        try {
            whenNew(FieldInitializer.class).withArguments(testClassInstance, field)
                    .thenThrow(new MockitoException("MockitoException"));
    
            // Act
            defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
    
            // Assert
            fail("Should throw exception");
        } catch (Exception e) {
            assertEquals("MockitoException", e.getMessage());
        }
    }
    
    @Test
    public void testInjectMocksOnFields_emptyInjectMocksFields_noMocksInjected() {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(Object.class));
        Object testClassInstance = new Object();
        
        // Act
        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        
        // Assert
        verify(mockCandidateFilter, never()).filterCandidate(anySet(), any(Field.class), any(Object.class));
    }
    
    @Test
    public void testInjectMocksOnFields_injectMockCandidate_filterCandidatesAndInject() {
        // Arrange
        Class<?> awaitingInjectionClazz = mock(Class.class);
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(Object.class));
        Object fieldInstance = mock(Object.class);
        Field field1 = mock(Field.class);
        Field field2 = mock(Field.class);
        Field[] orderedFields = new Field[]{field1, field2};
        when(supertypesLast.compare(any(Field.class), any(Field.class))).thenReturn(-1);
        when(awaitingInjectionClazz.getDeclaredFields()).thenReturn(orderedFields);
        when(mockCandidateFilter.filterCandidate(anySet(), any(Field.class), any(Object.class)))
                .thenReturn(mock(MockCandidateFilter.class));
        
        // Act
        defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);
        
        // Assert
        verify(mockCandidateFilter, times(1)).filterCandidate(anySet(), eq(field1), any(Object.class));
        verify(mockCandidateFilter, times(1)).filterCandidate(anySet(), eq(field2), any(Object.class));
        verify(mock(MockCandidateFilter.class), times(2)).thenInject();
    }
    
    @Test
    public void testInjectMocksOnFields_orderedInstanceFieldsFrom_returnOrderedFields() {
        // Arrange
        Class<?> awaitingInjectionClazz = mock(Class.class);
        Field field1 = mock(Field.class);
        Field field2 = mock(Field.class);
        Field field3 = mock(Field.class);
        Field[] declaredFields = new Field[]{field1, field2, field3};
        when(awaitingInjectionClazz.getDeclaredFields()).thenReturn(declaredFields);
        
        // Act
        Field[] orderedFields = defaultInjectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);
        
        // Assert
        assertEquals(field3, orderedFields[0]);
        assertEquals(field2, orderedFields[1]);
        assertEquals(field1, orderedFields[2]);
    }
}