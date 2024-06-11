
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_NoOuterClassInstance_ReturnsNewInstance() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);

        MyClass instance = instantiator.newInstance(MyClass.class);

        assertEquals(MyClass.class, instance.getClass());
    }

    @Test
    public void testNewInstance_WithOuterClassInstance_ReturnsNewInstanceWithOuterClass() {
        MyOuterClass outerClassInstance = new MyOuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);

        MyClass instance = instantiator.newInstance(MyClass.class);

        assertEquals(MyClass.class, instance.getClass());
        assertEquals(outerClassInstance, instance.getOuterClassInstance());
    }

    @Test(expected = InstantationException.class)
    public void testNewInstance_NoArgConstructor_ThrowsExceptionWhenFailsToCreateInstance() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);

        instantiator.newInstance(MyClassWithNoArgConstructor.class);
    }

    @Test(expected = InstantationException.class)
    public void testNewInstance_WithOuterClass_ThrowsExceptionWhenFailsToCreateInstance() {
        MyOuterClass outerClassInstance = new MyOuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);

        instantiator.newInstance(MyClassWithOuterClass.class);
    }

}
