import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BitInputStreamTest {

 private InputStream testInputStream;
 private BitInputStream bitInputStream;

 @Before
 public void setUp() throws Exception {
 testInputStream = new MockInputStream();
 bitInputStream = new BitInputStream(testInputStream, ByteOrder.LITTLE_ENDIAN);
 }

 @After
 public void tearDown() throws Exception {
 if (bitInputStream != null) {
 bitInputStream.close();
 }
 }

 @Test
 public void testReadBitsWhenEndOfStreamReached() throws Exception {
 final int count = 1;
 final long expected = -1;
 final long actual = bitInputStream.readBits(count);
 assertEquals(expected, actual);
 }

 @Test
 public void testReadBitsWithValidCountAndValidByteOrder() throws Exception {
 final int count = 1;
 final long expected = 0b00000001;
 final byte testByte = 1;
 testInputStream.write(testByte);
 final long actual = bitInputStream.readBits(count);
 assertEquals(expected, actual);
 }

 @Test
 public void testReadBitsWithValidCountAndInvalidByteOrder() throws Exception {
 final int count = 1;
 final long expected = 0b10000000;
 final byte testByte = 1;
 testInputStream.write(testByte);
 bitInputStream = new BitInputStream(testInputStream, ByteOrder.BIG_ENDIAN);
 final long actual = bitInputStream.readBits(count);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReadBitsWithInvalidCount() throws Exception {
 final int count = 64;
 bitInputStream.readBits(count);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReadBitsWithCountGreaterThanMaximum() throws Exception {
 final int count = 64;
 bitInputStream.readBits(count);
 }

 @Test
 public void testReadBitsMultipleTimesWithValidCount() throws Exception {
 final int count = 2;
 final long expectedFirst = 0b0011;
 final long expectedSecond = 0b1100;
 testInputStream.write(0b0111_1111);
 final long actualFirst = bitInputStream.readBits(count);
 final long actualSecond = bitInputStream.readBits(count);
 assertEquals(expectedFirst, actualFirst);
 assertEquals(expectedSecond, actualSecond);
 }

 @Test
 public void testClearBitCache() throws Exception {
 final int count = 1;
 final long expectedFirst = 0b00000010;
 testInputStream.write(0b00101010);
 final long actualFirst = bitInputStream.readBits(count);
 bitInputStream.clearBitCache();
 final long actualSecond = bitInputStream.readBits(count);
 assertEquals(expectedFirst, actualFirst);
 assertEquals(-1, actualSecond);
 }
}

class MockInputStream extends InputStream {
 @Override
 public int read() throws IOException {
 return 0;
 }
}