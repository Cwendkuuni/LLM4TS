import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {

    @Test
    public void equal_NullObjects_ReturnsTrue() {
        assertTrue(Objects.equal(null, null));
    }

    @Test
    public void equal_NullObjectAndNonNullObject_ReturnsFalse() {
        Object obj = new Object();
        assertFalse(Objects.equal(null, obj));
    }

    @Test
    public void equal_NonNullObjectAndNullObject_ReturnsFalse() {
        Object obj = new Object();
        assertFalse(Objects.equal(obj, null));
    }

    @Test
    public void equal_SameObject_ReturnsTrue() {
        Object obj = new Object();
        assertTrue(Objects.equal(obj, obj));
    }

    @Test
    public void equal_EqualNonNullObjects_ReturnsTrue() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertTrue(Objects.equal(obj1, obj2));
    }

    @Test
    public void equal_UnequalNonNullObjects_ReturnsFalse() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(Objects.equal(obj1, obj2));
    }

    @Test
    public void hashCode_NullObjects_ReturnsZero() {
        assertEquals(0, Objects.hashCode(null, null));
    }

    @Test
    public void hashCode_SingleNullObject_ReturnsZero() {
        assertEquals(0, Objects.hashCode(null));
    }

    @Test
    public void hashCode_NullObjectAndNonNullObject_ReturnsHashCodeOfNonNullObject() {
        Object obj = new Object();
        assertEquals(obj.hashCode(), Objects.hashCode(null, obj));
    }

    @Test
    public void hashCode_EqualNonNullObjects_ReturnsSameHashCode() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertEquals(obj1.hashCode(), Objects.hashCode(obj1, obj2));
    }

    @Test
    public void hashCode_UnequalNonNullObjects_ReturnsDifferentHashCodes() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertNotEquals(obj1.hashCode(), Objects.hashCode(obj1, obj2));
    }

    @Test
    public void toStringHelper_EmptyToStringHelper_ReturnsEmptyString() {
        Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
        assertEquals("ClassName{}", helper.toString());
    }

    @Test
    public void toStringHelper_ConstructToStringHelperWithClassName_ReturnsClassName() {
        Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
        assertEquals("ClassName", helper.toString());
    }

    @Test
    public void toStringHelper_AddNameAndValue_ReturnsFormattedString() {
        Objects.ToStringHelper helper = Objects.toStringHelper("ClassName")
                .add("name", "John")
                .add("age", 30);
        assertEquals("ClassName{name=John, age=30}", helper.toString());
    }

    @Test
    public void toStringHelper_OmitNullValues_SetOmitNullValuesTrue() {
        Objects.ToStringHelper helper = Objects.toStringHelper("ClassName").omitNullValues();
        assertTrue(helper.omitNullValues);
    }

    @Test
    public void firstNonNull_BothNull_ReturnsNull() {
        Object result = Objects.firstNonNull(null, null);
        assertNull(result);
    }

    @Test
    public void firstNonNull_FirstNonNull_ReturnsFirstObject() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object result = Objects.firstNonNull(obj1, obj2);
        assertSame(obj1, result);
    }

    @Test
    public void firstNonNull_SecondNonNull_ReturnsSecondObject() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object result = Objects.firstNonNull(null, obj2);
        assertSame(obj2, result);
    }

}