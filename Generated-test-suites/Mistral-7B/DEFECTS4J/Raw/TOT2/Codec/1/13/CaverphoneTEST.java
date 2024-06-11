import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphoneInstance = new Caverphone();
        String txt = "example";
        String expected = "2p2p2p2p2p2p2pF2F2F2F2F";
        String result = caverphoneInstance.caverphone(txt);
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testNullInput() {
        Caverphone caverphoneInstance = new Caverphone();
        String txt = null;
        caverphoneInstance.caverphone(txt);
    }

    @Test(expected = EncoderException.class)
    public void testEmptyInput() {
        Caverphone caverphoneInstance = new Caverphone();
        String txt = "";
        caverphoneInstance.caverphone(txt);
    }

    @Test
    public void testSpecialCharacters() {
        Caverphone caverphoneInstance = new Caverphone();
        String txt = "Example@123$";
        String expected = "A22A22A22A22F2F2F2F2F";
        String result = caverphoneInstance.caverphone(txt);
        assertEquals(expected, result);
    }
}