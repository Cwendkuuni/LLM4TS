import org.junit.Test;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBaseSingleFieldPeriodWithValidArguments() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = DurationFieldType.DAYS;
        assertEquals(5, period.getValue());
        assertEquals(fieldType, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(Period.ZERO.withField(fieldType, 5), period.toPeriod());
        assertEquals(new MutablePeriod().withField(fieldType, 5), period.toMutablePeriod());
    }

    @Test
    public void testBaseSingleFieldPeriodGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(10, period.getValue());
    }

    @Test
    public void testBaseSingleFieldPeriodSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.setValue(15);
        assertEquals(15, period.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriodInvalidDurationFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType invalidFieldType = new DurationFieldType() {
            @Override
            public String getName() {
                return "InvalidField";
            }

            @Override
            public int getUnitMillis() {
                return 1000;
            }

            @Override
            public DurationFieldType withLeniency(int leniency) {
                return this;
            }

            @Override
            public boolean isValidPartial() {
                return true;
            }
        };
        period.get(invalidFieldType);
    }

    @Test
    public void testBaseSingleFieldPeriodIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = DurationFieldType.DAYS;
        assertTrue(period.isSupported(fieldType));
        DurationFieldType invalidFieldType = new DurationFieldType() {
            @Override
            public String getName() {
                return "InvalidField";
            }

            @Override
            public int getUnitMillis() {
                return 1000;
            }

            @Override
            public DurationFieldType withLeniency(int leniency) {
                return this;
            }

            @Override
            public boolean isValidPartial() {
                return true;
            }
        };
        assertFalse(period.isSupported(invalidFieldType));
    }
}