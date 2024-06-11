import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;

public class BaseSingleFieldPeriodTest {

    private BaseSingleFieldPeriod period;

    @Before
    public void setUp() {
        period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
    }

    @Test
    public void testGetFieldType() {
        DurationFieldType fieldType = period.getFieldType();
        Assert.assertNotNull(fieldType);
        Assert.assertEquals(DurationFieldType.DAYS, fieldType);
    }

    @Test
    public void testGetPeriodType() {
        PeriodType periodType = period.getPeriodType();
        Assert.assertNotNull(periodType);
        Assert.assertEquals(PeriodType.standard(), periodType);
    }

    @Test
    public void testSize() {
        int size = period.size();
        Assert.assertEquals(1, size);
    }

    @Test
    public void testGetFieldTypeZero() {
        DurationFieldType fieldType = period.getFieldType(0);
        Assert.assertEquals(DurationFieldType.DAYS, fieldType);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeNegative() {
        period.getFieldType(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypePositive() {
        period.getFieldType(2);
    }

    @Test
    public void testGetValue() {
        int value = period.getValue();
        Assert.assertEquals(5, value);
    }

    @Test
    public void testGetValueZero() {
        int value = period.getValue(0);
        Assert.assertEquals(5, value);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueNegative() {
        period.getValue(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValuePositive() {
        period.getValue(2);
    }

    @Test
    public void testGet() {
        int value = period.get(DurationFieldType.DAYS);
        Assert.assertEquals(5, value);
    }

    @Test
    public void testIsSupported() {
        boolean supported = period.isSupported(DurationFieldType.DAYS);
        Assert.assertTrue(supported);

        boolean notSupported = period.isSupported(DurationFieldType.MINUTES);
        Assert.assertFalse(notSupported);
    }

    @Test
    public void testToPeriod() {
        Period period = period.toPeriod();
        Assert.assertNotNull(period);
        Assert.assertEquals(5, period.getDays());
    }

    @Test
    public void testToMutablePeriod() {
        MutablePeriod mutablePeriod = period.toMutablePeriod();
        Assert.assertNotNull(mutablePeriod);
        Assert.assertEquals(5, mutablePeriod.getDays());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        Assert.assertEquals(period, other);
    }

    @Test
    public void testHashCode() {
        int hashCode1 = period.hashCode();
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        int hashCode2 = other.hashCode();
        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testCompareToSame() {
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        int compareTo = period.compareTo(other);
        Assert.assertEquals(0, compareTo);
    }

    @Test
    public void testCompareToGreater() {
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        int compareTo = period.compareTo(other);
        Assert.assertEquals(-2, compareTo);
    }

    @Test
    public void testCompareToLess() {
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        int compareTo = period.compareTo(other);
        Assert.assertEquals(2, compareTo);
    }

}