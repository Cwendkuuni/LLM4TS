import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Assert;
import org.junit.Test;

public class BaseNCodecInputStreamTest {

 private BaseNCodec baseNCodec = new BaseNCodec();
 private InputStream input = new ByteArrayInputStream(new byte[]{0x3, 0x2, 0x1, 0x1, EOF});

 @Test
 public void testRead() throws IOException {
 BaseNCodecInputStream bncis = new BaseNCodecInputStream(input, baseNCodec, false);

 Assert.assertEquals(0x3, bncis.read()); // read first byte
 Assert.assertEquals(0x2, bncis.read()); // read second byte
 Assert.assertEquals(0x1, bncis.read()); // read third byte
 Assert.assertEquals(-1, bncis.read()); // reaching EOF
 }

 @Test(expected = NullPointerException.class)
 public void testNullByteArray() {
 BaseNCodecInputStream bncis = new BaseNCodecInputStream(input, baseNCodec, false);
 bncis.read(null, 0, 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testNegativeOffset() {
 BaseNCodecInputStream bncis = new BaseNCodecInputStream(input, baseNCodec, false);
 bncis.read(new byte[10], -1, 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testOffsetOutOfBounds() {
 BaseNCodecInputStream bncis = new BaseNCodecInputStream(input, baseNCodec, false);
 bncis.read(new byte[10], 11, 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testLengthOutOfBounds() {
 BaseNCodecInputStream bncis = new BaseNCodecInputStream(input, baseNCodec, false);
 bncis.read(new byte[10], 0, 11);
 }

 @Test(expected = IOException.class)
 public void testIOException() throws IOException {
 InputStream is = new ByteArrayInputStream(new byte[]{});
 BaseNCodecInputStream bncis = new BaseNCodecInputStream(is, baseNCodec, false);
 bncis.read();
 is.close();
 bncis.read(); // should throw an IOException
 }

 @Test
 public void testMarkSupported() {
 BaseNCodecInputStream bncis = new BaseNCodecInputStream(input, baseNCodec, false);
 Assert.assertFalse(bncis.markSupported());
 }

 @Test
 public void testReadLargeData() throws IOException {
 byte[] data = new byte[1024];
 BaseNCodecInputStream bncis = new BaseNCodecInputStream(input, baseNCodec, false);
 int read = bncis.read(data, 0, data.length);
 Assert.assertEquals(4, read); // we only have 4 bytes in the input
 }

 @Test
 public void testSkip() throws IOException {
 BaseNCodecInputStream bncis = new BaseNCodecInputStream(input, baseNCodec, false);
 bncis.skip(5); // skip 5 bytes

 byte[] readData = new byte[10];
 int read = bncis.read(readData, 0, readData.length); // read remaining bytes
 Assert.assertEquals(1, read); // only one byte remains
 }

 @Test
 public void testSkipLargeData() throws IOException {
 BaseNCodecInputStream bncis = new BaseNCodecInputStream(input, baseNCodec, false);
 bncis.skip(Integer.MAX_VALUE); // try to skip a large amount of data
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSkipNegativeLength() {
 BaseNCodecInputStream bncis = new BaseNCodecInputStream(input, baseNCodec, false);
 bncis.skip(-1); // skip a negative amount of data
 }

}