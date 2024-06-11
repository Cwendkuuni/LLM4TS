
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.Coders.CoderId;
import org.apache.commons.compress.archivers.sevenz.Coders.CoderBase;
import org.apache.commons.compress.archivers.sevenz.Coders.CopyDecoder;
import org.apache.commons.compress.archivers.sevenz.Coders.LZMADecoder;
import org.apache.commons.compress.archivers.sevenz.Coders.DeflateDecoder;
import org.apache.commons.compress.archivers.sevenz.Coders.BZIP2Decoder;
import org.apache.commons.compress.archivers.sevenz.Coders.AES256SHA256Decoder;

public class CodersTest {

    @Test
    public void testAddDecoder_copyMethod() throws Exception {
        byte[] data = "test data".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Coders.addDecoder(in, new Coders.Coder(), null);

        assertArrayEquals(data, out.toByteArray());
    }

    @Test
    public void testAddDecoder_lzmaMethod() throws Exception {
        byte[] data = new byte[1024];
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Coders.addDecoder(in, new Coders.Coder(), null);

        // TODO: Add assertion here
    }

    @Test
    public void testAddDecoder_deflateMethod() throws Exception {
        byte[] data = new byte[1024];
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Coders.addDecoder(in, new Coders.Coder(), null);

        // TODO: Add assertion here
    }

    @Test
    public void testAddDecoder_bzip2Method() throws Exception {
        byte[] data = new byte[1024];
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Coders.addDecoder(in, new Coders.Coder(), null);

        // TODO: Add assertion here
    }

    @Test
    public void testAddDecoder_aes256sha256Method() throws Exception {
        byte[] data = new byte[1024];
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Coders.addDecoder(in, new Coders.Coder(), null);

        // TODO: Add assertion here
    }

    @Test
    public void testAddEncoder_copyMethod() throws Exception {
        byte[] data = "test data".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Coders.addEncoder(out, SevenZMethod.COPY, null);

        assertArrayEquals(data, out.toByteArray());
    }

    @Test
    public void testAddEncoder_lzmaMethod() throws Exception {
        byte[] data = new byte[1024];
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Coders.addEncoder(out, SevenZMethod.LZMA, null);

        // TODO: Add assertion here
    }

    @Test
    public void testAddEncoder_deflateMethod() throws Exception {
        byte[] data = new byte[1024];
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Coders.addEncoder(out, SevenZMethod.DEFLATE, null);

        // TODO: Add assertion here
    }

    @Test
    public void testAddEncoder_bzip2Method() throws Exception {
        byte[] data = new byte[1024];
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Coders.addEncoder(out, SevenZMethod.BZIP2, null);

        // TODO: Add assertion here
    }

    @Test
    public void testAddEncoder_aes256sha256Method() throws Exception {
        byte[] data = new byte[1024];
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Coders.addEncoder(out, SevenZMethod.AES256SHA256, null);

        // TODO: Add assertion here
    }

    @Test(expected = IOException.class)
    public void testAddDecoder_unsupportedMethod() throws Exception {
        byte[] data = new byte[1024];
        ByteArrayInputStream in = new ByteArrayInputStream(data);

        Coders.addDecoder(in, new Coders.Coder(), null);
    }

    @Test(expected = IOException.class)
    public void testAddEncoder_unsupportedMethod() throws Exception {
        byte[] data = new byte[1024];
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Coders.addEncoder(out, SevenZMethod.UNKNOWN, null);
    }
}

