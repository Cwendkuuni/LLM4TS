import org.joda.time.*;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.FieldUtils;
import org.junit.*;

import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {
    private BasicChronology chronology;
    private BasicMonthOfYearDateTimeField field;

    @Before
    public void setUp() {
        chronology = new BasicChronology();
        field = new BasicMonthOfYearDateTimeField(chronology, 0);
    }

    @Test
    public void testGet() {
        long instant = chronology.getYearMonthDayMillis(2000, 1, 15);
        assertEquals(1, field.get(instant));
    }

    @Test
    public void testAdd() {
        long instant = chronology.getYearMonthDayMillis(2000, 1, 15);
        long result = field.add(instant, 1);
        assertEquals(31, chronology.getDayOfMonth(result));

        result = field.add(instant, -1);
        assertEquals(31, chronology.getDayOfMonth(result));
    }

    @Test
    public void testAddWrapField() {
        long instant = chronology.getYearMonthDayMillis(2000, 12, 15);
        long result = field.addWrapField(instant, 1);
        assertEquals(1, chronology.getMonthOfYear(result));
        
        result = field.addWrapField(instant, -1);
        assertEquals(1, chronology.getMonthOfYear(result));
    }

    @Test
    public void testSet() {
        long instant = chronology.getYearMonthDayMillis(2000, 1, 15);
        long result = field.set(instant, 12);
        assertEquals(12, chronology.getMonthOfYear(result));
    }

    @Test
    public void testIsLeap() {
        long instant = chronology.getYearMonthDayMillis(2000, 2, 15);
        assertTrue(field.isLeap(instant));
        
        instant = chronology.getYearMonthDayMillis(2001, 2, 15);
        assertFalse(field.isLeap(instant));
    }

    @Test
    public void testGetLeapAmount() {
        long instant = chronology.getYearMonthDayMillis(2000, 2, 15);
        assertEquals(1, field.getLeapAmount(instant));
        
        instant = chronology.getYearMonthDayMillis(2001, 2, 15);
        assertEquals(0, field.getLeapAmount(instant));
    }

    @Test
    public void testGetMinimumValue() {
        assertEquals(1, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        assertEquals(12, field.getMaximumValue());
    }
}