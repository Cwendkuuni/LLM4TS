import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FastDateFormatTest {

    FastDateFormat fastDateFormat;

    @Before
    public void setUp() {
        fastDateFormat = FastDateFormat.getInstance();
    }

    @Test
    public void testFormat_Object_StringBuffer_FieldPosition() {
        // Test case 1
        Object obj1 = new Object();
        StringBuffer buffer1 = new StringBuffer();
        FieldPosition position1 = new FieldPosition(0);
        StringBuffer result1 = fastDateFormat.format(obj1, buffer1, position1);
        assertNotNull(result1);
        assertEquals("Result of Object formatting is incorrect", result1.toString(), buffer1.toString());

        // Test case 2
        // Repeat the test case with different parameters

        // ...

    }

    @Test
    public void testFormat_long() {
        // Test case 1
        long millis1 = 1234567890L;
        String result1 = fastDateFormat.format(millis1);
        assertNotNull(result1);
        // Add assertion for the expected result

        // Test case 2
        // Repeat the test case with different parameters

        // ...
    }

    // Add test methods for other public methods in FastDateFormat class

}