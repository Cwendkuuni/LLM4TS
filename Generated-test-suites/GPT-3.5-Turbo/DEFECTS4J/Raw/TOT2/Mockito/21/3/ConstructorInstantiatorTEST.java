import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_noOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Object instance = instantiator.newInstance(Object.class);
        assertNotNull(instance);
    }

    @Test
    public void testNewInstance_withOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        Object instance = instantiator.newInstance(Object.class);
        assertNotNull(instance);
    }

    @Test
    public void testWithOuterClass_success() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        Object instance = instantiator.withOuterClass(Object.class);
        assertNotNull(instance);
    }

    @Test
    public void testNoArgConstructor_success() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Object instance = instantiator.noArgConstructor(Object.class);
        assertNotNull(instance);
    }

    @Test(expected = InstantationException.class)
    public void testWithOuterClass_exception() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        instantiator.withOuterClass(String.class);
    }

    @Test(expected = InstantationException.class)
    public void testNoArgConstructor_exception() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.noArgConstructor(String.class);
    }

}