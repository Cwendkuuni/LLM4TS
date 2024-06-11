
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    private ConstructorInstantiator constructorInstantiator;

    @Before
    public void setup() {
        constructorInstantiator = new ConstructorInstantiator(null);
    }

    @Test
    public void testNewInstance_noOuterClassInstance_returnsObjectWithParameterlessConstructor() {
        // Arrange
        Class<Object> cls = Object.class;

        // Act
        Object instance = constructorInstantiator.newInstance(cls);

        // Assert
        assertNotNull(instance);
        assertTrue(cls.isInstance(instance));
    }

    @Test
    public void testNewInstance_outerClassInstance_returnsObjectWithOuterClassConstructor() {
        // Arrange
        Class<TestClass> cls = TestClass.class;
        TestClass outerClassInstance = new TestClass();

        // Act
        TestClass instance = constructorInstantiator.newInstance(cls);

        // Assert
        assertNotNull(instance);
        assertTrue(cls.isInstance(instance));
        assertEquals(outerClassInstance, instance.getOuterClassInstance());
    }

    @Test(expected = InstantationException.class)
    public void testNewInstance_outerClassInstance_invalidOuterClassType_throwsInstantiationException() {
        // Arrange
        Class<TestClass> cls = TestClass.class;
        String outerClassInstance = "not an instance of TestClass";

        // Act
        constructorInstantiator.newInstance(cls);

        // Assert (Exception is expected)
    }

    @Test(expected = InstantationException.class)
    public void testNewInstance_noArgConstructor_throwsInstantiationException() {
        // Arrange
        Class<TestClassWithParameters> cls = TestClassWithParameters.class;

        // Act
        constructorInstantiator.newInstance(cls);

        // Assert (Exception is expected)
    }

    // Additional test cases for edge cases and exception handling can be added here...

}

class TestClass {
    public TestClass() {
    }

    public TestClass(Object param) {
    }

    public Object getOuterClassInstance() {
        return null;
    }
}

class TestClassWithParameters {
    public TestClassWithParameters(Object param) {
    }
}
