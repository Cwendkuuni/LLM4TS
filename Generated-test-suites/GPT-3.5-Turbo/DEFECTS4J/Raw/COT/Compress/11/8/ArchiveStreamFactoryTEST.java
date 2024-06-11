
import static org.junit.Assert.*;

import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test for AR
        InputStream arStream = new ByteArrayInputStream(new byte[]{});
        ArchiveInputStream arInput = factory.createArchiveInputStream(ArchiveStreamFactory.AR, arStream);
        assertNotNull(arInput);

        // Test for ZIP
        InputStream zipStream = new ByteArrayInputStream(new byte[]{});
        ArchiveInputStream zipInput = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, zipStream);
        assertNotNull(zipInput);

        // Test for TAR
        InputStream tarStream = new ByteArrayInputStream(new byte[]{});
        ArchiveInputStream tarInput = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, tarStream);
        assertNotNull(tarInput);

        // Test for JAR
        InputStream jarStream = new ByteArrayInputStream(new byte[]{});
        ArchiveInputStream jarInput = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, jarStream);
        assertNotNull(jarInput);

        // Test for CPIO
        InputStream cpioStream = new ByteArrayInputStream(new byte[]{});
        ArchiveInputStream cpioInput = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioStream);
        assertNotNull(cpioInput);

        // Test for DUMP
        InputStream dumpStream = new ByteArrayInputStream(new byte[]{});
        ArchiveInputStream dumpInput = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpStream);
        assertNotNull(dumpInput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[]{});
        factory.createArchiveInputStream(null, in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[]{});
        factory.createArchiveInputStream("unknown", in);
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test for AR
        OutputStream arStream = new ByteArrayOutputStream();
        ArchiveOutputStream arOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, arStream);
        assertNotNull(arOutput);

        // Test for ZIP
        OutputStream zipStream = new ByteArrayOutputStream();
        ArchiveOutputStream zipOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipStream);
        assertNotNull(zipOutput);

        // Test for TAR
        OutputStream tarStream = new ByteArrayOutputStream();
        ArchiveOutputStream tarOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, tarStream);
        assertNotNull(tarOutput);

        // Test for JAR
        OutputStream jarStream = new ByteArrayOutputStream();
        ArchiveOutputStream jarOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, jarStream);
        assertNotNull(jarOutput);

        // Test for CPIO
        OutputStream cpioStream = new ByteArrayOutputStream();
        ArchiveOutputStream cpioOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioStream);
        assertNotNull(cpioOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(null, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnknownArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream("unknown", out);
    }

    @Test
    public void testCreateArchiveInputStreamAutodetect() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test for ZIP
        InputStream zipStream = new ByteArrayInputStream(new byte[]{80, 75, 3, 4});
        ArchiveInputStream zipInput = factory.createArchiveInputStream(zipStream);
        assertNotNull(zipInput);

        // Test for JAR
        InputStream jarStream = new ByteArrayInputStream(new byte[]{80, 75, 3, 4});
        ArchiveInputStream jarInput = factory.createArchiveInputStream(jarStream);
        assertNotNull(jarInput);

        // Test for AR
        InputStream arStream = new ByteArrayInputStream(new byte[]{33, 60, 97, 114});
        ArchiveInputStream arInput = factory.createArchiveInputStream(arStream);
        assertNotNull(arInput);

        // Test for CPIO
        InputStream cpioStream = new ByteArrayInputStream(new byte[]{49, 23, 84, 47});
        ArchiveInputStream cpioInput = factory.createArchiveInputStream(cpioStream);
        assertNotNull(cpioInput);

        // Test for DUMP
        InputStream dumpStream = new ByteArrayInputStream(new byte[]{65, 66, 67, 68});
        ArchiveInputStream dumpInput = factory.createArchiveInputStream(dumpStream);
        assertNotNull(dumpInput);

        // Test for TAR
        InputStream tarStream = new ByteArrayInputStream(new byte[]{0, 120, 0, 8});
        ArchiveInputStream tarInput = factory.createArchiveInputStream(tarStream);
        assertNotNull(tarInput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamAutodetectWithNullInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamAutodetectWithUnsupportedInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = mock(InputStream.class);
        factory.createArchiveInputStream(in);

        verify(in, times(1)).markSupported();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamAutodetectNoArchiverFound() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[]{});
        factory.createArchiveInputStream(in);
    }

}

