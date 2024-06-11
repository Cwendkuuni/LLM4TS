import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.StreamingNotSupportedException;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveOutputStream_ZIP() throws Exception {
        ArchiveOutputStream outputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, createOutputStream());
        Assert.assertNotNull(outputStream);
        outputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_TAR() throws Exception {
        ArchiveOutputStream outputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, createOutputStream());
        Assert.assertNotNull(outputStream);
        outputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_JAR() throws Exception {
        ArchiveOutputStream outputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, createOutputStream());
        Assert.assertNotNull(outputStream);
        outputStream.close();
    }

    @Test(expected = StreamingNotSupportedException.class)
    public void testCreateArchiveOutputStream_SEVEN_Z() throws Exception {
        new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, createOutputStream());
    }

    @Test
    public void testCreateArchiveInputStream_ZIP() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_TAR() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_InvalidFormat() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        new ArchiveStreamFactory().createArchiveInputStream("INVALID_FORMAT", inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_DetectZIP() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(ZipArchiveInputStream.magicFooter.concat(new byte[0][0]));
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        Assert.assertEquals(ArchiveStreamFactory.ZIP, archiveInputStream.getFormat());
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_DetectJAR() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(JarArchiveInputStream.magicBytes);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        Assert.assertEquals(ArchiveStreamFactory.JAR, archiveInputStream.getFormat());
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_DetectTAR() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(TarArchiveInputStream.magicBytes);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        Assert.assertEquals(ArchiveStreamFactory.TAR, archiveInputStream.getFormat());
        archiveInputStream.close();
    }

    private OutputStream createOutputStream() throws IOException {
        return new ByteArrayOutputStream(1024);
    }
}