// PropertyAndSetterInjectionTest.java - Expert 1

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer.InitializationType;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.exceptions.Reporter;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injection;
    private FieldInitializationReport report;
    private Reporter reporter;
    private Class<?> mockitoMocks;
    private Object mockitoMock;

    @Before
    public void setUp() {
        injection = new PropertyAndSetterInjection();
        reporter = Reporter.MOCKITO_REPORTER;
        mockitoMocks = Mockito.class;
        mockitoMock = mock(mockitoMocks);
    }

    @Test
    public void testProcessInjection_FieldAnnotatedWithInjectMocksAnnotated_MockCandidatesFound() {
        // Given
        Object objectUnderTest = spy(new Object() {
            @InjectMocks
            private Object fieldToInject;
        });
        Class<?> fieldClass = Object.class;
        Object fieldInstanceNeedingInjection = objectUnderTest;
        Set<Object> mockCandidates = newMockSafeHashSet(Collections.singletonList(mockitoMock));

        // When
        boolean injectionOccurred = injection.processInjection(Field.class.getDeclaredField("fieldToInject"), objectUnderTest, mockCandidates);

        // Then
        assertEquals(true, injectionOccurred);
        // Verify that mockitoMock was injected
        assertNotNull(objectUnderTest.fieldToInject);
        Mockito.verify(mockitoMock).isMock();
    }

    @Test
    public void testProcessInjection_FieldAnnotatedWithInjectMocksAnnotated_NoMockCandidatesFound() {
        // Given
        Object objectUnderTest = spy(new Object() {
            @InjectMocks
            private Object fieldToInject;
        });
        Class<?> fieldClass = Object.class;
        Object fieldInstanceNeedingInjection = objectUnderTest;
        Set<Object> mockCandidates = newMockSafeHashSet();

        // When
        boolean injectionOccurred = injection.processInjection(Field.class.getDeclaredField("fieldToInject"), objectUnderTest, mockCandidates);

        // Then
        assertEquals(false, injectionOccurred);
        // Verify that no field was injected
        assertNull(objectUnderTest.fieldToInject);
        Mockito.verifyNoInteractions(mockitoMock);
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjection_FieldAnnotatedWithInjectMocksAnnotated_InitializationThrowsException() {
        // Given
        Object objectUnderTest = spy(new Object() {
            @InjectMocks
            private Object fieldToInject;
        });
        Class<?> fieldClass = Object.class;
        Object fieldInstanceNeedingInjection = objectUnderTest;
        Set<Object> mockCandidates = newMockSafeHashSet(Collections.singletonList(mockitoMock));

        // When & Then
        FieldInitializationReport report = injection.initializeInjectMocksField(Field.class.getDeclaredField("fieldToInject"), objectUnderTest);
        assertEquals(InitializationType.FIELD, report.initializationType());
        new Reporter().cannotInitializeForInjectMocksAnnotation("fieldToInject", new Exception("Test Exception"));
    }

    @Test
    public void testInjectMockCandidates_MockCandidatesFound() {
        // Given
        Class<?> mockitoMocksWithInjectedField = new Class<>() {
            @InjectMocks
            private Object injectedField;
        };
        Set<Object> mockCandidates = newMockSafeHashSet(Collections.singletonList(mockitoMock));
        Object instance = mockitoMocksWithInjectedField;

        // When
        boolean injectionOccurred = injection.injectMockCandidates(mockitoMocks.getClass(), mockCandidates, instance);

        // Then
        assertEquals(true, injectionOccurred);
        // Verify that mockitoMock was injected
        assertNotNull(instance.injectedField);
        Mockito.verify(mockitoMock).isMock();
    }

    @Test
    public void testInjectMockCandidates_NoMockCandidatesFound() {
        // Given
        Class<?> mockitoMocks = Mockito.class;
        Set<Object> mockCandidates = newMockSafeHashSet();
        Object instance = mockitoMocks;

        // When
        boolean injectionOccurred = injection.injectMockCandidates(mockitoMocks.getClass(), mockCandidates, instance);

        // Then
        assertEquals(false, injectionOccurred);
        // Verify that no field was injected
        assertNull(instance.injectedField);
        Mockito.verifyNoInteractions(mockitoMock);
    }

    @Test(expected = MockitoException.class)
    public void testInjectMockCandidates_InitializationThrowsException() {
        // Given
        Class<?> mockitoMocks = Mockito.class;
        Set<Object> mockCandidates = newMockSafeHashSet(Collections.singletonList(mockitoMock));
        Object instance = mockitoMocks;

        // When & Then
        FieldInitializationReport report = injection.initializeInjectMocksField(Field.class.getDeclaredField("injectedField"), instance);
        assertEquals(InitializationType.FIELD, report.initializationType());
        new Reporter().cannotInitializeForInjectMocksAnnotation("injectedField", new Exception("Test Exception"));
    }
}