import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField field;

    @Before
    public void setup() {
        field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
    }

    @Test
    public void testIsLenient() {
        Assert.assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        long instant = 1577836800000L; // 2020-01-01T00:00:00 UTC
        int actual = field.get(instant);
        int expected = 1; // January is the 1st month
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAdd() {
        long instant = 1577836800000L; // 2020-01-01T00:00:00 UTC
        int monthsToAdd = 1;
        long actual = field.add(instant, monthsToAdd);
        long expected = 1580515200000L; // 2020-02-01T00:00:00 UTC
        Assert.assertEquals(expected, actual);
    }

    // Add test cases for other public methods

}