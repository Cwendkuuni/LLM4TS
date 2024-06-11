import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();

        // Basic test case
        String input = "hello";
        String expectedOutput = "h3l2o2";
        String output = caverphone.caverphone(input);
        assertEquals(expectedOutput, output);

        // Edge cases
        String edgeCase1 = "EdgeCase1";
        String expectedEdgeCase1 = "E2d3g3eA1";
        String outputEdgeCase1 = caverphone.caverphone(edgeCase1);
        assertEquals(expectedEdgeCase1, outputEdgeCase1);

        String edgeCase2 = "EdgeCase2";
        String expectedEdgeCase2 = "E3d3g3gA1";
        String outputEdgeCase2 = caverphone.caverphone(edgeCase2);
        assertEquals(expectedEdgeCase2, outputEdgeCase2);

        // Exception handling
        try {
            caverphone.caverphone(null);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected
        }

        try {
            caverphone.caverphone("12345");
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected
        }
    }
}