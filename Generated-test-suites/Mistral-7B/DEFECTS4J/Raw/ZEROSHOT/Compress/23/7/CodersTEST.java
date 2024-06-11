import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class CodersTest {

    @Test
    public void testAddDecoder() throws IOException, NoSuchAlgorithmException {
        byte[] password = new byte[16];
        new SecureRandom().nextBytes(password);

        InputStream is = new ByteArrayInputStream(new byte[]{0x07, 0x01, 0x02, 0x03});
        Coder coder = new CopyDecoder();
        InputStream result = Coders.addDecoder(is, coder, password);
        Assert.assertSame(is, result);

        is = new ByteArrayInputStream(new byte[]{0x1a, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x2a, 0x45, 0x53, 0x54});
        coder = new LZMADecoder();
        result = Coders.addDecoder(is, coder, password);
        Assert.assertNotNull(result);
        LZMAInputStream lzmaInputStream = new LZMAInputStream(result);
        byte[] expected = new byte[]{0x54, 0x53, 0x2a, 0x45, 0x01};
        byte[] actual = new byte[5];
        int len = lzmaInputStream.read(actual, 0, actual.length);
        Assert.assertEquals(len, 5);
        Assert.assertArrayEquals(expected, actual);

        is = new ByteArrayInputStream(new byte[]{0x08, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16});
        coder = new DeflateDecoder();
        result = Coders.addDecoder(is, coder, password);
        Assert.assertNotNull(result);
        InflaterInputStream inflaterInputStream = new InflaterInputStream(result);
        byte[] buffer = new byte[17];
        len = inflaterInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(len, 17);
        Assert.assertArrayEquals(Arrays.copyOfRange(buffer, 14, 17), new byte[]{0x12, 0x13, 0x14});
    }

    @Test(expected = IOException.class)
    public void testAddDecoderUnsupportedMethod() throws IOException {
        byte[] password = new byte[16];
        new SecureRandom().nextBytes(password);

        InputStream is = new ByteArrayInputStream(new byte[]{0x1a, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17});
        Coder coder = new AES256SHA256Decoder();
        Coders.addDecoder(is, coder, password);
    }

    @Test
    public void testAddEncoder() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        Coder coder = new CopyEncoder();
        OutputStream result = Coders.addEncoder(os, SevenZMethod.COPY, null);
        Assert.assertSame(os, result);

        os = new ByteArrayOutputStream();
        coder = new LZMAEncoder();
        result = Coders.addEncoder(os, SevenZMethod.LZMA, null);
        Assert.assertNotNull(result);
        LZMAOutputStream lzmaOutputStream = new LZMAOutputStream(result);
        lzmaOutputStream.write("abcdefghijklmnopqrstuvwxyz".getBytes(StandardCharsets.UTF_8));
        lzmaOutputStream.finish();
        byte[] expected = new byte[]{0x1a, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01};
        Assert.assertArrayEquals(expected, result.toString().getBytes());

        os = new ByteArrayOutputStream();
        coder = new DeflateEncoder();
        result = Coders.addEncoder(os, SevenZMethod.DEFLATE, null);
        Assert.assertNotNull(result);
        DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(result, new Deflater(9, true));
        byte[] source = "abcdefghijklmnopqrstuvwxyz".getBytes(StandardCharsets.UTF_8);
        deflaterOutputStream.write(source, 0, source.length);
        deflaterOutputStream.finish();
        byte[] actual = result.toString().getBytes();
        byte[] expectedDeflate = new byte[2 + source.length];
        System.arraycopy(new byte[]{0x08, 0x01}, 0, expectedDeflate, 0, 2);
        System.arraycopy(source, 0, expectedDeflate, 2, source.length);
        Assert.assertArrayEquals(expectedDeflate, actual);
    }

    @Test(expected = IOException.class)
    public void testAddEncoderUnsupportedMethod() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Coder coder = new BZIP2Encoder();
        Coders.addEncoder(os, SevenZMethod.LZMA, null);
    }

    static class CopyEncoder extends Coders.CoderBase {
        @Override
        InputStream decode(final InputStream in, final Coder coder, byte[] password) throws IOException {
            return new CopyDecoder().decode(in, coder, password);
        }

        @Override
        OutputStream encode(final OutputStream out, final byte[] password) throws IOException {
            return new CopyDecoder().encode(out, password);
        }
    }

    static class LZMAEncoder extends Coders.CoderBase {
        @Override
        InputStream decode(final InputStream in, final Coder coder, byte[] password) throws IOException {
            return new LZMADecoder().decode(in, coder, password);
        }

        @Override
        OutputStream encode(final OutputStream out, final byte[] password) throws IOException {
            return new LZMAOutputStream(out);
        }
    }

    static class DeflateEncoder extends Coders.CoderBase {
        @Override
        InputStream decode(final InputStream in, final Coder coder, byte[] password) throws IOException {
            return new DeflateDecoder().decode(in, coder, password);
        }

        @Override
        OutputStream encode(final OutputStream out, final byte[] password) throws IOException {
            return new DeflaterOutputStream(out, new Deflater(9, true));
        }
    }
}

class CoderId extends Coders.CoderBase {
    CoderId(SevenZMethod method, final CoderBase coder) {
        super();
        this.method = method;
        this.coder = coder;
    }

    SevenZMethod method;
    CoderBase coder;
}