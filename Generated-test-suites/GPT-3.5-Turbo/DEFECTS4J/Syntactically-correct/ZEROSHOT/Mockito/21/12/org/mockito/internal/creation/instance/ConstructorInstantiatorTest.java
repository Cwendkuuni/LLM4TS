import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_noArgConstructor() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        String expectedResult = "Hello World";

        MyClass mock = constructorInstantiator.newInstance(MyClass.class);

        assertNotNull(mock);
        assertEquals(expectedResult, mock.getMessage());
    }

    @Test
    public void testNewInstance_withOuterClass() {
        String outerClassInstance = "OuterClassInstance";
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        String expectedResult = "Hello World";

        MyClass mock = constructorInstantiator.newInstance(MyClass.class);

        assertNotNull(mock);
        assertEquals(expectedResult, mock.getMessage());
    }

    @Test(expected = InstantationException.class)
    public void testNewInstance_withOuterClass_Exception() {
        String outerClassInstance = "InvalidOuterClass";
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);

        constructorInstantiator.newInstance(MyClass.class);
    }

    @Test(expected = InstantationException.class)
    public void testNewInstance_noArgConstructor_Exception() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);

        constructorInstantiator.newInstance(InvalidClass.class);
    }

    public class MyClass {
        public String getMessage() {
            return "Hello World";
        }
    }

    public class InvalidClass {
        public InvalidClass(String arg) {
            // Invalid constructor with parameter
        }
    }
}