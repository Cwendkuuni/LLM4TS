import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_WithNullOuterClassInstance_ReturnsInstanceWithNoArgConstructor() {
        // Arrange
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);

        // Act
        Object result = instantiator.newInstance(TestClass.class);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof TestClass);
    }

    @Test
    public void testNewInstance_WithNonNullOuterClassInstance_ReturnsInstanceWithOuterClassConstructor() {
        // Arrange
        TestClass outerClassInstance = new TestClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);

        // Act
        Object result = instantiator.newInstance(TestClass.class);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof TestClass);
        assertSame(outerClassInstance, ((TestClass) result).getOuterClassInstance());
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_WithTargetClassMissingNoArgConstructor_ThrowsInstantiationException() {
        // Arrange
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);

        // Act
        instantiator.newInstance(ClassWithoutNoArgConstructor.class);

        // Assert (Exception thrown)
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_WithInvalidOuterClassInstanceType_ThrowsInstantiationException() {
        // Arrange
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new InvalidOuterClass());

        // Act
        instantiator.newInstance(TestClass.class);

        // Assert (Exception thrown)
    }

    private class TestClass {
        private Object outerClassInstance;

        public TestClass() {
        }

        public TestClass(Object outerClassInstance) {
            this.outerClassInstance = outerClassInstance;
        }

        public Object getOuterClassInstance() {
            return outerClassInstance;
        }
    }

    private class ClassWithoutNoArgConstructor {
        public ClassWithoutNoArgConstructor(Object arg) {
        }
    }

    private class InvalidOuterClass {
    }
}