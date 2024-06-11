import org.junit.Test;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import static org.junit.Assert.assertEquals;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testSimpleDaysPeriod() {
        BaseSingleFieldPeriod daysPeriod = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        MutablePeriod expectedPeriod = new MutablePeriod();
        expectedPeriod.setDays(3);
        Period expected = expectedPeriod.toPeriod();

        Period actual = daysPeriod.toPeriod();
        assertEquals(expected, actual);

        int daysValue = daysPeriod.getValue();
        assertEquals(3, daysValue);
    }

    @Test
    public void testNegativePeriod() {
        BaseSingleFieldPeriod daysPeriod = new BaseSingleFieldPeriod(-3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        MutablePeriod expectedPeriod = new MutablePeriod();
        expectedPeriod.setDays(-3);
        Period expected = expectedPeriod.toPeriod();

        Period actual = daysPeriod.toPeriod();
        assertEquals(expected, actual);

        int daysValue = daysPeriod.getValue();
        assertEquals(-3, daysValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFieldType() {
        BaseSingleFieldPeriod daysPeriod = new BaseSingleFieldPeriod(3) {
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

    @Test(expected = IllegalArgumentException.class)
    public void testNullFieldType() {
        BaseSingleFieldPeriod daysPeriod = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPeriodType() {
        BaseSingleFieldPeriod daysPeriod = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
    }

}