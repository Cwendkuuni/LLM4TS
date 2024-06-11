import org.junit.Test;
import static org.junit.Assert.*; 
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.Caverphone; 

public class CaverphoneTest {

    @Test
    public void testCaverphoneEmptyString() {
        Caverphone caverphone = new Caverphone();
        String expected = "1111111111";
        String result = caverphone.caverphone("");
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneLowerCase() {
        Caverphone caverphone = new Caverphone();
        String input = "hello";
        String expected = "h2l2o";
        String result = caverphone.caverphone(input);
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneNullInput() {
        Caverphone caverphone = new Caverphone();
        String input = null;
        caverphone.caverphone(input);
    }

    @Test
    public void testCaverphoneHandlesSpecialCases() {
        Caverphone caverphone = new Caverphone();
        String input = "CoughCough";
        String expected = "cou2fcou2f";
        String result = caverphone.caverphone(input);
        assertEquals(expected, result);
        
        input = "tough";
        expected = "tou2f";
        result = caverphone.caverphone(input);
        assertEquals(expected, result);
    }
}