import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_withOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        MyClass instance = instantiator.newInstance(MyClass.class);
        assertNotNull(instance);
    }

    @Test
    public void testNewInstance_noArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        MyClass instance = instantiator.newInstance(MyClass.class);
        assertNotNull(instance);
    }

    @Test
    public void testWithOuterClass_exception() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        try {
            instantiator.withOuterClass(MyClass.class);
            fail("Expected Exception not thrown");
        } catch (Exception e) {
            assertEquals(InstantationException.class, e.getClass());
        }
    }

    @Test
    public void testParamsException() {
        try {
            ConstructorInstantiator.paramsException(MyClass.class, new Exception());
            fail("Expected Exception not thrown");
        } catch (InstantationException e) {
            //expected
        }
    }

    @Test
    public void testNoArgConstructor_exception() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.noArgConstructor(MyClass.class);
            fail("Expected Exception not thrown");
        } catch (Exception e) {
            assertEquals(InstantationException.class, e.getClass());
        }
    }

    @Test
    public void testNoArgConstructor_exceptionHandled() {
        try {
            ConstructorInstantiator.noArgConstructor(MyClass.class);
        } catch (InstantationException e) {
            //expected
        }
    }

}

class MyClass {}