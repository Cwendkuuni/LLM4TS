package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.BaseDateTime;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;

/**
 * Test cases for the BasicMonthOfYearDateTimeField class.
 */
public class BasicMonthOfYearDateTimeFieldTest {

    /** The test chronology. */
    private static final BasicChronology TEST_CHRONOLOGY = new GregorianChronology();;

    /** A test field instance. */
    private BasicMonthOfYearDateTimeField field;

    /** Set up test. */
    @Before
    public void setUp() {
        field = new BasicMonthOfYearDateTimeField(TEST_CHRONOLOGY, DateTimeConstants.JANUARY);
    }

    /**
     * Test isLenient() method.
     */
    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }

    /**
     * Test get() method.
     */
    @Test
    public void testGet() {
        assertEquals(DateTimeConstants.JANUARY, field.get(0L));
        assertEquals(DateTimeConstants.DECEMBER, field.get(365L));
        assertEquals(DateTimeConstants.JANUARY, field.get(DateTimeConstants.MILLIS_PER_DAY * 365));
        assertEquals(DateTimeConstants.JANUARY, field.get(DateTimeConstants.MILLIS_PER_DAY * 365 + 1L));
    }

    /**
     * Test add() method with int argument.
     */
    @Test
    public void testAddInt() {
        long testValue = DateTimeConstants.MILLIS_PER_DAY * 365;

        long result = field.add(testValue, 1);
        assertEquals(DateTimeConstants.JANUARY, field.get(result));

        result = field.add(testValue, -1);
        assertEquals(DateTimeConstants.DECEMBER, field.get(result));

        result = field.add(testValue, 2);
        assertEquals(DateTimeConstants.FEBRUARY, field.get(result));

        result = field.add(testValue, 12);
        assertEquals(DateTimeConstants.DECEMBER, field.get(result));

        result = field.add(testValue, 13);
        assertEquals(DateTimeConstants.JANUARY, field.get(result));

        result = field.add(testValue, 400);
        assertEquals(DateTimeConstants.JANUARY, field.get(result));
    }

    /**
     * Test add() method with long argument.
     */
    @Test
    public void testAddLong() {
        long testValue = DateTimeConstants.MILLIS_PER_DAY * 365;

        long result = field.add(testValue, 1L);
        assertEquals(DateTimeConstants.JANUARY, field.get(result));

        result = field.add(testValue, -1L);
        assertEquals(DateTimeConstants.DECEMBER, field.get(result));

        result = field.add(testValue, 2L);
        assertEquals(DateTimeConstants.FEBRUARY, field.get(result));

        result = field.add(testValue, 12L);
        assertEquals(DateTimeConstants.DECEMBER, field.get(result));

        result = field.add(testValue, 13L);
        assertEquals(DateTimeConstants.JANUARY, field.get(result));

        result = field.add(testValue, 400L);
        assertEquals(DateTimeConstants.JANUARY, field.get(result));
    }

    /**
     * Test addWrapField() method.
     */
    @Test
    public void testAddWrapField() {
        long testValue = DateTimeConstants.MILLIS_PER_DAY * 365;

        long result = field.addWrapField(testValue, 1);
        assertEquals(DateTimeConstants.JANUARY, field.get(result));

        result = field.addWrapField(testValue, -1);
        assertEquals(DateTimeConstants.DECEMBER, field.get(result));

        result = field.addWrapField(testValue, 12);
        assertEquals(DateTimeConstants.DECEMBER, field.get(result));

        result = field.addWrapField(testValue, 13);
        assertEquals(DateTimeConstants.JANUARY, field.get(result));
    }

    /**
     * Test getDifferenceAsLong() method.
     */
    @Test
    public void testGetDifferenceAsLong() {
        long minuendInstant = DateTimeConstants.MILLIS_PER_DAY * 365;
        long subtrahendInstant = DateTimeConstants.MILLIS_PER_DAY * 365 + 1;

        long difference = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(1, difference);

        minuendInstant = DateTimeConstants.MILLIS_PER_DAY * 365;
        subtrahendInstant = DateTimeConstants.MILLIS_PER_DAY * 365 - 1;

        difference = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(-1, difference);

        minuendInstant = DateTimeConstants.MILLIS_PER_DAY * 365 + 1;
        subtrahendInstant = DateTimeConstants.MILLIS_PER_DAY * 365;

        difference = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(1, difference);

        minuendInstant = DateTimeConstants.MILLIS_PER_DAY * 365 + 1;
        subtrahendInstant = DateTimeConstants.MILLIS_PER_DAY * 365 + 1;

        difference = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(0, difference);
    }

    /**
     * Test set() method.
     */
    @Test
    public void testSet() {
        long testInstant = DateTimeConstants.MILLIS_PER_DAY * 365;

        long result = field.set(testInstant, DateTimeConstants.JANUARY);
        assertEquals(DateTimeConstants.JANUARY, field.get(result));

        result = field.set(testInstant, DateTimeConstants.DECEMBER);
        assertEquals(DateTimeConstants.DECEMBER, field.get(result));

        result = field.set(testInstant, DateTimeConstants.JANUARY + 1);
        assertEquals(DateTimeConstants.FEBRUARY, field.get(result));

        result = field.set(testInstant, DateTimeConstants.JANUARY - 1);
        assertEquals(DateTimeConstants.DECEMBER, field.get(result));
    }

    /**
     * Test getRangeDurationField() method.
     */
    @Test
    public void testGetRangeDurationField() {
        DurationField result = field.getRangeDurationField();
        assertNotNull(result);
        assertEquals(DurationFieldType.YEARS, result.getFieldType());
    }

    /**
     * Test isLeap() method.
     */
    @Test
    public void testIsLeap() {
        long testInstant = DateTimeConstants.MILLIS_PER_DAY * 365;
        assertTrue(field.isLeap(testInstant));

        testInstant += DateTimeConstants.MILLIS_PER_DAY;
        assertFalse(field.isLeap(testInstant));
    }

    /**
     * Test getLeapAmount() method.
     */
    @Test
    public void testGetLeapAmount() {
        long testInstant = DateTimeConstants.MILLIS_PER_DAY * 365;
        assertEquals(0, field.getLeapAmount(testInstant));

        testInstant += DateTimeConstants.MILLIS_PER_DAY;
        assertEquals(1, field.getLeapAmount(testInstant));
    }

    /**
     * Test getLeapDurationField() method.
     */
    @Test
    public void testGetLeapDurationField() {
        DurationField result = field.getLeapDurationField();
        assertNotNull(result);
        assertEquals(DurationFieldType.DAYS, result.getFieldType());
    }

    /**
     * Test getMinimumValue() method.
     */
    @Test
    public void testGetMinimumValue() {
        int result = field.getMinimumValue();
        assertEquals(DateTimeConstants.JANUARY, result);
    }

    /**
     * Test getMaximumValue() method.
     */
    @Test
    public void testGetMaximumValue() {
        int result = field.getMaximumValue();
        assertEquals(DateTimeConstants.DECEMBER, result);
    }

    /**
     * Test roundFloor() method.
     */
    @Test
    public void testRoundFloor() {
        long testInstant = DateTimeConstants.MILLIS_PER_DAY * 365;

        long result = field.roundFloor(testInstant);
        assertEquals(DateTimeConstants.MILLIS_PER_DAY * 365, result);

        testInstant += DateTimeConstants.MILLIS_PER_DAY;
        result = field.roundFloor(testInstant);
        assertEquals(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY, result);

        testInstant += DateTimeConstants.MILLIS_PER_DAY;
        result = field.roundFloor(testInstant);
        assertEquals(DateTimeConstants.MILLIS_PER_DAY * 366, result);
    }

    /**
     * Test remainder() method.
     */
    @Test
    public void testRemainder() {
        long testInstant = DateTimeConstants.MILLIS_PER_DAY * 365;

        long result = field.remainder(testInstant);
        assertEquals(0, result);

        testInstant += DateTimeConstants.MILLIS_PER_DAY;
        result = field.remainder(testInstant);
        assertEquals(-DateTimeConstants.MILLIS_PER_DAY, result);

        testInstant += DateTimeConstants.MILLIS_PER_DAY;
        result = field.remainder(testInstant);
        assertEquals(DateTimeConstants.MILLIS_PER_DAY, result);

        testInstant += DateTimeConstants.MILLIS_PER_DAY;
        result = field.remainder(testInstant);
        assertEquals(0, result);

        testInstant += DateTimeConstants.MILLIS_PER_DAY;
        result = field.remainder(testInstant);
        assertEquals(-DateTimeConstants.MILLIS_PER_DAY, result);
    }
}