import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.*;

import org.apache.commons.codec.binary.BaseNCodec;

public class BaseNCodecInputStreamTest {

  private final InputStream mockInputStream = mock(InputStream.class);
  private final BaseNCodec mockBaseNCodec = mock(BaseNCodec.class);
  private final boolean doEncode = false;

  @Test
  public void testRead() throws IOException {
    BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, mockBaseNCodec, doEncode);

    when(mockInputStream.read()).thenReturn(65);

    int result = inputStream.read();
    Assert.assertEquals(65, result);

    verify(mockInputStream, times(1)).read();
  }

  @Test
  public void testReadWithZeroResult() throws IOException {
    BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, mockBaseNCodec, doEncode);

    when(mockInputStream.read()).thenReturn(0).thenReturn(65);

    int result = inputStream.read();
    Assert.assertEquals(65, result);

    verify(mockInputStream, times(2)).read();
  }

  @Test
  public void testReadWithNegativeResult() throws IOException {
    BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, mockBaseNCodec, doEncode);

    when(mockInputStream.read()).thenReturn(-1);

    int result = inputStream.read();
    Assert.assertEquals(-1, result);

    verify(mockInputStream, times(1)).read();
  }

  @Test
  public void testReadWithPositiveOverflow() throws IOException {
    BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, mockBaseNCodec, doEncode);

    when(mockInputStream.read()).thenReturn(256);

    int result = inputStream.read();
    Assert.assertEquals(0, result);

    verify(mockInputStream, times(1)).read();
  }

  @Test
  public void testReadWithNegativeOverflow() throws IOException {
    BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, mockBaseNCodec, doEncode);

    when(mockInputStream.read()).thenReturn(-256);

    int result = inputStream.read();
    Assert.assertEquals(0, result);

    verify(mockInputStream, times(1)).read();
  }

  @Test
  public void testReadWithException() throws IOException {
    BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, mockBaseNCodec, doEncode);

    when(mockInputStream.read()).thenThrow(IOException.class);

    try {
      inputStream.read();

      Assert.fail("IOException was not thrown");
    } catch (IOException ex) {
      // expected
    }

    verify(mockInputStream, times(1)).read();
  }

  @Test
  public void testReadWithEmptyBaseNCodec() throws IOException {
    BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, mockBaseNCodec, doEncode);

    when(mockBaseNCodec.hasData()).thenReturn(false);
    when(mockInputStream.read(any(byte[].class))).thenReturn(5);

    int result = inputStream.read(new byte[5]);

    Assert.assertEquals(5, result);

    verify(mockBaseNCodec, times(1)).hasData();
    verify(mockInputStream, times(1)).read(any(byte[].class));
  }

  @Test
  public void testReadWithNonEmptyBaseNCodec() throws IOException {
    BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, mockBaseNCodec, doEncode);

    when(mockBaseNCodec.hasData()).thenReturn(true);
    when(mockBaseNCodec.readResults(any(byte[].class), anyInt(), anyInt())).thenReturn(5);

    int result = inputStream.read(new byte[5]);

    Assert.assertEquals(5, result);

    verify(mockBaseNCodec, times(1)).hasData();
    verify(mockBaseNCodec, times(1)).readResults(any(byte[].class), anyInt(), anyInt());
  }

  @Test
  public void testReadWithNonEmptyBaseNCodecAndZeroResult() throws IOException {
    BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, mockBaseNCodec, doEncode);

    when(mockBaseNCodec.hasData()).thenReturn(true, true, true, false);
    when(mockBaseNCodec.readResults(any(byte[].class), anyInt(), anyInt())).thenReturn(0, 0, 0, 5);
    when(mockInputStream.read(any(byte[].class))).thenReturn(5);

    int result = inputStream.read(new byte[5]);

    Assert.assertEquals(5, result);

    verify(mockBaseNCodec, times(4)).hasData();
    verify(mockBaseNCodec, times(4)).readResults(any(byte[].class), anyInt(), anyInt());
    verify(mockInputStream, times(1)).read(any(byte[].class));
  }

  @Test(expected = NullPointerException.class)
  public void testReadWithNullByteArray() throws IOException {
    BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, mockBaseNCodec, doEncode);

    inputStream.read(null);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testReadWithNegativeOffset() throws IOException {
    BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, mockBaseNCodec, doEncode);

    inputStream.read(new byte[5], -1, 5);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testReadWithNegativeLength() throws IOException {
    BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, mockBaseNCodec, doEncode);

    inputStream.read(new byte[5], 0, -5);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testReadWithInvalidOffset() throws IOException {
    BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, mockBaseNCodec, doEncode);

    inputStream.read(new byte[5], 10, 5);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testReadWithInvalidLength() throws IOException {
    BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, mockBaseNCodec, doEncode);

    inputStream.read(new byte[5], 0, 10);
  }

  @Test
  public void testMarkSupported() {
    BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, mockBaseNCodec, doEncode);

    boolean result = inputStream.markSupported();

    Assert.assertFalse(result);    
  }

}