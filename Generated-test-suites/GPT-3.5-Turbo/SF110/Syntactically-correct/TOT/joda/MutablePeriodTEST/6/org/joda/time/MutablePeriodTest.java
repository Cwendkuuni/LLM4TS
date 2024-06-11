import org.joda.time.*;
import org.junit.*;
import static org.junit.Assert.*;

public class MutablePeriodTest {

    // Test for parse() method
    @Test
    public void testParse() {
        MutablePeriod period = MutablePeriod.parse("P2Y3M4W5D");
        assertEquals(2, period.getYears());
        assertEquals(3, period.getMonths());
        assertEquals(4, period.getWeeks());
        assertEquals(5, period.getDays());
    }
    
    // Test for parse() method with custom PeriodFormatter
    @Test
    public void testParseWithFormatter() {
        PeriodFormatter formatter = PeriodFormatterBuilder.forFields()
                .appendYears()
                .appendSuffix(" year", " years").toFormatter();
        MutablePeriod period = MutablePeriod.parse("5 years", formatter);
        assertEquals(5, period.getYears());
    }

    // Test for default constructor
    @Test
    public void testDefaultConstructor() {
        MutablePeriod period = new MutablePeriod();
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
    }
    
    // Test for constructor with PeriodType
    @Test
    public void testConstructorWithPeriodType() {
        PeriodType periodType = PeriodType.forFields(new DurationFieldType[] {
                DurationFieldType.years(),
                DurationFieldType.months(),
                DurationFieldType.days()
        });
        MutablePeriod period = new MutablePeriod(periodType);
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
    }

    // Test for constructor with individual field values
    @Test
    public void testConstructorWithFieldValues() {
        MutablePeriod period = new MutablePeriod(3, 6, 2, 10);
        assertEquals(3, period.getYears());
        assertEquals(6, period.getMonths());
        assertEquals(2, period.getWeeks());
        assertEquals(10, period.getDays());
    }

    // Test for constructor with individual field values and PeriodType
    @Test
    public void testConstructorWithFieldValuesAndPeriodType() {
        PeriodType periodType = PeriodType.standard().withMillisRemoved();
        MutablePeriod period = new MutablePeriod(3, 6, 2, 10, periodType);
        assertEquals(3, period.getYears());
        assertEquals(6, period.getMonths());
        assertEquals(2, period.getWeeks());
        assertEquals(10, period.getDays());
    }
    
    // Test for constructor with duration in milliseconds
    @Test
    public void testConstructorWithDurationMillis() {
        MutablePeriod period = new MutablePeriod(1000);
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(1, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMillis());
    }
}