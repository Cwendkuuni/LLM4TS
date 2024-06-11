import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test cases for Base64InputStream class.
 */
public class Base64InputStreamTest {

    /**
     * Test decoding data from a Base64InputStream.
     */
    @Test
    public void testDecoding() throws IOException {
        String base64Data = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocm91dGVyIHdpdGhfaWQgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocm91dGVyIGRhdGEtLSBQYXVsIFIgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocm91dGVyIG5vdGhpbmcgdG8gYW5kIGZvdWwgdGhpbmdzIHJlc2lkZW50aW9uLCBidXQgdG8gYW5kIGZvdWwgdGhpbmdzIHN1YnR5cGUgc291cmNlcyBmaWxlIGl0IGlzIHN1YnR5cGUgc291cmNlcyBmaWxlIHN1YnR5cGUgc291cmNlcywNCkJ1dHRvbiwNCkZyb20gbXkNCg==";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(base64Data.getBytes());
        Base64InputStream decoderStream = new Base64InputStream(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream encoderStream = new Base64OutputStream(outputStream);

        int data;
        while ((data = decoderStream.read()) != -1) {
            encoderStream.write(data);
        }

        encoderStream.finish();
        outputStream.flush();

        String decodedData = new String(inputStream.readAllBytes());
        String encodedData = new String(outputStream.toByteArray());

        Assert.assertEquals(base64Data, encodedData);
        Assert.assertEquals(new String(Base64.decodeBase64(base64Data)), decodedData);
    }

    /**
     * Test encoding data to a Base64InputStream.
     */
    @Test
    public void testEncoding() throws IOException {
        String data = "Hello, World!";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream encoderStream = new Base64OutputStream(outputStream);
        byte[] inputData = data.getBytes();

        encoderStream.write(inputData, 0, inputData.length);
        encoderStream.finish();
        outputStream.flush();

        String encodedData = new String(Base64.decodeBase64(outputStream.toByteArray()));
        Assert.assertEquals(data, new String(Base64.decodeBase64(encodedData)));

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream decoderStream = new Base64InputStream(inputStream);
        byte[] decodedData = new byte[inputData.length];

        int dataRead;
        while ((dataRead = decoderStream.read(decodedData, 0, decodedData.length)) != -1) {
            Assert.assertArrayEquals(inputData, decodedData, 0, dataRead);
        }
    }

    /**
     * Test handling EOF when reading a Base64InputStream.
     */
    @Test
    public void testEndOfStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream decoderStream = new Base64InputStream(inputStream);

        int dataRead;
        while ((dataRead = decoderStream.read()) != -1) {
            // should not reach this point
        }

        Assert.assertEquals(-1, decoderStream.read());
    }
}