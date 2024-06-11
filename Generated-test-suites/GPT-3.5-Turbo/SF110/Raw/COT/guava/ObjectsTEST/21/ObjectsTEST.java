
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual() {
        Object a = "Hello";
        Object b = "World";
        assertFalse(Objects.equal(a, b));
        assertEquals(Objects.equal(a, a), true);
        assertEquals(Objects.equal(null, null), true);
    }

    @Test
    public void testHashCode() {
        Object[] objects = {"Hello", "World"};
        assertEquals(Objects.hashCode(objects), -1136351995);
        assertEquals(Objects.hashCode(null), 0);
    }

    @Test
    public void testToStringHelper() {
        Object obj = new Object();
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper(obj);
        String expectedString = obj.getClass().getSimpleName() + "{}";
        assertEquals(toStringHelper.toString(), expectedString);
    }

    @Test
    public void testFirstNonNull() {
        Object first = "Hello";
        Object second = "World";
        assertEquals(Objects.firstNonNull(first, second), first);
        assertEquals(Objects.firstNonNull(null, second), second);
    }
}
