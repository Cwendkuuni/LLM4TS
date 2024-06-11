import static org.junit.Assert.*;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        String caverphoneCode = caverphone.caverphone("example");
        assertEquals("AXMPL11111", caverphoneCode);
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        String caverphoneCode = caverphone.encode("example");
        assertEquals("AXMPL11111", caverphoneCode);
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        boolean isCaverphoneEqual = caverphone.isCaverphoneEqual("example", "exampel");
        assertTrue(isCaverphoneEqual);
    }
}