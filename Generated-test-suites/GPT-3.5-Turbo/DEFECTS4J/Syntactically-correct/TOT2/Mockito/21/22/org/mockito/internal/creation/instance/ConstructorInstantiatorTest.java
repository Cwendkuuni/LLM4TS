import static org.junit.Assert.*;
import org.junit.Test;

public class ConstructorInstantiatorTest {

    private ConstructorInstantiator constructorInstantiator;

    @Before
    public void setUp() {
        constructorInstantiator = new ConstructorInstantiator(new Object());
    }

    @Test
    public void testNewInstance_NoOuterClass() {
        assertNull(constructorInstantiator.newInstance(String.class));
    }

    @Test
    public void testNewInstance_WithOuterClass() {
        assertNotNull(constructorInstantiator.newInstance(Integer.class));
    }

    @Test
    public void testWithOuterClass_Exception() {
        try {
            constructorInstantiator.withOuterClass(String.class);
            fail("Exception not thrown");
        } catch (InstantationException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testNoArgConstructor_Exception() {
        try {
            constructorInstantiator.noArgConstructor(Object.class);
            fail("Exception not thrown");
        } catch (InstantationException e) {
            assertNotNull(e);
        }
    }

    @Test(expected = InstantationException.class)
    public void testNoArgConstructor_Exception_Shortcut() {
        constructorInstantiator.noArgConstructor(String.class);
    }

}