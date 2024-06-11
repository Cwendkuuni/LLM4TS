import org.junit.Test;
import java.io.Writer;
import java.io.StringWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class XMLEncoderTest {
    private XMLEncoder xmlEncoder;

    @Test
    public void testGetEncoder() throws UnsupportedEncodingException {
        xmlEncoder = XMLEncoder.getEncoder("UTF-8");
        // Test if the xmlEncoder object is created correctly
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderWithNullEncoding() throws UnsupportedEncodingException {
        xmlEncoder = XMLEncoder.getEncoder(null);
        // Expecting IllegalArgumentException to be thrown
    }

    // Add additional test methods for other public methods

    // Helper method to compare the output of the Writer
    private String getWriterOutput(Writer writer) {
        return writer.toString();
    }
}