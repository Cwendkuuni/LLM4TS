import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.InflaterInputStream;

public class CodersTest {

    @Test
    public void testAddDecoder() throws IOException {
        Coders.Coder coder = new Coders.Coder();
        coder.decompressionMethodId = new byte[] {0x01, 0x02};
        ByteArrayInputStream is = new ByteArrayInputStream(new byte[] {0x01, 0x02});
        assertNotNull(Coders.addDecoder(is, coder, new byte[0]));
    }

    @Test
    public void testAddEncoder() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.COPY;
        assertNotNull(Coders.addEncoder(out, method, new byte[0]));
    }

    @Test
    public void testCoderBase() {
        Coders.CoderBase coderBase = new Coders.CoderBase() {
            @Override
            InputStream decode(InputStream in, Coders.Coder coder, byte[] password) throws IOException {
                return new InflaterInputStream(in);
            }
        };

        try {
            coderBase.encode(new ByteArrayOutputStream(), new byte[0]);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            assertEquals("method doesn't support writing", e.getMessage());
        }
    }

    // Additional test cases for edge cases and exception handling can be added here

}