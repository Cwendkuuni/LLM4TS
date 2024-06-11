import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.sevenz.Coders;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CodersTest {

    // Test addDecoder method
    @Test
    public void testAddDecoder() throws IOException {
        Coders.Coder coder = new Coders.Coder();
        coder.decompressionMethodId = new byte[]{1, 2, 3, 4};
        ByteArrayInputStream is = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        InputStream result = Coders.addDecoder(is, coder, null);
        byte[] output = new byte[10];
        result.read(output);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, output);
        result.close();
    }

    // Test addEncoder method
    @Test
    public void testAddEncoder() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Coders.SevenZMethod method = Coders.SevenZMethod.DEFLATE;
        OutputStream result = Coders.addEncoder(out, method, null);
        result.write(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        result.close();
        byte[] output = out.toByteArray();
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, output);
    }

    // Test decode method for Coder.LZMADecoder
    @Test
    public void testLZMADecoder() throws IOException {
        Coders.Coder coder = new Coders.Coder();
        coder.properties = new byte[]{5, 0, 0, 0, 0};
        ByteArrayInputStream is = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        InputStream result = new Coders.LZMADecoder().decode(is, coder, null);
        byte[] output = new byte[10];
        result.read(output);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, output);
        result.close();
    }

    // Test decode method for Coder.DeflateDecoder
    @Test
    public void testDeflateDecoder() throws IOException {
        Coders.Coder coder = new Coders.Coder();
        ByteArrayInputStream is = new ByteArrayInputStream(new byte[]{120, -100, 99, 96, 0, 0, 4, 80});
        InputStream result = new Coders.DeflateDecoder().decode(is, coder, null);
        byte[] output = new byte[10];
        result.read(output);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, output);
        result.close();
    }

    // Test decode method for Coder.BZIP2Decoder
    @Test
    public void testBZIP2Decoder() throws IOException {
        Coders.Coder coder = new Coders.Coder();
        ByteArrayInputStream is = new ByteArrayInputStream(new byte[]{2, 1, 2, 3, 4});
        InputStream result = new Coders.BZIP2Decoder().decode(is, coder, null);
        byte[] output = new byte[5];
        result.read(output);
        assertArrayEquals(new byte[]{2, 1, 2, 3, 4}, output);
        result.close();
    }

    // Test decode method for Coder.AES256SHA256Decoder
    @Test
    public void testAES256SHA256Decoder() throws IOException {
        Coders.Coder coder = new Coders.Coder();
        coder.properties = new byte[]{1, 0, -1};
        ByteArrayInputStream is = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        InputStream result = new Coders.AES256SHA256Decoder().decode(is, coder, new byte[]{1, 2, 3, 4});
        byte[] output = new byte[5];
        result.read(output);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, output);
        result.close();
    }

    // Test DummyByteAddingInputStream
    @Test
    public void testDummyByteAddingInputStream() throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(new byte[]{1, 2, 3});
        Coders.DummyByteAddingInputStream result = new Coders.DummyByteAddingInputStream(is);
        int read = result.read();
        assertEquals(1, read);
        read = result.read(new byte[3], 0, 3);
        assertEquals(2, read);
        result.close();
    }
}