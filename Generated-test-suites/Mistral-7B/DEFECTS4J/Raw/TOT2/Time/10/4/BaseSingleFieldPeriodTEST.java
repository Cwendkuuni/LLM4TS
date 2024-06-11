import org.junit.Test;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;

import static org.junit.Assert.assertEquals;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBasicFunctionality() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        assertEquals(5, period.getValue());
        DurationFieldType fieldType = period.getFieldType();
        assertEquals(DurationFieldType.DAYS, fieldType);
        Period standardPeriod = Period.standard(5, DurationFieldType.DAYS);
        assertEquals(standardPeriod, period.toPeriod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeValue() {
        new MySingleFieldPeriod(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFieldType() {
        new MySingleFieldPeriod(1, new InvalidFieldType());
    }

    @Test
    public void testAddition() {
        BaseSingleFieldPeriod p1 = new MySingleFieldPeriod(3);
        BaseSingleFieldPeriod p2 = new MySingleFieldPeriod(2);
        BaseSingleFieldPeriod sum = new MySingleFieldPeriod(p1.add(p2));
        assertEquals(5, sum.getValue());
    }

    @Test
    public void testComparison() {
        BaseSingleFieldPeriod p1 = new MySingleFieldPeriod(3);
        BaseSingleFieldPeriod p2 = new MySingleFieldPeriod(2);
        int comparisonResult = p1.compareTo(p2);
        assertEquals(1, comparisonResult);
    }

    @Test
    public void testImmutable() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        try {
            period.setValue(6);
        } catch (UnsupportedOperationException e) {
            assertEquals("Not supported", e.getMessage());
        }
    }

    @Test
    public void testMutablePeriod() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.add(period);
        assertEquals(5, mutablePeriod.get(DurationFieldType.DAYS));
    }

    // Add edge cases and exception handling scenarios here
}

class MySingleFieldPeriod extends BaseSingleFieldPeriod {

    MySingleFieldPeriod(int value) {
        super(value);
    }

    MySingleFieldPeriod(int value, DurationFieldType fieldType) {
        super(value, fieldType);
    }

    @Override
    public DurationFieldType getFieldType() {
        return DurationFieldType.DAYS;
    }

    @Override
    public PeriodType getPeriodType() {
        return PeriodType.standard();
    }

    // Add edge cases and exception handling scenarios here

}

class InvalidFieldType extends DurationFieldType {

    @Override
    public Field getField(Chronology chrono) {
        throw new UnsupportedOperationException("Invalid field type");
    }

}