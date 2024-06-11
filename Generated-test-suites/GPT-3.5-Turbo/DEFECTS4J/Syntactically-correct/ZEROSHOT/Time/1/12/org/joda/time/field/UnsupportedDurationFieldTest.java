import org.joda.time.DurationFieldType;
import static org.junit.Assert.*;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertNotNull(field);
    }

    @Test
    public void testGetType() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.seconds());
        assertEquals(DurationFieldType.seconds(), field.getType());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.minutes());
        assertEquals("minutes", field.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.hours());
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.days());
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.weeks());
        field.getValue(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.months());
        field.getValueAsLong(1000L);
    }
  
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long_long() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.years());
        field.getValue(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long_long() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.eras());
        field.getValueAsLong(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.centuries());
        field.getMillis(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.millis());
        field.getMillis(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int_long() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.minutes());
        field.getMillis(1000, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long_long() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.hours());
        field.getMillis(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_int() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.days());
        field.add(1000L, 2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_long() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.weeks());
        field.add(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference_long_long() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.months());
        field.getDifference(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong_long_long() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.years());
        field.getDifferenceAsLong(1000L, 2000L);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.eras());
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.centuries());
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.millis());
        assertEquals(1, field1.compareTo(field2));
        assertEquals(0, field2.compareTo(field1));
    }

    @Test
    public void testEquals_sameObject() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.minutes());
        assertTrue(field.equals(field));
    }

    @Test
    public void testEquals_differentObject_sameName() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.hours());
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.hours());
        assertTrue(field1.equals(field2));
    }

    @Test
    public void testEquals_differentObject_differentName() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.days());
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.hours());
        assertFalse(field1.equals(field2));
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.weeks());
        assertEquals("UnsupportedDurationField[weeks]", field.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.months());
        UnsupportedDurationField field2 = field1.readResolve();
        assertTrue(field1.equals(field2));
    }
}