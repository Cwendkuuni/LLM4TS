import org.junit.Test;
import static org.junit.Assert.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import org.apache.commons.compress.archivers.sevenz.Coders;

public class CodersTest {

    @Test
    public void testAddDecoder_Copy() throws IOException {
        InputStream input = new ByteArrayInputStream("Example text".getBytes());
        Coders.Coder coder = new Coders.Coder();
        coder.decompressionMethodId = SevenZMethod.COPY.getId();
        InputStream result = Coders.addDecoder(input, coder, null);
        assertEquals("Example text", new String(readAllBytes(result)));
    }

    @Test
    public void testAddDecoder_LZMA() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x00, 0x94, 0x00, 0x00, 0x00, 0x01, 0x81, 0x00});
        Coders.Coder coder = new Coders.Coder();
        coder.decompressionMethodId = SevenZMethod.LZMA.getId();
        InputStream result = Coders.addDecoder(input, coder, null);
        assertEquals(1, result.available());
    }

    @Test(expected = IOException.class)
    public void testAddDecoder_LZMA_DictionaryExceeded() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x00, 0x94, 0x00, 0x00, 0x00, 0x01, 0x81, 0x00});
        Coders.Coder coder = new Coders.Coder();
        coder.decompressionMethodId = SevenZMethod.LZMA.getId();
        coder.properties = new byte[]{0x00, 0x00, 0x00, 0x00, 0x02, 0x00};
        Coders.addDecoder(input, coder, null);
    }

    @Test
    public void testAddDecoder_Deflate() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x00, 0x94, 0x00, 0x00});
        Coders.Coder coder = new Coders.Coder();
        coder.decompressionMethodId = SevenZMethod.DEFLATE.getId();
        InputStream result = Coders.addDecoder(input, coder, null);
        assertEquals(0, result.available());
    }

    @Test
    public void testAddDecoder_BZip2() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x00, 0x94});
        Coders.Coder coder = new Coders.Coder();
        coder.decompressionMethodId = SevenZMethod.BZIP2.getId();
        InputStream result = Coders.addDecoder(input, coder, null);
        assertEquals(0, result.available());
    }

    @Test
    public void testAddDecoder_AES256SHA256() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x00, 0x06, 0x80, 0x00});
        Coders.Coder coder = new Coders.Coder();
        coder.decompressionMethodId = SevenZMethod.AES256SHA256.getId();
        InputStream result = Coders.addDecoder(input, coder, null);
        assertEquals(0, result.available());
    }

    @Test(expected = IOException.class)
    public void testAddDecoder_UnsupportedCompressionMethod() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x00, 0x94});
        Coders.Coder coder = new Coders.Coder();
        coder.decompressionMethodId = SevenZMethod.LZMA2.getId();
        Coders.addDecoder(input, coder, null);
    }

    @Test
    public void testAddEncoder_Copy() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        OutputStream result = Coders.addEncoder(output, SevenZMethod.COPY, null);
        result.write("Example text".getBytes());
        result.flush();
        String encodedString = new String(((ByteArrayOutputStream) output).toByteArray());
        assertEquals("Example text", encodedString);
    }

    @Test
    public void testAddEncoder_LZMA() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        OutputStream result = Coders.addEncoder(output, SevenZMethod.LZMA, null);
        result.write(1);
        result.flush();
        byte[] encodedBytes = ((ByteArrayOutputStream) output).toByteArray();
        assertEquals(8, encodedBytes.length);
        assertEquals(0, encodedBytes[0]);
        assertEquals(0, encodedBytes[1]);
        assertEquals(0, encodedBytes[2]);
        assertEquals(0, encodedBytes[3]);
        assertEquals(1, encodedBytes[4]);
        assertEquals(-127, encodedBytes[5]);
        assertEquals(1, encodedBytes[6]);
        assertEquals(0, encodedBytes[7]);
    }

    @Test
    public void testAddEncoder_Deflate() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        OutputStream result = Coders.addEncoder(output, SevenZMethod.DEFLATE, null);
        result.flush();
        byte[] encodedBytes = ((ByteArrayOutputStream) output).toByteArray();
        assertEquals(0, encodedBytes.length);
    }

    @Test
    public void testAddEncoder_BZip2() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        OutputStream result = Coders.addEncoder(output, SevenZMethod.BZIP2, null);
        result.flush();
        byte[] encodedBytes = ((ByteArrayOutputStream) output).toByteArray();
        assertEquals(0, encodedBytes.length);
    }

    @Test
    public void testAddEncoder_AES256SHA256() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        OutputStream result = Coders.addEncoder(output, SevenZMethod.AES256SHA256, null);
        result.flush();
        byte[] encodedBytes = ((ByteArrayOutputStream) output).toByteArray();
        assertEquals(0, encodedBytes.length);
    }

    @Test(expected = IOException.class)
    public void testAddEncoder_UnsupportedCompressionMethod() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        Coders.addEncoder(output, SevenZMethod.LZMA2, null);
    }

    private byte[] readAllBytes(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        return output.toByteArray();
    }
}