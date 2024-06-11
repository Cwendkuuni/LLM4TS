import static org.junit.Assert.*;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

	@Test
	public void testSetLongFileMode() {
		TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
		tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
		assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarArchiveOutputStream.getLongFileMode());
	}

	@Test
	public void testPutArchiveEntry() {
		TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
		TarArchiveEntry entry = new TarArchiveEntry("testEntry");
		try {
			tarArchiveOutputStream.putArchiveEntry(entry);
			tarArchiveOutputStream.write(new byte[10]);
			tarArchiveOutputStream.closeArchiveEntry();
		} catch (Exception e) {
			fail("Exception occurred: " + e.getMessage());
		}
	}

	@Test
	public void testCloseArchiveEntry() {
		TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
		TarArchiveEntry entry = new TarArchiveEntry("testEntry");
		try {
			tarArchiveOutputStream.putArchiveEntry(entry);
			tarArchiveOutputStream.write(new byte[10]);
			tarArchiveOutputStream.closeArchiveEntry();
		} catch (Exception e) {
			fail("Exception occurred: " + e.getMessage());
		}
	}

	@Test
	public void testWrite() {
		TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
		byte[] testBytes = new byte[5];
		try {
			tarArchiveOutputStream.write(testBytes, 0, testBytes.length);
		} catch (Exception e) {
			fail("Exception occurred: " + e.getMessage());
		}
	}

	@Test
	public void testFinish() {
		TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
		try {
			tarArchiveOutputStream.finish();
		} catch (Exception e) {
			fail("Exception occurred: " + e.getMessage());
		}
	}

	@Test
	public void testClose() {
		TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
		try {
			tarArchiveOutputStream.close();
		} catch (Exception e) {
			fail("Exception occurred: " + e.getMessage());
		}
	}

	@Test
	public void testCreateArchiveEntry() {
		TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
		File testFile = new File("testFile.txt");
		try {
			tarArchiveOutputStream.createArchiveEntry(testFile, "testEntry");
		} catch (Exception e) {
			fail("Exception occurred: " + e.getMessage());
		}
	}
}