import org.joda.time.DurationFieldType;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = mock(DurationFieldType.class);
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertNotNull(field);
    }

    @Test
    public void testGetType() {
        DurationFieldType type = mock(DurationFieldType.class);
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertEquals(type, field.getType());
    }

    @Test
    public void testGetName() {
        DurationFieldType type = mock(DurationFieldType.class);
        when(type.getName()).thenReturn("TestType");
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertEquals("TestType", field.getName());
    }

    @Test
    public void testIsSupported() {
        DurationFieldType type = mock(DurationFieldType.class);
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        DurationFieldType type = mock(DurationFieldType.class);
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertTrue(field.isPrecise());
    }

    @Test
    public void testGetValue_long() {
        DurationFieldType type = mock(DurationFieldType.class);
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertThrows(UnsupportedOperationException.class, () -> {
            field.getValue(1000L);
        });
    }

    // ... similar test cases for other methods

    @Test
    public void testGetInstance_NullType() {
        assertThrows(NullPointerException.class, () -> {
            UnsupportedDurationField.getInstance(null);
        });
    }

    @Test
    public void testEquals() {
        DurationFieldType type1 = mock(DurationFieldType.class);
        DurationFieldType type2 = mock(DurationFieldType.class);
        UnsupportedDurationField field1 = new UnsupportedDurationField(type1);
        UnsupportedDurationField field2 = new UnsupportedDurationField(type1);
        UnsupportedDurationField field3 = new UnsupportedDurationField(type2);
        assertTrue(field1.equals(field1));
        assertTrue(field1.equals(field2));
        assertFalse(field1.equals(field3));
    }

    // ... similar test cases for other identified edge cases and exception scenarios

}