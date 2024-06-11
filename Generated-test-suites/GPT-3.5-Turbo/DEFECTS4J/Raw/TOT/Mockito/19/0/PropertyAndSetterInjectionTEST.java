import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.MockInjectionStrategy;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class PropertyAndSetterInjectionTest {

    private MockCandidateFilter mockCandidateFilter;
    private PropertyAndSetterInjection propertyAndSetterInjection;

    @Before
    public void setup() {
        mockCandidateFilter = mock(MockCandidateFilter.class);
        propertyAndSetterInjection = new PropertyAndSetterInjection();
        propertyAndSetterInjection.setMockCandidateFilter(mockCandidateFilter);
    }

    @Test
    public void testProcessInjection_InjectableField_InjectsMockCandidates() throws NoSuchFieldException {
        // Arrange
        Field injectMocksField = PropertyAndSetterInjection.class.getDeclaredField("mockCandidates");
        Object injectMocksFieldOwner = new Object();
        Set<Object> mockCandidates = new HashSet<>();

        FieldInitializationReport report = mock(FieldInitializationReport.class);
        when(report.fieldClass()).thenReturn(MockInjectionStrategy.class);
        when(report.fieldInstance()).thenReturn(new Object());

        when(mockCandidateFilter.filterCandidate(anySet(), any(Field.class), any(Object.class)))
                .thenReturn(mock(Object.class));

        // Act
        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Assert
        assertTrue(result);
        verify(mockCandidateFilter, times(2)).filterCandidate(anySet(), any(Field.class), any(Object.class));
    }

    @Test
    public void testProcessInjection_UninjectableField_DoesNotInjectMockCandidates() throws NoSuchFieldException {
        // Arrange
        Field injectMocksField = PropertyAndSetterInjection.class.getDeclaredField("mockCandidates");
        Object injectMocksFieldOwner = new Object();
        Set<Object> mockCandidates = new HashSet<>();

        FieldInitializationReport report = mock(FieldInitializationReport.class);
        when(report.fieldClass()).thenReturn(Object.class);
        when(report.fieldInstance()).thenReturn(new Object());

        when(mockCandidateFilter.filterCandidate(anySet(), any(Field.class), any(Object.class)))
                .thenReturn(mock(Object.class));

        // Act
        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Assert
        assertFalse(result);
        verify(mockCandidateFilter, never()).filterCandidate(anySet(), any(Field.class), any(Object.class));
    }

    @Test
    public void testInitializeInjectMocksField_FieldInitializationException_ReporterCalled() throws NoSuchFieldException {
        // Arrange
        Field injectMocksField = PropertyAndSetterInjection.class.getDeclaredField("mockCandidates");
        Object injectMocksFieldOwner = new Object();

        FieldInitializationReport report = mock(FieldInitializationReport.class);
        when(report.fieldClass()).thenReturn(MockInjectionStrategy.class);
        when(report.fieldInstance()).thenReturn(new Object());

        MockitoException exception = new MockitoException("Exception message");
        InvocationTargetException cause = new InvocationTargetException(exception);
        when(mock(FieldInitializer.class).initialize())
                .thenThrow(new MockitoException("Exception message", cause));

        Reporter reporter = mock(Reporter.class);

        // Act
        propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);

        // Assert
        verify(reporter).fieldInitialisationThrewException(eq(injectMocksField), eq(exception));
    }

    @Test
    public void testInitializeInjectMocksField_OtherException_ReporterCalled() throws NoSuchFieldException {
        // Arrange
        Field injectMocksField = PropertyAndSetterInjection.class.getDeclaredField("mockCandidates");
        Object injectMocksFieldOwner = new Object();

        FieldInitializationReport report = mock(FieldInitializationReport.class);
        when(report.fieldClass()).thenReturn(MockInjectionStrategy.class);
        when(report.fieldInstance()).thenReturn(new Object());

        RuntimeException exception = new RuntimeException("Exception message");
        when(mock(FieldInitializer.class).initialize())
                .thenThrow(exception);

        Reporter reporter = mock(Reporter.class);

        // Act
        propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);

        // Assert
        verify(reporter).cannotInitializeForInjectMocksAnnotation(eq(injectMocksField.getName()), eq(exception));
    }

    @Test
    public void testInjectMockCandidates_OnePass_CandidateInjectedBySetter() {
        // Arrange
        Set<Object> mocks = new HashSet<>();
        Object instance = new Object();
        List<Field> orderedInstanceFields = Collections.singletonList(mock(Field.class));

        when(mockCandidateFilter.filterCandidate(anySet(), any(Field.class), any(Object.class)))
                .thenReturn(mock(Object.class));

        // Act
        boolean result = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);

        // Assert
        assertTrue(result);
        assertTrue(mocks.isEmpty());
    }

    @Test
    public void testInjectMockCandidates_OnePass_CandidateInjectedByField() {
        // Arrange
        Set<Object> mocks = new HashSet<>();
        Object instance = new Object();
        List<Field> orderedInstanceFields = Collections.singletonList(mock(Field.class));

        when(mockCandidateFilter.filterCandidate(anySet(), any(Field.class), any(Object.class)))
                .thenReturn(null);

        // Act
        boolean result = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);

        // Assert
        assertTrue(result);
        assertTrue(mocks.isEmpty());
    }

    @Test
    public void testInjectMockCandidates_OnePass_NoCandidateToInject() {
        // Arrange
        Set<Object> mocks = new HashSet<>();
        Object instance = new Object();
        List<Field> orderedInstanceFields = Collections.singletonList(mock(Field.class));

        when(mockCandidateFilter.filterCandidate(anySet(), any(Field.class), any(Object.class)))
                .thenReturn(null);

        // Act
        boolean result = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);

        // Assert
        assertFalse(result);
        assertFalse(mocks.isEmpty());
    }

    @Test
    public void testInjectMockCandidates_TwoPasses_CandidateInjectedBySetter() {
        // Arrange
        Set<Object> mocks = new HashSet<>();
        Object instance = new Object();
        List<Field> orderedInstanceFields = Collections.singletonList(mock(Field.class));

        when(mockCandidateFilter.filterCandidate(anySet(), any(Field.class), any(Object.class)))
                .thenReturn(mock(Object.class));

        // Act
        boolean result = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, true, orderedInstanceFields);

        // Assert
        assertTrue(result);
        assertTrue(mocks.isEmpty());
    }

    @Test
    public void testInjectMockCandidates_TwoPasses_CandidateInjectedByField() {
        // Arrange
        Set<Object> mocks = new HashSet<>();
        Object instance = new Object();
        List<Field> orderedInstanceFields = Collections.singletonList(mock(Field.class));

        when(mockCandidateFilter.filterCandidate(anySet(), any(Field.class), any(Object.class)))
                .thenReturn(null);

        // Act
        boolean result = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, true, orderedInstanceFields);

        // Assert
        assertTrue(result);
        assertTrue(mocks.isEmpty());
    }

    @Test
    public void testInjectMockCandidates_TwoPasses_NoCandidateToInject() {
        // Arrange
        Set<Object> mocks = new HashSet<>();
        Object instance = new Object();
        List<Field> orderedInstanceFields = Collections.singletonList(mock(Field.class));

        when(mockCandidateFilter.filterCandidate(anySet(), any(Field.class), any(Object.class)))
                .thenReturn(null);

        // Act
        boolean result = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, true, orderedInstanceFields);

        // Assert
        assertFalse(result);
        assertFalse(mocks.isEmpty());
    }

    @Test
    public void testOrderedInstanceFieldsFrom_ReturnsOrderedFieldsFromSubTypeToSuperType() {
        // Arrange
        Class<?> awaitingInjectionClazz = SubType.class;

        // Act
        List<Field> orderedInstanceFields = propertyAndSetterInjection.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // Assert
        assertEquals(2, orderedInstanceFields.size());
        assertEquals("subField1", orderedInstanceFields.get(0).getName());
        assertEquals("superField2", orderedInstanceFields.get(1).getName());
    }

    private static class SubType extends SuperType {
        private String subField1;
    }

    private static class SuperType {
        private String superField1;
        private String superField2;
    }

}