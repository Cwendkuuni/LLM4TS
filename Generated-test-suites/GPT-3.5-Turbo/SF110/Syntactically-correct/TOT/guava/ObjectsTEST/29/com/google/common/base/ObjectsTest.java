import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqualTypicalCase() {
        Object a = "test";
        Object b = "test";
        assertTrue(Objects.equal(a, b));
    }
    
    @Test
    public void testEqualNullCase() {
        Object a = null;
        Object b = null;
        assertTrue(Objects.equal(a, b));
    }
    
    @Test
    public void testEqualDifferentObjects() {
        Object a = "test";
        Object b = 123;
        assertFalse(Objects.equal(a, b));
    }
    
    @Test
    public void testHashCodeTypicalCase() {
        Object[] objects = {"test", 123, true};
        int expectedHashCode = objects.hashCode();
        int actualHashCode = Objects.hashCode(objects);
        assertEquals(expectedHashCode, actualHashCode);
    }
    
    @Test
    public void testHashCodeEmptyObjects() {
        Object[] objects = {};
        int expectedHashCode = objects.hashCode();
        int actualHashCode = Objects.hashCode(objects);
        assertEquals(expectedHashCode, actualHashCode);
    }
    
    @Test
    public void testToStringHelperTypicalCase() {
        Object obj = new Object();
        String expectedToString = "com.google.common.base.Objects$ToStringHelper{" + 
                                 "className=java.lang.Object, value=test}";
        String actualToString = Objects.toStringHelper(obj.getClass())
                                   .add("value", "test")
                                   .toString();
        assertEquals(expectedToString, actualToString);
    }
    
    @Test
    public void testToStringHelperOmitNullValues() {
        Object obj = new Object();
        String expectedToString = "com.google.common.base.Objects$ToStringHelper{" + 
                                 "className=java.lang.Object}";
        String actualToString = Objects.toStringHelper(obj.getClass())
                                   .add("value", null)
                                   .omitNullValues()
                                   .toString();
        assertEquals(expectedToString, actualToString);
    }
    
    @Test
    public void testFirstNonNullTypicalCase() {
        Object first = "test";
        Object second = "example";
        Object expectedNonNull = first;
        Object actualNonNull = Objects.firstNonNull(first, second);
        assertEquals(expectedNonNull, actualNonNull);
    }
    
    @Test
    public void testFirstNonNullNullFirst() {
        Object first = null;
        Object second = "example";
        Object expectedNonNull = second;
        Object actualNonNull = Objects.firstNonNull(first, second);
        assertEquals(expectedNonNull, actualNonNull);
    }
    
    @Test
    public void testFirstNonNullNullSecond() {
        Object first = "test";
        Object second = null;
        Object expectedNonNull = first;
        Object actualNonNull = Objects.firstNonNull(first, second);
        assertEquals(expectedNonNull, actualNonNull);
    }
    
}