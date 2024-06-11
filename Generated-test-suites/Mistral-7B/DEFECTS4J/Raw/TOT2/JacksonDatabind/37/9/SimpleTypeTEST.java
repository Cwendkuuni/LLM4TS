import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {
    // Test for protected SimpleType(Class<?> cls)

    @Test
    public void testSimpleTypeConstructionWithClass() {
        Class<?> testClass = String.class;
        SimpleType simpleType = new SimpleType(testClass);
        assertEquals(testClass, simpleType.getRawClass());
    }
}