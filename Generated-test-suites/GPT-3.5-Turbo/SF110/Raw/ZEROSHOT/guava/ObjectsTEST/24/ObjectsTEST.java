import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertFalse(Objects.equal(null, "abc"));
        assertFalse(Objects.equal("abc", null));
        assertTrue(Objects.equal("abc", "abc"));
        assertFalse(Objects.equal("abc", "def"));
    }

    @Test
    public void testHashCode() {
        Object[] objects = null;
        assertEquals(0, Objects.hashCode(objects));

        objects = new Object[]{1, "abc", true};
        assertEquals(1138028728, Objects.hashCode(objects));
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper("Test");

        assertNotNull(toStringHelper);

        toStringHelper.addValue("abc");
        assertEquals("Test{abc}", toStringHelper.toString());

        toStringHelper.add("name", "John");
        assertEquals("Test{name=John, abc}", toStringHelper.toString());

        toStringHelper.addValue(null);
        assertEquals("Test{name=John, abc}", toStringHelper.toString());

        toStringHelper.omitNullValues();
        assertEquals("Test{name=John, abc}", toStringHelper.toString());
    }

    @Test
    public void testFirstNonNull() {
        String first = null;
        String second = "abc";
        assertEquals("abc", Objects.firstNonNull(first, second));

        first = "def";
        second = null;
        assertEquals("def", Objects.firstNonNull(first, second));

        first = "ghi";
        second = "jkl";
        assertEquals("ghi", Objects.firstNonNull(first, second));
    }
}