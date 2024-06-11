import org.joda.time.BasicDateTime;
import org.joda.time.JodaTimeTestCase;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.MonthOfYearField;

public class BasicMonthOfYearDateTimeFieldTest extends JodaTimeTestCase {

    public void testBasicMonthOfYearDateTimeField() {
        BasicChronology isoChronology = new ISOChronology();
        MonthOfYearField field = new MonthOfYearDateTimeField(isoChronology, 2);

        long instant = new BasicDateTime(2012, 2, 29, isoChronology).getMillis();

        // Test get method
        int month = field.get(instant);
        assertEquals(2, month);

        // Test add method
        long newInstant = field.add(instant, 1);
        int newMonth = field.get(newInstant);
        assertEquals(3, newMonth);

        // Test addWrapField method
        long wrappedInstant = field.addWrapField(instant, 13);
        int wrappedMonth = field.get(wrappedInstant);
        assertEquals(4, wrappedMonth);

        // Test set method
        long setInstant = field.set(instant, 5);
        int setMonth = field.get(setInstant);
        assertEquals(5, setMonth);

        // Test getDifference method
        long diffInstant = field.set(instant, 7).add(1);
        long diff = field.getDifferenceAsLong(instant, diffInstant);
        assertEquals(1, diff);

        // Test isLeap method
        boolean isLeap = field.isLeap(instant);
        assertFalse(isLeap);

        // Test getLeapAmount method
        long leapInstant = new BasicDateTime(2012, 2, 29, isoChronology).getMillis();
        int leapAmount = field.getLeapAmount(leapInstant);
        assertEquals(0, leapAmount);

        isoChronology = new BasicChronology("TestChronology", DateTimeZone.forID("UTC"), null);
        field = new MonthOfYearDateTimeField(isoChronology, 3);

        // Test minimum and maximum values
        long minInstant = field.roundFloor(Long.MIN_VALUE);
        int minMonth = field.get(minInstant);
        assertEquals(1, minMonth);

        long maxInstant = field.roundFloor(Long.MAX_VALUE);
        int maxMonth = field.get(maxInstant);
        assertEquals(12, maxMonth);

        // Test serialization
        MonthOfYearDateTimeField serializedField = (MonthOfYearDateTimeField) field.readResolve();
        assertSame(field, serializedField);
    }
}