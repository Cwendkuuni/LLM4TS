import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        String input = "HelloWorld";
        String expected = "H2e2l2e2l2o2w2o2r2l2d";
        String result = caverphone.caverphone(input);
        assertEquals(expected, result);
    }
}