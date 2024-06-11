import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.joda.time.DurationFieldType;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new SampleSingleFieldPeriod(10);
        assertEquals(10, period.getValue());

        period = new SampleSingleFieldPeriod(20);
        assertEquals(20, period.getValue());
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new SampleSingleFieldPeriod(10);
        period.setValue(20);
        assertEquals(20, period.getValue());

        period.setValue(30);
        assertEquals(30, period.getValue());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new SampleSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new SampleSingleFieldPeriod(10);
        assertTrue(period1.equals(period2));

        BaseSingleFieldPeriod period3 = new SampleSingleFieldPeriod(20);
        assertFalse(period1.equals(period3));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new SampleSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new SampleSingleFieldPeriod(10);
        assertEquals(period1.hashCode(), period2.hashCode());

        BaseSingleFieldPeriod period3 = new SampleSingleFieldPeriod(20);
        assertFalse(period1.hashCode() == period3.hashCode());
    }

    private class SampleSingleFieldPeriod extends BaseSingleFieldPeriod {

        public SampleSingleFieldPeriod(int iPeriod) {
            super(iPeriod);
        }

        @Override
        public DurationFieldType getFieldType() {
            return null;
        }

        @Override
        public int compareTo(BaseSingleFieldPeriod o) {
            return 0;
        }

        @Override
        public int getPeriodType() {
            return 0;
        }

    }
}