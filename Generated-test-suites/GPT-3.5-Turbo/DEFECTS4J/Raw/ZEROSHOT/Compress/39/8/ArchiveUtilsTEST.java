import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.nio.charset.Charset;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

	@Test
	public void testToStringIsDirectory() {
		ArchiveEntry entry = new ArchiveEntry();
		entry.setDirectory(true);
		entry.setSize(100);
		entry.setName("test");

		String expected = "d     100 test";
		String result = ArchiveUtils.toString(entry);

		assertEquals(expected, result);
	}

	@Test
	public void testToStringIsNotDirectory() {
		ArchiveEntry entry = new ArchiveEntry();
		entry.setDirectory(false);
		entry.setSize(2000);
		entry.setName("main.c");

		String expected = "-    2000 main.c";
		String result = ArchiveUtils.toString(entry);

		assertEquals(expected, result);
	}

	@Test
	public void testMatchAsciiBufferWithOffsetAndLength() {
		String inputString = "test";
		byte[] buffer = inputString.getBytes();

		boolean result = ArchiveUtils.matchAsciiBuffer(inputString, buffer, 1, 3);

		assertTrue(result);
	}

	@Test
	public void testMatchAsciiBufferWithoutOffsetAndLength() {
		String inputString = "test";
		byte[] buffer = inputString.getBytes();

		boolean result = ArchiveUtils.matchAsciiBuffer(inputString, buffer);

		assertTrue(result);
	}

	@Test
	public void testToAsciiBytes() {
		String inputString = "test";
		byte[] expected = inputString.getBytes(Charset.forName("US-ASCII"));

		byte[] result = ArchiveUtils.toAsciiBytes(inputString);

		assertTrue(Arrays.equals(expected, result));
	}

	@Test
	public void testToAsciiStringWithOffsetAndLength() {
		byte[] inputBytes = "test".getBytes();
		int offset = 1;
		int length = 3;
		String expected = new String(inputBytes, offset, length, Charset.forName("US-ASCII"));

		String result = ArchiveUtils.toAsciiString(inputBytes, offset, length);

		assertEquals(expected, result);
	}

	@Test
	public void testToAsciiStringWithoutOffsetAndLength() {
		byte[] inputBytes = "test".getBytes();
		String expected = new String(inputBytes, Charset.forName("US-ASCII"));

		String result = ArchiveUtils.toAsciiString(inputBytes);

		assertEquals(expected, result);
	}

	@Test
	public void testIsEqualWithIgnoreTrailingNulls() {
		byte[] buffer1 = "test".getBytes();
		byte[] buffer2 = "test00".getBytes();

		boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true);

		assertTrue(result);
	}

	@Test
	public void testIsEqualWithoutIgnoreTrailingNulls() {
		byte[] buffer1 = "test".getBytes();
		byte[] buffer2 = "test00".getBytes();

		boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);

		assertFalse(result);
	}

	@Test
	public void testIsEqualWithOffsetAndLength() {
		byte[] buffer1 = "test".getBytes();
		byte[] buffer2 = "test".getBytes();

		boolean result = ArchiveUtils.isEqual(buffer1, 0, 3, buffer2, 0, 3);

		assertTrue(result);
	}

	@Test
	public void testIsEqualWithoutOffsetAndLength() {
		byte[] buffer1 = "test".getBytes();
		byte[] buffer2 = "test".getBytes();

		boolean result = ArchiveUtils.isEqual(buffer1, buffer2);

		assertTrue(result);
	}

	@Test
	public void testIsEqualWithIgnoreTrailingNullsAndArrayZero() {
		byte[] buffer1 = "test".getBytes();
		byte[] buffer2 = "test00".getBytes();

		boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);

		assertTrue(result);
	}

	@Test
	public void testIsEqualWithArrayZero() {
		byte[] buffer1 = "test".getBytes();
		byte[] buffer2 = "test".getBytes();

		boolean result = ArchiveUtils.isEqual(buffer1, buffer2);

		assertTrue(result);
	}

	@Test
	public void testIsEqualWithOffsetAndLengthAndArrayZero() {
		byte[] buffer1 = "test".getBytes();
		byte[] buffer2 = "test".getBytes();

		boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, 3, buffer2, 0, 3);

		assertTrue(result);
	}

	@Test
	public void testIsArrayZero() {
		byte[] a = new byte[4];

		boolean result = ArchiveUtils.isArrayZero(a, 4);

		assertTrue(result);
	}

	@Test
	public void testSanitize() {
		String s = "test\n";

		String expected = "test?";
		String result = ArchiveUtils.sanitize(s);

		assertEquals(expected, result);
	}
}