import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {
    @Test
    public void testConstructor() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass);
        assertEquals("int", intType.getGenericSignature().toString());
        assertFalse(intType.isContainerType());
    }
}