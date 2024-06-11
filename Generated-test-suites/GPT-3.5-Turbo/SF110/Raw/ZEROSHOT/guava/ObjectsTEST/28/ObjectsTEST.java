import static org.junit.Assert.*;
import org.junit.Test;
import com.google.common.base.Objects.*;
import javax.annotation.Nullable;

public class ObjectsTest {

    @Test
    public void testEqual_sameObject() {
        Object obj = new Object();
        assertTrue(Objects.equal(obj, obj));
    }

    @Test
    public void testEqual_equalObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(Objects.equal(obj1, obj2));
    }

    @Test
    public void testEqual_null() {
        Object obj = new Object();
        assertFalse(Objects.equal(obj, null));
    }

    @Test
    public void testEqual_bothNull() {
        assertTrue(Objects.equal(null, null));
    }

    @Test
    public void testHashCode_emptyArray() {
        Object[] objects = new Object[0];
        int expectedHash = java.util.Arrays.hashCode(objects);
        assertEquals(expectedHash, Objects.hashCode(objects));
    }

    @Test
    public void testHashCode_singleObject() {
        Object obj = new Object();
        int expectedHash = java.util.Arrays.hashCode(new Object[] { obj });
        assertEquals(expectedHash, Objects.hashCode(obj));
    }

    @Test
    public void testHashCode_multipleObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        int expectedHash = java.util.Arrays.hashCode(new Object[] { obj1, obj2 });
        assertEquals(expectedHash, Objects.hashCode(obj1, obj2));
    }

    @Test
    public void testToStringHelper_omitNullValues() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass").omitNullValues();
        assertNotNull(helper);
        // additional tests can be added to assert other methods of ToStringHelper
    }

    @Test
    public void testFirstNonNull_bothNonNull() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertEquals(obj1, Objects.firstNonNull(obj1, obj2));
    }

    @Test
    public void testFirstNonNull_firstNull() {
        Object obj2 = new Object();
        assertEquals(obj2, Objects.firstNonNull(null, obj2));
    }

    @Test
    public void testFirstNonNull_secondNull() {
        Object obj1 = new Object();
        assertEquals(obj1, Objects.firstNonNull(obj1, null));
    }

    @Test
    public void testFirstNonNull_bothNull() {
        assertNull(Objects.firstNonNull(null, null));
    }
}