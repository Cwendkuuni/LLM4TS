import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

public class ArchiveStreamFactoryTest {

	@Test
	public void testCreateArchiveInputStream() {
		ArchiveStreamFactory factory = new ArchiveStreamFactory();
		try {
			InputStream in = new ByteArrayInputStream(new byte[10]);
			ArchiveInputStream archiveIn = factory.createArchiveInputStream(ArchiveStreamFactory.AR, in);
			assertTrue(archiveIn instanceof ArArchiveInputStream);
			
			in = new ByteArrayInputStream(new byte[10]);
			archiveIn = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
			assertTrue(archiveIn instanceof ZipArchiveInputStream);
			
			in = new ByteArrayInputStream(new byte[10]);
			archiveIn = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, in);
			assertTrue(archiveIn instanceof TarArchiveInputStream);
			
			in = new ByteArrayInputStream(new byte[10]);
			archiveIn = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, in);
			assertTrue(archiveIn instanceof JarArchiveInputStream);
			
			in = new ByteArrayInputStream(new byte[10]);
			archiveIn = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
			assertTrue(archiveIn instanceof CpioArchiveInputStream);
			
			in = new ByteArrayInputStream(new byte[10]);
			archiveIn = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
			assertTrue(archiveIn instanceof DumpArchiveInputStream);
		} catch (ArchiveException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testCreateArchiveOutputStream() {
		ArchiveStreamFactory factory = new ArchiveStreamFactory();
		try {
			OutputStream out = new ByteArrayOutputStream();
			ArchiveOutputStream archiveOut = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out);
			assertTrue(archiveOut instanceof ArArchiveOutputStream);
			
			out = new ByteArrayOutputStream();
			archiveOut = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
			assertTrue(archiveOut instanceof ZipArchiveOutputStream);
			
			out = new ByteArrayOutputStream();
			archiveOut = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
			assertTrue(archiveOut instanceof TarArchiveOutputStream);
			
			out = new ByteArrayOutputStream();
			archiveOut = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
			assertTrue(archiveOut instanceof JarArchiveOutputStream);
			
			out = new ByteArrayOutputStream();
			archiveOut = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
			assertTrue(archiveOut instanceof CpioArchiveOutputStream);
		} catch (ArchiveException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testCreateArchiveInputStream_autoDetect() {
		ArchiveStreamFactory factory = new ArchiveStreamFactory();
		try {
			InputStream in = new ByteArrayInputStream(new byte[10]);
			ArchiveInputStream archiveIn = factory.createArchiveInputStream(in);
			assertTrue(archiveIn instanceof ZipArchiveInputStream);
			
			in = new ByteArrayInputStream(new byte[10]);
			archiveIn = factory.createArchiveInputStream(in);
			assertTrue(archiveIn instanceof JarArchiveInputStream);
			
			in = new ByteArrayInputStream(new byte[10]);
			archiveIn = factory.createArchiveInputStream(in);
			assertTrue(archiveIn instanceof ArArchiveInputStream);
			
			in = new ByteArrayInputStream(new byte[10]);
			archiveIn = factory.createArchiveInputStream(in);
			assertTrue(archiveIn instanceof CpioArchiveInputStream);
			
			in = new ByteArrayInputStream(new byte[10]);
			archiveIn = factory.createArchiveInputStream(in);
			assertTrue(archiveIn instanceof DumpArchiveInputStream);
			
			in = new ByteArrayInputStream(new byte[10]);
			archiveIn = factory.createArchiveInputStream(in);
			assertTrue(archiveIn instanceof TarArchiveInputStream);
			
			in = new ByteArrayInputStream(new byte[512]);
			archiveIn = factory.createArchiveInputStream(in);
			assertTrue(archiveIn instanceof TarArchiveInputStream);
			
			in = new ByteArrayInputStream(new byte[10]);
			archiveIn = factory.createArchiveInputStream(in);
			assertNull(archiveIn);
		} catch (ArchiveException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

}