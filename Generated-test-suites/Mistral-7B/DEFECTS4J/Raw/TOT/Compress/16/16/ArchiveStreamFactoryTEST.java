import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException, IOException {
        // Test creating an archive input stream from a known archive format and a byte array
        byte[] tarHeader = new byte[512];
        ArchiveInputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(tarHeader));
        TarArchiveEntry tarEntry = (TarArchiveEntry) inputStream.getNextEntry();
        assertEquals("testentry", tarEntry.getName());
        inputStream.close();

        // Test creating an archive input stream from a known archive format and an InputStream
        InputStream tarInputStream = new ByteArrayInputStream(tarHeader);
        inputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        TarArchiveEntry tarEntry2 = (TarArchiveEntry) inputStream.getNextEntry();
        assertEquals("testentry", tarEntry2.getName());
        inputStream.close();

        // Test creating an archive input stream from an unknown format
        byte[] unknownHeader = new byte[10];
        inputStream = new ArchiveStreamFactory().createArchiveInputStream(new ByteArrayInputStream(unknownHeader), "unknown");
        assertNull(inputStream.getNextEntry());
        inputStream.close();

        // Test creating an archive input stream from a null InputStream
        tarInputStream = null;
        assertThrows(IllegalArgumentException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream));

        // Test creating an archive input stream from an empty InputStream
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, emptyInputStream));

        // Test creating an archive input stream from an InputStream without mark support
        InputStream noMarkInputStream = new ByteArrayInputStream(tarHeader);
        assertThrows(IllegalArgumentException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, noMarkInputStream));

        // Test creating an archive input stream using the autodetection feature
        byte[] tarWithSignature = new byte[tarHeader.length + 512];
        System.arraycopy(tarHeader, 0, tarWithSignature, 0, tarHeader.length);
        TarArchiveEntry tarEntry3 = new TarArchiveEntry("testentry");
        tarEntry3.setSize(512);
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.putNextEntry(tarEntry3);
        tarOutputStream.finish();
        IOUtils.write(tarOutputStream.toByteArray(), 512, tarWithSignature, 0, tarHeader.length);
        InputStream autodetectStream = new ByteArrayInputStream(tarWithSignature);
        inputStream = new ArchiveStreamFactory().createArchiveInputStream(autodetectStream);
        TarArchiveEntry autodetectedEntry = (TarArchiveEntry) inputStream.getNextEntry();
        assertEquals("testentry", autodetectedEntry.getName());
        inputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException, IOException {
        // Test creating an archive output stream from a known archive format and an OutputStream
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream outputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
        outputStream.putArchiveEntry(new ZipArchiveEntry("testentry.txt"));
        outputStream.write(new byte[1024], 0, 1024);
        outputStream.closeArchiveEntry();
        outputStream.finish();
        byte[] zipContent = ((ByteArrayOutputStream) zipOutputStream).toByteArray();
        ZipFile zipFile = new ZipFile(new ByteArrayInputStream(zipContent));
        ZipArchiveEntry zipEntry = zipFile.getEntry("testentry.txt");
        assertEquals("testentry.txt", zipEntry.getName());
        zipFile.close();

        // Test creating an archive output stream from a known archive format and a null OutputStream
        OutputStream nullOutputStream = null;
        assertThrows(IllegalArgumentException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, nullOutputStream));

        // Test creating an archive output stream from an unknown archive format
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream("unknown", new ByteArrayOutputStream()));

        // Test creating an archive output stream using the autodetection feature
        OutputStream autodetectOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream autodetectedOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(autodetectOutputStream);
        autodetectedOutputStream.putArchiveEntry(new TarArchiveEntry("testentry.tar"));
        autodetectedOutputStream.finish();
        assertEquals("tar", autodetectedOutputStream.getFormat().getName());
        autodetectedOutputStream.close();
    }
}