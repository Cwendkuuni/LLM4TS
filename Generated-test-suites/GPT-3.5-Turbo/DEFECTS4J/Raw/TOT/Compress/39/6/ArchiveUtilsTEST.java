import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArchiveUtilsTest {

	@Before
	public void setUp() throws Exception {
		// Perform any necessary setup before each test method
	}

	@Test
	public void testToString() {
		ArchiveEntry entry = new ArchiveEntry();
		entry.setName("file.txt");
		entry.setDirectory(false);
		entry.setSize(1000);
		String expected = "-    1000 file.txt";
		String result = ArchiveUtils.toString(entry);
		assertEquals(expected, result);
	}

	@Test
	public void testMatchAsciiBuffer() {
		byte[] buffer = { 65, 66, 67, 68, 69 }; // ASCII values for A, B, C, D, E
		String expected = "ABCDE";
		boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
		assertTrue(result);
	}

	@Test
	public void testToAsciiBytes() {
		String inputString = "Hello World";
		byte[] expected = { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100 };
		byte[] result = ArchiveUtils.toAsciiBytes(inputString);
		assertArrayEquals(expected, result);
	}

	@Test
	public void testToAsciiString() {
		byte[] inputBytes = { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100 };
		String expected = "Hello World";
		String result = ArchiveUtils.toAsciiString(inputBytes);
		assertEquals(expected, result);
	}

	@Test
	public void testIsEqual() {
		byte[] buffer1 = { 1, 2, 3 };
		byte[] buffer2 = { 1, 2, 3 };
		assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
	}

	@Test
	public void testSanitize() {
		String s = "Hello\u0008World"; // Contains a backspace character
		String expected = "Hello?World";
		String result = ArchiveUtils.sanitize(s);
		assertEquals(expected, result);
	}

}