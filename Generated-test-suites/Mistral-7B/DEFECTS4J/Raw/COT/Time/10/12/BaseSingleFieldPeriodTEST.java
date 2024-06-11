package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.joda.time.BaseJodaTest;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;

/**
 * JUnit test class for {@link BaseSingleFieldPeriod}.
 */
public class BaseSingleFieldPeriodTest extends BaseJodaTest {

    /** The test period instance. */
    private BaseSingleFieldPeriod period;

    /** The test period type. */
    private PeriodType periodType;

    /** The test duration field type. */
    private DurationFieldType durationFieldType;

    /** The test chronology. */
    private Chronology chronology;

    /** The test instant. */
    private ReadableInstant instant;

    /**
     * Sets up the test environment.
     *
     * @throws Exception if initialization fails
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        periodType = PeriodType.standard();
        durationFieldType = DurationFieldType.DAYS;
        period = new TestSingleFieldPeriod(1);
        chronology = ISOChronology.getInstanceUTC();
        instant = new InstantMillis(System.currentTimeMillis());
    }

    /**
     * Tests the constructor.
     */
    @Test
    public void testConstructor() {
        period = new TestSingleFieldPeriod(0);
        assertEquals(0, period.getValue());

        period = new TestSingleFieldPeriod(1);
        assertEquals(1, period.getValue());
    }

    /**
     * Tests the getFieldType method.
     */
    @Test
    public void testGetFieldType() {
        assertSame(durationFieldType, period.getFieldType());
    }

    /**
     * Tests the getPeriodType method.
     */
    @Test
    public void testGetPeriodType() {
        assertSame(periodType, period.getPeriodType());
    }

    /**
     * Tests the size method.
     */
    @Test
    public void testSize() {
        assertEquals(1, period.size());
    }

    /**
     * Tests the getValue method.
     */
    @Test
    public void testGetValue() {
        assertEquals(1, period.getValue());
    }

    /**
     * Tests the getValue method with a negative value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeValue() {
        period = new TestSingleFieldPeriod(-1);
    }

    /**
     * Tests the get method.
     */
    @Test
    public void testGet() {
        assertEquals(1, period.get(durationFieldType));
    }

    /**
     * Tests the isSupported method.
     */
    @Test
    public void testIsSupported() {
        assertTrue(period.isSupported(durationFieldType));
        assertFalse(period.isSupported(DurationFieldType.HOURS));
    }

    /**
     * Tests the toPeriod method.
     */
    @Test
    public void testToPeriod() {
        Period period = period.toPeriod();
        assertEquals(1, period.getFields(periodType).get(0));
    }

    /**
     * Tests the toMutablePeriod method.
     */
    @Test
    public void testToMutablePeriod() {
        MutablePeriod mutablePeriod = period.toMutablePeriod();
        assertEquals(1, mutablePeriod.get(durationFieldType));
        assertEquals(mutablePeriod, period.toMutablePeriod());
    }

    /**
     * Tests the equals method.
     */
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod other = new TestSingleFieldPeriod(1);
        assertTrue(period.equals(other));
        assertTrue(other.equals(period));

        assertFalse(period.equals(null));
        assertFalse(period.equals(new Object()));
        assertFalse(period.equals(new TestSingleFieldPeriod(2)));
    }

    /**
     * Tests the hashCode method.
     */
    @Test
    public void testHashCode() {
        int hashCode = period.hashCode();
        BaseSingleFieldPeriod other = new TestSingleFieldPeriod(1);
        assertEquals(hashCode, other.hashCode());

        period.setValue(2);
        hashCode = period.hashCode();
        assertEquals(hashCode, other.hashCode());

        other.setValue(3);
        assertNotSame(hashCode, other.hashCode());
    }

    /**
     * Tests the compareTo method.
     */
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod other = new TestSingleFieldPeriod(2);
        assertEquals(-1, period.compareTo(other));
        assertEquals(1, other.compareTo(period));
        assertEquals(0, period.compareTo(period));
    }

    /**
     * Tests the between method with valid arguments.
     */
    @Test
    public void testBetweenValidArguments() {
        assertEquals(3, BaseSingleFieldPeriod.between(instant, instant.plus(Period.ofDays(3)), durationFieldType));
    }

    /**
     * Tests the between method with null arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullArguments() {
        BaseSingleFieldPeriod.between(null, instant, durationFieldType);
        BaseSingleFieldPeriod.between(instant, null, durationFieldType);
    }

    /**
     * Tests the between method with non-matching chronologies.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNonMatchingChronologies() {
        BaseSingleFieldPeriod.between(instant, instant.plus(Period.ofDays(3)), durationFieldType, ISOChronology.getInstanceUTC(), ISOChronology.getInstance(DateTimeZone.forID("America/New_York")));
    }

    /**
     * Tests the standardPeriodIn method with a valid argument.
     */
    @Test
    public void testStandardPeriodInValidArgument() {
        assertEquals(4, BaseSingleFieldPeriod.standardPeriodIn(period.toPeriod(), DurationFieldType.HOURS.getMillis()));
    }

    /**
     * Tests the standardPeriodIn method with a null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInNullArgument() {
        BaseSingleFieldPeriod.standardPeriodIn(null, DurationFieldType.HOURS.getMillis());
    }

    /**
     * Tests the standardPeriodIn method with an imprecise duration.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInImpreciseDuration() {
        ReadablePeriod imprecisePeriod = new TestImprecisePeriod(1, 2);
        BaseSingleFieldPeriod.standardPeriodIn(imprecisePeriod, DurationFieldType.HOURS.getMillis());
    }

    /**
     * A test single field period for testing purposes.
     */
    private static class TestSingleFieldPeriod extends BaseSingleFieldPeriod {

        /**
         * Constructor.
         *
         * @param value the period value
         */
        public TestSingleFieldPeriod(int value) {
            super(value);
        }

    }

    /**
     * A test imprecise period for testing purposes.
     */
    private static class TestImprecisePeriod implements ReadablePeriod {

        /** The period value. */
        private final int value;

        /** The number of fields. */
        private final int fields;

        /**
         * Constructor.
         *
         * @param value the period value
         * @param fields the number of fields
         */
        public TestImprecisePeriod(int value, int fields) {
            super();
            this.value = value;
            this.fields = fields;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int size() {
            return this.fields;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DurationFieldType getFieldType(int index) {
            return durationFieldType;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getValue(int index) {
            return this.value;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public PeriodType getPeriodType() {
            return periodType;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int get(DurationFieldType type) {
            return this.value;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isSupported(DurationFieldType type) {
            return true;
        }

    }

}