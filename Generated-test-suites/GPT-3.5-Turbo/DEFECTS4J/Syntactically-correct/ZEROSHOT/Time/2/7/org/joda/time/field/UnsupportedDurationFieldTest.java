import org.joda.time.DurationFieldType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    private UnsupportedDurationField unsupportedDurationField;

    @Before
    public void setUp() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.days());
    }

    @After
    public void tearDown() {
        unsupportedDurationField = null;
    }

    @Test
    public void testGetType() {
        Assert.assertEquals(DurationFieldType.days(), unsupportedDurationField.getType());
    }

    @Test
    public void testGetName() {
        Assert.assertEquals("days", unsupportedDurationField.getName());
    }

    @Test
    public void testIsSupported() {
        Assert.assertFalse(unsupportedDurationField.isSupported());
    }

    @Test
    public void testIsPrecise() {
        Assert.assertTrue(unsupportedDurationField.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long() {
        unsupportedDurationField.getValue(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long() {
        unsupportedDurationField.getValueAsLong(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long_long() {
        unsupportedDurationField.getValue(1000, 2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long_long() {
        unsupportedDurationField.getValueAsLong(1000, 2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int() {
        unsupportedDurationField.getMillis(100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long() {
        unsupportedDurationField.getMillis(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int_long() {
        unsupportedDurationField.getMillis(100, 200);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long_long() {
        unsupportedDurationField.getMillis(1000, 2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_int() {
        unsupportedDurationField.add(1000, 10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_long() {
        unsupportedDurationField.add(1000, 10L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference_long_long() {
        unsupportedDurationField.getDifference(2000, 1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        unsupportedDurationField.getDifferenceAsLong(2000, 1000);
    }

    @Test
    public void testGetUnitMillis() {
        Assert.assertEquals(0, unsupportedDurationField.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        Assert.assertEquals(0, unsupportedDurationField.compareTo(unsupportedDurationField));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        Assert.assertEquals(unsupportedDurationField, field);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        Assert.assertEquals(unsupportedDurationField.hashCode(), field.hashCode());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("UnsupportedDurationField[days]", unsupportedDurationField.toString());
    }

    @Test
    public void testReadResolve() {
        Assert.assertEquals(unsupportedDurationField, unsupportedDurationField.readResolve());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupported() {
        throw unsupportedDurationField.unsupported();
    }
}