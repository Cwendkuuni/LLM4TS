
import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    private ConstructorInstantiator constructorInstantiator;

    @Test
    public void testNoArgConstructor() {
        constructorInstantiator = new ConstructorInstantiator(null);
        String result = constructorInstantiator.newInstance(String.class);
        assertNotNull(result);
    }

    @Test
    public void testWithOuterClass() {
        constructorInstantiator = new ConstructorInstantiator(new Object());
        Integer result = constructorInstantiator.newInstance(Integer.class);
        assertNotNull(result);
    }

    @Test
    public void testParamsException() {
        constructorInstantiator = new ConstructorInstantiator(new Object());
        try {
            constructorInstantiator.newInstance(Double.class);
            fail("Should throw InstantiationException");
        } catch (InstantiationException e) {
            // Expected exception
        }
    }

    @Test
    public void testNoArgConstructorException() {
        constructorInstantiator = new ConstructorInstantiator(null);
        try {
            constructorInstantiator.newInstance(Double.class);
            fail("Should throw InstantiationException");
        } catch (InstantiationException e) {
            // Expected exception
        }
    }
}
