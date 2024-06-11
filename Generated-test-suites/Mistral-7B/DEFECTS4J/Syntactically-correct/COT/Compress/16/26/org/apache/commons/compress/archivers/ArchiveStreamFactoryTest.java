import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.junit.ArchiveContent;
import org.apache.commons.compress.archivers.junit.ArchiveTestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.apache.commons.compress.archivers.junit.ArchiveIOUtils.copy;

/**
 * Test class for ArchiveStreamFactory.
 */
public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_Ar() throws Exception {
        String archiverName = ArchiveStreamFactory.AR;
        InputStream inputStream = new ByteArrayInputStream( ArchiveTestUtils.AR_DATA );
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);
        ArchiveContent archiveContent = ArchiveTestUtils.assertInputStreamContents(ArchiveTestUtils.AR_CONTENTS, archiveInputStream);
        archiveInputStream.close();
        Assert.assertEquals(ArchiveTestUtils.AR_CONTENTS, archiveContent.getContents());
    }

    @Test
    public void testCreateArchiveInputStream_Ar_NullInput() throws Exception {
        String archiverName = ArchiveStreamFactory.AR;
        ArchiveException exception = Assert.assertThrows(ArchiveException.class, () -> ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, null));
        Assert.assertEquals("InputStream must not be null.", exception.getMessage());
    }

    @Test
    public void testCreateArchiveInputStream_Ar_UnknownFormat() throws Exception {
        String archiverName = "unknownFormat";
        InputStream inputStream = new ByteArrayInputStream(ArchiveTestUtils.AR_DATA);
        ArchiveException exception = Assert.assertThrows(ArchiveException.class, () -> ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream));
        Assert.assertEquals("Archiver: unknownFormat not found.", exception.getMessage());
    }

    @Test
    public void testCreateArchiveInputStream_Jar() throws Exception {
        String archiverName = ArchiveStreamFactory.JAR;
        InputStream inputStream = new ByteArrayInputStream( ArchiveTestUtils.JAR_DATA );
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);
        ArchiveContent archiveContent = ArchiveTestUtils.assertInputStreamContents(ArchiveTestUtils.JAR_MANIFEST_CONTENTS, archiveInputStream);
        archiveInputStream.close();
        Assert.assertEquals(ArchiveTestUtils.JAR_MANIFEST_CONTENTS, archiveContent.getManifest());
        archiveContent = ArchiveTestUtils.assertInputStreamContents(ArchiveTestUtils.JAR_ENTRY_CONTENTS, archiveInputStream);
        archiveInputStream.close();
        Assert.assertEquals(ArchiveTestUtils.JAR_ENTRY_CONTENTS, archiveContent.getContents());
    }

    @Test
    public void testCreateArchiveInputStream_Jar_NullInput() throws Exception {
        String archiverName = ArchiveStreamFactory.JAR;
        ArchiveException exception = Assert.assertThrows(ArchiveException.class, () -> ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, null));
        Assert.assertEquals("InputStream must not be null.", exception.getMessage());
    }

    @Test
    public void testCreateArchiveInputStream_Zip() throws Exception {
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream inputStream = new ByteArrayInputStream( ArchiveTestUtils.ZIP_DATA );
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);
        ArchiveContent archiveContent = ArchiveTestUtils.assertInputStreamContents(ArchiveTestUtils.ZIP_CONTENTS, archiveInputStream);
        archiveInputStream.close();
        Assert.assertEquals(ArchiveTestUtils.ZIP_CONTENTS, archiveContent.getContents());
    }

    @Test
    public void testCreateArchiveInputStream_Zip_NullInput() throws Exception {
        String archiverName = ArchiveStreamFactory.ZIP;
        ArchiveException exception = Assert.assertThrows(ArchiveException.class, () -> ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, null));
        Assert.assertEquals("InputStream must not be null.", exception.getMessage());
    }

    @Test
    public void testCreateArchiveInputStream_UnknownFormat() throws Exception {
        String archiverName = "unknownFormat";
        InputStream inputStream = new ByteArrayInputStream(ArchiveTestUtils.AR_DATA);
        ArchiveException exception = Assert.assertThrows(ArchiveException.class, () -> ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream));
        Assert.assertEquals("Archiver: unknownFormat not found.", exception.getMessage());
    }

    @Test
    public void testCreateArchiveOutputStream_Ar() throws IOException {
        String archiverName = ArchiveStreamFactory.AR;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiverName, outputStream);
        archiveOutputStream.putArchiveEntry(new MyArchiveEntry("test.txt"));
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        copy(inputStream, archiveOutputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();
        Assert.assertEquals("test data", new String( ((ByteArrayOutputStream)outputStream).toByteArray() ));
    }

    @Test
    public void testCreateArchiveOutputStream_Ar_NullOutput() throws IOException {
        String archiverName = ArchiveStreamFactory.AR;
        ArchiveException exception = Assert.assertThrows(ArchiveException.class, () -> ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiverName, null));
        Assert.assertEquals("OutputStream must not be null.", exception.getMessage());
    }

    @Test
    public void testCreateArchiveOutputStream_UnknownFormat() throws IOException {
        String archiverName = "unknownFormat";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveException exception = Assert.assertThrows(ArchiveException.class, () -> ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiverName, outputStream));
        Assert.assertEquals("Archiver: unknownFormat not found.", exception.getMessage());
    }

    @Test
    public void testCreateArchiveOutputStream_Jar() throws IOException {
        String archiverName = ArchiveStreamFactory.JAR;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiverName, outputStream);
        archiveOutputStream.putNextEntry(new JarArchiveEntry("test.txt", "test data".getBytes()));
        archiveOutputStream.finish();
        archiveOutputStream.close();
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream( ((ByteArrayOutputStream)outputStream).toByteArray() ));
        ArchiveContent archiveContent = ArchiveTestUtils.assertInputStreamContents(ArchiveTestUtils.JAR_ENTRY_CONTENTS, archiveInputStream);
        archiveInputStream.close();
        Assert.assertEquals(ArchiveTestUtils.JAR_ENTRY_CONTENTS, archiveContent.getContents());
    }

    @Test
    public void testCreateArchiveOutputStream_Jar_NullOutput() throws IOException {
        String archiverName = ArchiveStreamFactory.JAR;
        ArchiveException exception = Assert.assertThrows(ArchiveException.class, () -> ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiverName, null));
        Assert.assertEquals("OutputStream must not be null.", exception.getMessage());
    }

    @Test
    public void testCreateArchiveOutputStream_Tar() throws IOException {
        String archiverName = ArchiveStreamFactory.TAR;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiverName, outputStream);
        archiveOutputStream.putNextEntry(new TarArchiveEntry("test.txt", "test data".getBytes()));
        archiveOutputStream.finish();
        archiveOutputStream.close();
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream( ((ByteArrayOutputStream)outputStream).toByteArray() ));
        ArchiveContent archiveContent = ArchiveTestUtils.assertInputStreamContents(ArchiveTestUtils.TAR_ENTRY_CONTENTS, archiveInputStream);
        archiveInputStream.close();
        Assert.assertEquals(ArchiveTestUtils.TAR_ENTRY_CONTENTS, archiveContent.getContents());
    }

    @Test
    public void testCreateArchiveOutputStream_Tar_NullOutput() throws IOException {
        String archiverName = ArchiveStreamFactory.TAR;
        ArchiveException exception = Assert.assertThrows(ArchiveException.class, () -> ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiverName, null));
        Assert.assertEquals("OutputStream must not be null.", exception.getMessage());
    }

    @Test
    public void testCreateArchiveOutputStream_Zip() throws IOException {
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiverName, outputStream);
        archiveOutputStream.putNextEntry(new ZipArchiveEntry("test.txt", "test data".getBytes()));
        archiveOutputStream.finish();
        archiveOutputStream.close();
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream( ((ByteArrayOutputStream)outputStream).toByteArray() ));
        ArchiveContent archiveContent = ArchiveTestUtils.assertInputStreamContents(ArchiveTestUtils.ZIP_ENTRY_CONTENTS, archiveInputStream);
        archiveInputStream.close();
        Assert.assertEquals(ArchiveTestUtils.ZIP_ENTRY_CONTENTS, archiveContent.getContents());
    }

    @Test
    public void testCreateArchiveOutputStream_Zip_NullOutput() throws IOException {
        String archiverName = ArchiveStreamFactory.ZIP;
        ArchiveException exception = Assert.assertThrows(ArchiveException.class, () -> ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiverName, null));
        Assert.assertEquals("OutputStream must not be null.", exception.getMessage());
    }

    static class MyArchiveEntry extends TarArchiveEntry {
        MyArchiveEntry(String name) {
            super(name, null, null, 0, 0, null);
        }
    }

    static class JarArchiveEntry extends JarArchiveEntry {
        JarArchiveEntry(String name, byte[] data) {
            super(name, data, null);
        }
    }
}