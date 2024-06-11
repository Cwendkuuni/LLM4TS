import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqualWithEqualObjects() {
        assertTrue(Objects.equal("hello", "hello"));
    }

    @Test
    public void testEqualWithNullObjects() {
        assertTrue(Objects.equal(null, null));
    }

    @Test
    public void testEqualWithUnequalObjects() {
        assertFalse(Objects.equal("hello", "world"));
    }

    @Test
    public void testEqualWithOneNullObject() {
        assertFalse(Objects.equal("hello", null));
    }

    @Test
    public void testEqualWithDifferentTypes() {
        assertFalse(Objects.equal("hello", 10));
    }

    @Test
    public void testHashCodeWithNullObjects() {
        int expected = Arrays.hashCode(new Object[] {null, null});
        assertEquals(expected, Objects.hashCode(null, null));
    }

    @Test
    public void testHashCodeWithNonNullObjects() {
        int expected = Arrays.hashCode(new Object[] {"hello", "world"});
        assertEquals(expected, Objects.hashCode("hello", "world"));
    }

    @Test
    public void testToStringHelperWithNullObject() {
        String expected = "null";
        assertEquals(expected, Objects.toStringHelper(null).toString());
    }

    @Test
    public void testToStringHelperWithClassObject() {
        String expected = "ClassName{}";
        assertEquals(expected, Objects.toStringHelper(Objects.class).toString());
    }

    @Test
    public void testToStringHelperWithStringClassName() {
        String expected = "TestClassName{}";
        assertEquals(expected, Objects.toStringHelper("TestClassName").toString());
    }

    @Test
    public void testFirstNonNullWithBothNonNull() {
        String expected = "hello";
        assertEquals(expected, Objects.firstNonNull("hello", "world"));
    }

    @Test
    public void testFirstNonNullWithFirstNull() {
        String expected = "world";
        assertEquals(expected, Objects.firstNonNull(null, "world"));
    }
    
    @Test
    public void testFirstNonNullWithSecondNull() {
        String expected = "hello";
        assertEquals(expected, Objects.firstNonNull("hello", null));
    }
}