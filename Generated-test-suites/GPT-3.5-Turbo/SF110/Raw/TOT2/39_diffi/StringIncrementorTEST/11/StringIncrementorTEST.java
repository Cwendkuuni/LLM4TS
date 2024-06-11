import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        RelativeString result = StringIncrementor.diff("abc", "def");
        // Assert the result based on the expected output
        assertNotNull(result);
    }
}