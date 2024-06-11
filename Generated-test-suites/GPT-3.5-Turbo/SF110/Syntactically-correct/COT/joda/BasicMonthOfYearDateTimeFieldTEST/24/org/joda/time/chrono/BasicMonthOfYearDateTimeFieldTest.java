import org.junit.Test;
import org.joda.time.*;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {
    @Test
    public void isLenient() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        assertFalse(field.isLenient());
    }
    
    @Test
    public void get() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        long n = new DateTime(2022, 6, 15, 0, 0).getMillis();
        assertEquals(6, field.get(n));
    }
    
    @Test
    public void add() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        long n = new DateTime(2022, 8, 31, 0, 0).getMillis();
        assertEquals(new DateTime(2022, 10, 30, 0, 0).getMillis(), field.add(n, 2));
    }
    
    @Test
    public void addWrapField() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        long n = new DateTime(2022, 12, 31, 0, 0).getMillis();
        assertEquals(new DateTime(2023, 1, 31, 0, 0).getMillis(), field.addWrapField(n, 1));
    }
    
    @Test
    public void getDifferenceAsLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        long n1 = new DateTime(2022, 6, 15, 0, 0).getMillis();
        long n2 = new DateTime(2023, 1, 15, 0, 0).getMillis();
        assertEquals(7, field.getDifferenceAsLong(n1, n2));
    }
    
    @Test
    public void set() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        long n = new DateTime(2022, 6, 15, 0, 0).getMillis();
        assertEquals(new DateTime(2022, 3, 15, 0, 0).getMillis(), field.set(n, 3));
    }
    
    @Test
    public void isLeap() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 6);
        long n1 = new DateTime(2022, 6, 15, 0, 0).getMillis();
        long n2 = new DateTime(2022, 7, 15, 0, 0).getMillis();
        assertTrue(field.isLeap(n1));
        assertFalse(field.isLeap(n2));
    }
    
    @Test
    public void getLeapAmount() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 6);
        long n1 = new DateTime(2022, 6, 15, 0, 0).getMillis();
        long n2 = new DateTime(2022, 7, 15, 0, 0).getMillis();
        assertEquals(1, field.getLeapAmount(n1));
        assertEquals(0, field.getLeapAmount(n2));
    }
    
    @Test
    public void getMinimumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        assertEquals(1, field.getMinimumValue());
    }
    
    @Test
    public void getMaximumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        assertEquals(12, field.getMaximumValue());
    }
    
    @Test
    public void roundFloor() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        long n = new DateTime(2022, 6, 15, 0, 0).getMillis();
        assertEquals(new DateTime(2022, 6, 1, 0, 0).getMillis(), field.roundFloor(n));
    }
    
    @Test
    public void remainder() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        long n = new DateTime(2022, 6, 15, 0, 0).getMillis();
        assertEquals(new DateTime(2022, 6, 15, 0, 0).getMillis(), field.remainder(n));
    }
}