import org.junit.Test;
import org.joda.time.ReadableInstant;
import org.joda.time.chrono.ISOChronology;

public class BaseSingleFieldPeriodTest {

    @Test(expected = IllegalArgumentException.class)
    public void between_NullStart_ThrowsIllegalArgumentException() {
        ReadableInstant end = ISOChronology.getInstanceUTC().now();
        BaseSingleFieldPeriod.between(null, end, DurationFieldType.days());
    }

    @Test(expected = IllegalArgumentException.class)
    public void between_NullEnd_ThrowsIllegalArgumentException() {
        ReadableInstant start = ISOChronology.getInstanceUTC().now();
        BaseSingleFieldPeriod.between(start, null, DurationFieldType.days());
    }

    @Test(expected = IllegalArgumentException.class)
    public void between_NullFieldType_ThrowsIllegalArgumentException() {
        ReadableInstant start = ISOChronology.getInstanceUTC().now();
        ReadableInstant end = ISOChronology.getInstanceUTC().now();
        BaseSingleFieldPeriod.between(start, end, null);
    }

    // Add more test cases for the between method

    @Test(expected = IllegalArgumentException.class)
    public void between_NullStart_ThrowsIllegalArgumentException_ForReadablePartial() {
        ReadablePartial end = ISOChronology.getInstanceUTC().now();
        BaseSingleFieldPeriod.between(null, end, new BaseSingleFieldPeriod() {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void between_NullEnd_ThrowsIllegalArgumentException_ForReadablePartial() {
        ReadablePartial start = ISOChronology.getInstanceUTC().now();
        BaseSingleFieldPeriod.between(start, null, new BaseSingleFieldPeriod() {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void between_NullZeroInstance_ThrowsIllegalArgumentException_ForReadablePartial() {
        ReadablePartial start = ISOChronology.getInstanceUTC().now();
        ReadablePartial end = ISOChronology.getInstanceUTC().now();
        BaseSingleFieldPeriod.between(start, end, null);
    }

    // Add more test cases for the between method

    // Add test cases for standardPeriodIn method

    // Add test cases for getFieldType method

    // Add test cases for getPeriodType method

    // Add test cases for size method

    // Add test cases for getFieldType(int) method

    // Add test cases for getValue(int) method

    // Add test cases for get(DurationFieldType) method

    // Add test cases for isSupported method

    // Add test cases for toPeriod method

    // Add test cases for toMutablePeriod method

    // Add test cases for equals method

    // Add test cases for hashCode method

    // Add test cases for compareTo method
    
}