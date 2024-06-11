import org.joda.time.*;
import org.joda.time.field.UnsupportedDurationField;
import org.junit.*;

public class UnsupportedDurationFieldTest {

    private UnsupportedDurationField field;

    @Before
    public void setUp() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
    }

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        Assert.assertNotNull(field);
        Assert.assertEquals(field.getType(), type);
    }

    @Test
    public void testGetType() {
        Assert.assertEquals(field.getType(), DurationFieldType.millis());
    }

    @Test
    public void testGetName() {
        Assert.assertEquals(field.getName(), "millis");
    }

    @Test
    public void testIsSupported() {
        Assert.assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        Assert.assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long() {
        field.getValue(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long() {
        field.getValueAsLong(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long_long() {
        field.getValue(1L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long_long() {
        field.getValueAsLong(1L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int() {
        field.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long() {
        field.getMillis(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int_long() {
        field.getMillis(1, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long_long() {
        field.getMillis(1L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_int() {
        field.add(1L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_long() {
        field.add(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference_long_long() {
        field.getDifference(2L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong_long_long() {
        field.getDifferenceAsLong(2L, 1L);
    }

    @Test
    public void testGetUnitMillis() {
        Assert.assertEquals(field.getUnitMillis(), 0L);
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        Assert.assertEquals(field.compareTo(other), 0);
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        Assert.assertTrue(field.equals(other));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        Assert.assertEquals(field.hashCode(), other.hashCode());
    }

    @Test
    public void testToString() {
        Assert.assertEquals(field.toString(), "UnsupportedDurationField[millis]");
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField other = (UnsupportedDurationField) field.readResolve();
        Assert.assertTrue(field.equals(other));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupported() {
        throw field.unsupported();
    }
}