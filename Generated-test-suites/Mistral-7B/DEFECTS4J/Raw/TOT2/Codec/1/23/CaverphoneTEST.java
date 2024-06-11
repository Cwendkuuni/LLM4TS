import org.junit.Test;
import org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone_EmptyString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("");
        String expected = "1111111111";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphone_NullString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone(null);
        String expected = "1111111111";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphone_SingleLetter() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("a");
        String expected = "A";
        assertEquals(expected, result);
    }

    // Add more test cases for different inputs and edge cases
}