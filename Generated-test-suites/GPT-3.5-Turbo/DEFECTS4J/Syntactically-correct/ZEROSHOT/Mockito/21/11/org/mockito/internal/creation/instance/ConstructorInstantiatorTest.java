import org.junit.Test;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantationException;

import java.lang.reflect.Constructor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_withNoArgConstructor() throws Exception {
        // Arrange
        Class<?> cls = MyClass.class; // replace MyClass with the actual class name

        // Act
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Object instance = instantiator.newInstance(cls);

        // Assert
        assertEquals(cls, instance.getClass());
        verifyZeroInteractions(cls); // replace cls with the actual mockito mock object
    }

    @Test
    public void testNewInstance_withOuterClass() throws Exception {
        // Arrange
        Class<?> cls = MyClass.class; // replace MyClass with the actual class name
        Object outerClassInstance = mock(Object.class); // replace Object with the actual outer class instance

        // Act
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Object instance = instantiator.newInstance(cls);

        // Assert
        assertEquals(cls, instance.getClass());
        verifyZeroInteractions(cls); // replace cls with the actual mockito mock object
    }

    @Test(expected = InstantationException.class)
    public void testNewInstance_withOuterClass_throwsException() throws Exception {
        // Arrange
        Class<?> cls = MyClass.class; // replace MyClass with the actual class name
        Object outerClassInstance = mock(Object.class); // replace Object with the actual outer class instance

        // Act
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        instantiator.newInstance(cls);
    }

    private static class MyClass {
        // add any necessary fields and methods here
    }
}