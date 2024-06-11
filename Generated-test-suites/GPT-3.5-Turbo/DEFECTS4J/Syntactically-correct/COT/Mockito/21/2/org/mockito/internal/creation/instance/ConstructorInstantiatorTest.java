import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {
    
    @Test
    public void testConstructorInstantiatorWithNullOuterClassInstance() {
        Object outerClassInstance = null;
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        assertNull(constructorInstantiator.outerClassInstance);
    }
    
    @Test
    public void testConstructorInstantiatorWithNonNullOuterClassInstance() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        assertNotNull(constructorInstantiator.outerClassInstance);
    }
}