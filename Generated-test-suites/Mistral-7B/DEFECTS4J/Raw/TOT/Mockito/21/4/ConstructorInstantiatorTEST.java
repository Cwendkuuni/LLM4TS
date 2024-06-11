import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class ConstructorInstantiatorTest {

    @Test
    public void testNoArgConstructor() {
        ConstructorInstantiator instantIator = new ConstructorInstantiator(null);
        Mockito.doNothing().when(instantIator); // mock doNothing() method for testing purposes

        Class<Object> objectClass = Object.class;
        Object instance = instantIator.newInstance(objectClass);
        Assert.assertNotNull(instance);

        Object mockedInstance = Mockito.spy(instance);
        Whitebox.setInternalValue(mockedInstance, "testValue", "mockValue");

        Assert.assertEquals("mockValue", Whitebox.getInternalValue(mockedInstance, "testValue"));
    }

    @Test
    public void testInstanceWithOuterClass() {
        Object outerClassInstance = Mockito.mock(SomeClass.class);
        ConstructorInstantiator instantIator = new ConstructorInstantiator(outerClassInstance);

        Class<SomeClass> someClass = SomeClass.class;
        SomeClass instance = instantIator.newInstance(someClass);
        Assert.assertNotNull(instance);
        Assert.assertSame(outerClassInstance, instance.getOuterClassInstance());
    }

    @Test(expected = InstantiationException.class)
    public void testExceptionWithOuterClass() {
        Object outerClassInstance = Mockito.mock(SomeClass.class);
        ConstructorInstantiator instantIator = new ConstructorInstantiator(outerClassInstance);

        Class<SomeOtherClass> someOtherClass = SomeOtherClass.class;
        try {
            instantIator.newInstance(someOtherClass);
        } catch (InstantiationException e) {
            Assert.assertEquals("Unable to create mock instance of 'SomeOtherClass'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
            throw e;
        }
    }

    @Test(expected = InstantiationException.class)
    public void testExceptionNoArgConstructor() {
        ConstructorInstantiator instantIator = new ConstructorInstantiator(null);
        Class<SomeClass> someClass = SomeClass.class;
        try {
            instantIator.newInstance(someClass);
        } catch (InstantiationException e) {
            Assert.assertEquals("Unable to create mock instance of 'SomeClass'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
            throw e;
        }
    }
}

class SomeClass {
    private Object outerClassInstance;

    public SomeClass(Object outerClassInstance) {
        this.outerClassInstance = outerClassInstance;
    }

    public Object getOuterClassInstance() {
        return outerClassInstance;
    }
}

class SomeOtherClass {
    // This class should not have a parameter-less constructor
}