import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.StreamingNotSupportedException;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory archiveStreamFactory;
    private InputStream inputStream;
    private OutputStream outputStream;

    @Before
    public void setup() {
        archiveStreamFactory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream_AR() throws ArchiveException, IOException {
        InputStream arInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_UnknownFormat() throws ArchiveException, IOException {
        InputStream unknownFormatInputStream = new ByteArrayInputStream(new byte[0]);
        archiveStreamFactory.createArchiveInputStream("unknownFormat", unknownFormatInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_AR_Detected() throws ArchiveException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream os = new ByteArrayOutputStream(outputStream.size());
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, os);
        archiveOutputStream.putNextEntry(new org.apache.commons.compress.archivers.ar.ArArchiveEntry("test.txt", "Test Data.".getBytes()));
        archiveOutputStream.write("Test Data.".getBytes(), 0, "Test Data.".length());
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        InputStream arInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = StreamingNotSupportedException.class)
    public void testCreateArchiveOutputStream_SEVEN_Z() throws IOException {
        OutputStream sevenZOutputStream = new ByteArrayOutputStream();
        archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, sevenZOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_AR_Closed() throws ArchiveException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        archiveOutputStream.putNextEntry(new org.apache.commons.compress.archivers.ar.ArArchiveEntry("test.txt", "Test Data.".getBytes()));
        archiveOutputStream.write("Test Data.".getBytes(), 0, "Test Data.".length());
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        InputStream arInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
        archiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_Jar() throws ArchiveException, IOException {
        InputStream jarInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_UnsupportedFormat() throws ArchiveException, IOException {
        OutputStream unsupportedFormatOutputStream = new ByteArrayOutputStream();
        archiveStreamFactory.createArchiveOutputStream("unsupportedFormat", unsupportedFormatOutputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Tar() throws ArchiveException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream os = new ByteArrayOutputStream(outputStream.size());
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.TAR, os);
        archiveOutputStream.putNextEntry(new TarArchiveEntry("test.txt", "Test Data.".getBytes()));
        archiveOutputStream.write("Test Data.".getBytes(), 0, "Test Data.".length());
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        InputStream tarInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_NullInput() throws ArchiveException {
        archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_NullOutput() throws ArchiveException {
        archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }

    @Test
    public void testCreateArchiveInputStream_Zip() throws ArchiveException, IOException {
        InputStream zipInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_Cpio() throws ArchiveException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream os = new ByteArrayOutputStream(outputStream.size());
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, os);
        archiveOutputStream.putNextEntry(new CpioArchiveEntry("test.txt", "Test Data.".getBytes()));
        archiveOutputStream.write("Test Data.".getBytes(), 0, "Test Data.".length());
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        InputStream cpioInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_Zip_Closed() throws ArchiveException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        archiveOutputStream.putNextEntry(new ZipArchiveEntry("test.txt", "Test Data.".getBytes()));
        archiveOutputStream.write("Test Data.".getBytes(), 0, "Test Data.".length());
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        InputStream zipInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        archiveInputStream.close();
        archiveOutputStream.close();
    }
}