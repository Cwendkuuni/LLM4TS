
import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    @Test
    public void testDiffEqualStrings() {
        RelativeString result = StringIncrementor.diff("abc", "abc");
        assertEquals(0, result.linksCount());
    }

    @Test
    public void testPatchEmptyRelativeString() {
        String patchedString = StringIncrementor.patch("abc", new RelativeString());
        assertEquals("abc", patchedString);
    }
}
