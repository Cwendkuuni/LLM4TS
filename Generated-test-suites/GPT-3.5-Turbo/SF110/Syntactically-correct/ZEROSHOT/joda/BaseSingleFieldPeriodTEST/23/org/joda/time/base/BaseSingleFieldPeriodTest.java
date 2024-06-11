import org.joda.time.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween_ForReadableInstant() {
        DateTime start = new DateTime(2000, 1, 1, 0, 0);
        DateTime end = new DateTime(2000, 1, 2, 0, 0);
        
        int result = BaseSingleFieldPeriod.between(start, end, DateTimeFieldType.day());
        
        assertEquals(1, result);
    }
    
    @Test
    public void testBetween_ForReadablePartial() {
        LocalDate start = new LocalDate(2000, 1, 1);
        LocalDate end = new LocalDate(2000, 2, 1);
        PeriodType periodType = PeriodType.standard();
        
        int result = BaseSingleFieldPeriod.between(start, end, periodType);
        
        assertEquals(1, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_ForNullReadableInstant() {
        DateTime start = null;
        DateTime end = new DateTime(2000, 1, 2, 0, 0);
        
        BaseSingleFieldPeriod.between(start, end, DateTimeFieldType.day());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_ForNullReadablePartial() {
        LocalDate start = null;
        LocalDate end = new LocalDate(2000, 2, 1);
        PeriodType periodType = PeriodType.standard();
        
        BaseSingleFieldPeriod.between(start, end, periodType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_ForDifferentSizeReadablePartial() {
        LocalDate start = new LocalDate(2000, 1, 1);
        LocalDateTime end = new LocalDateTime(2000, 2, 1, 1, 0);
        PeriodType periodType = PeriodType.standard();
        
        BaseSingleFieldPeriod.between(start, end, periodType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_ForDifferentFieldTypeReadablePartial() {
        LocalDate start = new LocalDate(2000, 1, 1);
        LocalDate end = new LocalDate(2000, 2, 1);
        PeriodType periodType = PeriodType.dayTime();
        
        BaseSingleFieldPeriod.between(start, end, periodType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_ForNonContiguousReadablePartial() {
        LocalDate start = new LocalDate(2000, 1, 1);
        LocalDate middle = new LocalDate(2000, 1, 10);
        LocalDate end = new LocalDate(2000, 2, 1);
        PeriodType periodType = PeriodType.standard();
        
        BaseSingleFieldPeriod.between(start, middle, periodType);
        BaseSingleFieldPeriod.between(middle, end, periodType);   
    }
    
    @Test
    public void testStandardPeriodIn() {
        Period period = Period.months(3);
        long n = 3;
        
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
        
        assertEquals(1, result);
    }
    
    @Test
    public void testStandardPeriodIn_ForNullPeriod() {
        Period period = null;
        long n = 3;
        
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
        
        assertEquals(0, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodIn_ForNonPrecisePeriodField() {
        Period period = Period.years(1);
        long n = 3;
        
        BaseSingleFieldPeriod.standardPeriodIn(period, n);
    }
    
    @Test
    public void testConstructorAndGetters() {
        BaseSingleFieldPeriod period = new TestBaseSingleFieldPeriod(5);
        
        assertEquals(5, period.getValue());
        assertEquals(TestBaseSingleFieldPeriod.FIELD_TYPE, period.getFieldType());
        assertEquals(TestBaseSingleFieldPeriod.PERIOD_TYPE, period.getPeriodType());
        assertEquals(1, period.size());
        assertEquals(TestBaseSingleFieldPeriod.FIELD_TYPE, period.getFieldType(0));
        assertEquals(5, period.getValue(0));
        assertEquals(5, period.get(TestBaseSingleFieldPeriod.FIELD_TYPE));
    }
    
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new TestBaseSingleFieldPeriod(5);
        
        assertTrue(period.isSupported(TestBaseSingleFieldPeriod.FIELD_TYPE));
        assertFalse(period.isSupported(TestBaseSingleFieldPeriod.OTHER_FIELD_TYPE));
    }
    
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new TestBaseSingleFieldPeriod(5);
        
        Period result = period.toPeriod();
        
        assertEquals(Period.ZERO.withFields(period), result);
    }
    
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new TestBaseSingleFieldPeriod(5);
        
        MutablePeriod result = period.toMutablePeriod();
        
        MutablePeriod expected = new MutablePeriod();
        expected.add(period);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new TestBaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new TestBaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period3 = new TestBaseSingleFieldPeriod(10);
        
        assertTrue(period1.equals(period2));
        assertFalse(period1.equals(period3));
        assertFalse(period1.equals(null));
        assertFalse(period1.equals("test"));
    }
    
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new TestBaseSingleFieldPeriod(5);
        int expectedHashCode = 27 * (27 * 17 + 5) + TestBaseSingleFieldPeriod.FIELD_TYPE.hashCode();
        
        assertEquals(expectedHashCode, period.hashCode());
    }
    
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new TestBaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new TestBaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period3 = new TestBaseSingleFieldPeriod(5);
        
        assertTrue(period1.compareTo(period2) < 0);
        assertTrue(period2.compareTo(period1) > 0);
        assertTrue(period1.compareTo(period3) == 0);
    }
    
    private static class TestBaseSingleFieldPeriod extends BaseSingleFieldPeriod {
        private static final long serialVersionUID = 1L;
        private static final DurationFieldType FIELD_TYPE = DurationFieldType.days();
        private static final DurationFieldType OTHER_FIELD_TYPE = DurationFieldType.weeks();
        private static final PeriodType PERIOD_TYPE = PeriodType.standard();
        
        protected TestBaseSingleFieldPeriod(int iPeriod) {
            super(iPeriod);
        }
        
        @Override
        public DurationFieldType getFieldType() {
            return FIELD_TYPE;
        }
        
        @Override
        public PeriodType getPeriodType() {
            return PERIOD_TYPE;
        }
    }

}