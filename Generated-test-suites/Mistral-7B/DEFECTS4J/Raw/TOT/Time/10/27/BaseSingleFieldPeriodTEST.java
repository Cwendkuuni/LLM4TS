import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBaseSingleFieldPeriod_days() {
        // Test creating a BaseSingleFieldPeriod object with 5 days
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        // Test getting the field type and period type
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());

        // Test getting the size, value, and fields
        assertEquals(1, period.size());
        assertEquals(5, period.getValue(0));
        assertEquals(5, period.get(DurationFieldType.DAYS));

        // Test adding the period to a MutablePeriod object
        MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.add(period);
        Period addedPeriod = mutablePeriod.toPeriod();
        assertEquals(5, addedPeriod.getDays());

        // Test comparing the period to another object of the same class
        BaseSingleFieldPeriod anotherPeriod = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(period, anotherPeriod);

        // Test creating a BaseSingleFieldPeriod object with 0 value
        BaseSingleFieldPeriod zeroPeriod = new BaseSingleFieldPeriod(0) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(zeroPeriod, Period.ZERO);

        // Test creating a BaseSingleFieldPeriod object with negative value
        BaseSingleFieldPeriod negativePeriod = new BaseSingleFieldPeriod(-5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(-5, negativePeriod.getValue());
        assertEquals(-5, negativePeriod.get(DurationFieldType.DAYS));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriod_invalidFieldType() {
        new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.MONTHS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
    }
}