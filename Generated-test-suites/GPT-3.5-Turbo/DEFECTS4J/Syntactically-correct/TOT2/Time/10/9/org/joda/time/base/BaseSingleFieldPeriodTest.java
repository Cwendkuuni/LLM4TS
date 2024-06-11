import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.setValue(10);
        assertEquals(10, period.getValue());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        assertEquals(DurationFieldType.days(), period.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        assertEquals(PeriodType.days(), period.getPeriodType());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(1, period.size());
    }

    @Test
    public void testGetFieldTypeAtIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };
        assertEquals(DurationFieldType.days(), period.getFieldType(0));
    }

    @Test
    public void testGetValueAtIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue(0));
    }

    @Test
    public void testGetDurationFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };
        assertEquals(5, period.get(DurationFieldType.days()));
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };
        assertTrue(period.isSupported(DurationFieldType.days()));
        assertFalse(period.isSupported(DurationFieldType.hours()));
    }

    // Additional test cases for edge cases and exception handling scenarios can be added here

}