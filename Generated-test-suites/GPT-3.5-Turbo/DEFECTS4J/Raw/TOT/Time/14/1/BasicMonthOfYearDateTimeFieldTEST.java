import org.joda.time.DateTime;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.field.FieldUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField basicMonthOfYearDateTimeField;

    @Before
    public void setup() {
        BasicChronology chronology = new BasicChronology();
        int leapMonth = DateTimeFieldType.monthOfYear().getMaximumValue();
        basicMonthOfYearDateTimeField = new BasicMonthOfYearDateTimeField(chronology, leapMonth);
    }

    @Test
    public void testGet() {
        long instant = new DateTime(2000, 1, 1, 0, 0).getMillis();
        Assert.assertEquals(1, basicMonthOfYearDateTimeField.get(instant));
    }

    @Test
    public void testAdd() {
        long instant = new DateTime(2000, 1, 1, 0, 0).getMillis();
        long newInstant = basicMonthOfYearDateTimeField.add(instant, 2);
        Assert.assertEquals(new DateTime(2000, 3, 1, 0, 0).getMillis(), newInstant);
    }

    @Test
    public void testAddWrapField() {
        long instant = new DateTime(2000, 12, 1, 0, 0).getMillis();
        long newInstant = basicMonthOfYearDateTimeField.addWrapField(instant, 1);
        Assert.assertEquals(new DateTime(2000, 1, 1, 0, 0).getMillis(), newInstant);
    }

    @Test
    public void testSet() {
        long instant = new DateTime(2000, 1, 1, 0, 0).getMillis();
        long newInstant = basicMonthOfYearDateTimeField.set(instant, 2);
        Assert.assertEquals(new DateTime(2000, 2, 1, 0, 0).getMillis(), newInstant);
    }

    @Test
    public void testIsLeap() {
        long instant = new DateTime(2000, 1, 1, 0, 0).getMillis();
        Assert.assertTrue(basicMonthOfYearDateTimeField.isLeap(instant));
    }

    @Test
    public void testGetMinimumValue() {
        Assert.assertEquals(1, basicMonthOfYearDateTimeField.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        Assert.assertEquals(DateTimeFieldType.monthOfYear().getMaximumValue(), basicMonthOfYearDateTimeField.getMaximumValue());
    }

}