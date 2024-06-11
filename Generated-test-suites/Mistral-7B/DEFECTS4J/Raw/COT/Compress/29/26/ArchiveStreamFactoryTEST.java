import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.compress.archivers.ArchiveStreamFactory.AR;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.ARJ;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.CPIO;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.JAR;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.TAR;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.ZIP;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_Zip() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(JAR, inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_Tar() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(TAR, inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_UnsupportedFormat() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        new ArchiveStreamFactory().createArchiveInputStream("UnsupportedFormat", inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Ar() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(AR, inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = StreamingNotSupportedException.class)
    public void testCreateArchiveInputStream_SevenZ() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        new ArchiveStreamFactory().createArchiveInputStream(SEVEN_Z, inputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_Zip() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ZIP, outputStream);
        Assert.assertNotNull(archiveOutputStream);
        archiveOutputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_UnsupportedFormat() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream("UnsupportedFormat", outputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_Tar() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(TAR, outputStream);
        Assert.assertNotNull(archiveOutputStream);
        archiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_Jar() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(JAR, outputStream);
        Assert.assertNotNull(archiveOutputStream);
        archiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_Ar() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(AR, outputStream);
        Assert.assertNotNull(archiveOutputStream);
        archiveOutputStream.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullOutputStream() throws Exception {
        new ArchiveStreamFactory().createArchiveOutputStream(JAR, null);
    }

    @Test
    public void testCreateArchiveInputStream_Arj() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ARJ, inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_Cpio() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(CPIO, outputStream);
        Assert.assertNotNull(archiveOutputStream);
        archiveOutputStream.close();
    }

}