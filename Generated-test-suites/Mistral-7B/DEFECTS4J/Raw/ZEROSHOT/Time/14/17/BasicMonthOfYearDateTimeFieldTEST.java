import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.MutableDateTime;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        Chronology chrono = new BasicChronology("MyChrono", ISOChronology.INSTANCE);
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, 2);

        // test getter methods
        assertEquals(DateTimeFieldType.monthOfYear(), field.getType());
        assertEquals(chrono, field.getChronology());
        assertEquals(2, field.getMaximumValue());

        // test isLenient()
        assertFalse(field.isLenient());

        // test get() method
        long testTime = BaseDateTime.parse("2000-02-28T12:00:00.000Z").getMillis();
        int month = field.get(testTime);
        assertEquals(2, month);

        // test add() method
        long newTime = field.add(testTime, 3);
        assertEquals(5, field.get(newTime));

        // test addWrapField() method
        newTime = field.addWrapField(testTime, 13);
        assertEquals(3, field.get(newTime));

        // test getDifferenceAsLong() method
        long diff = field.getDifferenceAsLong(testTime, testTime + 3153600000L);
        assertEquals(12, (int) diff);

        // test set() method
        newTime = field.set(testTime, 10);
        assertEquals(10, field.get(newTime));

        // test getRangeDurationField() method
        DurationField rangeDurationField = field.getRangeDurationField();
        assertEquals(DateTimeFieldType.years(), rangeDurationField.getType());

        // test isLeap() method
        MutableDateTime date = new MutableDateTime(2000, 2, 29, 0, 0, 0, 0);
        assertTrue(field.isLeap(date.getMillis()));

        // test getLeapAmount() method
        assertEquals(1, field.getLeapAmount(date.getMillis()));

        // test getLeapDurationField() method
        DurationField leapDurationField = field.getLeapDurationField();
        assertEquals(DateTimeFieldType.days(), leapDurationField.getType());

        // test getMinimumValue() method
        assertEquals(1, field.getMinimumValue());

        // test getMaximumValue() method
        assertEquals(2, field.getMaximumValue());

        // test roundFloor() method
        long roundFloor = field.roundFloor(testTime);
        assertEquals(BaseDateTime.parse("2000-02-01T00:00:00.000Z").getMillis(), roundFloor);

        // test remainder() method
        long remainder = field.remainder(testTime);
        assertEquals(28, (int) remainder);

        // test serialization
        field = (BasicMonthOfYearDateTimeField) field.readResolve();
        assertEquals(2, field.getMaximumValue());
    }
}