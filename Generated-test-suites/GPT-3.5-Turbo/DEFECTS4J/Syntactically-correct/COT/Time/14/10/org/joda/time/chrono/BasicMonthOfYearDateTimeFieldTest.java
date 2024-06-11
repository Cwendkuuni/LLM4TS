import org.joda.time.chrono.BasicChronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.ReadablePartial;
import org.joda.time.field.FieldUtils;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicChronology iChronology = new BasicChronology();
    private BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 1);

    @Test
    public void testIsLenient() {
        assert !field.isLenient();
    }

    @Test
    public void testGet() {
        long instant = 1234567890L;
        assert field.get(instant) == 2;
    }

    @Test
    public void testAdd_long_int() {
        long instant = 1234567890L;
        int months = 2;
        assert field.add(instant, months) == 1234567890L;
    }

    @Test
    public void testAdd_long_long() {
        long instant = 1234567890L;
        long months = 2L;
        assert field.add(instant, months) == 1234567890L;
    }

    @Test
    public void testAddWrapField() {
        long instant = 1234567890L;
        int months = 2;
        assert field.addWrapField(instant, months) == 1234567890L;
    }

    @Test
    public void testGetDifferenceAsLong() {
        long minuendInstant = 1234567890L;
        long subtrahendInstant = 9876543210L;
        long difference = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assert difference == -8;
    }

    @Test
    public void testSet() {
        long instant = 1234567890L;
        int month = 2;
        long newInstant = field.set(instant, month);
        assert newInstant == 1234567890L;
    }

    @Test
    public void testIsLeap() {
        long instant = 1234567890L;
        assert field.isLeap(instant);
    }

    @Test
    public void testGetLeapAmount() {
        long instant = 1234567890L;
        assert field.getLeapAmount(instant) == 1;
    }

    @Test
    public void testGetMinimumValue() {
        assert field.getMinimumValue() == 1;
    }

    @Test
    public void testGetMaximumValue() {
        assert field.getMaximumValue() == 12;
    }

    @Test
    public void testRoundFloor() {
        long instant = 1234567890L;
        assert field.roundFloor(instant) == 1234567890L;
    }

    @Test
    public void testRemainder() {
        long instant = 1234567890L;
        assert field.remainder(instant) == 0L;
    }
}