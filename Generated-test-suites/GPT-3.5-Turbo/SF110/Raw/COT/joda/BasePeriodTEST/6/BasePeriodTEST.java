import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.convert.*;
import org.joda.time.field.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BasePeriodTest {

    private BasePeriod basePeriod;

    @Before
    public void setUp() {
        basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, null);
    }

    @Test
    public void testGetPeriodType() {
        assertEquals(null, basePeriod.getPeriodType());
    }

    @Test
    public void testGetValue() {
        assertEquals(1, basePeriod.getValue(0));
        assertEquals(2, basePeriod.getValue(1));
        assertEquals(3, basePeriod.getValue(2));
        assertEquals(4, basePeriod.getValue(3));
        assertEquals(5, basePeriod.getValue(4));
        assertEquals(6, basePeriod.getValue(5));
        assertEquals(7, basePeriod.getValue(6));
        assertEquals(8, basePeriod.getValue(7));
    }

    @Test
    public void testToDurationFrom() {
        ReadableInstant readableInstant = new DateTime();
        Duration duration = basePeriod.toDurationFrom(readableInstant);
        assertNotNull(duration);
    }

    @Test
    public void testToDurationTo() {
        ReadableInstant readableInstant = new DateTime();
        Duration duration = basePeriod.toDurationTo(readableInstant);
        assertNotNull(duration);
    }

    @Test
    public void testSetField() {
        basePeriod.setField(DurationFieldType.years(), 10);
        assertEquals(10, basePeriod.getValue(0));
    }

    @Test
    public void testAddField() {
        basePeriod.addField(DurationFieldType.years(), 10);
        assertEquals(11, basePeriod.getValue(0));
    }

    @Test
    public void testMergePeriod() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, null);
        basePeriod.mergePeriod(period);
        assertEquals(2, basePeriod.getValue(0));
        assertEquals(4, basePeriod.getValue(1));
        assertEquals(6, basePeriod.getValue(2));
        assertEquals(8, basePeriod.getValue(3));
        assertEquals(10, basePeriod.getValue(4));
        assertEquals(12, basePeriod.getValue(5));
        assertEquals(14, basePeriod.getValue(6));
        assertEquals(16, basePeriod.getValue(7));
    }

    @Test
    public void testAddPeriod() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, null);
        basePeriod.addPeriod(period);
        assertEquals(2, basePeriod.getValue(0));
        assertEquals(4, basePeriod.getValue(1));
        assertEquals(6, basePeriod.getValue(2));
        assertEquals(8, basePeriod.getValue(3));
        assertEquals(10, basePeriod.getValue(4));
        assertEquals(12, basePeriod.getValue(5));
        assertEquals(14, basePeriod.getValue(6));
        assertEquals(16, basePeriod.getValue(7));

    }
}