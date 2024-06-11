import org.apache.commons.compress.archivers.tar.TarUtils;
import org.apache.commons.compress.utils.ByteStreams;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 // Test case for parseOctal method
 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{'0', '1', '2', '7', '1', '0'};
 long expectedResult = 012710L;
 long result = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expectedResult, result);
 }

 // Test case for parseOctal with invalid input
 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalWithInvalidInput() {
 byte[] buffer = new byte[]{'0', '1', '2', '8', '1', '0'};
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 // Test case for parseName method
 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'x', 't', 'x'};
 String expectedResult = "tar.txt";
 String result = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expectedResult, result);
 }

 // Test case for parseName with trailing spaces
 @Test
 public void testParseNameWithTrailingSpaces() {
 byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'x', 't', 'x', ' ', ' ', ' '};
 String expectedResult = "tar.txt";
 String result = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expectedResult, result);
 }

 // Test case for formatNameBytes method
 @Test
 public void testFormatNameBytes() {
 String name = "test.txt";
 byte[] buffer = new byte[256];
 int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 byte[] expectedResult = new byte[]{'t', 'e', 's', 't', '.', 't', 'x', 't'};
 assertArrayEquals(expectedResult, Arrays.copyOfRange(buffer, 0, offset));
 }

 // Test case for formatUnsignedOctalString method
 @Test
 public void testFormatUnsignedOctalString() {
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(012710L, buffer, 0, buffer.length);
 byte[] expectedResult = new byte[]{'0', '1', '2', '7', '1', '0', '0', '0', '0', '0'};
 assertArrayEquals(expectedResult, Arrays.copyOfRange(buffer, 0, buffer.length));
 }

 // Test case for formatOctalBytes method
 @Test
 public void testFormatOctalBytes() {
 byte[] buffer = new byte[12];
 TarUtils.formatOctalBytes(012710L, buffer, 0, buffer.length);
 byte[] expectedResult = new byte[]{'0', '1', '2', '7', '1', '0', ' ', '0', '0', '0', '0', '0'};
 assertArrayEquals(expectedResult, Arrays.copyOfRange(buffer, 0, buffer.length));
 }

 // Test case for computeCheckSum method
 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'h', 'e', 'a', 'd', 'e', 'r', ' ', '1', '2', '3', '4', '5'};
 long expectedResult = 2549521488L;
 long result = TarUtils.computeCheckSum(buffer);
 assertEquals(expectedResult, result);
 }
}