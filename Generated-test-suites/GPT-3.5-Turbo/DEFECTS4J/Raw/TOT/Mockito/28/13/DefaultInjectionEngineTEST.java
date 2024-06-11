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
    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setup() {
        defaultInjectionEngine = new DefaultInjectionEngine();
        mockCandidateFilter = mock(MockCandidateFilter.class);
        defaultInjectionEngine.setMockCandidateFilter(mockCandidateFilter);
    }

    @Test
    public void testInjectMocksOnFields_InjectMocksFieldExists_MocksInjected() {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Field injectMocksField = mock(Field.class);
        injectMocksFields.add(injectMocksField);
        Set<Object> mocks = new HashSet<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        Object testClassInstance = new Object();
        FieldInitializer fieldInitializer = mock(FieldInitializer.class);
        when(fieldInitializer.initialize()).thenReturn(mock1);
        when(mockCandidateFilter.filterCandidate(eq(mocks), eq(injectMocksField), eq(mock1))).thenReturn(mockCandidateFilter);
        when(mockCandidateFilter.thenInject()).thenReturn(true);

        // Act
        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        verify(fieldInitializer, times(1)).initialize();
        verify(mockCandidateFilter, times(1)).filterCandidate(eq(mocks), eq(injectMocksField), eq(mock1));
        verify(mockCandidateFilter, times(1)).thenInject();
    }

    @Test
    public void testInjectMocksOnFields_InjectMocksFieldDoesNotExist_NoMocksInjected() {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();

        // Act
        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        verifyZeroInteractions(mockCandidateFilter);
    }

    @Test
    public void testInjectMocksOnFields_InjectMocksFieldInitializeFails_NoMocksInjected() {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Field injectMocksField = mock(Field.class);
        injectMocksFields.add(injectMocksField);
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();
        FieldInitializer fieldInitializer = mock(FieldInitializer.class);
        MockitoException exception = new MockitoException("Initialization failed");
        when(fieldInitializer.initialize()).thenThrow(exception);
        Reporter reporter = mock(Reporter.class);
        defaultInjectionEngine.setReporter(reporter);

        // Act
        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        verify(fieldInitializer, times(1)).initialize();
        verify(reporter, times(1)).cannotInitializeForInjectMocksAnnotation(eq(injectMocksField.getName()), eq(exception));
        verifyZeroInteractions(mockCandidateFilter);
    }

    @Test
    public void testInjectMocksOnFields_InjectMocksFieldTypeMatchesNoCandidate_MocksNotInjected() {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Field injectMocksField = mock(Field.class);
        injectMocksFields.add(injectMocksField);
        Set<Object> mocks = new HashSet<>();
        Object mock1 = mock(Object.class);
        mocks.add(mock1);
        Object testClassInstance = new Object();
        FieldInitializer fieldInitializer = mock(FieldInitializer.class);
        when(fieldInitializer.initialize()).thenReturn(mock1);
        when(mockCandidateFilter.filterCandidate(eq(mocks), eq(injectMocksField), eq(mock1))).thenReturn(mockCandidateFilter);
        when(mockCandidateFilter.thenInject()).thenReturn(false);

        // Act
        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        verify(fieldInitializer, times(1)).initialize();
        verify(mockCandidateFilter, times(1)).filterCandidate(eq(mocks), eq(injectMocksField), eq(mock1));
        verify(mockCandidateFilter, times(0)).thenInject();
    }

    @Test
    public void testInjectMocksOnFields_InjectMocksFieldWithSuperTypes_MocksInjectedInOrder() {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Field injectMocksField = mock(Field.class);
        injectMocksFields.add(injectMocksField);
        Set<Object> mocks = new HashSet<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        Object testClassInstance = new Object();
        FieldInitializer fieldInitializer = mock(FieldInitializer.class);
        when(fieldInitializer.initialize()).thenReturn(mock1);
        when(mockCandidateFilter.filterCandidate(eq(mocks), eq(injectMocksField), eq(mock1))).thenReturn(mockCandidateFilter);
        when(mockCandidateFilter.thenInject()).thenReturn(true);
        Class<?> injectMocksFieldType = Object.class;
        Class<?> superClass1 = mock(Class.class);
        Class<?> superClass2 = mock(Class.class);
        when(superClass1.getSuperclass()).thenReturn(injectMocksFieldType);
        when(superClass2.getSuperclass()).thenReturn(superClass1);
        when(superClass2.getSuperclass()).thenReturn(null);
        Field[] fields = {injectMocksField};
        when(injectMocksFieldType.getDeclaredFields()).thenReturn(fields);

        // Act
        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        verify(fieldInitializer, times(1)).initialize();
        verify(mockCandidateFilter, times(1)).filterCandidate(eq(mocks), eq(injectMocksField), eq(mock1));
        verify(mockCandidateFilter, times(1)).filterCandidate(eq(mocks), eq(injectMocksField), eq(mock2));
        verify(mockCandidateFilter, times(2)).thenInject();
    }
}