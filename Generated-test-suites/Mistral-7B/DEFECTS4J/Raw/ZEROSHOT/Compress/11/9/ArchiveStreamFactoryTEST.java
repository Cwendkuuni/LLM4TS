import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveOutputStream;
import org.junit.Test;
import java.io.*;

import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamAR() throws Exception {
        String archiverName = ArchiveStreamFactory.AR;
        InputStream inputStream = new ByteArrayInputStream(new byte[100]);
        ArchiveInputStream expected = new ArArchiveInputStream(inputStream);
        ArchiveInputStream actual = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);
        assertEquals(expected.getClass(), actual.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamUnknownArchiver() throws Exception {
        String archiverName = "unknown";
        InputStream inputStream = new ByteArrayInputStream(new byte[100]);
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(archiverName, inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNullArchiverName() throws Exception {
        String archiverName = null;
        InputStream inputStream = new ByteArrayInputStream(new byte[100]);
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(archiverName, inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNullInputStream() throws Exception {
        String archiverName = ArchiveStreamFactory.AR;
        InputStream inputStream = null;
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(archiverName, inputStream);
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectionAR() throws Exception {
        String archiverName = null;
        InputStream inputStream = new ByteArrayInputStream(new byte[12]);
        ArchiveInputStream expected = new ArArchiveInputStream(inputStream);
        ArchiveInputStream actual = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);
        assertEquals(expected.getClass(), actual.getClass());
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectionTAR() throws Exception {
        String archiverName = null;
        InputStream inputStream = new ByteArrayInputStream(new byte[512]);
        ArchiveInputStream expected = new TarArchiveInputStream(inputStream);
        ArchiveInputStream actual = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);
        assertEquals(expected.getClass(), actual.getClass());
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectionZIP() throws Exception {
        String archiverName = null;
        InputStream inputStream = new ByteArrayInputStream(new byte[12]);
        ArchiveInputStream expected = new ZipArchiveInputStream(inputStream);
        ArchiveInputStream actual = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);
        assertEquals(expected.getClass(), actual.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamAutoDetectionUnknown() throws Exception {
        String archiverName = null;
        InputStream inputStream = new ByteArrayInputStream(new byte[100]);
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(archiverName, inputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamAR() throws Exception {
        String archiverName = ArchiveStreamFactory.AR;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream expected = new ArArchiveOutputStream(outputStream);
        ArchiveOutputStream actual = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiverName, outputStream);
        assertEquals(expected.getClass(), actual.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamUnknownArchiver() throws Exception {
        String archiverName = "unknown";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(archiverName, outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamNullArchiverName() throws Exception {
        String archiverName = null;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(archiverName, outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamNullOutputStream() throws Exception {
        String archiverName = ArchiveStreamFactory.AR;
        OutputStream outputStream = null;
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(archiverName, outputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamAutoDetection() throws Exception {
        String archiverName = null;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream expected = new ArArchiveOutputStream(outputStream);
        ArchiveOutputStream actual = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiverName, outputStream);
        assertEquals(expected.getClass(), actual.getClass());
    }
}