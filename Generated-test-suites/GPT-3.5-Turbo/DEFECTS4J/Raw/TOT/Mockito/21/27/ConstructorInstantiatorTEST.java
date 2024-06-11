import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNoArgConstructor() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        SomeClass obj = constructorInstantiator.newInstance(SomeClass.class);
        assertNotNull(obj);
    }

    @Test
    public void testWithOuterClass() {
        SomeOuterClass outerClassInstance = new SomeOuterClass();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        SomeClass obj = constructorInstantiator.newInstance(SomeClass.class);
        assertNotNull(obj);
    }

    @Test
    public void testNoArgConstructorError() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        try {
            SomeOtherClass obj = constructorInstantiator.newInstance(SomeOtherClass.class);
            fail("Expected InstantationException to be thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'SomeOtherClass'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }

    @Test
    public void testWithOuterClassError() {
        SomeOuterClass outerClassInstance = new SomeOuterClass();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        try {
            SomeOtherClass obj = constructorInstantiator.newInstance(SomeOtherClass.class);
            fail("Expected InstantationException to be thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'SomeOtherClass'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }
}