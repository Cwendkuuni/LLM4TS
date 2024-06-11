import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNoArgConstructor_InstanceIsNull_ReturnsInstance() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);

        MyClass result = instantiator.newInstance(MyClass.class);

        assertNotNull(result);
        assertTrue(result instanceof MyClass);
    }

    @Test
    public void testNoArgConstructor_InstanceIsNotNull_ReturnsException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());

        try {
            instantiator.newInstance(MyClass.class);
            fail("Expected InstantationException to be thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'MyClass'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }

    @Test
    public void testWithOuterClass_InstanceIsNull_ReturnsException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);

        try {
            instantiator.newInstance(MyClass.class);
            fail("Expected InstantationException to be thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'MyClass'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }

    @Test
    public void testWithOuterClass_InstanceIsNotNullAndOuterClassTypeMismatch_ReturnsException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator("Invalid Outer Class");

        try {
            instantiator.newInstance(MyClass.class);
            fail("Expected InstantationException to be thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'MyClass'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }

    @Test
    public void testWithOuterClass_InstanceIsNotNullAndOuterClassTypeMatch_ReturnsInstance() {
        OuterClass outerClass = new OuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClass);

        MyClass result = instantiator.newInstance(MyClass.class);

        assertNotNull(result);
        assertTrue(result instanceof MyClass);
        assertEquals(outerClass, result.getOuterClass());
    }

    private class MyClass {
        private OuterClass outerClass;
        
        public OuterClass getOuterClass() {
            return outerClass;
        }
    }

    private class OuterClass {
        
    }
}