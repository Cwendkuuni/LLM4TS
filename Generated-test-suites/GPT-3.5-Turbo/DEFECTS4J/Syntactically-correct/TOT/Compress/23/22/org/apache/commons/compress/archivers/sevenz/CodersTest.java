import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.Coders.CoderId;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Test;

public class CodersTest {

    // Testing addDecoder method with supported compression method
    @Test
    public void testAddDecoderSupported() throws IOException {
        byte[] data = new byte[]{1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        CoderId coderId = new CoderId(SevenZMethod.COPY, new Coders.CopyDecoder());
        InputStream result = Coders.addDecoder(in, coderId, null);
        assertNotNull(result);
    }

    // Testing addDecoder method with unsupported compression method
    @Test(expected = IOException.class)
    public void testAddDecoderUnsupported() throws IOException {
        byte[] data = new byte[]{1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        CoderId coderId = new CoderId(SevenZMethod.LZMA, new Coders.LZMADecoder());
        Coders.addDecoder(in, coderId, null);
    }

    // Testing addDecoder method with null input stream
    @Test(expected = IOException.class)
    public void testAddDecoderNullInputStream() throws IOException {
        Coders.addDecoder(null, null, null);
    }

    // Testing addEncoder method with supported compression method
    @Test
    public void testAddEncoderSupported() throws IOException {
        byte[] data = new byte[]{1, 2, 3, 4, 5};
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Coders.addEncoder(out, SevenZMethod.COPY, null);
        assertEquals(out.toByteArray().length, data.length);
    }

    // Testing addEncoder method with unsupported compression method
    @Test(expected = IOException.class)
    public void testAddEncoderUnsupported() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Coders.addEncoder(out, SevenZMethod.LZMA, null);
    }

    // Testing addEncoder method with null output stream
    @Test(expected = IOException.class)
    public void testAddEncoderNullOutputStream() throws IOException {
        Coders.addEncoder(null, null, null);
    }
}