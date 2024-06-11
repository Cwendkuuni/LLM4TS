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

    private DefaultInjectionEngine engine;
    private Field field;
    private Object testInstance;

    @Before
    public void setup() throws Exception {
        engine = new DefaultInjectionEngine();
        field = TestClass.class.getDeclaredField("testField");
        testInstance = new TestClass();
    }

    @Test
    public void testInjectMocksOnFields() throws Exception {
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(field);

        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(MockedClass.class));

        engine.injectMocksOnFields(injectMocksFields, mocks, testInstance);

        verify(field, times(1)).set(testInstance, mocks.iterator().next());
    }

    @Test
    public void testInjectMocksOnFields_NoCandidates() throws Exception {
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(field);

        Set<Object> mocks = new HashSet<>();
        engine.injectMocksOnFields(injectMocksFields, mocks, testInstance);

        verifyZeroInteractions(field);
    }

    @Test
    public void testInjectMocksOnFields_InitializationFailed() throws Exception {
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(field);

        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(MockedClass.class));

        doThrow(MockitoException.class).when(field).set(testInstance, mocks.iterator().next());

        engine.injectMocksOnFields(injectMocksFields, mocks, testInstance);

        verify(field, times(1)).set(testInstance, mocks.iterator().next());
        verify(new Reporter(), times(1)).cannotInitializeForInjectMocksAnnotation(eq(field.getName()), any(MockitoException.class));
    }

    @Test
    public void testOrderedInstanceFieldsFrom() throws Exception {
        Class<?> clazz = TestClass.class;

        Field[] orderedFields = engine.orderedInstanceFieldsFrom(clazz);

        Field[] expectedFields = {TestClass.class.getDeclaredField("testField"), TestClass.class.getDeclaredField("otherField")};
        assertArrayEquals(expectedFields, orderedFields);
    }

    private class TestClass {
        private MockedClass testField;
        private OtherClass otherField;
    }

    private class MockedClass {}

    private class OtherClass {}

}