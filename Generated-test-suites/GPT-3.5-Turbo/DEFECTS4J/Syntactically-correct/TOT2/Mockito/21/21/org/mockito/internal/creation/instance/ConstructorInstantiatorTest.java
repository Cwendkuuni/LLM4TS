import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.creation.instance.ConstructorInstantiator;

public class ConstructorInstantiatorTest {

    @Test
    public void testNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        MyClass obj = instantiator.newInstance(MyClass.class);
        assertNotNull(obj);
    }

    @Test
    public void testWithOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new OuterClass());
        MyClass obj = instantiator.newInstance(MyClass.class);
        assertNotNull(obj);
    }

    @Test
    public void testWithOuterClassException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new OuterClass());
        Exception exception = assertThrows(InstantationException.class, () -> {
            instantiator.newInstance(InvalidClass.class);
        });
        assertEquals("Unable to create mock instance of 'InvalidClass'. Please ensure that the outer instance has correct type and that the target class has parameter-less constructor.", exception.getMessage());
    }

    @Test
    public void testNoArgConstructorException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Exception exception = assertThrows(InstantationException.class, () -> {
            instantiator.newInstance(InvalidClass.class);
        });
        assertEquals("Unable to create mock instance of 'InvalidClass'. Please ensure it has parameter-less constructor.", exception.getMessage());
    }

    // Additional test cases for edge cases and error scenarios can be added here

}