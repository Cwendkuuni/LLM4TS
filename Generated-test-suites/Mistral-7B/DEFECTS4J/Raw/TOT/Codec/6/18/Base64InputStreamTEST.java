import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for Base64InputStream.
 */
public class Base64InputStreamTest {

    /**
     * Test encoding and decoding of simple data.
     */
    @Test
    public void testEncodeDecode() throws IOException {
        String source = "Hello, World!";

        // Encode and save data to byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64Output = new Base64OutputStream(outputStream, false);
        base64Output.write(source.getBytes("UTF-8"));
        base64Output.close();

        // Decode byte array to string
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64Input = new Base64InputStream(inputStream, false);
        byte[] buffer = new byte[inputStream.available()];
        int length = base64Input.read(buffer);
        String decoded = new String(buffer, 0, length, "UTF-8");

        // Compare original and decoded data
        Assert.assertEquals(source, decoded);
    }

    /**
     * Test decoding of invalid Base64 data.
     */
    @Test(expected = IOException.class)
    public void testDecodeInvalidData() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("invalid Base64 data".getBytes("UTF-8"));
        Base64InputStream base64Input = new Base64InputStream(inputStream, false);
        byte[] buffer = new byte[1024];
        base64Input.read(buffer);
    }

    /**
     * Test decoding of data with incorrect line length.
     */
    @Test(expected = IOException.class)
    public void testDecodeInvalidLineLength() throws IOException {
        String source = "Hello, World!";

        // Encode and save data to byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64Output = new Base64OutputStream(outputStream, 5);
        base64Output.write(source.getBytes("UTF-8"));
        base64Output.close();

        // Decode byte array with incorrect line length
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64Input = new Base64InputStream(inputStream, false, -1, null);
        byte[] buffer = new byte[1024];
        base64Input.read(buffer);
    }

    /**
     * Test encoding and decoding of multiple data parts.
     */
    @Test
    public void testEncodeDecodeMultipleParts() throws IOException {
        String source1 = "Hello, World!";
        String source2 = "Test data.";

        // Encode and save first data part to byte array
        ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
        Base64OutputStream base64Output1 = new Base64OutputStream(outputStream1, false);
        base64Output1.write(source1.getBytes("UTF-8"));
        base64Output1.write('\n');
        base64Output1.close();

        // Encode and save second data part to byte array
        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        Base64OutputStream base64Output2 = new Base64OutputStream(outputStream2, false);
        base64Output2.write(source2.getBytes("UTF-8"));
        base64Output2.write('\n');
        base64Output2.close();

        // Merge byte arrays and decode data
        byte[] data = new byte[outputStream1.size() + outputStream2.size()];
        System.arraycopy(outputStream1.toByteArray(), 0, data, 0, outputStream1.size());
        System.arraycopy(outputStream2.toByteArray(), 0, data, outputStream1.size(), outputStream2.size());

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64Input = new Base64InputStream(inputStream, false);

        byte[] buffer1 = new byte[1024];
        int length1 = base64Input.read(buffer1);
        String decoded1 = new String(buffer1, 0, length1, "UTF-8");
        Assert.assertEquals(source1, decoded1);

        byte[] buffer2 = new byte[1024];
        int length2 = base64Input.read(buffer2);
        String decoded2 = new String(buffer2, 0, length2, "UTF-8");
        Assert.assertEquals(source2, decoded2);
    }
}