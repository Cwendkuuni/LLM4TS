import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConstructorInstantiatorTest {

    @Test
    public void testNoArgConstructor() {
        ConstructorInstantiator instantIator = new ConstructorInstantiator(null);
        Class<?> dummyClass = DummyClass.class;

        try {
            Object instance = instantIator.newInstance(dummyClass);
            assertTrue(instance instanceof DummyClass);
        } catch (InstantiationException e) {
            fail("Unexpected InstantiationException: " + e.getMessage());
        }
    }

    @Test
    public void testConstructorWithOuterInstance() {
        Object outerInstance = new OuterClass();
        ConstructorInstantiator instantIator = new ConstructorInstantiator(outerInstance);
        Class<?> innerClass = InnerClass.class;

        try {
            Object instance = instantIator.newInstance(innerClass);
            assertTrue(instance instanceof InnerClass);
            InnerClass castedInstance = (InnerClass) instance;
            assertEquals(outerInstance, castedInstance.getOuterInstance());
        } catch (InstantiationException e) {
            fail("Unexpected InstantiationException: " + e.getMessage());
        }
    }

    @Test(expected = InstantiationException.class)
    public void testConstructorWithNoParameterLessConstructor() {
        Class<?> classWithNoParameterLessConstructor = NoParameterLessConstructorClass.class;
        ConstructorInstantiator instantIator = new ConstructorInstantiator(null);

        try {
            instantIator.newInstance(classWithNoParameterLessConstructor);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            // expected
        }
    }

    @Test(expected = InstantiationException.class)
    public void testConstructorWithWrongOuterClassType() {
        Object outerInstance = new WrongOuterClassType();
        ConstructorInstantiator instantIator = new ConstructorInstantiator(outerInstance);
        Class<?> innerClass = InnerClass.class;

        try {
            instantIator.newInstance(innerClass);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            // expected
        }
    }

    @Test
    public void testParamsExceptionIsThrownCorrectly() {
        Class<?> classWithException = ExceptionClass.class;
        ConstructorInstantiator instantIator = new ConstructorInstantiator(null);

        try {
            instantIator.newInstance(classWithException);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            String expectedMessage = "Unable to create mock instance of 'ExceptionClass'.\nPlease ensure it has parameter-less constructor.";
            String actualMessage = e.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }
    }

    private static class DummyClass {
        //Empty class for testing
    }

    private static class InnerClass {
        private final Object outerInstance;

        public InnerClass(Object outerInstance) {
            this.outerInstance = outerInstance;
        }

        public Object getOuterInstance() {
            return outerInstance;
        }
    }

    private static class OuterClass {
        //Empty class for testing
    }

    private static class NoParameterLessConstructorClass {
        private NoParameterLessConstructorClass() {
            throw new RuntimeException("This class should not be instantiated");
        }
    }

    private static class ExceptionClass {
        //Empty class with no parameter-less constructor for testing
    }

    private static class WrongOuterClassType {
        //Empty class of different type than expected for testing
    }
}