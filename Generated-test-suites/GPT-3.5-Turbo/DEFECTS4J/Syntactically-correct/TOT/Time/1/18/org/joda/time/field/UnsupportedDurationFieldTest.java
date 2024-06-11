import org.joda.time.DurationFieldType;
import org.junit.Assert;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        DurationFieldType type = UnsupportedDurationField.getInstance(DurationFieldType.weeks()).getType();
        Assert.assertEquals(DurationFieldType.weeks(), type);
    }

    @Test
    public void testGetName() {
        String name = UnsupportedDurationField.getInstance(DurationFieldType.minutes()).getName();
        Assert.assertEquals("minutes", name);
    }

    @Test
    public void testIsSupported() {
        boolean isSupported = UnsupportedDurationField.getInstance(DurationFieldType.years()).isSupported();
        Assert.assertFalse(isSupported);
    }

    @Test
    public void testIsPrecise() {
        boolean isPrecise = UnsupportedDurationField.getInstance(DurationFieldType.hours()).isPrecise();
        Assert.assertTrue(isPrecise);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long() {
        UnsupportedDurationField.getInstance(DurationFieldType.days()).getValue(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long() {
        UnsupportedDurationField.getInstance(DurationFieldType.weeks()).getValueAsLong(2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long_long() {
        UnsupportedDurationField.getInstance(DurationFieldType.seconds()).getValue(1000, 5000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long_long() {
        UnsupportedDurationField.getInstance(DurationFieldType.millis()).getValueAsLong(2000, 3000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int() {
        UnsupportedDurationField.getInstance(DurationFieldType.hours()).getMillis(5);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long() {
        UnsupportedDurationField.getInstance(DurationFieldType.minutes()).getMillis(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int_long() {
        UnsupportedDurationField.getInstance(DurationFieldType.days()).getMillis(3, 10000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long_long() {
        UnsupportedDurationField.getInstance(DurationFieldType.months()).getMillis(2000L, 3000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_int() {
        UnsupportedDurationField.getInstance(DurationFieldType.years()).add(1000, 5);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_long() {
        UnsupportedDurationField.getInstance(DurationFieldType.hours()).add(5000L, 1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField.getInstance(DurationFieldType.seconds()).getDifference(1000L, 5000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField.getInstance(DurationFieldType.millis()).getDifferenceAsLong(2000L, 3000L);
    }

    @Test
    public void testGetUnitMillis() {
        long unitMillis = UnsupportedDurationField.getInstance(DurationFieldType.seconds()).getUnitMillis();
        Assert.assertEquals(0, unitMillis);
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type = DurationFieldType.weeks();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        int result = field.compareTo(new UnsupportedDurationField(type));

        Assert.assertEquals(1, result);
    }

    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.hours();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);

        Assert.assertEquals(field1, field2);
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.days();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        int hashCode = field.hashCode();

        Assert.assertEquals(type.getName().hashCode(), hashCode);
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.minutes();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        String result = field.toString();

        Assert.assertEquals("UnsupportedDurationField[minutes]", result);
    }

    @Test
    public void testReadResolve() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = (UnsupportedDurationField) field1.readResolve();

        Assert.assertSame(field1, field2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupported() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        throw field.unsupported();
    }
}