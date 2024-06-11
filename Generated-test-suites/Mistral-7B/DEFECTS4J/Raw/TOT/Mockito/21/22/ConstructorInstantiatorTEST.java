import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndParameterlessClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Integer intInstance = instantiator.newInstance(Integer.class);
        Assert.assertNull(intInstance);

        try {
            instantiator.newInstance(String.class);
            fail("Expected InstantiationException for String class.");
        } catch (InstantiationException e) {
            // Expected
        }
    }

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndConstructorWithParameter() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.newInstance(String[].class);
            fail("Expected InstantiationException for String[] class.");
        } catch (InstantiationException e) {
            // Expected
        }
    }

    @Test
    public void testNewInstanceWithNonNullOuterClassInstanceAndParameterlessClass() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Integer intInstance = instantiator.newInstance(Integer.class);
        Assert.assertNotNull(intInstance);
    }

    @Test
    public void testNewInstanceWithNonNullOuterClassInstanceAndConstructorWithParam() {
        Object outerClassInstance = new Object();
        Constructor<String> stringConstructor = String.class.getDeclaredConstructor(String.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        String stringInstance = instantiator.newInstance(String.class);
        try {
            Whitebox.setInternalState(stringInstance, "value", "test");
            String result = stringInstance.toString();
            Assert.assertEquals("test", result);
        } catch (IllegalAccessException e) {
            fail("Exception should not be thrown.");
        }
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithNonExistentClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(NonExistentClass.class);
        fail("Should not reach this point.");
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithNoArgConstructorThrowsException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.newInstance(ExceptionThrowingClass.class);
            fail("Expected InstantiationException.");
        } catch (InstantiationException e) {
            // Expected
        }
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithConstructorThrowsException() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        try {
            instantiator.newInstance(ExceptionThrowingClassWithParam.class);
            fail("Expected InstantiationException.");
        } catch (InstantiationException e) {
            // Expected
        }
    }
}

class NonExistentClass {
}

class ExceptionThrowingClass {
    public ExceptionThrowingClass() throws Exception {
        throw new Exception("Test exception");
    }
}

class ExceptionThrowingClassWithParam {
    public ExceptionThrowingClassWithParam(String param) throws Exception {
        throw new Exception("Test exception with param: " + param);
    }
}