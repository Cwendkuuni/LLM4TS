import org.joda.time.DurationFieldType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    private UnsupportedDurationField unsupportedDurationField;

    @Before
    public void setUp() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
    }

    @Test
    public void getType() {
        assertEquals(DurationFieldType.MINUTES, unsupportedDurationField.getType());
    }

    @Test
    public void getName() {
        assertEquals("minutes", unsupportedDurationField.getName());
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
        unsupportedDurationField.getValue(100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getValueAsLong() {
        unsupportedDurationField.getValueAsLong(100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getValueWithInstant() {
        unsupportedDurationField.getValue(100, 500);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getValueAsLongWithInstant() {
        unsupportedDurationField.getValueAsLong(100, 500);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getMillisWithValue() {
        unsupportedDurationField.getMillis(100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getMillisWithValueAsLong() {
        unsupportedDurationField.getMillis(100L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getMillisWithValueAndInstant() {
        unsupportedDurationField.getMillis(100, 500);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getMillisWithValueAsLongAndInstant() {
        unsupportedDurationField.getMillis(100L, 500);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void add() {
        unsupportedDurationField.add(100, 50);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addAsLong() {
        unsupportedDurationField.add(100L, 50L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getDifference() {
        unsupportedDurationField.getDifference(500, 100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getDifferenceAsLong() {
        unsupportedDurationField.getDifferenceAsLong(500, 100);
    }

    @Test
    public void getUnitMillis() {
        assertEquals(0, unsupportedDurationField.getUnitMillis());
    }

    @Test
    public void compareTo() {
        assertEquals(0, unsupportedDurationField.compareTo(unsupportedDurationField));
    }

    @Test
    public void equals() {
        assertTrue(unsupportedDurationField.equals(unsupportedDurationField));
    }

    @Test
    public void hashCodeTest() {
        assertEquals("minutes".hashCode(), unsupportedDurationField.hashCode());
    }

    @Test
    public void toStringTest() {
        assertEquals("UnsupportedDurationField[minutes]", unsupportedDurationField.toString());
    }

}