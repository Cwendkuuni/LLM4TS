import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.apache.commons.codec.EncoderException;

public class CaverphoneTest {

    Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphone() {
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("aha", caverphone.caverphone("Aha"));
        assertEquals("ctm", caverphone.caverphone("catch'em"));
        assertEquals("zn", caverphone.caverphone("zoomin"));
        assertEquals("onntaa", caverphone.caverphone("ontology"));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeWithNonStringObject() throws EncoderException {
        caverphone.encode(new Object());
    }

    @Test
    public void testEncode() {
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("aha", caverphone.encode("Aha"));
        assertEquals("ctm", caverphone.encode("catch'em"));
        assertEquals("zn", caverphone.encode("zoomin"));
        assertEquals("onntaa", caverphone.encode("ontology"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual("Aha", "aha"));
        assertFalse(caverphone.isCaverphoneEqual("aha", "ctm"));
        assertFalse(caverphone.isCaverphoneEqual("zoomin", "onntaa"));
        assertTrue(caverphone.isCaverphoneEqual("ontology", "onntaa"));
    }

}