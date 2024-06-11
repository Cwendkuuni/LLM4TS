import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.apache.commons.codec.binary.BaseNCodecOutputStream;
import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BaseNCodecInputStreamTest {

 private BaseNCodec baseNCodec;
 private BaseNCodecInputStream baseNCodecInputStream;
 private ByteArrayOutputStream byteArrayOutputStream;
 private ByteArrayInputStream byteArrayInputStream;

 @Before
 public void setUp() {
 baseNCodec = new BaseNCodec();
 byteArrayOutputStream = new ByteArrayOutputStream();
 byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 baseNCodecInputStream = new BaseNCodecInputStream(byteArrayInputStream, baseNCodec, false);
 }

 /**
 * Test for the read method.
 *
 * @throws IOException
 */
 @Test
 public void testRead() throws IOException {
 byte[] byteArray = new byte[]{-63, -63, -63, -63, -63, -63, -63, -63};
 byteArrayOutputStream.write(byteArray);
 byteArrayOutputStream.flush();

 int result = baseNCodecInputStream.read();
 Assert.assertNotEquals(BaseNCodecInputStream.EOF, result);

 byte[] readByteArray = new byte[1];
 baseNCodecInputStream.read(readByteArray, 0, 1);
 Assert.assertArrayEquals(new byte[]{byteArray[0]}, readByteArray);
 }

 /**
 * Test for the read method when EOF is reached.
 *
 * @throws IOException
 */
 @Test
 public void testReadWhenEofReached() throws IOException {
 int result = baseNCodecInputStream.read();
 Assert.assertEquals(BaseNCodecInputStream.EOF, result);
 }

 /**
 * Test for the read method when it is called multiple times and EOF has not been reached yet.
 *
 * @throws IOException
 */
 @Test
 public void testReadMultipleTimes() throws IOException {
 byte[] byteArray = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
 byteArrayOutputStream.write(byteArray);
 byteArrayOutputStream.flush();

 int result = 0;
 for (int i = 0; i < byteArray.length; i++) {
 result = baseNCodecInputStream.read();
 Assert.assertNotEquals(BaseNCodecInputStream.EOF, result);
 Assert.assertEquals(byteArray[i], (byte) (result & 0xFF));
 }

 Assert.assertEquals(BaseNCodecInputStream.EOF, baseNCodecInputStream.read());
 }

 /**
 * Test for the read method when it is called multiple times with a byte array.
 *
 * @throws IOException
 */
 @Test
 public void testReadMultipleTimesWithByteArray() throws IOException {
 byte[] byteArray = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
 byte[] buffer = new byte[byteArray.length];

 int result = 0;
 for (int i = 0; i < byteArray.length; i++) {
 result = baseNCodecInputStream.read(buffer, 0, 1);
 Assert.assertNotEquals(BaseNCodecInputStream.EOF, result);
 Assert.assertEquals(byteArray[i], buffer[0]);
 }

 Assert.assertEquals(BaseNCodecInputStream.EOF, baseNCodecInputStream.read(buffer, 0, 1));
 }

 /**
 * Test for the read method with a null byte array.
 *
 * @throws IOException
 */
 @Test(expected = NullPointerException.class)
 public void testReadWithNullByteArray() throws IOException {
 baseNCodecInputStream.read(null, 0, 1);
 }

 /**
 * Test for the read method with a negative offset.
 *
 * @throws IOException
 */
 @Test(expected = IndexOutOfBoundsException.class)
 public void testReadWithNegativeOffset() throws IOException {
 byte[] byteArray = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
 byte[] buffer = new byte[byteArray.length + 1];
 baseNCodecInputStream.read(buffer, -1, 1);
 }

 /**
 * Test for the read method with a negative length.
 *
 * * @throws IOException
 */
 @Test(expected = IndexOutOfBoundsException.class)
 public void testReadWithNegativeLength() throws IOException {
 byte[] byteArray = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
 byte[] buffer = new byte[byteArray.length + 1];
 baseNCodecInputStream.read(buffer, 0, -1);
 }

 /**
 * Test for the read method with a length greater than the remaining data.
 *
 * @throws IOException
 */
 @Test(expected = IndexOutOfBoundsException.class)
 public void testReadWithLengthGreaterThanRemainingData() throws IOException {
 byte[] byteArray = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
 byte[] buffer = new byte[byteArray.length + 1];
 baseNCodecInputStream.read(buffer, 0, byteArray.length + 1);
 }

 /**
 * Test for the markSupported method.
 * @throws IOException
 */
 @Test
 public void testMarkSupported() throws IOException {
 Assert.assertFalse(baseNCodecInputStream.markSupported());
 }

 /**
 * Test for the skip method with a negative skip length.
 *
 * @throws IOException
 */
 @Test(expected = IllegalArgumentException.class)
 public void testSkipWithNegativeLength() throws IOException {
 baseNCodecInputStream.skip(-1);
 }

 /**
 * Test for the skip method with a positive skip length.
 *
 * @throws IOException
 */
 @Test
 public void testSkipWithPositiveLength() throws IOException {
 int skipLength = 10;
 baseNCodecInputStream.skip(skipLength);
 }

 /**
 * Test for the available method.
 *
 * @throws IOException
 */
 @Test
 public void testAvailable() throws IOException {
 int available = baseNCodecInputStream.available();
 Assert.assertEquals(0, available);
 }

 /**
 * Test for the close method.
 *
 * @throws IOException
 */
 @Test
 public void testClose() throws IOException {
 baseNCodecInputStream.close();
 }
}