import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_withOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        MyClass myClass = instantiator.newInstance(MyClass.class);
        assertNotNull(myClass);
    }

    @Test
    public void testNewInstance_noOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        MyClass myClass = instantiator.newInstance(MyClass.class);
        assertNotNull(myClass);
    }

    @Test(expected = InstantationException.class)
    public void testNewInstance_withOuterClass_Exception() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        instantiator.newInstance(InvalidClass.class);
    }

    @Test(expected = InstantationException.class)
    public void testNewInstance_noArgConstructor_Exception() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        instantiator.newInstance(NoArgConstructorClass.class);
    }

    public static class MyClass {}

    public static class InvalidClass {}

    public static class NoArgConstructorClass {
        public NoArgConstructorClass(String arg) {}
    }
}