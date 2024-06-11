import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantationException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ConstructorInstantiatorTest {
    
    private ConstructorInstantiator constructorInstantiator;
    
    @Before
    public void setUp() {
        constructorInstantiator = new ConstructorInstantiator(null);
    }
    
    @Test
    public void newInstance_withNullObject_shouldCallNoArgConstructor() {
        // Arrange
        Class<MyClass> cls = MyClass.class;

        // Act
        MyClass instance = constructorInstantiator.newInstance(cls);

        // Assert
        assertNotNull(instance);
        assertTrue(instance.getClass().equals(cls));
    }
    
    @Test
    public void newInstance_withNonNullObject_shouldCallWithOuterClassConstructor() {
        // Arrange
        Object outerInstance = mock(Object.class);
        constructorInstantiator = new ConstructorInstantiator(outerInstance);
        Class<MyClass> cls = MyClass.class;

        // Act
        MyClass instance = constructorInstantiator.newInstance(cls);

        // Assert
        assertNotNull(instance);
        assertTrue(instance.getClass().equals(cls));
        assertEquals(outerInstance, instance.getOuterClassInstance());
    }
    
    @Test
    public void newInstance_withClassHavingNoArgConstructor_shouldReturnInstance() {
        // Arrange
        Class<ClassWithNoArgConstructor> cls = ClassWithNoArgConstructor.class;

        // Act
        ClassWithNoArgConstructor instance = constructorInstantiator.newInstance(cls);

        // Assert
        assertNotNull(instance);
        assertTrue(instance.getClass().equals(cls));
    }
    
    @Test(expected = InstantationException.class)
    public void newInstance_withClassHavingNoArgConstructor_shouldThrowException() {
        // Arrange
        Class<ClassWithoutNoArgConstructor> cls = ClassWithoutNoArgConstructor.class;

        // Act
        constructorInstantiator.newInstance(cls);

        // Assert
        // Exception expected
    }
    
    @Test(expected = InstantationException.class)
    public void newInstance_withClassHavingWithOuterClassConstructor_shouldThrowException() {
        // Arrange
        Class<ClassWithOuterClassConstructor> cls = ClassWithOuterClassConstructor.class;

        // Act
        constructorInstantiator.newInstance(cls);

        // Assert
        // Exception expected
    }
    
    private static class MyClass {
        private final Object outerClassInstance;
        
        public MyClass(Object outerClassInstance) {
            this.outerClassInstance = outerClassInstance;
        }
        
        public Object getOuterClassInstance() {
            return outerClassInstance;
        }
    }
    
    private static class ClassWithNoArgConstructor {
        public ClassWithNoArgConstructor() {
            // No implementation needed
        }
    }
    
    private static class ClassWithoutNoArgConstructor {
        public ClassWithoutNoArgConstructor(int arg) {
            // No implementation needed
        }
    }
    
    private static class ClassWithOuterClassConstructor {
        public ClassWithOuterClassConstructor(Object outerClassInstance) {
            // No implementation needed
        }
    }
}