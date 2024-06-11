package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.junit.runners.MockitoJUnitRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.internal.util.reflection.Reflections.getDeclaredField;
import static org.mockito.internal.util.reflection.Reflections.setAccessible;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndParameterLessClass() {
        ConstructorInstantiator instantIator = new ConstructorInstantiator(null);
        assertInstanceOf(String.class, instantIator.newInstance(String.class));
    }

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndClassWithConstructor() {
        ConstructorInstantiator instantIator = new ConstructorInstantiator(null);
        assertThrows(InstantiationException.class, () -> instantIator.newInstance(CustomClassWithConstructor.class));
    }

    @Test
    public void testNewInstanceWithNonNullOuterClassInstanceAndParameterLessClass() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantIator = new ConstructorInstantiator(outerClassInstance);
        assertInstanceOf(String.class, instantIator.newInstance(String.class));
    }

    @Test
    public void testNewInstanceWithNonNullOuterClassInstanceAndClassWithConstructor() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantIator = new ConstructorInstantiator(outerClassInstance);
        Class<CustomClassWithConstructor> cls = CustomClassWithConstructor.class;

        FieldSetter.setAccessible(getDeclaredField(ConstructorInstantiator.class, "noArgConstructor"), instantIator);
        Constructor<CustomClassWithConstructor> constructor = cls.getDeclaredConstructor();

        CustomClassWithConstructor instance = instantIator.withOuterClass(cls);
        assertInstanceOf(CustomClassWithConstructor.class, instance);
        assertSame(outerClassInstance, constructor.newInstance(outerClassInstance).outerClassInstance);
    }

    @Test
    public void testNewInstanceWithNonNullOuterClassInstanceAndClassWithNoDefaultConstructor() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantIator = new ConstructorInstantiator(outerClassInstance);
        Class<CustomClassWithNoDefaultConstructor> cls = CustomClassWithNoDefaultConstructor.class;

        assertThrows(InstantiationException.class, () -> instantIator.newInstance(cls));
    }

    static class CustomClassWithConstructor {
        CustomClassWithConstructor(Object outerClassInstance) {
        }
        Object outerClassInstance;
    }

    static class CustomClassWithNoDefaultConstructor {
        // no default constructor
    }
}