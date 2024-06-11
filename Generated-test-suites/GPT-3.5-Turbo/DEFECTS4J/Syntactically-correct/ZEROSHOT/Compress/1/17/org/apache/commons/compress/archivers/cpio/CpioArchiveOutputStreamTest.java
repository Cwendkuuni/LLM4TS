import java.io.ByteArrayOutputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {

	private CpioArchiveOutputStream cpioArchiveOutputStream;

	@Before
	public void setup() {
		cpioArchiveOutputStream = new CpioArchiveOutputStream(new ByteArrayOutputStream());
	}

	@Test
	public void testConstructorWithFormat() {
		CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(new ByteArrayOutputStream(),
				CpioConstants.FORMAT_NEW);
		Assert.assertNotNull(cpioArchiveOutputStream);
	}

	@Test
	public void testConstructorWithoutFormat() {
		CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(new ByteArrayOutputStream());
		Assert.assertNotNull(cpioArchiveOutputStream);
	}

	@Test
	public void testEnsureOpen() {
		try {
			cpioArchiveOutputStream.ensureOpen();
		} catch (Exception e) {
			Assert.fail("Exception occurred: " + e.getMessage());
		}
	}

	@Test
	public void testSetFormat() {
		cpioArchiveOutputStream.setFormat(CpioConstants.FORMAT_NEW);
	}

	@Test
	public void testPutNextEntry() {
		try {
			CpioArchiveEntry entry = new CpioArchiveEntry();
			entry.setName("testfile");
			String contents = "12345";
			entry.setFileSize(contents.length());
			cpioArchiveOutputStream.putNextEntry(entry);
		} catch (Exception e) {
			Assert.fail("Exception occurred: " + e.getMessage());
		}
	}

	@Test
	public void testCloseArchiveEntry() {
		try {
			CpioArchiveEntry entry = new CpioArchiveEntry();
			entry.setName("testfile");
			String contents = "12345";
			entry.setFileSize(contents.length());
			cpioArchiveOutputStream.putNextEntry(entry);
			cpioArchiveOutputStream.closeArchiveEntry();
		} catch (Exception e) {
			Assert.fail("Exception occurred: " + e.getMessage());
		}
	}

	@Test
	public void testWriteByteArr() {
		try {
			byte[] content = "test content".getBytes();
			cpioArchiveOutputStream.write(content);
		} catch (Exception e) {
			Assert.fail("Exception occurred: " + e.getMessage());
		}
	}

	@Test
	public void testFinish() {
		try {
			cpioArchiveOutputStream.finish();
		} catch (Exception e) {
			Assert.fail("Exception occurred: " + e.getMessage());
		}
	}

	@Test
	public void testClose() {
		try {
			cpioArchiveOutputStream.close();
		} catch (Exception e) {
			Assert.fail("Exception occurred: " + e.getMessage());
		}
	}
}