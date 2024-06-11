import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.joda.time.DateTimeUtils;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.MutablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;

public class BaseSingleFieldPeriodTest {
    
    private BaseSingleFieldPeriod baseSingleFieldPeriod;
    
    @Before
    public void setUp() {
        baseSingleFieldPeriod = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }
            
            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
    }
    
    @Test
    public void testBetweenWithReadableInstant() {
        ReadableInstant readableInstant = new DateTime(2019, 1, 1, 0, 0, 0);
        ReadableInstant readableInstant2 = new DateTime(2019, 1, 2, 0, 0, 0);
        DurationFieldType durationFieldType = DurationFieldType.years();

        int result = BaseSingleFieldPeriod.between(readableInstant, readableInstant2, durationFieldType);

        assertEquals(1, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNullReadableInstant() {
        ReadableInstant readableInstant = null;
        ReadableInstant readableInstant2 = new DateTime(2019, 1, 2, 0, 0, 0);
        DurationFieldType durationFieldType = DurationFieldType.years();

        BaseSingleFieldPeriod.between(readableInstant, readableInstant2, durationFieldType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNullDurationFieldType() {
        ReadableInstant readableInstant = new DateTime(2019, 1, 1, 0, 0, 0);
        ReadableInstant readableInstant2 = new DateTime(2019, 1, 2, 0, 0, 0);
        DurationFieldType durationFieldType = null;

        BaseSingleFieldPeriod.between(readableInstant, readableInstant2, durationFieldType);
    }
    
    @Test
    public void testBetweenWithReadablePartial() {
        ReadablePartial readablePartial = new DateTime(2019, 1, 1, 0, 0, 0);
        ReadablePartial readablePartial2 = new DateTime(2019, 1, 2, 0, 0, 0);
        ReadablePeriod readablePeriod = null;

        int result = BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);

        assertEquals(0, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNullReadablePartial() {
        ReadablePartial readablePartial = null;
        ReadablePartial readablePartial2 = new DateTime(2019, 1, 2, 0, 0, 0);
        ReadablePeriod readablePeriod = null;

        BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithDifferentSizeReadablePartial() {
        ReadablePartial readablePartial = new DateTime(2019, 1, 1, 0, 0, 0);
        ReadablePartial readablePartial2 = new DateTime(2019, 1, 2, 0, 0, 0);
        ReadablePeriod readablePeriod = null;

        BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithDifferentFieldTypeReadablePartial() {
        ReadablePartial readablePartial = new DateTime(2019, 1, 1, 0, 0, 0);
        ReadablePartial readablePartial2 = new DateTime(2019, 1, 2, 0, 0, 0);
        ReadablePeriod readablePeriod = null;

        BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNonContiguousReadablePartial() {
        ReadablePartial readablePartial = new DateTime(2019, 1, 1, 0, 0, 0);
        ReadablePartial readablePartial2 = new DateTime(2019, 1, 3, 0, 0, 0);
        ReadablePeriod readablePeriod = null;

        BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
    }
    
    @Test
    public void testStandardPeriodInWithReadablePeriod() {
        ReadablePeriod readablePeriod = null;
        long n = 1;

        int result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);

        assertEquals(0, result);
    }
    
    @Test
    public void testStandardPeriodInWithZeroReadablePeriod() {
        ReadablePeriod readablePeriod = null;
        long n = 1;

        int result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);

        assertEquals(0, result);
    }
    
    @Test
    public void testStandardPeriodInWithPreciseReadablePeriod() {
        ReadablePeriod readablePeriod = new Period(1, DurationFieldType.days());
        long n = 1;

        int result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);

        assertEquals(1, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInWithNonPreciseReadablePeriod() {
        ReadablePeriod readablePeriod = new Period(1, DurationFieldType.months());
        long n = 1;

        BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
    }
    
    @Test
    public void testGetValue() {
        int result = baseSingleFieldPeriod.getValue();

        assertEquals(1, result);
    }
    
    @Test
    public void testSetValue() {
        baseSingleFieldPeriod.setValue(2);

        int result = baseSingleFieldPeriod.getValue();

        assertEquals(2, result);
    }
    
    @Test
    public void testSize() {
        int result = baseSingleFieldPeriod.size();

        assertEquals(1, result);
    }
    
    @Test
    public void testGetFieldTypeAtIndex0() {
        DurationFieldType result = baseSingleFieldPeriod.getFieldType(0);

        assertEquals(null, result);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeAtIndex1() {
        baseSingleFieldPeriod.getFieldType(1);
    }
    
    @Test
    public void testGetValueAtIndex0() {
        int result = baseSingleFieldPeriod.getValue(0);

        assertEquals(1, result);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueAtIndex1() {
        baseSingleFieldPeriod.getValue(1);
    }
    
    @Test
    public void testGetWithMatchingFieldType() {
        DurationFieldType durationFieldType = null;

        int result = baseSingleFieldPeriod.get(durationFieldType);

        assertEquals(1, result);
    }
    
    @Test
    public void testGetWithDifferentFieldType() {
        DurationFieldType durationFieldType = DurationFieldType.years();

        int result = baseSingleFieldPeriod.get(durationFieldType);

        assertEquals(0, result);
    }
    
    @Test
    public void testIsSupportedWithMatchingFieldType() {
        DurationFieldType durationFieldType = null;

        boolean result = baseSingleFieldPeriod.isSupported(durationFieldType);

        assertTrue(result);
    }
    
    @Test
    public void testIsSupportedWithDifferentFieldType() {
        DurationFieldType durationFieldType = DurationFieldType.years();

        boolean result = baseSingleFieldPeriod.isSupported(durationFieldType);

        assertFalse(result);
    }
    
    @Test
    public void testToPeriod() {
        Period result = baseSingleFieldPeriod.toPeriod();

        assertEquals(Period.ZERO, result);
    }
    
    @Test
    public void testToMutablePeriod() {
        MutablePeriod result = baseSingleFieldPeriod.toMutablePeriod();

        assertEquals(new MutablePeriod().add(baseSingleFieldPeriod), result);
    }
    
    @Test
    public void testEqualsWithEqualObjects() {
        Object o = baseSingleFieldPeriod;

        boolean result = baseSingleFieldPeriod.equals(o);

        assertTrue(result);
    }
    
    @Test
    public void testEqualsWithDifferentObject() {
        Object o = new Object();

        boolean result = baseSingleFieldPeriod.equals(o);

        assertFalse(result);
    }
    
    @Test
    public void testEqualsWithDifferentPeriodType() {
        ReadablePeriod readablePeriod = new Period(1, DurationFieldType.days());
        Object o = readablePeriod;

        boolean result = baseSingleFieldPeriod.equals(o);

        assertFalse(result);
    }
    
    @Test
    public void testEqualsWithDifferentValue() {
        ReadablePeriod readablePeriod = new Period(2, DurationFieldType.days());
        Object o = readablePeriod;

        boolean result = baseSingleFieldPeriod.equals(o);

        assertFalse(result);
    }
    
    @Test
    public void testHashCode() {
        int result = baseSingleFieldPeriod.hashCode();

        assertEquals(27 * (27 * 17 + 1) + 0, result);
    }
    
    @Test
    public void testCompareToWithEqualObjects() {
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }
            
            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        int result = baseSingleFieldPeriod.compareTo(baseSingleFieldPeriod2);

        assertEquals(0, result);
    }
    
    @Test(expected = ClassCastException.class)
    public void testCompareToWithDifferentClasses() {
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new ExtendedBaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }
            
            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        baseSingleFieldPeriod.compareTo(baseSingleFieldPeriod2);
    }
}

class ExtendedBaseSingleFieldPeriod extends BaseSingleFieldPeriod {
    
    public ExtendedBaseSingleFieldPeriod(int iPeriod) {
        super(iPeriod);
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