import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.MockInjectionStrategy;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static org.mockito.Mockito.*;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injectionStrategy;
    private MockInjectionStrategy mockInjectionStrategy;

    @Before
    public void setup() {
        injectionStrategy = new PropertyAndSetterInjection();
        mockInjectionStrategy = mock(MockInjectionStrategy.class);
    }

    @Test
    public void processInjection_shouldInitializeInjectMocksField() throws NoSuchFieldException {
        // Arrange
        TestClass1 testClass = new TestClass1();
        Field injectMocksField = testClass.getClass().getDeclaredField("testField");

        // Act
        boolean result = injectionStrategy.processInjection(injectMocksField, testClass, new HashSet<>());

        // Assert
        Assert.assertTrue(result);
        Assert.assertNotNull(testClass.getTestField());
    }

    @Test(expected = Reporter.class)
    public void processInjection_shouldThrowReporterExceptionWhenCannotInitializeInjectMocksField() throws NoSuchFieldException {
        // Arrange
        TestClass2 testClass = new TestClass2();
        Field injectMocksField = testClass.getClass().getDeclaredField("testField");

        // Act
        injectionStrategy.processInjection(injectMocksField, testClass, new HashSet<>());
    }

    @Test(expected = Reporter.class)
    public void processInjection_shouldThrowReporterExceptionWhenFieldInitializationThrowsInvocationTargetException() throws NoSuchFieldException {
        // Arrange
        TestClass3 testClass = new TestClass3();
        Field injectMocksField = testClass.getClass().getDeclaredField("testField");

        // Act
        injectionStrategy.processInjection(injectMocksField, testClass, new HashSet<>());
    }

    @Test
    public void injectMockCandidates_shouldInjectMocksOnFields() {
        // Arrange
        TestClass4 testClass = new TestClass4();
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add("mock1");
        mockCandidates.add("mock2");

        // Act
        boolean result = injectionStrategy.injectMockCandidates(TestClass4.class, mockCandidates, testClass);

        // Assert
        Assert.assertTrue(result);
        Assert.assertNull(testClass.getMock1());
        Assert.assertNull(testClass.getMock2());
        Assert.assertNull(testClass.getMock3());
    }

    @Test
    public void injectMockCandidatesOnFields_shouldInjectMockCandidatesOnFields() {
        // Arrange
        TestClass4 testClass = new TestClass4();
        Set<Object> mocks = new HashSet<>();
        mocks.add("mock1");
        mocks.add("mock2");

        List<Field> orderedInstanceFields = new ArrayList<>();
        Field field1 = getField(TestClass4.class, "mock1");
        Field field2 = getField(TestClass4.class, "mock2");
        Field field3 = getField(TestClass4.class, "mock3");
        orderedInstanceFields.add(field1);
        orderedInstanceFields.add(field2);
        orderedInstanceFields.add(field3);

        // Act
        boolean result = injectionStrategy.injectMockCandidatesOnFields(mocks, testClass, false, orderedInstanceFields);

        // Assert
        Assert.assertTrue(result);
        Assert.assertNull(testClass.getMock1());
        Assert.assertNull(testClass.getMock2());
        Assert.assertNull(testClass.getMock3());
        Assert.assertEquals(0, mocks.size());
        Assert.assertEquals(1, orderedInstanceFields.size());
        Assert.assertEquals(field3, orderedInstanceFields.get(0));
    }

    private Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    private static class TestClass1 {
        private Object testField;

        public Object getTestField() {
            return testField;
        }
    }

    private static class TestClass2 {
        private Object testField;

        public Object getTestField() {
            return testField;
        }
    }

    private static class TestClass3 {
        private Object testField;

        public Object getTestField() {
            return testField;
        }
    }

    private static class TestClass4 {
        private Object mock1;
        private Object mock2;
        private Object mock3;

        public Object getMock1() {
            return mock1;
        }

        public Object getMock2() {
            return mock2;
        }

        public Object getMock3() {
            return mock3;
        }
    }
}