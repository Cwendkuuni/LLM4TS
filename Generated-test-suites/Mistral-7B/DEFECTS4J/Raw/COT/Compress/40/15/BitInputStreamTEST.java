import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BitInputStreamTest {

 private InputStream inputStream;
 private ByteOrder byteOrder;
 private BitInputStream bitInputStream;

 @Before
 public void setup() throws Exception {
 inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05});
 byteOrder = ByteOrder.LITTLE_ENDIAN;
 bitInputStream = new BitInputStream(inputStream, byteOrder);
 }

 @Test
 public void testConstructor() {
 BitInputStream bitInputStream = new BitInputStream(null, byteOrder);
 assertNull(bitInputStream);
 }

 @Test
 public void testClose() throws Exception {
 inputStream.close();
 bitInputStream.close();
 assertFalse(inputStream.isOpen());
 assertFalse(bitInputStream.isOpen());
 }

 @Test
 public void testClearBitCache() throws Exception {
 bitInputStream.clearBitCache();
 assertEquals(0, bitInputStream.bitsCached);
 assertEquals(0, bitInputStream.bitsCachedSize);
 }

 @Test
 public void testReadBits_ValidCount() throws Exception {
 // Test reading 1 bit
 assertEquals(1, bitInputStream.readBits(1));
 // Test reading multiple bits
 assertEquals(0b11111111 & 0xFF, bitInputStream.readBits(8));
 // Test reading beyond available bits, should return -1
 assertEquals(-1, bitInputStream.readBits(9));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReadBits_InvalidCount() throws Exception {
 bitInputStream.readBits(-1);
 }

 @Test
 public void testReadBits_EndOfStream() throws Exception {
 // Read all bits from the stream
 long bits = 0;
 for (int i = 0; i < 32 * 5; i++) {
 bits |= bitInputStream.readBits(1);
 }
 // Try to read more bits, should return -1
 assertEquals(-1, bitInputStream.readBits(1));
 }

 @Test
 public void testReadBits_LittleEndian() throws Exception {
 byteOrder = ByteOrder.LITTLE_ENDIAN;
 bitInputStream = new BitInputStream(inputStream, byteOrder);
 // Test reading multiple bits from the first byte
 assertEquals(0b11111111 & 0xFF, bitInputStream.readBits(8));
 // Test reading multiple bits from the second byte
 assertEquals(0b00111111 & 0xFF, bitInputStream.readBits(6));
 // Test reading multiple bits from the third byte
 assertEquals(0b00001111, bitInputStream.readBits(4));
 // Test reading multiple bits from the fourth byte
 assertEquals(0b00000111, bitInputStream.readBits(3));
 // Test reading multiple bits from the fifth byte
 assertEquals(0b00000011, bitInputStream.readBits(2));
 }

 @Test
 public void testReadBits_BigEndian() throws Exception {
 byteOrder = ByteOrder.BIG_ENDIAN;
 bitInputStream = new BitInputStream(inputStream, byteOrder);
 // Test reading multiple bits from the first byte
 assertEquals(0b00000011, bitInputStream.readBits(2));
 // Test reading multiple bits from the second byte
 assertEquals(0b00001111, bitInputStream.readBits(4));
 // Test reading multiple bits from the third byte
 assertEquals(0b00111111 & 0xFF, bitInputStream.readBits(6));
 // Test reading multiple bits from the fourth byte
 assertEquals(0b11111111 & 0xFF, bitInputStream.readBits(8));
 }
}