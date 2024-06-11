import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "abcd123!@#$";
        String expected = "ABCD";
        String actual = SoundexUtils.clean(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testDifference() {
        StringEncoder encoder = new SomeEncoder(); // Replace SomeEncoder with actual encoder
        String s1 = "abc";
        String s2 = "def";
        int expected = 0; // Modify expected value based on encoder
        int actual = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "123";
        String es2 = "abc";
        int expected = 0;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
    }
}