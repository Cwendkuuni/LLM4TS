
import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testConstructorInstantiatorWithOuterClass() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        
        MyClass myInstance = instantiator.newInstance(MyClass.class);
        
        assertNotNull(myInstance);
        assertEquals(outerClassInstance, myInstance.getOuterClassInstance());
    }

    @Test
    public void testConstructorInstantiatorWithoutOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        
        MyClass myInstance = instantiator.newInstance(MyClass.class);
        
        assertNotNull(myInstance);
        assertNull(myInstance.getOuterClassInstance());
    }

    @Test(expected = InstantationException.class)
    public void testConstructorInstantiatorWithIncorrectOuterClassType() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        
        instantiator.newInstance(String.class);
    }

    @Test(expected = InstantationException.class)
    public void testConstructorInstantiatorWithoutParameterlessConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        
        instantiator.newInstance(MyClassWithNonParameterlessConstructor.class);
    }
}
