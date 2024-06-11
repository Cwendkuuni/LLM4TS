import static org.junit.Assert.assertEquals;
import org.joda.time.DateTimeField;
import org.joda.time.Chronology;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {
    
    private LenientDateTimeField field;
    private DateTimeField wrappedField;
    private Chronology base;

    @Before
    public void setUp() {
        wrappedField = new StrictDateTimeField();
        base = new Chronology();

        field = new LenientDateTimeField(wrappedField, base);
    }

    @Test
    public void testIsLenient() {
        assertEquals(true, field.isLenient());
    }

    @Test
    public void testSet() {
        long instant = 1234567890;
        int value = 100;
        long expected = instant + value;

        long result = field.set(instant, value);

        assertEquals(expected, result);
    }
}