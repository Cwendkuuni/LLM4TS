import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleType() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass);
        assertEquals("Expected SimpleType instance", intType.getClass(), SimpleType.class);
    }

    /**
     * Edge Case: Test with null class
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullClass() {
        SimpleType nullType = new SimpleType(null);
    }
}