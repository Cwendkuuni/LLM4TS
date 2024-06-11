import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.UnsupportedDurationField;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    private BaseSingleFieldPeriod period;
    
    @Before
    public void setUp() {
        period = new TestSingleFieldPeriod(5);
    }
    
    @After
    public void tearDown() {
        period = null;
    }
    
    @Test
    public void testBetween() {
        DateTime dateTime1 = new DateTime(2020, 1, 1, 0, 0, 0, ISOChronology.getInstanceUTC());
        DateTime dateTime2 = new DateTime(2020, 2, 1, 0, 0, 0, ISOChronology.getInstanceUTC());
        
        int result = TestSingleFieldPeriod.between(dateTime1, dateTime2, Months.months());
        
        Assert.assertEquals("Months between two dates should be 1", 1, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_NullReadableInstant() {
        TestSingleFieldPeriod.between(null, null, Months.months());
    }
    
    @Test
    public void testBetween_SameDates() {
        DateTime dateTime1 = new DateTime(2020, 1, 1, 0, 0, 0, ISOChronology.getInstanceUTC());
        
        int result = TestSingleFieldPeriod.between(dateTime1, dateTime1, Months.months());
        
        Assert.assertEquals("Months between same dates should be 0", 0, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_NonContiguousReadablePartial() {
        LocalDateTime localDateTime1 = new LocalDateTime(2020, 1, 1, 0, 0, 0, ISOChronology.getInstanceUTC());
        LocalDateTime localDateTime2 = new LocalDateTime(2020, 2, 1, 0, 0, 0, ISOChronology.getInstanceUTC());
        
        TestSingleFieldPeriod.between(localDateTime1, localDateTime2, TestSingleFieldPeriodStub.singletonPeriod());
    }
    
    @Test
    public void testBetween_ReadablePartialWithDifferentSize() {
        LocalDateTime localDateTime1 = new LocalDateTime(2020, 1, 1, 0, 0, 0, ISOChronology.getInstanceUTC());
        LocalDateTime localDateTime2 = new LocalDateTime(2020, 2, 1, 0, 0, 0, ISOChronology.getInstanceUTC());
        
        try {
            TestSingleFieldPeriod.between(localDateTime1, localDateTime2, TestSingleFieldPeriodStub.singletonPeriodWithInvalidSize());
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Unexpected exception message", "ReadablePartial objects must have the same set of fields", e.getMessage());
        }
    }
    
    @Test
    public void testBetween_ReadablePartialWithDifferentFields() {
        LocalDateTime localDateTime1 = new LocalDateTime(2020, 1, 1, 0, 0, 0, ISOChronology.getInstanceUTC());
        LocalDateTime localDateTime2 = new LocalDateTime(2020, 2, 1, 0, 0, 0, ISOChronology.getInstanceUTC());
        
        try {
            TestSingleFieldPeriod.between(localDateTime1, localDateTime2, TestSingleFieldPeriodStub.differentFieldsPeriod());
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Unexpected exception message", "ReadablePartial objects must have the same set of fields", e.getMessage());
        }
    }
    
    @Test
    public void testStandardPeriodIn() {
        Period period = new Period(1, 2, 3, 4, 5, 6, 7, 8);
        
        int result = TestSingleFieldPeriod.standardPeriodIn(period, 1000);
        
        Assert.assertEquals("Standard period in should match", 819964448, result);
    }
    
    @Test
    public void testStandardPeriodIn_NullReadablePeriod() {
        int result = TestSingleFieldPeriod.standardPeriodIn(null, 1000);
        
        Assert.assertEquals("Standard period in with null readable period should be 0", 0, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodIn_UnsupportedDurationField() {
        Period period = new Period(1, 2, 3, 4, 5, 6, 7, 8);
        
        TestSingleFieldPeriod.standardPeriodIn(period, 0);
    }
    
    @Test
    public void testGetValue() {
        int result = period.getValue();
        
        Assert.assertEquals("Returned value should match", 5, result);
    }
    
    @Test
    public void testSetValue() {
        period.setValue(10);
        
        int result = period.getValue();
        
        Assert.assertEquals("Returned value should match", 10, result);
    }
    
    @Test
    public void testGetFieldType() {
        Assert.assertNotNull("Field type should not be null", period.getFieldType());
    }
    
    @Test
    public void testGetPeriodType() {
        Assert.assertNotNull("Period type should not be null", period.getPeriodType());
    }
    
    @Test
    public void testSize() {
        int result = period.size();
        
        Assert.assertEquals("Size should be 1", 1, result);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldType_InvalidIndex() {
        period.getFieldType(1);
    }
    
    @Test
    public void testGetFieldType_ValidIndex() {
        Assert.assertNotNull("Field type should not be null", period.getFieldType(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValue_InvalidIndex() {
        period.getValue(1);
    }
    
    @Test
    public void testGetValue_ValidIndex() {
        int result = period.getValue(0);
        
        Assert.assertEquals("Value should match", 5, result);
    }
    
    @Test
    public void testGet() {
        int result = period.get(TestSingleFieldPeriodStub.singletonFieldType());
        
        Assert.assertEquals("Returned value should match", 5, result);
    }
    
    @Test
    public void testGet_UnsupportedFieldType() {
        int result = period.get(new UnsupportedDurationField(TestSingleFieldPeriodStub.singletonFieldType()));
        
        Assert.assertEquals("Returned value should match", 0, result);
    }
    
    @Test
    public void testIsSupported() {
        boolean result = period.isSupported(TestSingleFieldPeriodStub.singletonFieldType());
        
        Assert.assertTrue("Field type should be supported", result);
    }
    
    @Test
    public void testToPeriod() {
        Period result = period.toPeriod();
        
        Assert.assertNotNull("Returned period should not be null", result);
        Assert.assertEquals("Returned period value should match", 5, result.getMonths());
    }
    
    @Test
    public void testToMutablePeriod() {
        MutablePeriod result = period.toMutablePeriod();
        
        Assert.assertNotNull("Returned mutable period should not be null", result);
        Assert.assertEquals("Returned mutable period value should match", 5, result.getMonths());
    }
    
    @Test
    public void testEquals() {
        TestSingleFieldPeriod equalPeriod = new TestSingleFieldPeriod(5);
        TestSingleFieldPeriod differentPeriod = new TestSingleFieldPeriod(10);
        
        boolean result1 = period.equals(equalPeriod);
        boolean result2 = period.equals(differentPeriod);
        
        Assert.assertTrue("Equal periods should be equal", result1);
        Assert.assertFalse("Different periods should not be equal", result2);
    }
    
    @Test
    public void testHashCode() {
        int result = period.hashCode();
        
        Assert.assertEquals("HashCode should match", -1881029459, result);
    }
    
    @Test
    public void testCompareTo() {
        TestSingleFieldPeriod smallerPeriod = new TestSingleFieldPeriod(3);
        TestSingleFieldPeriod largerPeriod = new TestSingleFieldPeriod(7);
        
        int result1 = period.compareTo(smallerPeriod);
        int result2 = period.compareTo(largerPeriod);
        int result3 = period.compareTo(period);
        int result4 = period.compareTo(null);
        
        Assert.assertTrue("Comparison with smaller period should be positive", result1 > 0);
        Assert.assertTrue("Comparison with larger period should be negative", result2 < 0);
        Assert.assertEquals("Comparison with same period should be 0", 0, result3);
        Assert.assertTrue("Comparison with null should be positive", result4 > 0);
    }
    
    private static class TestSingleFieldPeriod extends BaseSingleFieldPeriod {
        
        public TestSingleFieldPeriod(int period) {
            super(period);
        }
        
        public static int between(DateTime start, DateTime end, DurationFieldType fieldType) {
            return BaseSingleFieldPeriod.between(start, end, fieldType);
        }
        
        public static int between(LocalDateTime start, LocalDateTime end, Period fieldType) {
            return BaseSingleFieldPeriod.between(start, end, fieldType);
        }
        
        public static int standardPeriodIn(Period period, long unitMillis) {
            return BaseSingleFieldPeriod.standardPeriodIn(period, unitMillis);
        }
        
        public static Period singletonPeriod() {
            return Period.months(1);
        }
        
        public static Period singletonPeriodWithInvalidSize() {
            return Period.months(2).withYears(2);
        }
        
        public static Period differentFieldsPeriod() {
            return Period.weeks(1);
        }
        
        public static DurationFieldType singletonFieldType() {
            return DurationFieldType.months();
        }
        
        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.months();
        }
        
        @Override
        public org.joda.time.PeriodType getPeriodType() {
            return org.joda.time.PeriodType.yearMonthDay();
        }
        
    }
    
    private static class TestSingleFieldPeriodStub extends BaseSingleFieldPeriod {
        
        public TestSingleFieldPeriodStub(int period) {
            super(period);
        }
        
        public static DurationFieldType singletonFieldType() {
            return DurationFieldType.months();
        }
        
        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.months();
        }
        
        @Override
        public org.joda.time.PeriodType getPeriodType() {
            return org.joda.time.PeriodType.yearMonthDay();
        }
        
    }
}