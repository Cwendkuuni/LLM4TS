import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    // Test for constructors and getters

    @Test
    public void testBaseSingleFieldPeriodConstructor() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = DurationFieldType.days();
        assertEquals(fieldType, period.getFieldType());
        assertEquals(5, period.getValue());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType expectedFieldType = DurationFieldType.days();
        DurationFieldType actualFieldType = period.getFieldType();
        assertEquals(expectedFieldType, actualFieldType);
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        PeriodType expectedPeriodType = PeriodType.standard();
        PeriodType actualPeriodType = period.getPeriodType();
        assertEquals(expectedPeriodType, actualPeriodType);
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int expectedSize = 1;
        int actualSize = period.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testGetFieldTypeAtIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType expectedFieldType = DurationFieldType.days();
        DurationFieldType actualFieldType = period.getFieldType(0);
        assertEquals(expectedFieldType, actualFieldType);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueAtInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getValue(1);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int expectedValue = 5;
        int actualValue = period.getValue();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.setValue(6);
        int expectedValue = 6;
        int actualValue = period.getValue();
        assertEquals(expectedValue, actualValue);
    }

    // Test for methods between and standardPeriodIn

    @Test
    public void testBetweenStartAndEnd() {
        ReadableInstant start = DateTimeUtils.currentTime().withFieldAdded(ISOChronology.getInstanceUTC(), DurationFieldType.days(), 2);
        ReadableInstant end = DateTimeUtils.currentTime().withFieldAdded(ISOChronology.getInstanceUTC(), DurationFieldType.days(), 5);
        DurationFieldType fieldType = DurationFieldType.days();

        int result = BaseSingleFieldPeriod.between(start, end, fieldType);
        int expectedResult = 3;
        assertEquals(expectedResult, result);
    }

    @Test
    public void testBetweenStartAndEndInvalid() {
        ReadableInstant start = null;
        ReadableInstant end = DateTimeUtils.currentTime();
        DurationFieldType fieldType = DurationFieldType.days();

        try {
            int result = BaseSingleFieldPeriod.between(start, end, fieldType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBetweenStartAndEndNull() {
        ReadableInstant start = null;
        ReadableInstant end = DateTimeUtils.currentTime();
        DurationFieldType fieldType = DurationFieldType.days();

        try {
            int result = BaseSingleFieldPeriod.between(start, end, fieldType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new MutablePeriod().withFields(2, DurationFieldType.days(), 3, DurationFieldType.hours(), 4, DurationFieldType.minutes());
        long millisPerUnit = 1000L * 60L * 60L * 24L * 30L; // 30 days

        int result = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        int expectedResult = 630;
        assertEquals(expectedResult, result);
    }

    @Test
    public void testStandardPeriodInNullPeriod() {
        ReadablePeriod period = null;
        long millisPerUnit = 1000L * 60L * 60L * 24L * 30L; // 30 days

        try {
            int result = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}