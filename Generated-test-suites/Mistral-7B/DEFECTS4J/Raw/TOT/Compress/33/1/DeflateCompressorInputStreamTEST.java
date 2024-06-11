import org.apache.commons.compress.compressors.DeflateParameters;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testDecompressNormalCase() throws IOException {
        byte[] inputData = new byte[]{(byte) 0x42, (byte) 0x5a, (byte) 0x61, (byte) 0x7a, (byte) 0x6d, (byte) 0x00, (byte) 0x2a};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());

        byte[] expectedOutput = new byte[]{(byte) 0x61, (byte) 0x7a, (byte) 0x6d};
        byte[] output = new byte[expectedOutput.length];
        int length = testStream.read(output);

        Assert.assertEquals("Unexpected length", expectedOutput.length, length);
        Assert.assertArrayEquals("Unexpected output", expectedOutput, output);
    }

    @Test
    public void testDecompressWithZlibHeader() throws IOException {
        byte[] inputData = new byte[]{(byte) 0x78, (byte) 0xda, (byte) 0x10, 0, 0, 0, 0, 0x00, 0x2a};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());

        byte[] expectedOutput = new byte[]{(byte) 0x61, (byte) 0x7a, (byte) 0x6d};
        byte[] output = new byte[expectedOutput.length];
        int length = testStream.read(output);

        Assert.assertEquals("Unexpected length", expectedOutput.length, length);
        Assert.assertArrayEquals("Unexpected output", expectedOutput, output);
    }

    @Test(expected = IOException.class)
    public void testDecompressNegativeSize() throws IOException {
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(IOUtils.toInputStream(new byte[0]), new DeflateParameters());
        testStream.skip(-1);
    }

    @Test(expected = IOException.class)
    public void testDecompressEmptyInput() throws IOException {
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(IOUtils.toInputStream(new byte[0]), new DeflateParameters());
        testStream.read();
    }
}