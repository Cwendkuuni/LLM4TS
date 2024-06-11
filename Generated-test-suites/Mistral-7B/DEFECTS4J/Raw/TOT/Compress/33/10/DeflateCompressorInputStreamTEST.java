import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.DeflateParameters;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.apache.commons.compress.compressors.DeflateCompressorInputStream.available;
import static org.apache.commons.compress.compressors.DeflateCompressorInputStream.count;
import static org.apache.commons.compress.compressors.DeflateCompressorInputStream.read;
import static org.apache.commons.compress.compressors.DeflateCompressorInputStream.read;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DeflateCompressorInputStream.class)
public class DeflateCompressorInputStreamTest {

    @Test
    public void testDecompressorInputStreamWithValidInput() throws IOException {
        // given
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78, 0x5e, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04, 0x14, 0x00, 0x04, 0x08, 0x00, 0x00, 0x00, 0x0f, (byte) 0x78, 0x9c, 0x01, 0x00, 0x00, 0x00, 0x00, 0xdb, 0x01, 0x00, 0x00, 0x00, 0x00, 0xdb, 0xdb, 0x01, 0x00, 0x00, 0x00, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[10];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // when
        int bytesRead = 0;
        while (bytesRead != -1) {
            bytesRead = compressorInputStream.read(buffer);
            if (bytesRead != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        byte[] expectedOutput = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};

        // then
        byte[] actualOutput = outputStream.toByteArray();
        assertEquals("Unexpected decompressed data", expectedOutput, actualOutput);
        assertFalse("Unexpected end of stream flag", compressorInputStream.available() > 0);
        compressorInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testDecompressorInputStreamWithMalformedInput() throws IOException {
        // given
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78, 0x5e, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04, 0x14, 0x00, 0x04, 0x08, 0x00, 0x00, 0x00, 0x0f, (byte) 0x78, 0x9c, 0x01, 0x00, 0x00, 0x00, 0x00, 0xdb, 0x01, 0x00, 0x00, 0x00, 0x00, 0xdb, 0xdb, 0x01, 0x00, 0x00, 0x00, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

        // when and then
        compressorInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testDecompressorInputStreamWithNullInput() {
        // given
        InputStream inputStream = null;

        // when and then
        try {
            DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        } catch (IOException e) {
            // expected
            assertEquals("InputStream must not be null", "stream is marked as closed", e.getMessage());
        }
    }

    @Test
    public void testDecompressorInputStreamCount() throws IOException {
        // given
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78, 0x5e, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04, 0x14, 0x00, 0x04, 0x08, 0x00, 0x00, 0x00, 0x0f, (byte) 0x78, 0x9c, 0x01, 0x00, 0x00, 0x00, 0x00, 0xdb, 0x01, 0x00, 0x00, 0x00, 0x00, 0xdb, 0xdb, 0x01, 0x00, 0x00, 0x00, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

        // when
        compressorInputStream.read();
        compressorInputStream.read(new byte[10]);
        compressorInputStream.count(1);
        compressorInputStream.count(1000);

        // then
        assertEquals("Unexpected number of bytes read", 1, compressorInputStream.available());
        compressorInputStream.close();
    }
}