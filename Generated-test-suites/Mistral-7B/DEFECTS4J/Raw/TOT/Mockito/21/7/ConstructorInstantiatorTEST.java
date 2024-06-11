import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InstantiationStrategy;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class ConstructorInstantiatorTest {

    private Object outerClassInstance;
    private ConstructorInstantiator constructorInstantiator;

    @Before
    public void setup() {
        outerClassInstance = Mockito.mock(String.class);
        constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
    }

    @Test
    public void testNewInstance_WithNullOuterClassInstance_ReturnsInstanceCreatedWithNoArguments() {
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Class<?> cls = invocation.getArgumentAt(0, Class.class);
                Object instance = ConstructorInstantiator.noArgConstructor(cls);
                verify(outerClassInstance, never()).getClass();
                return instance;
            }
        }).when(constructorInstantiator).newInstance(ArgumentMatchers.any(Class.class));

        String testClass = String.class;
        String instance = constructorInstantiator.newInstance(testClass);
        assertEquals("", instance);
    }

    @Test
    public void testNewInstance_WithNonNullOuterClassInstance_ReturnsInstanceCreatedWithArguments() {
        String arg = "outer";
        String testClass = String.class;

        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Class<?> cls = invocation.getArgumentAt(0, Class.class);
                Constructor<String> constructor = cls.getDeclaredConstructor(String.class);
                constructor.setAccessible(true);
                Object instance = constructor.newInstance(arg);
                verify(outerClassInstance).getClass();
                return instance;
            }
        }).when(constructorInstantiator).newInstance(testClass);

        String instance = constructorInstantiator.newInstance(testClass);
        assertEquals(arg, instance);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_ThrowException_WhenClassHasNoParameterLessConstructor() {
        Class<Object> testClass = Object.class;
        constructorInstantiator.newInstance(testClass);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_ThrowException_WhenClassHasExceptionDuringInstanceCreation() {
        Class<Object> testClass = new TestExceptionClass();
        doThrow(new InstantiationException("Exception message")).when(constructorInstantiator).newInstance(testClass);

        try {
            constructorInstantiator.newInstance(testClass);
        } catch (InstantiationException e) {
            throw e;
        }
    }

    private static class TestExceptionClass {
        TestExceptionClass() throws InstantiationException {
            throw new InstantiationException("Exception message");
        }
    }

    private static class InstantiationException extends Exception {
        InstantiationException(String message, Throwable cause) {
            super(message, cause);
        }

        InstantiationException(String message) {
            super(message);
        }
    }
}