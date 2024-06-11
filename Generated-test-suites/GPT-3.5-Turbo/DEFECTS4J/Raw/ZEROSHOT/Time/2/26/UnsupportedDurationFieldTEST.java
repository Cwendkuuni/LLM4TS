import org.joda.time.DurationFieldType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    private UnsupportedDurationField unsupportedDurationField;

    @Before
    public void setUp() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.years());
    }

    @Test
    public void getType() {
        assertEquals(DurationFieldType.years(), unsupportedDurationField.getType());
    }

    @Test
    public void getName() {
        assertEquals("years", unsupportedDurationField.getName());
    }

    @Test
    public void isSupported() {
        assertFalse(unsupportedDurationField.isSupported());
    }

    @Test
    public void isPrecise() {
        assertTrue(unsupportedDurationField.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getValue() {
        unsupportedDurationField.getValue(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getValueAsLong() {
        unsupportedDurationField.getValueAsLong(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getValue_withInstant() {
        unsupportedDurationField.getValue(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getValueAsLong_withInstant() {
        unsupportedDurationField.getValueAsLong(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getMillis() {
        unsupportedDurationField.getMillis(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getMillisAsLong() {
        unsupportedDurationField.getMillis(0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getMillis_withInstant() {
        unsupportedDurationField.getMillis(0, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getMillisAsLong_withInstant() {
        unsupportedDurationField.getMillis(0L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void add_withIntValue() {
        unsupportedDurationField.add(0L, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void add_withLongValue() {
        unsupportedDurationField.add(0L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getDifference() {
        unsupportedDurationField.getDifference(0L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getDifferenceAsLong() {
        unsupportedDurationField.getDifferenceAsLong(0L, 0L);
    }

    @Test
    public void getUnitMillis() {
        assertEquals(0L, unsupportedDurationField.getUnitMillis());
    }

    @Test
    public void compareTo() {
        UnsupportedDurationField otherField = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals(0, unsupportedDurationField.compareTo(otherField));
    }
}