package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Constructor;

import org.junit.Test;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_NullOuterClassInstance_ReturnsNoArgConstructor() {
        Class<Foo> fooClass = Foo.class;
        InstantiationException exception = assertThrows(InstantiationException.class, () -> new ConstructorInstantiator(null).newInstance(fooClass));
        assertEquals("Unable to create mock instance of 'Foo'.\nPlease ensure it has parameter-less constructor.", exception.getMessage());
        assertNotNull(ConstructorInstantiator.noArgConstructor(fooClass));
    }

    @Test
    public void testNewInstance_NullOuterClassInstance_ThrowsException() {
        Class<Foo> fooClass = Foo.class;
        InstantiationException exception = assertThrows(InstantiationException.class, () -> new ConstructorInstantiator(null).newInstance(fooClass));
        assertEquals("Unable to create mock instance of 'Foo'.\nPlease ensure it has parameter-less constructor.", exception.getMessage());
    }

    @Test
    public void testNewInstance_NullClass_ThrowsException() {
        Object outerClassInstance = mock(Bar.class);
        InstantiationException exception = assertThrows(InstantiationException.class, () -> new ConstructorInstantiator(outerClassInstance).newInstance(null));
        assertEquals("Unable to create mock instance of null.\nPlease provide a valid class.", exception.getMessage());
    }

    @Test
    public void testNewInstance_HasConstructorWithOuterClass_ReturnsInstance() {
        Object outerClassInstance = mock(Bar.class);
        Class<Foo> fooClass = Foo.class;
        Foo instance = new ConstructorInstantiator(outerClassInstance).newInstance(fooClass);
        assertNotNull(instance);
    }

    @Test
    public void testNewInstance_NoConstructorWithOuterClass_ThrowsException() {
        Object outerClassInstance = mock(Bar.class);
        Class<Baz> bazClass = Baz.class;
        InstantiationException exception = assertThrows(InstantiationException.class, () -> new ConstructorInstantiator(outerClassInstance).newInstance(bazClass));
        assertEquals("Unable to create mock instance of 'Baz'.\nPlease ensure that the class has a constructor that accepts an instance of type: org.mockito.internal.Bar.", exception.getMessage());
    }

    @Test
    public void testWithOuterClass_HasConstructor_ReturnsInstance() {
        Object outerClassInstance = mock(Bar.class);
        Class<Foo> fooClass = Foo.class;
        Foo instance = new ConstructorInstantiator(outerClassInstance).withOuterClass(fooClass);
        assertNotNull(instance);
    }

    @Test
    public void testWithOuterClass_NoConstructor_ThrowsException() {
        Object outerClassInstance = mock(Bar.class);
        Class<Baz> bazClass = Baz.class;
        InstantiationException exception = assertThrows(InstantantionException.class, () -> new ConstructorInstantiator(outerClassInstance).withOuterClass(bazClass));
        assertEquals("Unable to create mock instance of 'Baz'.\nPlease ensure that the class has a constructor that accepts an instance of type: org.mockito.internal.Bar.", exception.getMessage());
    }

    @Test
    public void testParamsException_HasMessage_ReturnsException() {
        Class<Foo> fooClass = Foo.class;
        Exception exception = new RuntimeException();
        InstantiationException instException = new ConstructorInstantiator().paramsException(fooClass, exception);
        assertNotNull(instException);
        assertEquals("Unable to create mock instance of 'Foo'.\nPlease ensure that the target class has parameter-less constructor.", instException.getMessage());
    }

    @Test
    public void testParamsException_NullClass_ThrowsException() {
        Exception exception = new RuntimeException();
        InstantiationException instException = new ConstructorInstantiator().paramsException(null, exception);
        assertNotNull(instException);
        assertEquals("Unable to create mock instance of null.\nPlease provide a valid class.", instException.getMessage());
    }

    @Test
    public void testNoArgConstructor_HasInstance_ReturnsInstance() {
        Class<Foo> fooClass = Foo.class;
        Foo instance = ConstructorInstantiator.noArgConstructor(fooClass);
        assertNotNull(instance);
    }

    @Test
    public void testNoArgConstructor_NullClass_ThrowsException() {
        InstantiationException exception = assertThrows(InstantiationException.class, () -> ConstructorInstantiator.noArgConstructor(null));
        assertEquals("Unable to create mock instance of null.\nPlease provide a valid class.", exception.getMessage());
    }

    private static class Foo {}
    private static class Bar {}
    private static class Baz {}
}