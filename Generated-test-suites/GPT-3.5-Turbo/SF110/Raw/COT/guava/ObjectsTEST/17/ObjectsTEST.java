
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqualWithEqualObjects() {
        Object a = new Object();
        Object b = new Object();
        assertTrue(Objects.equal(a, a));
        assertTrue(Objects.equal(b, b));
        assertTrue(Objects.equal(a, b));
    }

    @Test
    public void testEqualWithNullObjects() {
        Object a = null;
        Object b = null;
        assertTrue(Objects.equal(a, a));
        assertTrue(Objects.equal(b, b));
        assertTrue(Objects.equal(a, b));
    }

    @Test
    public void testEqualWithDifferentObjects() {
        Object a = new Object();
        Object b = new Object();
        assertFalse(Objects.equal(a, b));
    }

    @Test
    public void testHashCodeWithNullObjects() {
        Object[] objects = null;
        assertEquals(Objects.hashCode(objects), 0);
    }

    @Test
    public void testHashCodeWithEmptyObjects() {
        Object[] objects = new Object[0];
        assertEquals(Objects.hashCode(objects), 1);
    }

    @Test
    public void testHashCodeWithObjects() {
        Object[] objects = new Object[3];
        objects[0] = new Object();
        objects[1] = new Object();
        objects[2] = new Object();
        assertEquals(Objects.hashCode(objects), Arrays.hashCode(objects));
    }

    @Test
    public void testToStringHelper() {
        Object object = new Object();
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper(object);
        assertNotNull(toStringHelper);
    }

    @Test
    public void testToStringHelperWithClass() {
        Class<?> clazz = this.getClass();
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper(clazz);
        assertNotNull(toStringHelper);
    }

    @Test
    public void testToStringHelperWithString() {
        String className = "Test";
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper(className);
        assertNotNull(toStringHelper);
    }

    @Test
    public void testFirstNonNullWithBothNull() {
        Object first = null;
        Object second = null;
        assertNull(Objects.firstNonNull(first, second));
    }

    @Test
    public void testFirstNonNullWithFirstNull() {
        Object first = null;
        Object second = new Object();
        assertNotNull(Objects.firstNonNull(first, second));
        assertSame(Objects.firstNonNull(first, second), second);
    }

    @Test
    public void testFirstNonNullWithSecondNull() {
        Object first = new Object();
        Object second = null;
        assertNotNull(Objects.firstNonNull(first, second));
        assertSame(Objects.firstNonNull(first, second), first);
    }

    @Test
    public void testFirstNonNullWithBothNotNull() {
        Object first = new Object();
        Object second = new Object();
        assertNotNull(Objects.firstNonNull(first, second));
        assertNotSame(Objects.firstNonNull(first, second), first);
        assertNotSame(Objects.firstNonNull(first, second), second);
    }

    @Test
    public void testToStringHelperOmitNullValues() {
        Object object = new Object();
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper(object)
                .omitNullValues();
        assertNotNull(toStringHelper);
    }

    @Test
    public void testToStringHelperAdd() {
        Object object1 = new Object();
        Object object2 = new Object();
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper(object1)
                .add("name1", object1)
                .add("name2", object2);
        assertNotNull(toStringHelper);
    }

    @Test
    public void testToStringHelperAddValue() {
        Object object = new Object();
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper(object)
                .addValue("value");
        assertNotNull(toStringHelper);
    }

    @Test
    public void testToStringHelperAddValueWithBoolean() {
        Object object = new Object();
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper(object)
                .addValue(true);
        assertNotNull(toStringHelper);
    }

    @Test
    public void testToStringHelperAddValueWithChar() {
        Object object = new Object();
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper(object)
                .addValue('c');
        assertNotNull(toStringHelper);
    }

    @Test
    public void testToStringHelperAddValueWithDouble() {
        Object object = new Object();
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper(object)
                .addValue(3.14);
        assertNotNull(toStringHelper);
    }

    @Test
    public void testToStringHelperAddValueWithFloat() {
        Object object = new Object();
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper(object)
                .addValue(3.14f);
        assertNotNull(toStringHelper);
    }

    @Test
    public void testToStringHelperAddValueWithInt() {
        Object object = new Object();
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper(object)
                .addValue(42);
        assertNotNull(toStringHelper);
    }

    @Test
    public void testToStringHelperAddValueWithLong() {
        Object object = new Object();
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper(object)
                .addValue(42L);
        assertNotNull(toStringHelper);
    }

    @Test
    public void testToStringHelperToString() {
        Object object1 = new Object();
        Object object2 = new Object();
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper(object1)
                .add("name1", object1)
                .add("name2", object2);
        assertNotNull(toStringHelper.toString());
    }
}
