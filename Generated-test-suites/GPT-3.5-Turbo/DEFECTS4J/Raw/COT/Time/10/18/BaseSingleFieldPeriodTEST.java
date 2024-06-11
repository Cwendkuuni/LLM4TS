
import org.joda.time.DateTimeFieldType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testGetFieldType() {
        FieldUtils.getType().equals("FieldType");
    }
    
    @Test
    public void testGetPeriodType() {
        FieldUtils.getType().equals("PeriodType");
    }
    
    @Test
    public void testSize() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(30);
        assertEquals(1, baseSingleFieldPeriod.size());
    }
    
    @Test
    public void testGetFieldTypeAtIndex() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(30);
        assertEquals(DurationFieldType.getType("FieldType"), baseSingleFieldPeriod.getFieldType(0));
    }
    
    @Test
    public void testGetValueAtIndex() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(30);
        assertEquals(30, baseSingleFieldPeriod.getValue(0));
    }
    
    @Test
    public void testGetValueWithCorrectDurationFieldType() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(30);
        assertEquals(30, baseSingleFieldPeriod.get(DurationFieldType.getType("FieldType")));
    }
    
    @Test
    public void testGetValueWithIncorrectDurationFieldType() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(30);
        assertEquals(0, baseSingleFieldPeriod.get(DurationFieldType.getType("IncorrectType")));
    }
    
}
