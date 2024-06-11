import org.joda.time.JodaTimeTestCase;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.MonthOfYearDateTimeField;

public class BasicMonthOfYearDateTimeFieldTest extends JodaTimeTestCase {

    public void testBasicMonthOfYearDateTimeField() {
        BasicChronology isoChronology = new ISOChronology();
        MonthOfYearDateTimeField field = new MonthOfYearDateTimeField(isoChronology, 2);

        // Test get and set methods
        long instant = 0L;
        int month = 1;
        long setResult = field.set(instant, month);
        assertEquals(month, field.get(setResult));

        // Test add methods
        int amount = 3;
        long addResult = field.add(instant, amount);
        assertEquals(month + amount, field.get(addResult));

        // Test addWrapField method
        addResult = field.addWrapField(instant, amount);
        assertEquals(month + amount, field.get(addResult));

        // Test add method with long parameter
        long months = 5L;
        addResult = field.add(instant, months);
        assertEquals(month + (int) months, field.get(addResult));

        // Test add method with int and int[] parameters
        int[] values = {1, 2};
        addResult = field.add(instant, 3, values, 2);
        assertEquals(month + 3, field.get(addResult[0]));

        // Test getDifferenceAsLong method
        long otherInstant = 2L;
        long difference = field.getDifferenceAsLong(instant, otherInstant);
        assertEquals(month - (field.get(otherInstant) + 1), difference);

        // Test isLeap method
        assertTrue(field.isLeap(instant));
        assertFalse(field.isLeap(instant + DateTimeConstants.MILLIS_PER_DAY));

        // Test getLeapAmount method
        assertEquals(1, field.getLeapAmount(instant));
        assertEquals(0, field.getLeapAmount(instant + DateTimeConstants.MILLIS_PER_DAY));

        // Test getMinimumValue and getMaximumValue methods
        assertEquals(1, field.getMinimumValue());
        assertEquals(12, field.getMaximumValue());

        // Test roundFloor and remainder methods
        long roundFloorResult = field.roundFloor(instant);
        long remainderResult = field.remainder(instant);
        assertEquals(0, remainderResult);
        assertEquals(FieldUtils.getWrappedValue(month, 1, 1, 12), field.get(roundFloorResult));

        // Test serialization
        MonthOfYearDateTimeField clonedField = (MonthOfYearDateTimeField) field.clone();
        assertEquals(field, clonedField);
    }

    @Override
    protected void setUp() {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}