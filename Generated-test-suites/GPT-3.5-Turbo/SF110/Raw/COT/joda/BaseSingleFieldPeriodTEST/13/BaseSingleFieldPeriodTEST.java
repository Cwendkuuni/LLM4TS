
import org.joda.time.DurationFieldType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.Period;
import org.joda.time.MutablePeriod;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.junit.Assert;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {
    
    // Stub class for testing
    class BaseSingleFieldPeriodStub extends BaseSingleFieldPeriod {
    
        public BaseSingleFieldPeriodStub(int i) {
            super(i);
        }
    
        @Override
        public DurationFieldType getFieldType() {
            return null;
        }
    
        @Override
        public PeriodType getPeriodType() {
            return null;
        }
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriodStub period = new BaseSingleFieldPeriodStub(10);
        Assert.assertEquals(10, period.getValue());
    }
    
    @Test
    public void testSetValue() {
        BaseSingleFieldPeriodStub period = new BaseSingleFieldPeriodStub(10);
        period.setValue(20);
        Assert.assertEquals(20, period.getValue());
    }
    
    @Test
    public void testSize() {
        BaseSingleFieldPeriodStub period = new BaseSingleFieldPeriodStub(10);
        Assert.assertEquals(1, period.size());
    }
    
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriodStub period = new BaseSingleFieldPeriodStub(10);
        Assert.assertNull(period.getFieldType());
    }
    
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriodStub period = new BaseSingleFieldPeriodStub(10);
        Assert.assertNull(period.getPeriodType());
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeWithInvalidIndex() {
        BaseSingleFieldPeriodStub period = new BaseSingleFieldPeriodStub(10);
        period.getFieldType(1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithInvalidIndex() {
        BaseSingleFieldPeriodStub period = new BaseSingleFieldPeriodStub(10);
        period.getValue(1);
    }
    
    @Test
    public void testGet() {
        BaseSingleFieldPeriodStub period = new BaseSingleFieldPeriodStub(10);
        Assert.assertEquals(10, period.get(period.getFieldType()));
    }
    
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriodStub period = new BaseSingleFieldPeriodStub(10);
        Assert.assertTrue(period.isSupported(period.getFieldType()));
        Assert.assertFalse(period.isSupported(DurationFieldType.millis()));
    }
    
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriodStub period = new BaseSingleFieldPeriodStub(10);
        Assert.assertNotNull(period.toPeriod());
    }
    
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriodStub period = new BaseSingleFieldPeriodStub(10);
        Assert.assertNotNull(period.toMutablePeriod());
    }
    
    @Test
    public void testEquals() {
        BaseSingleFieldPeriodStub period1 = new BaseSingleFieldPeriodStub(10);
        BaseSingleFieldPeriodStub period2 = new BaseSingleFieldPeriodStub(10);
        Assert.assertTrue(period1.equals(period2));
        Assert.assertFalse(period1.equals("some other object"));
    }
    
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriodStub period = new BaseSingleFieldPeriodStub(10);
        Assert.assertEquals(270, period.hashCode());
    }
    
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriodStub period1 = new BaseSingleFieldPeriodStub(10);
        BaseSingleFieldPeriodStub period2 = new BaseSingleFieldPeriodStub(20);
        Assert.assertTrue(period1.compareTo(period2) < 0);
        Assert.assertTrue(period2.compareTo(period1) > 0);
        Assert.assertEquals(0, period1.compareTo(period1));
        try {
            period1.compareTo("some other object");
            Assert.fail("ClassCastException not thrown");
        } catch (ClassCastException e) {
            // exception thrown as expected
        }
    }
}
