import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDateTime;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.junit.Before;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField monthField;
    private static final int LEAP_MONTH = DateTimeConstants.FEBRUARY;

    @Before
    public void setUp() {
        BasicChronology chronology = BasicChronology.getInstance();
        monthField = new BasicMonthOfYearDateTimeField(chronology, LEAP_MONTH);
    }

    @Test
    public void testType() {
        assertEquals(DateTimeConstants.monthOfYear(), monthField.getType());
    }

    @Test
    public void testGet() {
        long instant = new DateTime(2020, 2, 15, 12, 0).getMillis();
        assertEquals(2, monthField.get(instant));
    }

    @Test
    public void testAdd() {
        long instant = new DateTime(2020, 2, 15, 12, 0).getMillis();
        long result = monthField.add(instant, 2);
        assertEquals(new DateTime(2020, 4, 15, 12, 0).getMillis(), result);
    }

    @Test
    public void testAddWrapField() {
        long instant = new DateTime(2020, 12, 15, 12, 0).getMillis();
        long result = monthField.addWrapField(instant, 2);
        assertEquals(new DateTime(2021, 2, 15, 12, 0).getMillis(), result);
    }

    @Test
    public void testGetDifferenceAsLong() {
        long instant1 = new DateTime(2020, 2, 15, 12, 0).getMillis();
        long instant2 = new DateTime(2021, 1, 15, 12, 0).getMillis();
        long difference = monthField.getDifferenceAsLong(instant1, instant2);
        assertEquals(11, difference);
    }

    @Test
    public void testSet() {
        long instant = new DateTime(2020, 2, 15, 12, 0).getMillis();
        long result = monthField.set(instant, 4);
        assertEquals(new DateTime(2020, 4, 15, 12, 0).getMillis(), result);
    }

    @Test
    public void testRoundFloor() {
        long instant = new DateTime(2020, 2, 15, 12, 0).getMillis();
        long result = monthField.roundFloor(instant);
        assertEquals(new LocalDateTime(2020, 2, 1, 0, 0).toDateTime().getMillis(), result);
    }

    @Test
    public void testRemainder() {
        long instant = new DateTime(2020, 2, 15, 12, 0).getMillis();
        long result = monthField.remainder(instant);
        assertEquals(15 * 60 * 60 * 1000, result);
    }

}