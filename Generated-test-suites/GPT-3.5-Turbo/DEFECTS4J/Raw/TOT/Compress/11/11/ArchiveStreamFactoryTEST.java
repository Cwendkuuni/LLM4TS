import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

	@Test
	public void testCreateArchiveInputStream_ValidArchiverNameAndInputStream() {
		String archiverName = "zip";
		InputStream in = new ByteArrayInputStream(new byte[0]);

		try {
			ArchiveStreamFactory factory = new ArchiveStreamFactory();
			ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(archiverName, in);
			assertNotNull(archiveInputStream);
		} catch (ArchiveException e) {
			fail("ArchiveException should not be thrown");
		}
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCreateArchiveInputStream_NullArchiverName() throws ArchiveException {
		String archiverName = null;
		InputStream in = new ByteArrayInputStream(new byte[0]);

		ArchiveStreamFactory factory = new ArchiveStreamFactory();
		factory.createArchiveInputStream(archiverName, in);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCreateArchiveInputStream_NullInputStream() throws ArchiveException {
		String archiverName = "zip";
		InputStream in = null;

		ArchiveStreamFactory factory = new ArchiveStreamFactory();
		factory.createArchiveInputStream(archiverName, in);
	}

	@Test
	public void testCreateArchiveOutputStream_ValidArchiverNameAndOutputStream() {
		String archiverName = "zip";
		OutputStream out = new ByteArrayOutputStream();

		try {
			ArchiveStreamFactory factory = new ArchiveStreamFactory();
			ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(archiverName, out);
			assertNotNull(archiveOutputStream);
		} catch (ArchiveException e) {
			fail("ArchiveException should not be thrown");
		}
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCreateArchiveOutputStream_NullArchiverName() throws ArchiveException {
		String archiverName = null;
		OutputStream out = new ByteArrayOutputStream();

		ArchiveStreamFactory factory = new ArchiveStreamFactory();
		factory.createArchiveOutputStream(archiverName, out);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCreateArchiveOutputStream_NullOutputStream() throws ArchiveException {
		String archiverName = "zip";
		OutputStream out = null;

		ArchiveStreamFactory factory = new ArchiveStreamFactory();
		factory.createArchiveOutputStream(archiverName, out);
	}

	@Test
	public void testCreateArchiveInputStream_AutoDetect_ValidInputStream() {
		InputStream in = null;
		try {
			File file = new File("test.zip");
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(new byte[]{80, 75, 3, 4});
			fos.close();

			in = new FileInputStream(file);

			ArchiveStreamFactory factory = new ArchiveStreamFactory();
			ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(in);
			assertNotNull(archiveInputStream);
		} catch (Exception e) {
			fail("Exception should not be thrown");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					// Ignore
				}
			}
		}
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCreateArchiveInputStream_AutoDetect_NullInputStream() throws ArchiveException {
		InputStream in = null;

		ArchiveStreamFactory factory = new ArchiveStreamFactory();
		factory.createArchiveInputStream(in);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCreateArchiveInputStream_AutoDetect_NoMarkSupport() throws ArchiveException {
		InputStream in = new ByteArrayInputStream(new byte[0]);

		ArchiveStreamFactory factory = new ArchiveStreamFactory();
		factory.createArchiveInputStream(in);
	}

	@Test(expected=ArchiveException.class)
	public void testCreateArchiveInputStream_AutoDetect_UnknownArchiver() throws Exception {
		InputStream in = null;
		try {
			File file = new File("test");
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(new byte[12]);
			fos.close();

			in = new FileInputStream(file);

			ArchiveStreamFactory factory = new ArchiveStreamFactory();
			factory.createArchiveInputStream(in);

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					// Ignore
				}
			}
		}
	}

}