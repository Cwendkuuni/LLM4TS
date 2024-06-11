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
    private DefaultInjectionEngine injectionEngine;
    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setUp() {
        injectionEngine = new DefaultInjectionEngine();
        mockCandidateFilter = mock(MockCandidateFilter.class);
        injectionEngine.mockCandidateFilter = mockCandidateFilter;
    }

    @Test
    public void testInjectMocksOnFields() throws Exception {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Field field = DefaultInjectionEngineTest.class.getDeclaredField("mockCandidateFilter");
        injectMocksFields.add(field);
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new DefaultInjectionEngineTest();

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        verify(mockCandidateFilter).filterCandidate(mocks, field, testClassInstance);
    }

    @Test(expected = Reporter.class)
    public void testInjectMocksOnFields_MockitoException() throws Exception {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Field field = DefaultInjectionEngine.class.getDeclaredField("mockCandidateFilter");
        injectMocksFields.add(field);
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new DefaultInjectionEngine();

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        // Expect MockitoException to be thrown
    }

    @Test
    public void testInjectMocksOnFields_noFields() throws Exception {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new DefaultInjectionEngineTest();

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        verifyZeroInteractions(mockCandidateFilter);
    }

    @Test
    public void testInjectMocksOnFields_superclassFields() throws Exception {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Field field1 = DefaultInjectionEngineTest.class.getDeclaredField("mockCandidateFilter");
        injectMocksFields.add(field1);
        Field field2 = DefaultInjectionEngine.class.getDeclaredField("mockCandidateFilter");
        injectMocksFields.add(field2);
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new DefaultInjectionEngineTest();

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        verify(mockCandidateFilter, times(2)).filterCandidate(eq(mocks), any(Field.class), eq(testClassInstance));
    }

    @Test
    public void testInjectMocksOnFields_orderOfFields() throws Exception {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Field field1 = DefaultInjectionEngineTest.class.getDeclaredField("mockCandidateFilter");
        injectMocksFields.add(field1);
        Field field2 = DefaultInjectionEngineTest.class.getDeclaredField("injectionEngine");
        injectMocksFields.add(field2);
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new DefaultInjectionEngineTest();

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        InOrder inOrder = inOrder(mockCandidateFilter);
        inOrder.verify(mockCandidateFilter).filterCandidate(mocks, field1, testClassInstance);
        inOrder.verify(mockCandidateFilter).filterCandidate(mocks, field2, testClassInstance);
    }

    @Test
    public void testInjectMocksOnFields_noMocksToBeInjected() throws Exception {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Field field = DefaultInjectionEngineTest.class.getDeclaredField("mockCandidateFilter");
        injectMocksFields.add(field);
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new DefaultInjectionEngineTest();

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        verify(mockCandidateFilter).filterCandidate(mocks, field, testClassInstance);
    }

    @Test
    public void testInjectMockCandidate() throws Exception {
        // Arrange
        Class<?> awaitingInjectionClazz = DefaultInjectionEngineTest.class;
        Set<Object> mocks = new HashSet<>();
        Object fieldInstance = new DefaultInjectionEngineTest();

        // Act
        injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        // Assert
        verify(mockCandidateFilter).filterCandidate(mocks, any(Field.class), eq(fieldInstance));
    }

    @Test
    public void testOrderedInstanceFieldsFrom() throws Exception {
        // Arrange
        Class<?> awaitingInjectionClazz = DefaultInjectionEngineTest.class;

        // Act
        Field[] fields = injectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // Assert
        boolean isSorted = true;
        for (int i = 0; i < fields.length - 1; i++) {
            if (fields[i].getType().isAssignableFrom(fields[i + 1].getType()))
                isSorted = false;
        }
        assertTrue(isSorted);
    }
}