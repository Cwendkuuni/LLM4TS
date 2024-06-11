import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.FieldInitializer;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine injectionEngine;
    private FieldInitializer fieldInitializerMock;
    private Reporter reporterMock;

    @Before
    public void setUp() {
        injectionEngine = new DefaultInjectionEngine();
        fieldInitializerMock = mock(FieldInitializer.class);
        reporterMock = mock(Reporter.class);
    }

    // Test injectMocksOnFields with a single field annotated with @InjectMocks
    @Test
    public void testInjectMocksOnFields_SingleField() throws Exception {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Field injectMocksField = DefaultInjectionEngine.class.getDeclaredField("mockCandidateFilter");
        injectMocksFields.add(injectMocksField);

        Set<Object> mocks = new HashSet<>();
        Object mockCandidateFilterMock = mock(Object.class);
        mocks.add(mockCandidateFilterMock);

        Object testClassInstance = new Object();

        // Mock the FieldInitializer to return a mock for injectMocksField
        when(fieldInitializerMock.initialize()).thenReturn(mockCandidateFilterMock);
        whenNew(FieldInitializer.class).withArguments(testClassInstance, injectMocksField).thenReturn(fieldInitializerMock);

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        verify(fieldInitializerMock).initialize();
        verify(mockCandidateFilterMock, times(1)).getClass();
    }

    // Test injectMocksOnFields with multiple fields annotated with @InjectMocks
    @Test
    public void testInjectMocksOnFields_MultipleFields() throws Exception {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Field injectMocksField1 = DefaultInjectionEngine.class.getDeclaredField("mockCandidateFilter");
        Field injectMocksField2 = DefaultInjectionEngine.class.getDeclaredField("supertypesLast");
        injectMocksFields.add(injectMocksField1);
        injectMocksFields.add(injectMocksField2);

        Set<Object> mocks = new HashSet<>();
        Object mockCandidateFilterMock = mock(Object.class);
        Object supertypesLastMock = mock(Object.class);
        mocks.add(mockCandidateFilterMock);
        mocks.add(supertypesLastMock);

        Object testClassInstance = new Object();

        // Mock the FieldInitializer to return a mock for each injectMocksField
        when(fieldInitializerMock.initialize()).thenReturn(mockCandidateFilterMock).thenReturn(supertypesLastMock);
        whenNew(FieldInitializer.class).withAnyArguments().thenReturn(fieldInitializerMock);

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        verify(fieldInitializerMock, times(2)).initialize();
        verify(mockCandidateFilterMock, times(1)).getClass();
        verify(supertypesLastMock, times(1)).getClass();
    }

    // Test injectMocksOnFields throws MockitoException when field initialization fails
    @Test
    public void testInjectMocksOnFields_FieldInitializationFailure() throws Exception {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Field injectMocksField = DefaultInjectionEngine.class.getDeclaredField("mockCandidateFilter");
        injectMocksFields.add(injectMocksField);

        Set<Object> mocks = new HashSet<>();

        Object testClassInstance = new Object();

        // Mock the FieldInitializer to throw a MockitoException during initialization
        when(fieldInitializerMock.initialize()).thenThrow(MockitoException.class);
        whenNew(FieldInitializer.class).withAnyArguments().thenReturn(fieldInitializerMock);

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        verify(fieldInitializerMock).initialize();
        verify(reporterMock, times(1)).cannotInitializeForInjectMocksAnnotation(any(String.class), any(MockitoException.class));
    }

    // Test injectMocksOnFields with sub-type to super-type injection order
    @Test
    public void testInjectMocksOnFields_SubTypeToSuperType() throws Exception {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Field injectMocksField = DefaultInjectionEngine.class.getDeclaredField("mockCandidateFilter");
        injectMocksFields.add(injectMocksField);

        Set<Object> mocks = new HashSet<>();
        Object mockCandidateFilterMock = mock(Object.class);
        Object subTypeMock = mock(Object.class);
        mocks.add(mockCandidateFilterMock);
        mocks.add(subTypeMock);

        Object testClassInstance = new Object();

        // Mock the FieldInitializer to return a mock for injectMocksField
        when(fieldInitializerMock.initialize()).thenReturn(mockCandidateFilterMock);
        whenNew(FieldInitializer.class).withAnyArguments().thenReturn(fieldInitializerMock);

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        verify(fieldInitializerMock).initialize();
        verify(mockCandidateFilterMock, times(1)).getClass();
        verify(subTypeMock, times(1)).getClass();
    }

    // Test injectMockCandidate with successful injection
    @Test
    public void testInjectMockCandidate_SuccessfulInjection() throws Exception {
        // Arrange
        Class<?> awaitingInjectionClazz = DefaultInjectionEngine.class;
        Set<Object> mocks = new HashSet<>();
        Object mockCandidateFilterMock = mock(Object.class);
        mocks.add(mockCandidateFilterMock);

        Object fieldInstance = new Object();

        Field field = DefaultInjectionEngine.class.getDeclaredField("mockCandidateFilter");

        // Act
        injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        // Assert
        verify(mockCandidateFilterMock, times(1)).getClass();
    }

    // Test orderedInstanceFieldsFrom with ordering from sub-type to super-type
    @Test
    public void testOrderedInstanceFieldsFrom_SubTypeToSuperTypeOrder() {
        // Arrange
        Class<?> awaitingInjectionClazz = DefaultInjectionEngine.class;

        // Act
        Field[] orderedFields = injectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // Assert
        for (int i = 0; i < orderedFields.length - 1; i++) {
            Class<?> fieldType1 = orderedFields[i].getType();
            Class<?> fieldType2 = orderedFields[i + 1].getType();
            assertTrue(fieldType1.isAssignableFrom(fieldType2));
        }
    }

}