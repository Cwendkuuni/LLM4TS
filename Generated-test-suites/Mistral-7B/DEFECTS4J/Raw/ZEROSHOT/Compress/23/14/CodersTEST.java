import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CodersTest {

 private InputStream inputStream;
 private OutputStream outputStream;

 @Test
 public void testAddDecoder() throws Exception {
 inputStream = mock(InputStream.class);
 SevenZMethod method = SevenZMethod.BZIP2;
 byte[] password = new byte[16];
 when(inputStream.readAllBytes()).thenReturn(new byte[1]);
 Coder coder = mock(BZIP2Decoder.class);
 when(coder.decode(inputStream, null, password)).thenReturn(inputStream);
 when(coder.encode(outputStream, password)).thenThrow(UnsupportedOperationException.class);

 Coder result = Coders.addDecoder(inputStream, method, password);
 assertArrayEquals(BZIP2Decoder.class.getName().getBytes(), result.getClass().getName().getBytes());
 }

 @Test(expected = IOException.class)
 public void testAddDecoderUnsupportedMethod() throws Exception {
 inputStream = mock(InputStream.class);
 SevenZMethod method = new SevenZMethod(new byte[]{0x01, 0x02});
 byte[] password = new byte[1];

 Coder result = Coders.addDecoder(inputStream, method, password);
 assertNull(result);
 }

 @Test
 public void testAddEncoder() throws Exception {
 outputStream = mock(OutputStream.class);
 SevenZMethod method = SevenZMethod.DEFLATE;
 byte[] password = new byte[16];
 Coder coder = mock(DeflateDecoder.class);
 when(coder.encode(outputStream, password)).thenReturn(outputStream);

 Coder result = Coders.addEncoder(outputStream, method, password);
 assertArrayEquals(DeflateDecoder.class.getName().getBytes(), result.getClass().getName().getBytes());
 }

 @Test(expected = IOException.class)
 public void testAddEncoderUnsupportedMethod() throws Exception {
 outputStream = mock(OutputStream.class);
 SevenZMethod method = new SevenZMethod(new byte[]{0x01, 0x02});
 byte[] password = new byte[1];

 Coder result = Coders.addEncoder(outputStream, method, password);
 assertNull(result);
 }
}