import org.joda.time.JodaTimeTestCase;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.JodaFields;
import org.joda.time.field.MonthOfYearDateTimeField;
import org.joda.time.local.DateTime;

public class BasicMonthOfYearDateTimeFieldTest extends JodaTimeTestCase {

    public void testBasic() {
        BasicChronology isoChrono = new ISOChronology();
        MonthOfYearDateTimeField field = new MonthOfYearDateTimeField(isoChrono, 2);

        long instant = DateTime.now().getMillis();
        int currentMonth = field.get(instant);
        assertEquals(12, currentMonth);

        long newInstant = field.add(instant, 1);
        int newMonth = field.get(newInstant);
        assertEquals(0, newMonth); // rolls over to January

        // test negative values
        long oldInstant = field.add(instant, -1);
        int oldMonth = field.get(oldInstant);
        assertEquals(1, oldMonth);

        long roundFloor = field.roundFloor(instant);
        long remainder = field.remainder(instant);
        assertEquals(instant, roundFloor + remainder);

        // test getMaximumValue() and getMinimumValue()
        int maxValue = field.getMaximumValue();
        int minValue = field.getMinimumValue();
        assertEquals(12, maxValue);
        assertEquals(0, minValue);

        // test isLeap() and getLeapAmount()
        long leapInstant = new DateTime(2000, 2, 29, 0, 0, 0, 0).getMillis();
        assertTrue(field.isLeap(leapInstant));
        assertEquals(1, field.getLeapAmount(leapInstant));

        long nonLeapInstant = new DateTime(1999, 2, 28, 0, 0, 0, 0).getMillis();
        assertFalse(field.isLeap(nonLeapInstant));
        assertEquals(0, field.getLeapAmount(nonLeapInstant));

        // test getRangeDurationField() and add(long, long)
        DurationField range = field.getRangeDurationField();
        long oneYear = range.getMillis(1);
        long newInstant2 = field.add(instant, 2);
        long newInstant3 = field.add(newInstant2, 3);
        long newInstant4 = field.add(newInstant3, 4);
        assertEquals(instant + 9 * oneYear, newInstant4);

        // test add(ReadablePartial, int[], int)
        DateTime partial = new DateTime(2022, 6, 30, 0, 0, 0, 0);
        int[] values = {2, 28, 29};
        int monthToAdd = 3;
        int[] newValues = field.add(partial, JodaFields.MONTH_OF_YEAR, values, monthToAdd);
        DateTime newPartial = partial.withMonthOfYear(newValues[JodaFields.MONTH_OF_YEAR]);
        assertEquals(newPartial.getMonthOfYear(), newValues[JodaFields.MONTH_OF_YEAR]);
        assertEquals(newPartial.getDayOfMonth(), newValues[JodaFields.DAY_OF_MONTH]);
        assertEquals(newPartial.getYear(), newValues[JodaFields.YEAR]);

        // test addWrapField(long, int)
        long wrapInstant = DateTime.now().getMillis();
        int wrapMonths = 13;
        long wrappedInstant = field.addWrapField(instant, wrapMonths);
        int wrappedMonth = field.get(wrappedInstant);
        assertEquals(1, wrappedMonth); // rolls over to January

        // test getDifferenceAsLong(long, long)
        long diffInstant = new DateTime(2000, 2, 28, 0, 0, 0, 0).getMillis();
        long diffMonths = 12;
        long diffMillis = field.getDifferenceAsLong(instant, diffInstant);
        long expectedDiffMillis = (instant - diffInstant) % (FieldUtils.getMillisPerDay(isoChrono) * iMax);
        assertEquals(expectedDiffMillis, diffMillis);
        assertEquals(diffMonths, FieldUtils.getWrappedValue(currentMonth, diffMonths, 0, 12));
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}