import org.joda.time.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BasePeriodTest {

    private BasePeriod basePeriod;
    private ReadableInstant fromInstant;
    private ReadableInstant toInstant;
    private ReadablePeriod readablePeriod;
    private ReadableDuration readableDuration;

    @Before
    public void setUp() {
        basePeriod = new BasePeriod(1, 2, 0, 4, 5, 6, 7, 8);
        fromInstant = new DateTime(2021, 1, 1, 0, 0);
        toInstant = new DateTime(2022, 1, 1, 0, 0);
        readablePeriod = new Period(1, 2, 0, 4, 5, 6, 7, 8);
        readableDuration = new Duration(1000);
    }

    @Test
    public void testConstructor_WithPeriodValues() {
        assertEquals(1, basePeriod.getValue(DurationFieldType.years()));
        assertEquals(2, basePeriod.getValue(DurationFieldType.months()));
        assertEquals(0, basePeriod.getValue(DurationFieldType.weeks()));
        assertEquals(4, basePeriod.getValue(DurationFieldType.days()));
        assertEquals(5, basePeriod.getValue(DurationFieldType.hours()));
        assertEquals(6, basePeriod.getValue(DurationFieldType.minutes()));
        assertEquals(7, basePeriod.getValue(DurationFieldType.seconds()));
        assertEquals(8, basePeriod.getValue(DurationFieldType.millis()));
    }

    @Test
    public void testConstructor_WithInstantValues() {
        basePeriod = new BasePeriod(fromInstant, toInstant, PeriodType.standard());
        assertEquals(1, basePeriod.getValue(DurationFieldType.years()));
        assertEquals(1, basePeriod.getValue(DurationFieldType.months())); 
        assertEquals(0, basePeriod.getValue(DurationFieldType.weeks()));
        assertEquals(0, basePeriod.getValue(DurationFieldType.days()));
        assertEquals(0, basePeriod.getValue(DurationFieldType.hours()));
        assertEquals(0, basePeriod.getValue(DurationFieldType.minutes()));
        assertEquals(0, basePeriod.getValue(DurationFieldType.seconds()));
        assertEquals(0, basePeriod.getValue(DurationFieldType.millis()));
    }

    @Test
    public void testConstructor_WithReadablePeriod() {
        basePeriod = new BasePeriod(readablePeriod);
        assertEquals(1, basePeriod.getValue(DurationFieldType.years()));
        assertEquals(2, basePeriod.getValue(DurationFieldType.months()));
        assertEquals(0, basePeriod.getValue(DurationFieldType.weeks()));
        assertEquals(4, basePeriod.getValue(DurationFieldType.days()));
        assertEquals(5, basePeriod.getValue(DurationFieldType.hours()));
        assertEquals(6, basePeriod.getValue(DurationFieldType.minutes()));
        assertEquals(7, basePeriod.getValue(DurationFieldType.seconds()));
        assertEquals(8, basePeriod.getValue(DurationFieldType.millis()));
    }

    @Test
    public void testConstructor_WithReadableInstantAndReadableDuration() {
        basePeriod = new BasePeriod(fromInstant, readableDuration, PeriodType.standard());
        assertEquals(0, basePeriod.getValue(DurationFieldType.years()));
        assertEquals(0, basePeriod.getValue(DurationFieldType.months()));
        assertEquals(0, basePeriod.getValue(DurationFieldType.weeks()));
        assertEquals(0, basePeriod.getValue(DurationFieldType.days()));
        assertEquals(0, basePeriod.getValue(DurationFieldType.hours()));
        assertEquals(0, basePeriod.getValue(DurationFieldType.minutes()));
        assertEquals(0, basePeriod.getValue(DurationFieldType.seconds()));
        assertEquals(1000, basePeriod.getValue(DurationFieldType.millis()));
    }

    @Test
    public void testGetPeriodType() {
        PeriodType periodType = basePeriod.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
    }

    @Test
    public void testGetValue() {
        int value = basePeriod.getValue(DurationFieldType.years());
        assertEquals(1, value);
    }

    @Test
    public void testToDurationFrom() {
        Duration duration = basePeriod.toDurationFrom(fromInstant);
        assertEquals(31536000000L, duration.getMillis());
    }

    @Test
    public void testToDurationTo() {
        Duration duration = basePeriod.toDurationTo(toInstant);
        assertEquals(-31536000000L, duration.getMillis());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetField_InvalidFieldType() {
        basePeriod.setField(DurationFieldType.centuries(), 2);
    }

    @Test
    public void testSetField_ValidFieldType() {
        basePeriod.setField(DurationFieldType.years(), 2);
        assertEquals(2, basePeriod.getValue(DurationFieldType.years()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddField_InvalidFieldType() {
        basePeriod.addField(DurationFieldType.centuries(), 2);
    }

    @Test
    public void testAddField_ValidFieldType() {
        basePeriod.addField(DurationFieldType.years(), 2);
        assertEquals(3, basePeriod.getValue(DurationFieldType.years()));
    }

    @Test
    public void testMergePeriod() {
        BasePeriod mergePeriod = new BasePeriod(readablePeriod);
        basePeriod.mergePeriod(mergePeriod);
        assertEquals(2, basePeriod.getValue(DurationFieldType.months()));
    }

    @Test
    public void testAddPeriod() {
        BasePeriod addPeriod = new BasePeriod(readablePeriod);
        basePeriod.addPeriod(addPeriod);
        assertEquals(4, basePeriod.getValue(DurationFieldType.days()));
    }
}