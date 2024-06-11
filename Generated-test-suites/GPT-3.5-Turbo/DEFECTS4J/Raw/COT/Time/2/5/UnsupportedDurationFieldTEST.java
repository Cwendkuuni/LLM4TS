import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {
    
    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        DurationFieldType type = field.getType();
        assert(type == DurationFieldType.MILLIS);
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        String name = field.getName();
        assert(name.equals(DurationFieldType.SECONDS.getName()));
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        boolean supported = field.isSupported();
        assert(supported == false);
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        boolean precise = field.isPrecise();
        assert(precise == true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        field.getValue(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.WEEKS);
        field.getValueAsLong(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MONTHS);
        field.getValue(1000, 2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        field.getValueAsLong(1000, 2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        field.getMillis(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.getMillis(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        field.getMillis(1000, 2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAsLongWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        field.getMillis(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        field.add(1000L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.WEEKS);
        field.add(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MONTHS);
        field.getDifference(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        field.getDifferenceAsLong(1000L, 2000L);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        long unitMillis = field.getUnitMillis();
        assert(unitMillis == 0);
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        int compareTo = field1.compareTo(field2);
        assert(compareTo == 0);
    }
    
}