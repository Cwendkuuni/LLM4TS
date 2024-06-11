import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BitInputStreamTest {

 private InputStream mockInputStream;
 private BitInputStream bitInputStream;

 @Before
 public void setUp() {
 mockInputStream = Mockito.mock(InputStream.class);
 bitInputStream = new BitInputStream(mockInputStream, ByteOrder.LITTLE_ENDIAN);
 }

 @Test
 public void testConstructor() {
 ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
 InputStream inputStream = new ByteArrayInputStream(new byte[0]);
 BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
 assertEquals(byteOrder, bitInputStream.byteOrder);
 }

 @Test
 public void testClearBitCache() throws IOException {
 bitInputStream.clearBitCache();
 assertEquals(0, bitInputStream.bitsCached);
 assertEquals(0, bitInputStream.bitsCachedSize);
 }

 @Test
 public void testReadBitsNormalUseCase() throws IOException {
 byte[] testData = new byte[]{(byte) 0x0F, (byte) 0x1A, (byte) 0x2B};
 mockInputStream = new ByteArrayInputStream(testData);
 when(mockInputStream.read()).thenReturn(testData[0]).thenReturn(testData[1]).thenReturn(testData[2]);

 int bitsToRead = 7;
 long expectedResult = 0x01_111_111; // binary: 0001 1111 1111
 long actualResult = bitInputStream.readBits(bitsToRead);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testReadBitsEdgeCases() throws IOException {
 // Test edge case: reading 0 bits
 bitInputStream.clearBitCache();
 long actualResult = bitInputStream.readBits(0);
 assertEquals(-1, actualResult);

 // Test edge case: reading more bits than available
 bitInputStream.clearBitCache();
 byte[] testData = new byte[]{0x0F};
 mockInputStream = new ByteArrayInputStream(testData);
 when(mockInputStream.read()).thenReturn(testData[0]);
 int bitsToRead = 7;
 assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(bitsToRead));
 }

 @Test
 public void testReadBitsExceptionScenarios() throws IOException {
 // Test exception scenario: InputStream returns EOF before any data is read
 byte[] testData = new byte[]{0x0F};
 mockInputStream = new ByteArrayInputStream(testData);
 when(mockInputStream.read()).thenReturn(-1);
 int bitsToRead = 7;
 assertThrows(IOException.class, () -> bitInputStream.readBits(bitsToRead));
 }
}