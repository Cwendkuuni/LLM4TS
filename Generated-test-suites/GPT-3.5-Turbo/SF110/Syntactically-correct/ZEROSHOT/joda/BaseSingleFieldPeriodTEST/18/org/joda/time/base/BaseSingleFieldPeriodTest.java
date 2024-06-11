import org.joda.time.*;
import org.joda.time.chrono.ISOChronology;
import org.junit.*;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween_ReadableInstant() {
        long millis = 1000000L;
        long millis2 = 2000000L;
        int expected = 1;
        int actual = BaseSingleFieldPeriod.between(new Instant(millis), new Instant(millis2), DurationFieldType.days());
        assertEquals(expected, actual);
    }
    
    @Test
    public void testBetween_ReadablePartial() {
        LocalDate date = new LocalDate(2022, 1, 1);
        LocalDate date2 = new LocalDate(2022, 1, 2);
        int expected = 1;
        int actual = BaseSingleFieldPeriod.between(date, date2, Period.years());
        assertEquals(expected, actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_NullReadableInstant() {
        BaseSingleFieldPeriod.between(null, new Instant(), DurationFieldType.minutes());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_NullReadablePartial() {
        BaseSingleFieldPeriod.between(null, new LocalDate(2022, 1, 1), Period.months());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_DifferentSizeReadablePartial() {
        LocalTime time = new LocalTime(12, 0);
        LocalDate date = new LocalDate(2022, 1, 1);
        BaseSingleFieldPeriod.between(time, date, Period.hours());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_DifferentFieldsReadablePartial() {
        LocalDate date = new LocalDate(2022, 1, 1);
        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 12, 0);
        BaseSingleFieldPeriod.between(date, dateTime, Period.minutes());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_NonContiguousReadablePartial() {
        LocalDate date = new LocalDate(2022, 1, 1);
        LocalDate date2 = new LocalDate(2022, 1, 3);
        BaseSingleFieldPeriod.between(date, date2, Period.days());
    }
    
    @Test
    public void testStandardPeriodIn() {
        Period period = new Period(1, 2, 3, 4, 5, 6, 7, 8);
        long n = 1000L;
        int expected = 126006721;
        int actual = BaseSingleFieldPeriod.standardPeriodIn(period, n);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testStandardPeriodIn_NullPeriod() {
        int expected = 0;
        int actual = BaseSingleFieldPeriod.standardPeriodIn(null, 1000L);
        assertEquals(expected, actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodIn_UnpreciseDurationField() {
        Period period = Period.days(1);
        long n = 1000L;
        BaseSingleFieldPeriod.standardPeriodIn(period, n);
    }
    
    @Test
    public void testBaseSingleFieldPeriod() {
        int iPeriod = 5;
        BaseSingleFieldPeriod instance = new TestSingleFieldPeriod(iPeriod);
        int expected = iPeriod;
        int actual = instance.getValue();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod instance = new TestSingleFieldPeriod(5);
        int iPeriod = 10;
        instance.setValue(iPeriod);
        int expected = iPeriod;
        int actual = instance.getValue();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod instance = new TestSingleFieldPeriod(5);
        DurationFieldType expected = DurationFieldType.days();
        DurationFieldType actual = instance.getFieldType();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod instance = new TestSingleFieldPeriod(5);
        PeriodType expected = PeriodType.standard();
        PeriodType actual = instance.getPeriodType();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSize() {
        BaseSingleFieldPeriod instance = new TestSingleFieldPeriod(5);
        int expected = 1;
        int actual = instance.size();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetFieldTypeInt() {
        BaseSingleFieldPeriod instance = new TestSingleFieldPeriod(5);
        DurationFieldType expected = DurationFieldType.days();
        DurationFieldType actual = instance.getFieldType(0);
        assertEquals(expected, actual);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeInt_IndexOutOfBounds() {
        BaseSingleFieldPeriod instance = new TestSingleFieldPeriod(5);
        instance.getFieldType(1);
    }
    
    @Test
    public void testGetValueInt() {
        BaseSingleFieldPeriod instance = new TestSingleFieldPeriod(5);
        int expected = 5;
        int actual = instance.getValue(0);
        assertEquals(expected, actual);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueInt_IndexOutOfBounds() {
        BaseSingleFieldPeriod instance = new TestSingleFieldPeriod(5);
        instance.getValue(1);
    }
    
    @Test
    public void testGet() {
        BaseSingleFieldPeriod instance = new TestSingleFieldPeriod(5);
        int expected = 5;
        int actual = instance.get(DurationFieldType.days());
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGet_NotSupported() {
        BaseSingleFieldPeriod instance = new TestSingleFieldPeriod(5);
        int expected = 0;
        int actual = instance.get(DurationFieldType.months());
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod instance = new TestSingleFieldPeriod(5);
        boolean expected = true;
        boolean actual = instance.isSupported(DurationFieldType.days());
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsSupported_NotSupported() {
        BaseSingleFieldPeriod instance = new TestSingleFieldPeriod(5);
        boolean expected = false;
        boolean actual = instance.isSupported(DurationFieldType.months());
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod instance = new TestSingleFieldPeriod(5);
        Period expected = Period.days(5);
        Period actual = instance.toPeriod();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod instance = new TestSingleFieldPeriod(5);
        MutablePeriod expected = new MutablePeriod().withDays(5);
        MutablePeriod actual = instance.toMutablePeriod();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals_SameInstance() {
        BaseSingleFieldPeriod instance = new TestSingleFieldPeriod(5);
        boolean expected = true;
        boolean actual = instance.equals(instance);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals_SameValues() {
        BaseSingleFieldPeriod instance1 = new TestSingleFieldPeriod(5);
        BaseSingleFieldPeriod instance2 = new TestSingleFieldPeriod(5);
        boolean expected = true;
        boolean actual = instance1.equals(instance2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals_DifferentValues() {
        BaseSingleFieldPeriod instance1 = new TestSingleFieldPeriod(5);
        BaseSingleFieldPeriod instance2 = new TestSingleFieldPeriod(10);
        boolean expected = false;
        boolean actual = instance1.equals(instance2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals_DifferentType() {
        BaseSingleFieldPeriod instance = new TestSingleFieldPeriod(5);
        Object obj = new Object();
        boolean expected = false;
        boolean actual = instance.equals(obj);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod instance = new TestSingleFieldPeriod(5);
        int expected = 457;
        int actual = instance.hashCode();
        assertEquals(expected, actual);
    }
    
    @Test(expected = ClassCastException.class)
    public void testCompareTo_DifferentClasses() {
        BaseSingleFieldPeriod instance1 = new TestSingleFieldPeriod(5);
        BaseSingleFieldPeriod instance2 = new TestSingleFieldPeriod2(5);
        instance1.compareTo(instance2);
    }
    
    @Test
    public void testCompareTo_SmallerValue() {
        BaseSingleFieldPeriod instance1 = new TestSingleFieldPeriod(5);
        BaseSingleFieldPeriod instance2 = new TestSingleFieldPeriod(10);
        int expected = -1;
        int actual = instance1.compareTo(instance2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCompareTo_LargerValue() {
        BaseSingleFieldPeriod instance1 = new TestSingleFieldPeriod(10);
        BaseSingleFieldPeriod instance2 = new TestSingleFieldPeriod(5);
        int expected = 1;
        int actual = instance1.compareTo(instance2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCompareTo_SameValue() {
        BaseSingleFieldPeriod instance1 = new TestSingleFieldPeriod(5);
        BaseSingleFieldPeriod instance2 = new TestSingleFieldPeriod(5);
        int expected = 0;
        int actual = instance1.compareTo(instance2);
        assertEquals(expected, actual);
    }
    
    private class TestSingleFieldPeriod extends BaseSingleFieldPeriod {
        TestSingleFieldPeriod(int iPeriod) {
            super(iPeriod);
        }

        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.days();
        }

        @Override
        public PeriodType getPeriodType() {
            return PeriodType.standard();
        }
    }
    
    private class TestSingleFieldPeriod2 extends BaseSingleFieldPeriod {
        TestSingleFieldPeriod2(int iPeriod) {
            super(iPeriod);
        }

        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.hours();
        }

        @Override
        public PeriodType getPeriodType() {
            return PeriodType.standard();
        }
    }
}