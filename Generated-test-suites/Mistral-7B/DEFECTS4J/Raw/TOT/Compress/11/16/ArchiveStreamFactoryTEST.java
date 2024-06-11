import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamFromNameAndInputStream() throws Exception {
        String archiverName = "zip";
        InputStream inputStream = new ByteArrayInputStream("Zip signature".getBytes());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
        assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromInvalidNameAndInputStream() throws Exception {
        String archiverName = "invalid";
        InputStream inputStream = new ByteArrayInputStream("Zip signature".getBytes());
        new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamFromNameAndOutputStream() throws Exception {
        String archiverName = "tar";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        assertTrue(archiveOutputStream instanceof TarArchiveOutputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamFromInvalidNameAndOutputStream() throws Exception {
        String archiverName = "invalid";
        OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("Zip signature".getBytes());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamFromInvalidInputStream() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("Invalid signature".getBytes());
        try {
            new ArchiveStreamFactory().createArchiveInputStream(inputStream);
            fail("ArchiveException expected");
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamAndWriteArchiveEntry() throws Exception {
        String archiverName = "jar";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        archiveOutputStream.putArchiveEntry(new JarArchiveEntry("test.txt"));
        archiveOutputStream.write(new byte[100], 0, 100);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        archiveOutputStream.close();
        byte[] bytes = outputStream.toByteArray();
        JarArchiveInputStream jarArchiveInputStream = new JarArchiveInputStream(new ByteArrayInputStream(bytes));
        JarArchiveEntry jarArchiveEntry = (JarArchiveEntry) jarArchiveInputStream.getNextEntry();
        assertEquals("test.txt", jarArchiveEntry.getName());
        jarArchiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamWithCorruptedSignature() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("Invalid signature".getBytes());
        try {
            new ArchiveStreamFactory().createArchiveInputStream(inputStream);
            fail("ArchiveException expected");
        } catch (ArchiveException e) {
            // expected
        }
    }
}