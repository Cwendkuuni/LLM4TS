import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testBase64InputStreamConstructor1() {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] expectedByteArray = new byte[5];
        int readBytes = base64InputStream.read(expectedByteArray);
        Assert.assertEquals(4, readBytes);
        Assert.assertEquals("VG8g", new String(expectedByteArray, 0, 4));

        try {
            base64InputStream.read(expectedByteArray);
            Assert.fail();
        } catch (IOException e) {
            // expected
        }

        try {
            base64InputStream.markSupported();
            Assert.fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testBase64InputStreamConstructor2() {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        byte[] expectedByteArray = new byte[13];
        int readBytes = base64InputStream.read(expectedByteArray);
        Assert.assertEquals(13, readBytes);
        Assert.assertEquals("Uw==VG8g", new String(expectedByteArray, 0, 13));

        try {
            base64InputStream.read(expectedByteArray);
            Assert.fail();
        } catch (IOException e) {
            // expected
        }

        try {
            base64InputStream.markSupported();
            Assert.fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testBase64InputStreamConstructor3() {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64 base64 = new Base64();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 76, new byte[]{0x0D, 0x0A});

        byte[] expectedByteArray = new byte[5];
        int readBytes = base64InputStream.read(expectedByteArray);
        Assert.assertEquals(4, readBytes);
        Assert.assertEquals("VG8g", new String(expectedByteArray, 0, 4));

        try {
            base64InputStream.read(expectedByteArray);
            Assert.fail();
        } catch (IOException e) {
            // expected
        }

        try {
            base64InputStream.markSupported();
            Assert.fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(byteArrayOutputStream);
        byte[] inputByteArray = "Test".getBytes();
        base64OutputStream.write(inputByteArray, 0, inputByteArray.length);
        base64OutputStream.finish();
        byte[] decodedByteArray = Base64.decodeBase64(byteArrayOutputStream.toByteArray());

        Base64InputStream testBase64InputStream = new Base64InputStream(new ByteArrayInputStream(decodedByteArray), false);
        int readBytes2 = testBase64InputStream.read(expectedByteArray);
        Assert.assertEquals(4, readBytes2);
        Assert.assertEquals("Test", new String(expectedByteArray, 0, 4));
    }

    @Test
    public void testBase64InputStreamWithLineLengthAndLineSeparator() {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64 base64 = new Base64(76, new byte[]{0x0D, 0x0A});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 76, new byte[]{0x0D, 0x0A});

        byte[] expectedByteArray = new byte[5];
        int readBytes = base64InputStream.read(expectedByteArray, 0, 4);
        Assert.assertEquals(4, readBytes);
        Assert.assertEquals("VG8g", new String(expectedByteArray, 0, 4));

        int readBytes2 = base64InputStream.read(expectedByteArray);
        Assert.assertEquals(-1, readBytes2);

        byte[] expectedByteArray2 = new byte[53];
        int readBytes3 = base64InputStream.read(expectedByteArray2);
        Assert.assertEquals(53, readBytes3);

        String decodedLine = new String(expectedByteArray2, 0, readBytes3);
        String expectedDecodedLine = "VG8gTW9kZXM=";
        Assert.assertEquals(expectedDecodedLine, decodedLine);

        byte[] expectedByteArray3 = new byte[16];
        int readBytes4 = base64InputStream.read(expectedByteArray3);
        Assert.assertEquals(-1, readBytes4);

        try {
            base64InputStream.markSupported();
            Assert.fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
}