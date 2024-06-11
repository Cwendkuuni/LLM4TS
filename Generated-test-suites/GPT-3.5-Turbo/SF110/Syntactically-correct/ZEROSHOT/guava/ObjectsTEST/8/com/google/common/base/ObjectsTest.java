import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        boolean result = Objects.equal(null, null);
        assertEquals(true, result);

        result = Objects.equal(null, "test");
        assertEquals(false, result);

        result = Objects.equal("test", null);
        assertEquals(false, result);

        result = Objects.equal("test", "test");
        assertEquals(true, result);

        result = Objects.equal("test1", "test2");
        assertEquals(false, result);
    }

    @Test
    public void testHashCode() {
        int result = Objects.hashCode();
        assertEquals(0, result);

        result = Objects.hashCode(null);
        assertEquals(0, result);

        result = Objects.hashCode("test1", "test2");
        assertEquals(-1364096937, result);
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("className");
        helper.addValue(true);
        String result = helper.toString();
        assertEquals("className{true}", result);
    }

    @Test
    public void testFirstNonNull() {
        Integer result = Objects.firstNonNull(null, 5);
        assertEquals(Integer.valueOf(5), result);

        result = Objects.firstNonNull(10, null);
        assertEquals(Integer.valueOf(10), result);

        result = Objects.firstNonNull(15, 20);
        assertEquals(Integer.valueOf(15), result);
    }
}