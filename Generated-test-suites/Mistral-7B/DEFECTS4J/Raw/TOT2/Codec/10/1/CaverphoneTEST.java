import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();

        // Test empty string
        assertEquals("1111111111", caverphone.caverphone(""));

        // Test single character
        assertEquals("A", caverphone.caverphone("a"));

        // Test multiple characters
        assertEquals("A2A2F2S2S2P2K", caverphone.caverphone("Apple"));
    }
}