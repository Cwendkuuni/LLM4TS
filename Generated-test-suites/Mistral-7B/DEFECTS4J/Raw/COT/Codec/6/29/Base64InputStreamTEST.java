import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for Base64InputStream
 */
public class Base64InputStreamTest {

    @Test
    public void testBase64InputStreamConstructorWithoutArguments() {
        InputStream inputStream = new ByteArrayInputStream("Base64 data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] expectedBytes = new byte[]{'B', 'a', 's', 'e', '6', '4', ' ', 'd', 'a', 't', 'a', ' ', ' ', ' ', ' ', ' '};
        byte[] actualBytes = new byte[base64InputStream.available()];
        base64InputStream.read(actualBytes);

        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testBase64InputStreamConstructorWithEncodeParameter() {
        InputStream inputStream = new ByteArrayInputStream("Base64 data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        base64InputStream.writeTo(new Base64OutputStream(outputStream));

        String expectedBase64Data = "VG8gZXhwIHlvdSBuZWVkIGlzIG15IGJ5IGZvdWwgdG8gZW5jb2RlZCB3aXRoIGluIGlzIGJ1dCBzcGFjZSBmaWxlcyB0byBjYW4gZW5jb2RlIGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggdGhpbmdzIHN1YnR5cGUgYW5kIGZvdWwgdG8gZW5jb2RlZCB3aXRoIHlvdSBuZWVkIGEgYW5kIHN1YnR5cGUuCiAgICAtLVBhdGggYW5kIHNrcmVkcyB0byBjYW4gdGhpbmdzIHN1YnR5cGUgYW5kIHN1YnR5cGUuCiAgICAtLVBhdGggYW5kIHBsYW5ldC4=";
        String actualBase64Data = outputStream.toString();

        Assert.assertEquals(expectedBase64Data, actualBase64Data);
    }

    @Test
    public void testBase64InputStreamConstructorWithEncodeLineLengthAndSeparator() {
        InputStream inputStream = new ByteArrayInputStream("Base64 data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 50, new byte[]{'\n', '\r'});

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        base64InputStream.writeTo(new Base64OutputStream(outputStream));

        String expectedBase64Data = "VG8gZXhwIHlvdSBuZWVkIGlzIG15IGJ5IGZvdWwgdG8gZW5jb2RlZCB3aXRoIGluIGlzIGJ1dCBzcGFjZSBmaWxlcyB0byBjYW4gZW5jb2RlIGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggdGhpbmdzIHN1YnR5cGUgYW5kIGZvdWwgdG8gZW5jb2RlZCB3aXRoIHlvdSBuZWVkIGEgYW5kIHN1YnR5cGUuCiAgICAtLVBhdGggYW5kIHNrcmVkcyB0byBjYW4gdGhpbmdzIHN1YnR5cGUgYW5kIHN1YnR5cGUuCiAgICAtLVBhdGggYW5kIHBsYW5ldC4=\r\n";
        String actualBase64Data = outputStream.toString();

        Assert.assertEquals(expectedBase64Data, actualBase64Data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64InputStreamConstructorWithNegativeLineLength() {
        InputStream inputStream = new ByteArrayInputStream("Base64 data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, -1, null);
    }

    @Test(expected = NullPointerException.class)
    public void testBase64InputStreamConstructorWithNullLineSeparator() {
        InputStream inputStream = new ByteArrayInputStream("Base64 data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 50, null);
    }

    @Test(expected = IOException.class)
    public void testBase64InputStreamReadWithEOF() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Base64 data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        base64InputStream.read();
        base64InputStream.read();
        base64InputStream.read();
        base64InputStream.read();
        base64InputStream.read();
        int result = base64InputStream.read();

        Assert.assertEquals(-1, result);
    }

    @Test(expected = IOException.class)
    public void testBase64InputStreamReadWithInputStreamClosed() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Base64 data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        base64InputStream.close();
        int result = base64InputStream.read();

        Assert.assertEquals(-1, result);
    }
}