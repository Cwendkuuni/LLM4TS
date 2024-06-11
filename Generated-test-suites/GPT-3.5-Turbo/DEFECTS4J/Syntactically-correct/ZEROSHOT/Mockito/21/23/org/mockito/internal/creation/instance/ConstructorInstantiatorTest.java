import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.creation.instance.ConstructorInstantiator;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_withNullOuterClassInstance_shouldReturnNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Object instance = instantiator.newInstance(Object.class);
        assertNotNull(instance);
        assertTrue(instance instanceof Object);
    }

    @Test
    public void testNewInstance_withNonNullOuterClassInstance_shouldReturnWithOuterClass() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Object instance = instantiator.newInstance(Object.class);
        assertNotNull(instance);
        assertTrue(instance instanceof Object);
    }

    @Test
    public void testWithOuterClass_withValidClass_shouldReturnInstance() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Object instance = instantiator.withOuterClass(Object.class);
        assertNotNull(instance);
        assertTrue(instance instanceof Object);
    }

    @Test(expected = InstantationException.class)
    public void testWithOuterClass_withInvalidClass_shouldThrowException() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        instantiator.withOuterClass(String.class);
    }

    @Test(expected = InstantationException.class)
    public void testWithOuterClass_withExceptionInInstantiation_shouldThrowException() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        instantiator.withOuterClass(ExceptionClass.class);
    }

    @Test(expected = InstantationException.class)
    public void testNoArgConstructor_withInvalidClass_shouldThrowException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.noArgConstructor(String.class);
    }

    @Test(expected = InstantationException.class)
    public void testNoArgConstructor_withExceptionInInstantiation_shouldThrowException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.noArgConstructor(ExceptionClass.class);
    }

    private static class ExceptionClass {
        public ExceptionClass() throws Exception {
            throw new Exception("Instantiation error");
        }
    }
}