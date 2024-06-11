import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveOutputStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamAr() throws Exception {
        String archiverName = ArchiveStreamFactory.AR;
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);
        assertEquals(ArchiveInputStream.class, archiveInputStream.getClass());
        assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamZip() throws Exception {
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);
        assertEquals(ArchiveInputStream.class, archiveInputStream.getClass());
        assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamUnknownFormat() throws Exception {
        String archiverName = "unknown";
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveStreamFactory factory = ArchiveStreamFactory.newInstance();
        factory.createArchiveInputStream(archiverName, inputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamAr() throws Exception {
        String archiverName = ArchiveStreamFactory.AR;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiverName, outputStream);
        assertEquals(ArchiveOutputStream.class, archiveOutputStream.getClass());
        assertInstanceOf(ArArchiveOutputStream.class, archiveOutputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStreamZip() throws Exception {
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiverName, outputStream);
        assertEquals(ArchiveOutputStream.class, archiveOutputStream.getClass());
        assertInstanceOf(ZipArchiveOutputStream.class, archiveOutputStream);
        archiveOutputStream.putNextEntry(new JarArchiveEntry("test.txt"));
        archiveOutputStream.write("Test content".getBytes(), 0, "Test content".length());
        archiveOutputStream.finish();
        archiveOutputStream.close();
        assertArrayEquals("Test content".getBytes(), ((ByteArrayOutputStream) outputStream).toByteArray());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamUnknownFormat() throws Exception {
        String archiverName = "unknown";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveStreamFactory factory = ArchiveStreamFactory.newInstance();
        factory.createArchiveOutputStream(archiverName, outputStream);
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectAr() throws Exception {
        String archiverName = null;
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        inputStream.mark(1024);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);
        assertEquals(ArchiveInputStream.class, archiveInputStream.getClass());
        assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectZip() throws Exception {
        String archiverName = null;
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        inputStream.mark(1024);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);
        assertEquals(ArchiveInputStream.class, archiveInputStream.getClass());
        assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamAutoDetectUnknownFormat() throws Exception {
        String archiverName = null;
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveStreamFactory factory = ArchiveStreamFactory.newInstance();
        factory.createArchiveInputStream(archiverName, inputStream);
    }
}