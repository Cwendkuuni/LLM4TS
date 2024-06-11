import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.utils.ByteUtils;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.*;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@Category(org.apache.commons.compress.test.TestCategories.Unit)
public class CodersTest {

    @Test
    public void testAddDecoder() throws Exception {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{-1, 0x07, 0x01, 0x02, 0x00, 0x01, 0x00, 0x00});
        final Coder copyCoder = new Coder() {{
            decompositionMethodId = new byte[]{SevenZMethod.COPY.getId()};
        }};

        final InputStream decoder = Coders.addDecoder(inputStream, copyCoder, null);
        final byte[] result = ByteUtils.toByteArray(decoder);
        assertArrayEquals(new byte[]{0x01, 0x00, 0x00}, result);

        final Coder lzmaCoder = new Coder() {{
            decompositionMethodId = new byte[]{SevenZMethod.LZMA.getId()};
        }};

        try {
            Coders.addDecoder(inputStream, lzmaCoder, null);
            fail("Exception expected");
        } catch (IOException e) {
            assertEquals("Unsupported compression method [LZMA]", e.getMessage());
        }
    }

    @Test
    public void testAddEncoder() throws Exception {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final SevenZMethod deflateMethod = SevenZMethod.DEFLATE;
        final Coder deflateCoder = new Coder() {{
            method = deflateMethod;
        }};

        final byte[] password = new byte[32];
        new Random().nextBytes(password);

        final OutputStream encoder = Coders.addEncoder(outputStream, deflateMethod, password);
        final byte[] input = new byte[10_000];
        new Random().nextBytes(input);
        encoder.write(input, 0, input.length);
        encoder.close();

        final InputStream decoder = Coders.addDecoder(new ByteArrayInputStream(outputStream.toByteArray()), deflateCoder, password);
        final byte[] result = ByteUtils.toByteArray(decoder);
        assertArrayEquals(input, result);

        final Coder aes256Sha256Coder = new Coder() {{
            method = SevenZMethod.AES256SHA256;
        }};

        try {
            Coders.addEncoder(outputStream, SevenZMethod.BZIP2, password);
            fail("Exception expected");
        } catch (IOException e) {
            assertEquals("Unsupported compression method [BZIP2]", e.getMessage());
        }
    }

    @Test
    public void testLzmaDecoder() throws Exception {
        final byte[] input = new byte[1024 * 1024 * 2];
        new Random().nextBytes(input);

        final ByteArrayInputStream in = new ByteArrayInputStream(input);
        final Coder lzmaCoder = new Coder() {{
            decompositionMethodId = new byte[]{SevenZMethod.LZMA.getId()};
        }};

        final InputStream decoder = Coders.addDecoder(in, lzmaCoder, null);
        final byte[] result = ByteUtils.toByteArray(decoder);
        assertArrayEquals(input, result);
    }

    @Test
    public void testDeflateDecoder() throws Exception {
        final byte[] input = new byte[1024 * 1024 * 2];
        new Random().nextBytes(input);

        final ByteArrayInputStream in = new ByteArrayInputStream(input);
        final Coder deflateCoder = new Coder() {{
            decompositionMethodId = new byte[]{SevenZMethod.DEFLATE.getId()};
        }};

        final InputStream decoder = Coders.addDecoder(in, deflateCoder, null);
        final byte[] result = ByteUtils.toByteArray(decoder);
        assertArrayEquals(input, result);
    }

    @Test
    public void testBzip2Decoder() throws Exception {
        final byte[] input = new byte[1024 * 1024 * 2];
        new Random().nextBytes(input);

        final ByteArrayInputStream in = new ByteArrayInputStream(input);
        final Coder bzip2Coder = new Coder() {{
            decompositionMethodId = new byte[]{SevenZMethod.BZIP2.getId()};
        }};

        final InputStream decoder = Coders.addDecoder(in, bzip2Coder, null);
        final byte[] result = ByteUtils.toByteArray(decoder);
        assertArrayEquals(input, result);
    }

    @Test
    public void testAes256Sha256Decoder() throws Exception {
        final byte[] salt = new byte[16];
        new Random().nextBytes(salt);
        final byte[] iv = new byte[16];
        new Random().nextBytes(iv);
        final byte[] key = new byte[32];
        new Random().nextBytes(key);

        final byte[] password = new byte[32];
        System.arraycopy(key, 0, password, 0, key.length);

        final byte[] input = new byte[1024 * 1024 * 2];
        new Random().nextBytes(input);

        final ByteArrayInputStream in = new ByteArrayInputStream(ByteBuffer.allocate(1024 * 1024 * 3).put(salt).put(iv).put(input).array());
        final Coder aes256Sha256Coder = new Coder() {{
            properties = new byte[salt.length + iv.length + input.length + 3];
            properties[0] = 0xff;
            properties[1] = (byte) (0xff & (salt.length + iv.length));
            System.arraycopy(salt, 0, properties, 2, salt.length);
            System.arraycopy(iv, 0, properties, 2 + salt.length, iv.length);
            properties[2 + salt.length + iv.length] = 0;
            properties[2 + salt.length + iv.length + 1] = 0xff;
            properties[3] = 0;
            decompositionMethodId = new byte[]{SevenZMethod.AES256SHA256.getId()};
        }};

        final InputStream decoder = Coders.addDecoder(in, aes256Sha256Coder, password);
        final byte[] result = ByteUtils.toByteArray(decoder);
        assertArrayEquals(input, result);
    }

    @Test
    public void testCopyDecoder() throws Exception {
        final byte[] input = new byte[1024 * 1024 * 2];
        new Random().nextBytes(input);

        final ByteArrayInputStream in = new ByteArrayInputStream(input);
        final Coder copyCoder = new Coder() {{
            decompositionMethodId = new byte[]{SevenZMethod.COPY.getId()};
        }};

        final InputStream decoder = Coders.addDecoder(in, copyCoder, null);
        final byte[] result = ByteUtils.toByteArray(decoder);
        assertArrayEquals(input, result);
    }

    private static class Coder {
        byte[] decompositionMethodId;
        byte[] properties;
    }
}