package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorInstantiatorTest {

    @Test
    public void testWithNullOuterClassInstance() {
        Class<String> stringClass = String.class;
        assertNull(new ConstructorInstantiator(null).newInstance(stringClass));
    }

    @Test
    public void testNoArgConstructor() {
        Class<String> stringClass = String.class;
        assertInstanceOf(String.class, new ConstructorInstantiator(mock(Object.class)).newInstance(stringClass));
    }

    @Test(expected = InstantiationException.class)
    public void testNoArgConstructorFailure() {
        Class<String> stringClass = String[].class; // wrong class
        new ConstructorInstantiator(mock(Object.class)).newInstance(stringClass);
    }

    @Test
    public void testConstructorWithOneArgument() {
        Object outerClassInstance = mock(Object.class);
        Class<String> stringClass = String.class;
        String result = new ConstructorInstantiator(outerClassInstance).newInstance(stringClass);
        Mockito.verify(outerClassInstance).getClass();
        assertInstanceOf(String.class, result);
    }

    @Test(expected = InstantiationException.class)
    public void testConstructorWithOneArgumentFailure() {
        Object outerClassInstance = mock(Object.class);
        Class<String> stringClass = Integer.class;
        new ConstructorInstantiator(outerClassInstance).newInstance(stringClass);
    }

    @Test
    public void testConstructorWithTwoArguments() {
        Object outerClassInstance = mock(Object.class);
        Class<String> stringClass = String[].class;
        Constructor<String[]> constructor = stringClass.getDeclaredConstructor(Object.class, Object.class);
        String[] result = new ConstructorInstantiator(outerClassInstance).newInstance(stringClass);
        Mockito.verify(outerClassInstance, Mockito.atLeastOnce()).getClass();
        Mockito.verify(outerClassInstance, Mockito.atLeastOnce(new Matcher<InvocationOnMock>() {
            @Override
            public boolean matches(InvocationOnMock invocation) {
                Object[] arguments = invocation.getArguments();
                return arguments.length == 2 && arguments[0] == outerClassInstance;
            }
        }));
        assertArrayEquals(new String[]{}, result);
    }

    @Test(expected = InstantiationException.class)
    public void testConstructorWithTwoArgumentsFailure() {
        Object outerClassInstance = mock(Object.class);
        Class<String> stringClass = Integer.class;
        new ConstructorInstantiator(outerClassInstance).newInstance(stringClass);
    }

    @Test
    public void testParamsException() {
        Object outerClassInstance = mock(Object.class);
        Class<String> stringClass = String[].class;
        assertThrows(InstantiationException.class, () -> {
            new ConstructorInstantiator(outerClassInstance).newInstance(stringClass);
        });
    }

    @Test
    public void testNoArgConstructorException() {
        Class<String> stringClass = String.class;
        assertThrows(InstantiationException.class, () -> {
            new ConstructorInstantiator(null).newInstance(stringClass);
        });
    }

    private void assertArrayEquals(Object expected, Object actual) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected == null || actual == null) {
            throw new AssertionError();
        }
        if (expected.getClass() != actual.getClass()) {
            throw new AssertionError();
        }
        Object[] expectedArray = (Object[]) expected;
        Object[] actualArray = (Object[]) actual;
        for (int i = 0; i < expectedArray.length; i++) {
            assertEquals(expectedArray[i], actualArray[i]);
        }
    }

    @Test
    public void testWithOuterClassNullConstructor() {
        Object outerClassInstance = null;
        Class<String> stringClass = String.class;
        try {
            new ConstructorInstantiator(outerClassInstance).newInstance(stringClass);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            assertEquals("Unable to create mock instance of 'String'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }

    @Test
    public void testWithOuterClassWrongConstructor() {
        Object outerClassInstance = mock(Object.class);
        Class<String> stringClass = String[].class;
        try {
            new ConstructorInstantiator(outerClassInstance).newInstance(stringClass);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            assertEquals("Unable to create mock instance of 'String[]'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }

    @Test
    public void testWithOuterClassCorrectConstructor() {
        Object outerClassInstance = mock(Object.class);
        Class<String> stringClass = String[].class;
        Constructor<String[]> constructor = stringClass.getDeclaredConstructor(Object.class);
        String[] result = new ConstructorInstantiator(outerClassInstance).newInstance(stringClass);
        Mockito.verify(outerClassInstance, Mockito.atLeastOnce()).getClass();
        assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testWithOuterClassException() {
        Object outerClassInstance = mock(Object.class);
        Class<String> stringClass = String.class;
        try {
            new ConstructorInstantiator(outerClassInstance).newInstance(stringClass);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            assertEquals("Unable to create mock instance of 'String'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }
}