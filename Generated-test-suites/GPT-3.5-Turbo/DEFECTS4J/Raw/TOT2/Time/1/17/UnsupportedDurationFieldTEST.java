import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.joda.time.DurationFieldType;
import org.junit.Before;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    private UnsupportedDurationField field;

    @Before
    public void setUp() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.days());
    }

    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.days(), field.getType());
    }

    @Test
    public void testGetName() {
        assertEquals("days", field.getName());
    }

    @Test
    public void testIsSupported() {
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        assertTrue(field.isPrecise());
    }

    @Test
    public void testGetValue() {
        try {
            field.getValue(10);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testGetValueAsLong() {
        try {
            field.getValueAsLong(10);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    // More test cases for other methods can be added here

}