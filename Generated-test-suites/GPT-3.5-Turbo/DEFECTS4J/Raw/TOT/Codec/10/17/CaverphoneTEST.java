import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    private Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphone_WithNull_Returns1111111111() {
        String result = caverphone.caverphone(null);
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphone_WithEmptyString_Returns1111111111() {
        String result = caverphone.caverphone("");
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphone_WithLowercaseLetters_ReturnsCorrectCode() {
        String result = caverphone.caverphone("example");
        assertEquals("XAMPL1111", result);
    }

    @Test
    public void testCaverphone_WithUppercaseLetters_ReturnsCorrectCode() {
        String result = caverphone.caverphone("EXAMPLE");
        assertEquals("XAMPL1111", result);
    }

    @Test
    public void testCaverphone_WithNumbers_ReturnsCorrectCode() {
        String result = caverphone.caverphone("ex4mpl3");
        assertEquals("XAMPL1111", result);
    }

    @Test
    public void testCaverphone_WithSpecialCharacters_ReturnsCorrectCode() {
        String result = caverphone.caverphone("!@#$%^&*()_-+=|\\]}[{':;\"/><.,");
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphone_WithStandardInput_ReturnsCorrectCode() {
        String result = caverphone.caverphone("Caverphone");
        assertEquals("2431111111", result);
    }

    @Test
    public void testIsCaverphoneEqual_WithEqualStrings_ReturnsTrue() {
        boolean result = caverphone.isCaverphoneEqual("example", "example");
        assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqual_WithDifferentStrings_ReturnsFalse() {
        boolean result = caverphone.isCaverphoneEqual("example1", "example2");
        assertFalse(result);
    }
}