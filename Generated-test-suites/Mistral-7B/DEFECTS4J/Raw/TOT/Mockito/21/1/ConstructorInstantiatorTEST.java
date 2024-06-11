package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.mockito.exceptions.base.InstantiationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndParameterlessClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        assertThrows(InstantiationException.class, () -> instantiator.newInstance(String.class));
    }

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndNonParameterlessClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        assertThrows(InstantiationException.class, () -> instantiator.newInstance(OuterClassWithConstructor.class));
    }

    @Test
    public void testNewInstanceWithValidOuterClassInstanceAndParameterlessClass() {
        Object outerInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        String instance = instantiator.newInstance(String.class);
        assertNotNull(instance);
    }

    @Test
    public void testNewInstanceWithValidOuterClassInstanceAndClassWithConstructor() {
        Object outerInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        OuterClassWithConstructor outerClassInstance = instantiator.newInstance(OuterClassWithConstructor.class);
        assertNotNull(outerClassInstance);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithValidOuterClassInstanceAndClassWithNoConstructor() {
        Object outerInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        instantiator.newInstance(NoConstructorClass.class);
    }

    @Test
    public void testNewInstanceWithInvalidOuterClassInstanceType() {
        Object outerInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        assertThrows(InstantiationException.class, () -> instantiator.newInstance(Int.class));
    }

    @Test
    public void testParamsException() {
        Object outerInstance = new Object();
        Class<OuterClassWithConstructor> cls = OuterClassWithConstructor.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        assertThrows(InstantiationException.class, () -> instantiator.withOuterClass(cls));

        InstantiationException exception = assertThrows(InstantiationException.class, () -> instantiator.newInstance(cls));
        String expectedMessage = "Unable to create mock instance of 'OuterClassWithConstructor'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.";
        String actualMessage = exception.getMessage().substring(0, expectedMessage.length());
        assertTrue(actualMessage.startsWith(expectedMessage));
    }

    @Test
    public void testNoArgConstructorException() {
        Class<NoArgConstructorClass> cls = NoArgConstructorClass.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        assertThrows(InstantiationException.class, () -> instantiator.noArgConstructor(cls));

        InstantiationException exception = assertThrows(InstantiationException.class, () -> instantiator.newInstance(cls));
        String expectedMessage = "Unable to create mock instance of 'NoArgConstructorClass'.\nPlease ensure it has parameter-less constructor.";
        String actualMessage = exception.getMessage().substring(0, expectedMessage.length());
        assertTrue(actualMessage.startsWith(expectedMessage));
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClassException() {
        Object outerInstance = new Object();
        Class<OuterClassWithNoConstructor> cls = OuterClassWithNoConstructor.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        instantiator.withOuterClass(cls);
    }

    private static class OuterClassWithConstructor {
        OuterClassWithConstructor() {}
    }

    private static class OuterClassWithNoConstructor {
        // empty class
    }

    private static class NoArgConstructorClass {
        NoArgConstructorClass() throws Exception {
            throw new InstantiationException("Without constructor exception.");
        }
    }
}