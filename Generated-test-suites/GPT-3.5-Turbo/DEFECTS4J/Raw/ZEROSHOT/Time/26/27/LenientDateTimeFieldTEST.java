import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.chrono.ISOChronology;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {
    
    private LenientDateTimeField lenientField;
    private DateTimeField wrappedField;
    private Chronology baseChronology;
    
    @Before
    public void setUp() {
        wrappedField = new DateTimeField() {
            @Override
            public int get(long instant) {
                return 0;
            }
            
            @Override
            public String getName() {
                return "WrappedField";
            }
            
            @Override
            public Chronology getChronology() {
                return baseChronology;
            }
            
            @Override
            public long roundFloor(long instant) {
                return 0;
            }
            
            @Override
            public long roundCeiling(long instant) {
                return 0;
            }
            
            @Override
            public long roundHalfFloor(long instant) {
                return 0;
            }
            
            @Override
            public long roundHalfCeiling(long instant) {
                return 0;
            }
            
            @Override
            public long roundHalfEven(long instant) {
                return 0;
            }
            
            @Override
            public long remainder(long instant) {
                return 0;
            }
        };
        baseChronology = ISOChronology.getInstance();
        lenientField = new LenientDateTimeField(wrappedField, baseChronology);
    }
    
    @After
    public void tearDown() {
        lenientField = null;
        wrappedField = null;
        baseChronology = null;
    }
    
    @Test
    public void testIsLenient() {
        assertTrue(lenientField.isLenient());
    }
    
    @Test
    public void testSet() {
        DateTimeUtils.setCurrentMillisFixed(0);
        long instant = 0;
        int value = 1;
        long expected = 1;
        assertEquals(expected, lenientField.set(instant, value));
    }
}