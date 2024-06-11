import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.DateTimeField;
import org.joda.time.chrono.GJChronology;

public class LenientDateTimeFieldTest {
    @Test
    public void testGetInstance() {
        DateTimeField baseField = GJChronology.getInstance().year();
        DateTimeField lenientField = LenientDateTimeField.getInstance(baseField, GJChronology.getInstance());
        
        assertNotNull(lenientField);
        assertTrue(lenientField instanceof LenientDateTimeField);
    }
    
    @Test
    public void testGetInstanceWithNullField() {
        DateTimeField lenientField = LenientDateTimeField.getInstance(null, GJChronology.getInstance());
        assertNull(lenientField);
    }
    
    @Test
    public void testGetInstanceWithAlreadyLenientField() {
        DateTimeField baseField = GJChronology.getInstance().year();
        DateTimeField lenientField = LenientDateTimeField.getInstance(baseField.withMinimumValue(), GJChronology.getInstance());
        assertSame(baseField.withMinimumValue(), lenientField);
    }

    @Test
    public void testGetInstanceWithStrictField() {
        DateTimeField baseField = GJChronology.getInstance().year();
        DateTimeField strictField = new StrictDateTimeField(baseField);
        DateTimeField lenientField = LenientDateTimeField.getInstance(strictField, GJChronology.getInstance());
        assertTrue(lenientField instanceof LenientDateTimeField);
        assertSame(baseField, ((LenientDateTimeField)lenientField).getWrappedField());
    }
    
    @Test
    public void testSetWithDifferentInstantValues() {
        DateTimeField baseField = GJChronology.getInstance().year();
        LenientDateTimeField lenientField = new LenientDateTimeField(baseField, GJChronology.getInstance());
        
        long instant1 = 1577836800000L; // 2020-01-01T00:00:00Z
        int value1 = 2022;
        long expected1 = 1672560000000L; // 2023-01-01T00:00:00Z
        assertEquals(expected1, lenientField.set(instant1, value1));

        long instant2 = 1577836800000L; // 2020-01-01T00:00:00Z
        int value2 = 1899;
        long expected2 = 1525132800000L; // 2018-01-01T00:00:00Z
        assertEquals(expected2, lenientField.set(instant2, value2));
    }
    
    @Test
    public void testSetWithValuesWithinAndOutsideBounds() {
        DateTimeField baseField = GJChronology.getInstance().year();
        LenientDateTimeField lenientField = new LenientDateTimeField(baseField, GJChronology.getInstance());
        
        long instant = 1577836800000L; // 2020-01-01T00:00:00Z
        
        int value1 = 1899; // Lower bound of the field
        long expected1 = 1525132800000L; // 2018-01-01T00:00:00Z
        assertEquals(expected1, lenientField.set(instant, value1));
        
        int value2 = 3999; // Upper bound of the field
        long expected2 = 4102444800000L; // 2099-01-01T00:00:00Z
        assertEquals(expected2, lenientField.set(instant, value2));
        
        int value3 = -1899; // Outside the lower bound of the field
        long expected3 = 1672560000000L; // 2023-01-01T00:00:00Z
        assertEquals(expected3, lenientField.set(instant, value3));
        
        int value4 = 4399; // Outside the upper bound of the field
        long expected4 = 4217616000000L; // 2109-01-01T00:00:00Z
        assertEquals(expected4, lenientField.set(instant, value4));
    }
}