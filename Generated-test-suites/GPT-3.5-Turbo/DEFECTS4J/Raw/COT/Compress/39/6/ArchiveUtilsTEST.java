import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {
	
	@Test
	public void testToString() {
		ArchiveEntry entry = new ArchiveEntry();
		entry.setDirectory(false);
		entry.setSize(2000);
		entry.setName("main.c");
		
		String expected = "-    2000 main.c";
		String actual = ArchiveUtils.toString(entry);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testMatchAsciiBuffer() {
		String expected = "test";
		byte[] buffer = "test".getBytes(StandardCharsets.US_ASCII);
		
		boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
		
		assertTrue(result);
	}
	
	@Test
	public void testToAsciiBytes() {
		String inputString = "test";
		
		byte[] expected = "test".getBytes(StandardCharsets.US_ASCII);
		byte[] actual = ArchiveUtils.toAsciiBytes(inputString);
		
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testToAsciiString() {
		byte[] inputBytes = "test".getBytes(StandardCharsets.US_ASCII);
		
		String expected = "test";
		String actual = ArchiveUtils.toAsciiString(inputBytes);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testToAsciiStringWithOffsetAndLength() {
		byte[] inputBytes = "test".getBytes(StandardCharsets.US_ASCII);
		
		String expected = "te";
		String actual = ArchiveUtils.toAsciiString(inputBytes, 0, 2);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testIsEqual() {
		byte[] buffer1 = "test".getBytes(StandardCharsets.US_ASCII);;
		byte[] buffer2 = "test".getBytes(StandardCharsets.US_ASCII);;
		
		boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
		
		assertTrue(result);
	}
	
	@Test
	public void testIsEqualWithIgnoreTrailingNulls() {
		byte[] buffer1 = "test\0".getBytes(StandardCharsets.US_ASCII);
		byte[] buffer2 = "test".getBytes(StandardCharsets.US_ASCII);
		
		boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);
		
		assertTrue(result);
	}
	
	@Test
	public void testIsEqualWithOffsetAndLength() {
		byte[] buffer1 = "test".getBytes(StandardCharsets.US_ASCII);
		byte[] buffer2 = "test".getBytes(StandardCharsets.US_ASCII);
		
		boolean result = ArchiveUtils.isEqual(buffer1, 0, 2, buffer2, 0, 2);
		
		assertTrue(result);
	}
	
	@Test
	public void testIsEqualWithOffsetAndLengthAndIgnoreTrailingNulls() {
		byte[] buffer1 = "test\0".getBytes(StandardCharsets.US_ASCII);
		byte[] buffer2 = "test".getBytes(StandardCharsets.US_ASCII);
		
		boolean result = ArchiveUtils.isEqual(buffer1, 0, 2, buffer2, 0, 2, true);
		
		assertTrue(result);
	}
	
	@Test
	public void testIsArrayZero() {
		byte[] a = new byte[5];
		
		boolean result = ArchiveUtils.isArrayZero(a, 5);
		
		assertTrue(result);
	}
	
	@Test
	public void testSanitize() {
		String s = "\u0010test\u0008";
		
		String expected = "?test?";
		String actual = ArchiveUtils.sanitize(s);
		
		assertEquals(expected, actual);
	}

}