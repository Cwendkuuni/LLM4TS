import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injectionStrategy;

    @Before
    public void setup() {
        injectionStrategy = new PropertyAndSetterInjection();
    }

    @Test
    public void processInjection_InjectMocksFieldInitialized_InjectsMocks() throws NoSuchFieldException {
        // Arrange
        TestClass testClass = new TestClass();
        Field injectMocksField = TestClass.class.getDeclaredField("field");
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mock(MockClass1.class));
        mockCandidates.add(mock(MockClass2.class));

        // Act
        boolean result = injectionStrategy.processInjection(injectMocksField, testClass, mockCandidates);

        // Assert
        assertTrue(result);
        assertNotNull(testClass.getField());
    }

    @Test
    public void processInjection_InjectMocksFieldNotInitialized_InjectsMocks() throws NoSuchFieldException {
        // Arrange
        TestClass testClass = new TestClass();
        Field injectMocksField = TestClass.class.getDeclaredField("field");
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mock(MockClass1.class));
        mockCandidates.add(mock(MockClass2.class));

        // Act
        boolean result = injectionStrategy.processInjection(injectMocksField, testClass, mockCandidates);

        // Assert
        assertTrue(result);
        assertNotNull(testClass.getField());
    }

    @Test
    public void processInjection_InjectionNotPossible_DoesNotInjectMocks() throws NoSuchFieldException {
        // Arrange
        TestClass testClass = new TestClass();
        Field injectMocksField = TestClass.class.getDeclaredField("field");
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mock(MockClass1.class));

        // Act
        boolean result = injectionStrategy.processInjection(injectMocksField, testClass, mockCandidates);

        // Assert
        assertFalse(result);
        assertNull(testClass.getField());
    }

    @Test
    public void initializeInjectMocksField_FieldInitialized_ReturnsFieldInitializationReport() throws NoSuchFieldException {
        // Arrange
        TestClass testClass = new TestClass();
        Field field = TestClass.class.getDeclaredField("field");

        // Act
        FieldInitializationReport report = injectionStrategy.initializeInjectMocksField(field, testClass);

        // Assert
        assertNotNull(report);
    }

    @Test
    public void initializeInjectMocksField_FieldNotInitialized_ThrowsCannotInitializeException() throws NoSuchFieldException {
        // Arrange
        TestClass testClass = new TestClass();
        Field field = TestClass.class.getDeclaredField("field");

        // Act
        try {
            injectionStrategy.initializeInjectMocksField(field, testClass);
            fail("Exception not thrown");
        } catch (Exception e) {
            // Assert
            assertEquals(MockitoException.class, e.getClass());
        }
    }

    @Test
    public void injectMockCandidates_InjectMocksFieldClassNotObject_InjectsMocks() {
        // Arrange
        TestClass testClass = new TestClass();
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(MockClass1.class));
        mocks.add(mock(MockClass2.class));

        // Act
        boolean result = injectionStrategy.injectMockCandidates(MockClass1.class, mocks, testClass);

        // Assert
        assertTrue(result);
        assertNotNull(testClass.getField());
    }

    @Test
    public void injectMockCandidates_InjectMocksFieldClassObject_DoesNotInjectMocks() {
        // Arrange
        TestClass testClass = new TestClass();
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(MockClass1.class));
        mocks.add(mock(MockClass2.class));

        // Act
        boolean result = injectionStrategy.injectMockCandidates(Object.class, mocks, testClass);

        // Assert
        assertFalse(result);
        assertNull(testClass.getField());
    }

    @Test
    public void injectMockCandidatesOnFields_NoMocks_InjectsNoMocks() {
        // Arrange
        TestClass testClass = new TestClass();
        Set<Object> mocks = new HashSet<>();
        List<Field> orderedFields = new ArrayList<>();

        // Act
        boolean result = injectionStrategy.injectMockCandidatesOnFields(mocks, testClass, false, orderedFields);

        // Assert
        assertFalse(result);
        assertNull(testClass.getField());
    }

    @Test
    public void injectMockCandidatesOnFields_InjectMockCandidate_MockInjected() throws NoSuchFieldException {
        // Arrange
        TestClass testClass = new TestClass();
        MockClass1 mock = mock(MockClass1.class);
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock);
        List<Field> orderedFields = new ArrayList<>();
        orderedFields.add(TestClass.class.getDeclaredField("field"));

        // Act
        boolean result = injectionStrategy.injectMockCandidatesOnFields(mocks, testClass, false, orderedFields);

        // Assert
        assertTrue(result);
        assertNotNull(testClass.getField());
        assertSame(mock, testClass.getField());
    }

    @Test
    public void orderedInstanceFieldsFrom_NoFields_ReturnsEmptyList() {
        // Arrange
        Class<TestClass> clazz = TestClass.class;

        // Act
        List<Field> result = injectionStrategy.orderedInstanceFieldsFrom(clazz);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}

class TestClass {
    private MockClass1 field;

    public MockClass1 getField() {
        return field;
    }

    public void setField(MockClass1 field) {
        this.field = field;
    }
}

interface MockClass1 {}

interface MockClass2 {}