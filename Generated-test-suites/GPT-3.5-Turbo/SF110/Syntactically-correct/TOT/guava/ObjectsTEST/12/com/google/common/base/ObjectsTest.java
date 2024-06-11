import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    private Objects.ToStringHelper toStringHelper;
    private Objects.ToStringHelper nullToStringHelper;

    @Before
    public void setUp() {
        toStringHelper = Objects.toStringHelper("TestClass");
        nullToStringHelper = Objects.toStringHelper(null);
    }

    // Test cases for equal method
    @Test
    public void testEqual_WhenBothObjectsAreNull_ReturnsTrue() {
        assertTrue(Objects.equal(null, null));
    }

    @Test
    public void testEqual_WhenObjectsAreEqual_ReturnsTrue() {
        assertTrue(Objects.equal("Test", "Test"));
    }

    @Test
    public void testEqual_WhenObjectsAreNotEqual_ReturnsFalse() {
        assertFalse(Objects.equal("Test", "Different"));
    }

    // Test cases for hashCode method
    @Test
    public void testHashCode_WhenObjectsAreNull_ReturnsZero() {
        assertEquals(0, Objects.hashCode(null));
    }

    @Test
    public void testHashCode_WhenObjectsAreEmpty_ReturnsZero() {
        assertEquals(0, Objects.hashCode());
    }

    @Test
    public void testHashCode_WhenObjectsAreNotNull_ReturnsHashCode() {
        assertEquals(12345, Objects.hashCode("Test", 123, true));
    }

    // Test cases for toStringHelper method
    @Test
    public void testToStringHelper_WhenClassIsNotNull_ReturnsToStringHelper() {
        assertNotNull(Objects.toStringHelper("TestClass"));
    }

    @Test
    public void testToStringHelper_WhenClassIsNull_ReturnsToStringHelperWithClassNameNull() {
        assertNotNull(Objects.toStringHelper(null));
        assertEquals("Null", nullToStringHelper.toString());
    }

    // Test cases for firstNonNull method
    @Test
    public void testFirstNonNull_WhenBothObjectsAreNull_ReturnsNull() {
        assertNull(Objects.firstNonNull(null, null));
    }

    @Test
    public void testFirstNonNull_WhenFirstObjectIsNull_ReturnsSecondObject() {
        assertEquals("Second", Objects.firstNonNull(null, "Second"));
    }

    @Test
    public void testFirstNonNull_WhenSecondObjectIsNull_ReturnsFirstObject() {
        assertEquals("First", Objects.firstNonNull("First", null));
    }

    @Test
    public void testFirstNonNull_WhenBothObjectsAreNotNull_ReturnsFirstObject() {
        assertEquals("First", Objects.firstNonNull("First", "Second"));
    }

    // Test cases for ToStringHelper class
    @Test
    public void testToStringHelper_AddValue_WhenValueIsNull_ValueIsAdded() {
        toStringHelper.addValue(null);
        assertEquals("TestClass{}", toStringHelper.toString());
    }

    @Test
    public void testToStringHelper_AddValue_WhenValueIsNotNull_ValueIsAdded() {
        toStringHelper.addValue("Test");
        assertEquals("TestClass{Test}", toStringHelper.toString());
    }

    @Test
    public void testToStringHelper_Add_WhenNameAndValueAreNull_ValueIsAdded() {
        toStringHelper.add(null, null);
        assertEquals("TestClass{}", toStringHelper.toString());
    }

    @Test
    public void testToStringHelper_Add_WhenNameIsNullAndValueIsNotNull_ValueIsAdded() {
        toStringHelper.add(null, "Test");
        assertEquals("TestClass{Test}", toStringHelper.toString());
    }

    @Test
    public void testToStringHelper_Add_WhenNameIsNotNullAndValueIsNotNull_NameAndValueAreAdded() {
        toStringHelper.add("Name", "Value");
        assertEquals("TestClass{Name=Value}", toStringHelper.toString());
    }

    @Test
    public void testToStringHelper_OmitNullValues_WhenOmitNullValuesIsCalled_NullValuesAreOmitted() {
        toStringHelper.omitNullValues();
        toStringHelper.add("Name1", null);
        toStringHelper.add(null, "Value2");
        toStringHelper.add("Name3", "Value3");
        toStringHelper.add("Name4", null);
        toStringHelper.add("Name5", "Value5");
        assertEquals("TestClass{Name3=Value3, Name5=Value5}", toStringHelper.toString());
    }

}