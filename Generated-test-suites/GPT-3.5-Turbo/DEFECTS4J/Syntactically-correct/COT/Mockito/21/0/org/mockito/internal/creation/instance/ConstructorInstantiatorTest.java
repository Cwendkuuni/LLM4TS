import static org.junit.Assert.*;
import org.junit.Test;

public class ConstructorInstantiatorTest {

    @Test
    public void testConstructorInstantiator() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        
        assertNotNull(constructorInstantiator);
        assertEquals(outerClassInstance, constructorInstantiator.getOuterClassInstance());
    }
}