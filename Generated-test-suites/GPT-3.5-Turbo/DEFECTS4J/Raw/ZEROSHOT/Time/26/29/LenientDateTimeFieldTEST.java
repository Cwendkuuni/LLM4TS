import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.field.LenientDateTimeField;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {
    private DateTimeField field;
    private Chronology base;

    @Before
    public void setUp() {
        base = new DateTime(2019, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).getChronology();
        field = LenientDateTimeField.getInstance(new DateTimeFieldType("test"), base);
    }

    @After
    public void tearDown() {
        base = null;
        field = null;
    }

    @Test
    public void testGet() {
        long instant = new DateTime(2019, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).getMillis();
        int value = field.get(instant);
        Assert.assertEquals(0, value);
    }

    @Test
    public void testGetMinimumValue() {
        int value = field.getMinimumValue();
        Assert.assertEquals(0, value);
    }

    @Test
    public void testGetMaximumValue() {
        int value = field.getMaximumValue();
        Assert.assertEquals(Integer.MAX_VALUE, value);
    }

    @Test
    public void testAdd() {
        long instant = new DateTime(2019, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).getMillis();
        long result = field.add(instant, 10);
        Assert.assertEquals(instant + 10, result);
    }

    @Test
    public void testAddNegativeValue() {
        long instant = new DateTime(2019, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).getMillis();
        long result = field.add(instant, -10);
        Assert.assertEquals(instant - 10, result);
    }

    @Test
    public void testAddWrapField() {
        long instant = new DateTime(2019, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).getMillis();
        long result = field.addWrapField(instant, 10);
        Assert.assertEquals(instant + 10, result);
    }

    @Test
    public void testAddWrapFieldNegativeValue() {
        long instant = new DateTime(2019, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).getMillis();
        long result = field.addWrapField(instant, -10);
        Assert.assertEquals(instant - 10, result);
    }

    @Test
    public void testAddWrapFieldExceedMax() {
        long instant = new DateTime(2019, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).getMillis();
        long result = field.addWrapField(instant, Integer.MAX_VALUE);
        Assert.assertEquals(instant + Integer.MAX_VALUE, result);
    }

    @Test
    public void testAddWrapFieldExceedMin() {
        long instant = new DateTime(2019, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).getMillis();
        long result = field.addWrapField(instant, Integer.MIN_VALUE);
        Assert.assertEquals(instant + Integer.MIN_VALUE, result);
    }
    
    @Test
    public void testSet() {
        long instant = new DateTime(2019, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).getMillis();
        long result = field.set(instant, 10);
        Assert.assertEquals(instant + 10, result);
    }
    
    @Test
    public void testSetOutOfRange() {
        long instant = new DateTime(2019, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).getMillis();
        long result = field.set(instant, Integer.MAX_VALUE);
        Assert.assertEquals(instant + Integer.MAX_VALUE, result);
    }
}