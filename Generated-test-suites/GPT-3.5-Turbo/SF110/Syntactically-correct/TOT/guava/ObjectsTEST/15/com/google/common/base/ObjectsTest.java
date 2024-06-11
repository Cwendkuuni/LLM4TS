
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual_sameObjects() {
        Object a = new Object();
        Object b = a;
        assertTrue(Objects.equal(a, b));
    }

    @Test
    public void testEqual_nullObjects() {
        Object a = null;
        Object b = null;
        assertTrue(Objects.equal(a, b));
    }

    @Test
    public void testEqual_oneNullObject() {
        Object a = new Object();
        Object b = null;
        assertFalse(Objects.equal(a, b));
    }

    @Test
    public void testEqual_differentObjects() {
        Object a = new Object();
        Object b = new Object();
        assertFalse(Objects.equal(a, b));
    }

    @Test
    public void testHashCode_noObjects() {
        int expected = 0;
        int actual = Objects.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode_singleObject() {
        Object[] objects = {new Object()};
        int expected = objects[0].hashCode();
        int actual = Objects.hashCode(objects);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode_sameObjects() {
        Object[] objects = {new Object(), new Object()};
        int expected = objects[0].hashCode() ^ objects[1].hashCode();
        int actual = Objects.hashCode(objects);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode_oneNullObject() {
        Object[] objects = {null};
        int expected = 0;
        int actual = Objects.hashCode(objects);
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringHelper_simpleObject() {
        Object obj = new Object();
        String expected = obj.toString();
        String actual = Objects.toStringHelper(obj).toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringHelper_classObject() {
        Class<?> clazz = Object.class;
        String expected = clazz.getSimpleName();
        String actual = Objects.toStringHelper(clazz).toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringHelper_stringObject() {
        String className = "CustomClass";
        String expected = className;
        String actual = Objects.toStringHelper(className).toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testFirstNonNull_firstNotNull() {
        String first = "first";
        String second = "second";
        String expected = first;
        String actual = Objects.firstNonNull(first, second);
        assertEquals(expected, actual);
    }

    @Test
    public void testFirstNonNull_firstNull() {
        String first = null;
        String second = "second";
        String expected = second;
        String actual = Objects.firstNonNull(first, second);
        assertEquals(expected, actual);
    }

    @Test
    public void testFirstNonNull_bothNull() {
        String first = null;
        String second = null;
        String expected = null;
        String actual = Objects.firstNonNull(first, second);
        assertEquals(expected, actual);
    }

}
