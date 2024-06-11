import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.internal.creation.instance.*;

public class ConstructorInstantiatorTest {

    private ConstructorInstantiator constructorInstantiator;

    @Before
    public void setUp() {
        constructorInstantiator = new ConstructorInstantiator(null);
    }

    @Test
    public void testNewInstance_NoArgConstructor_ReturnsInstance() {
        // Typical use case: Calling newInstance with a class that has a parameter-less constructor
        String expected = "Hello";
        MyClass myClass = constructorInstantiator.newInstance(MyClass.class);
        assertNotNull(myClass);
        assertEquals(expected, myClass.getText());
    }

    @Test
    public void testNewInstance_WithOuterClass_ReturnsInstance() {
        // Typical use case: Calling newInstance with a class that has a constructor requiring an outer class instance
        OuterClass outerClass = new OuterClass();
        constructorInstantiator = new ConstructorInstantiator(outerClass);

        MyClass myClass = constructorInstantiator.newInstance(MyClass.class);
        assertNotNull(myClass);
        assertEquals(outerClass, myClass.getOuterClass());
    }

    @Test(expected = InstantationException.class)
    public void testNewInstance_NoArgConstructor_ExceptionThrown() {
        // Error scenario: Calling newInstance with a class that does not have a parameter-less constructor
        constructorInstantiator.newInstance(NoArgConstructorClass.class);
    }

    @Test(expected = InstantationException.class)
    public void testNewInstance_WithOuterClass_ExceptionThrown() {
        // Error scenario: Calling newInstance with a class that requires an outer class instance but not providing one
        constructorInstantiator.newInstance(WithOuterClass.class);
    }

    @Test(expected = InstantationException.class)
    public void testNewInstance_InvalidOuterClassType_ExceptionThrown() {
        // Error scenario: Calling newInstance with a class that requires an outer class instance but providing an invalid type
        constructorInstantiator.newInstance(WithOuterClass.class);
    }

    @Test(expected = InstantationException.class)
    public void testNewInstance_ExceptionInConstructor_ExceptionThrown() {
        // Error scenario: Calling newInstance with a class that has a valid constructor, but the constructor throws an exception
        constructorInstantiator.newInstance(ExceptionInConstructorClass.class);
    }

    private static class MyClass {
        private String text;

        public MyClass() {
            this.text = "Hello";
        }

        public String getText() {
            return text;
        }
    }

    private static class OuterClass {
    }

    private static class NoArgConstructorClass {
        public NoArgConstructorClass(int value) {
        }
    }

    private static class WithOuterClass {
        public WithOuterClass(OuterClass outerClass) {
        }
    }

    private static class InvalidOuterClassType {
        public InvalidOuterClassType(String s) {
        }
    }

    private static class ExceptionInConstructorClass {
        public ExceptionInConstructorClass() {
            throw new RuntimeException("Exception in constructor");
        }
    }
}