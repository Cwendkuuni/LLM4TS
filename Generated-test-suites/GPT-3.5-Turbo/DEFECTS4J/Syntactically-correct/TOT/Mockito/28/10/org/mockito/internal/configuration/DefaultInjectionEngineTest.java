import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine defaultInjectionEngine;

    @Before
    public void setUp() {
        defaultInjectionEngine = new DefaultInjectionEngine();
    }

    @Test
    public void testInjectMocksOnFields_singleFieldWithInjectMocksAnnotation() {
        class TestClass {
            @InjectMocks
            private Dependency dependency;
        }

        Set<Field> injectMocksFields = new HashSet<>();
        Field dependencyField = TestClass.class.getDeclaredField("dependency");
        injectMocksFields.add(dependencyField);

        Set<Object> mocks = new HashSet<>();
        Dependency mockedDependency = mock(Dependency.class);
        mocks.add(mockedDependency);

        TestClass testClassInstance = new TestClass();

        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        verify(mockedDependency, times(1)).setField(any());
        verify(mockedDependency, never()).getField();
    }

    @Test
    public void testInjectMocksOnFields_multipleFieldsWithInjectMocksAnnotation() {
        class TestClass {
            @InjectMocks
            private Dependency dependency;

            @InjectMocks
            private AnotherDependency anotherDependency;
        }

        Set<Field> injectMocksFields = new HashSet<>();
        Field dependencyField = TestClass.class.getDeclaredField("dependency");
        Field anotherDependencyField = TestClass.class.getDeclaredField("anotherDependency");
        injectMocksFields.add(dependencyField);
        injectMocksFields.add(anotherDependencyField);

        Set<Object> mocks = new HashSet<>();
        Dependency mockedDependency = mock(Dependency.class);
        AnotherDependency mockedAnotherDependency = mock(AnotherDependency.class);
        mocks.add(mockedDependency);
        mocks.add(mockedAnotherDependency);

        TestClass testClassInstance = new TestClass();

        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        verify(mockedDependency, times(1)).setField(any());
        verify(mockedDependency, never()).getField();

        verify(mockedAnotherDependency, times(1)).setField(any());
        verify(mockedAnotherDependency, never()).getField();
    }

    @Test
    public void testInjectMocksOnFields_fieldInitializationThrowsException() {
        class TestClass {
            @InjectMocks
            private Dependency dependency;
        }

        Set<Field> injectMocksFields = new HashSet<>();
        Field dependencyField = TestClass.class.getDeclaredField("dependency");
        injectMocksFields.add(dependencyField);

        Set<Object> mocks = new HashSet<>();
        Dependency mockedDependency = mock(Dependency.class);
        mocks.add(mockedDependency);

        TestClass testClassInstance = new TestClass();

        // Force initialization to throw an exception
        Mockito.when(dependencyField.get(testClassInstance)).thenThrow(new MockitoException("Initialization error"));

        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        verify(mockedDependency, never()).setField(any());
        verify(mockedDependency, never()).getField();

        assertEquals("Initialization error", testClassInstance.getDependency().getError());
    }

    @Test
    public void testInjectMocksOnFields_multipleDependenciesWithSameType() {
        class TestClass {
            @InjectMocks
            private Dependency dependency;

            @InjectMocks
            private AnotherDependency anotherDependency;
        }

        Set<Field> injectMocksFields = new HashSet<>();
        Field dependencyField = TestClass.class.getDeclaredField("dependency");
        Field anotherDependencyField = TestClass.class.getDeclaredField("anotherDependency");
        injectMocksFields.add(dependencyField);
        injectMocksFields.add(anotherDependencyField);

        Set<Object> mocks = new HashSet<>();
        // Two dependencies with the same type
        Dependency mockedDependency1 = mock(Dependency.class);
        Dependency mockedDependency2 = mock(Dependency.class);
        mocks.add(mockedDependency1);
        mocks.add(mockedDependency2);

        TestClass testClassInstance = new TestClass();

        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        verify(mockedDependency1, never()).setField(any());
        verify(mockedDependency1, never()).getField();

        verify(mockedDependency2, never()).setField(any());
        verify(mockedDependency2, never()).getField();

        verify(mockedDependency1, times(1)).setAnotherField(any());
        verify(mockedDependency1, never()).getAnotherField();

        verify(mockedDependency2, times(1)).setAnotherField(any());
        verify(mockedDependency2, never()).getAnotherField();
    }

}