import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

public class Base64InputStreamTest {

  @Test
  public void testRead() throws IOException {
    String input = "Hello World";
    byte[] byteArray = Base64.encodeBase64(input.getBytes());
    InputStream inputStream = new ByteArrayInputStream(byteArray);
    Base64InputStream base64InputStream = new Base64InputStream(inputStream);

    int c;
    StringBuilder stringBuilder = new StringBuilder();
    while ((c = base64InputStream.read()) != -1) {
      stringBuilder.append((char) c);
    }

    assertEquals(input, stringBuilder.toString());

    base64InputStream.close();
  }

  @Test
  public void testReadWithOffsetAndLength() throws IOException {
    String input = "Hello World";
    byte[] byteArray = Base64.encodeBase64(input.getBytes());
    InputStream inputStream = new ByteArrayInputStream(byteArray);
    Base64InputStream base64InputStream = new Base64InputStream(inputStream);

    byte[] output = new byte[input.length()];

    int bytesRead = base64InputStream.read(output, 0, input.length());

    assertEquals(input.length(), bytesRead);
    assertEquals(input, new String(output));

    base64InputStream.close();
  }

  @Test
  public void testReadWithOffsetAndLengthGreaterThanBuffer() throws IOException {
    String input = "Hello World";
    byte[] byteArray = Base64.encodeBase64(input.getBytes());
    InputStream inputStream = new ByteArrayInputStream(byteArray);
    Base64InputStream base64InputStream = new Base64InputStream(inputStream);

    byte[] output = new byte[input.length() + 5];

    int bytesRead = base64InputStream.read(output, 2, 5);

    assertEquals(5, bytesRead);
    assertEquals(0, output[0]);
    assertEquals(0, output[1]);
    assertEquals(byteArray[0], output[2]);
    assertEquals(byteArray[1], output[3]);
    assertEquals(byteArray[2], output[4]);
    assertEquals(byteArray[3], output[5]);
    assertEquals(byteArray[4], output[6]);

    base64InputStream.close();
  }

  @Test(expected = NullPointerException.class)
  public void testReadWithNullArray() throws IOException {
    byte[] byteArray = Base64.encodeBase64("Hello World".getBytes());
    InputStream inputStream = new ByteArrayInputStream(byteArray);
    Base64InputStream base64InputStream = new Base64InputStream(inputStream);

    base64InputStream.read(null, 0, 1);

    base64InputStream.close();
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testReadWithInvalidOffset() throws IOException {
    byte[] byteArray = Base64.encodeBase64("Hello World".getBytes());
    InputStream inputStream = new ByteArrayInputStream(byteArray);
    Base64InputStream base64InputStream = new Base64InputStream(inputStream);

    base64InputStream.read(new byte[10], -1, 1);

    base64InputStream.close();
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testReadWithInvalidLength() throws IOException {
    byte[] byteArray = Base64.encodeBase64("Hello World".getBytes());
    InputStream inputStream = new ByteArrayInputStream(byteArray);
    Base64InputStream base64InputStream = new Base64InputStream(inputStream);

    base64InputStream.read(new byte[10], 0, -1);

    base64InputStream.close();
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testReadWithInvalidBuffer() throws IOException {
    byte[] byteArray = Base64.encodeBase64("Hello World".getBytes());
    InputStream inputStream = new ByteArrayInputStream(byteArray);
    Base64InputStream base64InputStream = new Base64InputStream(inputStream);

    base64InputStream.read(new byte[5], 0, 10);

    base64InputStream.close();
  }

  @Test(expected = IOException.class)
  public void testReadThrowsIOException() throws IOException {
    InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
    Base64InputStream base64InputStream = new Base64InputStream(inputStream);

    base64InputStream.close();
    base64InputStream.read();

    base64InputStream.close();
  }

  @Test
  public void testMarkSupported() throws IOException {
    InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
    Base64InputStream base64InputStream = new Base64InputStream(inputStream);

    assertFalse(base64InputStream.markSupported());

    base64InputStream.close();
  }
}