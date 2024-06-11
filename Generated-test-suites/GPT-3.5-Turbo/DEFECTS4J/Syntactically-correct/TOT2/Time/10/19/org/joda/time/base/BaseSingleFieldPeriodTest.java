import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals("Incorrect value returned", 10, period.getValue());
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        period.setValue(20);
        assertEquals("Value not set correctly", 20, period.getValue());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals("Incorrect field type returned", DurationFieldType.days(), period.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals("Incorrect period type returned", PeriodType.days(), period.getPeriodType());
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertTrue("Field type should be supported", period.isSupported(DurationFieldType.days()));
        assertFalse("Field type should not be supported", period.isSupported(DurationFieldType.hours()));
    }

    // Add more test cases for other methods as needed

}