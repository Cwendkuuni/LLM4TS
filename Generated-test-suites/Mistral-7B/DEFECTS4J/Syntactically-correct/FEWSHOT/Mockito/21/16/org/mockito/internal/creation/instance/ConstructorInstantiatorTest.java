package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorInstantiatorTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testNewInstanceWithNullOuterClassInstance() {
        Class cls = mock(Class.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);

        Class<?> result = instantiator.newInstance(cls);

        assertNotNull(result);
        assertEquals(cls.getSimpleName(), result.getSimpleName());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNewInstanceWithNotNullOuterClassInstance() {
        Object outerClassInstance = mock(Object.class);
        Class cls = mock(Class.class);

        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);

        Class<?> result = instantiator.newInstance(cls);

        assertNotNull(result);
        assertEquals(cls.getSimpleName(), result.getSimpleName());

        try {
            Constructor<Object> constructor = cls.getDeclaredConstructor();
            InvocationOnMock invocation = mock(InvocationOnMock.class);
            constructor.newInstance(outerClassInstance, invocation);
        } catch (Exception e) {
            // expected, as the constructor is not called in the test
        }
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithClassThatHasNoArgConstructorButNoConstructorWithOuterClass() {
        Class cls = mock(Class.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);

        instantiator.newInstance(cls);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithClassThatHasArgConstructorButNoConstructorWithOuterClass() {
        Class cls = mock(Class.class);
        Constructor<Object> constructor = mock(Constructor.class);
        when(cls.getDeclaredConstructor(isA(Class.class))).thenReturn(constructor);

        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);

        instantiator.newInstance(cls);
    }

    @Test
    public void testNewInstanceWithClassThatHasConstructorWithOuterClass() {
        Class cls = mock(Class.class);
        Constructor<Object> constructor = mock(Constructor.class);
        when(cls.getDeclaredConstructor(isA(Class.class))).thenReturn(constructor);

        Object outerClassInstance = mock(Object.class);

        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);

        Class<?> result = instantiator.newInstance(cls);

        assertNotNull(result);
        assertEquals(cls.getSimpleName(), result.getSimpleName());

        verify(constructor).newInstance(outerClassInstance);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithClassThatHasNoParameterlessConstructor() {
        Class cls = mock(Class.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);

        instantiator.newInstance(cls);
    }
}