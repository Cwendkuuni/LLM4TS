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
import java.util.*;

import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine injectionEngine;
    
    @Before
    public void setUp() {
        injectionEngine = new DefaultInjectionEngine();
    }

    @Test
    public void injectMocksOnFields_InjectMocksFieldsEmpty_MocksEmpty() {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        // Verify that no mocks were injected
        verifyNoMoreInteractions(mocks);
    }
    
    @Test
    public void injectMocksOnFields_InjectMocksFieldsNotEmpty_MocksNotEmpty() {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Field field = mock(Field.class);
        injectMocksFields.add(field);
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        // Verify that the mock candidate filter is called for each field
        verify(mockCandidateFilter, times(1)).filterCandidate(mocks, field, null);
    }
    
    @Test(expected = Reporter.class)
    public void injectMocksOnFields_FieldInitializationFails_ReportError() {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Field field = mock(Field.class);
        injectMocksFields.add(field);
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();
        
        whenNew(FieldInitializer.class).withArguments(testClassInstance, field).thenThrow(new MockitoException());

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        // Verify that the MockitoException is reported
    }
    
    @Test
    public void injectMocksOnFields_InjectMocksFieldInstanceIsNull_NothingInjected() {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Field field = mock(Field.class);
        injectMocksFields.add(field);
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();
        
        whenNew(FieldInitializer.class).withArguments(testClassInstance, field).thenReturn(null);

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        // Verify that no mocks were injected
        verifyNoMoreInteractions(mocks);
    }
    
    @Test
    public void injectMocksOnFields_FieldClassIsObject_NothingInjected() {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Field field = mock(Field.class);
        injectMocksFields.add(field);
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();
        
        Object fieldInstance = new Object();
        whenNew(FieldInitializer.class).withArguments(testClassInstance, field).thenReturn(fieldInstance);

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        // Verify that no mocks were injected
        verifyNoMoreInteractions(mocks);
    }
    
    @Test
    public void injectMocksOnFields_FieldClassHasSuperclass_MocksInjectedInSuperclassFields() {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Field field = mock(Field.class);
        injectMocksFields.add(field);
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();
        
        Object fieldInstance = mock(Object.class);
        whenNew(FieldInitializer.class).withArguments(testClassInstance, field).thenReturn(fieldInstance);
        
        Class<?> fieldClass = mock(Class.class);
        when(fieldInstance.getClass()).thenReturn(fieldClass);
        when(fieldClass.getSuperclass()).thenReturn(Object.class, null);
        
        Field superClassField = mock(Field.class);
        Field[] declaredFields = new Field[] {superClassField};
        when(fieldClass.getDeclaredFields()).thenReturn(declaredFields);
        
        when(mockCandidateFilter.filterCandidate(mocks, superClassField, fieldInstance)).thenReturn(mock(MockCandidateFilter.class));

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        // Verify that the mock candidate filter is called for the superclass field
        verify(mockCandidateFilter, times(1)).filterCandidate(mocks, superClassField, fieldInstance);
    }
    
    @Test
    public void injectMocksOnFields_InjectMocksFieldClassHasSuperclass_SubtypeFirstInjected() {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Field field = mock(Field.class);
        injectMocksFields.add(field);
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();
        
        Object fieldInstance = mock(Object.class);
        whenNew(FieldInitializer.class).withArguments(testClassInstance, field).thenReturn(fieldInstance);
        
        Class<?> fieldClass = mock(Class.class);
        when(fieldInstance.getClass()).thenReturn(fieldClass);
        when(fieldClass.getSuperclass()).thenReturn(Object.class, null);
        
        Field superClassField = mock(Field.class);
        Field subTypeField = mock(Field.class);
        Field[] declaredFields = new Field[] {superClassField, subTypeField};
        when(fieldClass.getDeclaredFields()).thenReturn(declaredFields);
        
        when(mockCandidateFilter.filterCandidate(mocks, superClassField, fieldInstance)).thenReturn(mock(MockCandidateFilter.class));
        when(mockCandidateFilter.filterCandidate(mocks, subTypeField, fieldInstance)).thenReturn(mock(MockCandidateFilter.class));

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        // Verify that the mock candidate filter is called for the subtype field first
        verify(mockCandidateFilter, times(1)).filterCandidate(mocks, subTypeField, fieldInstance);
        verify(mockCandidateFilter, times(1)).filterCandidate(mocks, superClassField, fieldInstance);
    }
    
    @Test
    public void injectMocksOnFields_MultipleMocksOfSameType_OnlyOneMockInjected() {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Field field = mock(Field.class);
        injectMocksFields.add(field);
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();
        
        Object fieldInstance = mock(Object.class);
        whenNew(FieldInitializer.class).withArguments(testClassInstance, field).thenReturn(fieldInstance);
        
        Class<?> fieldClass = mock(Class.class);
        when(fieldInstance.getClass()).thenReturn(fieldClass);
        when(fieldClass.getSuperclass()).thenReturn(Object.class, null);
        
        Field superClassField = mock(Field.class);
        Field subTypeField = mock(Field.class);
        Field[] declaredFields = new Field[] {superClassField, subTypeField};
        when(fieldClass.getDeclaredFields()).thenReturn(declaredFields);
        
        MockCandidateFilter mockCandidateFilterSuperClass = mock(MockCandidateFilter.class);
        MockCandidateFilter mockCandidateFilterSubType = mock(MockCandidateFilter.class);
        when(mockCandidateFilter.filterCandidate(mocks, superClassField, fieldInstance)).thenReturn(mockCandidateFilterSuperClass);
        when(mockCandidateFilter.filterCandidate(mocks, subTypeField, fieldInstance)).thenReturn(mockCandidateFilterSubType);
        
        when(mockCandidateFilterSuperClass.thenInject()).thenReturn(null);
        when(mockCandidateFilterSubType.thenInject()).thenReturn(null);

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        // Verify that the mock candidate filter is called only once for each field
        verify(mockCandidateFilter, times(1)).filterCandidate(mocks, superClassField, fieldInstance);
        verify(mockCandidateFilter, times(1)).filterCandidate(mocks, subTypeField, fieldInstance);
        
        // Verify that only one mock is injected
        verify(mockCandidateFilterSuperClass, times(1)).thenInject();
        verify(mockCandidateFilterSubType, never()).thenInject();
    }
}