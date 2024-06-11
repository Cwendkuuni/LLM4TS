import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.joda.time.DateTimeField;
import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.field.LenientDateTimeField;

public class LenientDateTimeFieldTest {

    private static final long TEST_INSTANT = 1000L;
    
    private LenientDateTimeField lenientField;
    private DateTimeField wrappedField;
    private Chronology baseChronology;
    
    @Before
    public void setUp() {
        wrappedField = new MockDateTimeField();
        baseChronology = new MockChronology();
        lenientField = new LenientDateTimeField(wrappedField, baseChronology);
    }
    
    @Test
    public void testIsLenient() {
        assertTrue(lenientField.isLenient());
    }
    
    @Test
    public void testSet() {
        long expectedInstant = 2000L;
        int difference = 5;
        
        DateTimeUtils.setCurrentMillisFixed(TEST_INSTANT);
        long result = lenientField.set(TEST_INSTANT, difference);
        
        assertEquals(expectedInstant, result);
    }
    
    @Test
    public void testGetWrappedField() {
        assertSame(wrappedField, lenientField.getWrappedField());
    }
    
    // Mock classes
    
    private static class MockDateTimeField extends DateTimeField {
        
        protected MockDateTimeField() {
            super(null);
        }
        
        @Override
        public int get(long instant) {
            return (int) (instant / 1000L); // divide by 1000 to get seconds
        }
        
        @Override
        public long set(long instant, int value) {
            return instant;
        }
        
        @Override
        public int getMinimumValue() {
            return 0;
        }
        
        @Override
        public int getMaximumValue() {
            return 100;
        }
    }
    
    private static class MockChronology extends Chronology {
        
        @Override
        public DateTimeField[] getFields() {
            return null;
        }
        
        @Override
        public DateTimeField[] getFieldsByReversePriority() {
            return null;
        }
        
        @Override
        public DateTimeField getField(int index) {
            return null;
        }
        
        @Override
        public String toString() {
            return "MockChronology";
        }
    }
}