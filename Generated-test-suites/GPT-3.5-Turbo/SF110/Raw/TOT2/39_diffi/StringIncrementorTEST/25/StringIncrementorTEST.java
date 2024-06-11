import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        String a = "abcde";
        String b = "acdbe";
        RelativeString result = StringIncrementor.diff(a, b);
        
        assertNotNull(result);
        // Add more assertions as needed
    }
}