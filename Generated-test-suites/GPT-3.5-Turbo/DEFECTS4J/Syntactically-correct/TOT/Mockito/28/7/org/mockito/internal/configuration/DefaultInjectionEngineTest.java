import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine injectionEngine;

    @Before
    public void setup() {
        injectionEngine = new DefaultInjectionEngine();
    }

    @Test
    public void injectMocksOnFields_InjectsMocks_WhenFieldsAreAnnotatedWithInjectMocks() throws Exception {
        // Arrange
        Field injectMocksField = DummyTestClass.class.getDeclaredField("dummyInjectMocksField");
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(injectMocksField);

        Set<Object> mocks = new HashSet<>();
        Object mock1 = mock(MockedClass1.class);
        mocks.add(mock1);
        Object mock2 = mock(MockedClass2.class);
        mocks.add(mock2);

        DummyTestClass testClassInstance = new DummyTestClass();

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        assertNotNull(testClassInstance.dummyInjectMocksField);
        assertEquals(mock1, testClassInstance.dummyInjectMocksField.mock1);
        assertEquals(mock2, testClassInstance.dummyInjectMocksField.mock2);
    }

    @Test
    public void injectMocksOnFields_IgnoresField_WhenInitializationFails() throws Exception {
        // Arrange
        Field injectMocksField = DummyTestClass.class.getDeclaredField("dummyInjectMocksField");
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(injectMocksField);

        Set<Object> mocks = new HashSet<>();
        Object mock1 = mock(MockedClass1.class);
        mocks.add(mock1);
        Object mock2 = mock(MockedClass2.class);
        mocks.add(mock2);

        DummyTestClass testClassInstance = new DummyTestClass();

        // Assume that initialization fails
        MockitoAnnotations.initMocks(testClassInstance);

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        assertNull(testClassInstance.dummyInjectMocksField);
    }

    @Test
    public void injectMockCandidate_InjectsMockBySetter_WhenOnlyOneCandidateAvailable() throws Exception {
        // Arrange
        Object awaitingInjectionInstance = new DummyTestClass();
        Set<Object> mocks = new HashSet<>();
        Object mock1 = mock(MockedClass1.class);
        mocks.add(mock1);

        // Act
        injectionEngine.injectMockCandidate(awaitingInjectionInstance.getClass(), mocks, awaitingInjectionInstance);

        // Assert
        assertNotNull(((DummyTestClass) awaitingInjectionInstance).dummyInjectMocksField);
        assertEquals(mock1, ((DummyTestClass) awaitingInjectionInstance).dummyInjectMocksField.mock1);
    }

    @Test
    public void injectMockCandidate_InjectsMockByField_WhenSetterNotAvailable() throws Exception {
        // Arrange
        Object awaitingInjectionInstance = new DummyTestClass();
        Set<Object> mocks = new HashSet<>();
        Object mock1 = mock(MockedClass1.class);
        mocks.add(mock1);

        // Act
        injectionEngine.injectMockCandidate(awaitingInjectionInstance.getClass(), mocks, awaitingInjectionInstance);

        // Assert
        assertNotNull(((DummyTestClass) awaitingInjectionInstance).dummyInjectMocksField);
        assertEquals(mock1, ((DummyTestClass) awaitingInjectionInstance).dummyInjectMocksField.mock1);
    }

    @Test
    public void injectMockCandidate_DoesNotFail_WhenNoCandidateAvailable() throws Exception {
        // Arrange
        Object awaitingInjectionInstance = new DummyTestClass();
        Set<Object> mocks = new HashSet<>();

        // Act
        injectionEngine.injectMockCandidate(awaitingInjectionInstance.getClass(), mocks, awaitingInjectionInstance);

        // Assert
        assertNotNull(((DummyTestClass) awaitingInjectionInstance).dummyInjectMocksField);
        assertNull(((DummyTestClass) awaitingInjectionInstance).dummyInjectMocksField.mock1);
    }

    @Test
    public void orderedInstanceFieldsFrom_SortsFieldsByHierarchy() throws Exception {
        // Arrange
        Class<?> awaitingInjectionClazz = DummyTestClass.class;

        // Act
        Field[] orderedFields = injectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // Assert
        assertEquals(3, orderedFields.length);
        assertEquals(MockedClass1.class, orderedFields[0].getType());
        assertEquals(MockedClass2.class, orderedFields[1].getType());
        assertEquals(MockedClass3.class, orderedFields[2].getType());
    }

    private static class DummyTestClass {
        @InjectMocks
        private DummyInjectMocksField dummyInjectMocksField;
    }

    @SuppressWarnings("unused")
    private static class DummyInjectMocksField {
        private MockedClass1 mock1;
        private MockedClass2 mock2;
        private MockedClass3 mock3;

        public void setMock1(MockedClass1 mock1) {
            this.mock1 = mock1;
        }
    }

    private static class MockedClass1 {
    }

    private static class MockedClass2 {
    }

    private static class MockedClass3 {
    }
}