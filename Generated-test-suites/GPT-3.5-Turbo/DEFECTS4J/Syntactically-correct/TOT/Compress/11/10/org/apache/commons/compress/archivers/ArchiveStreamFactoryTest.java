import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory archiveStreamFactory;
    private InputStream inputStream;

    @Before
    public void setUp() throws Exception {
        archiveStreamFactory = new ArchiveStreamFactory();
        inputStream = new ByteArrayInputStream(new byte[1024]);
    }

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream("zip", inputStream);
        assertNotNull("Archive input stream should not be null", archiveInputStream);
        assertTrue("Created archive input stream should be an instance of ZipArchiveInputStream",
                archiveInputStream instanceof ZipArchiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() throws ArchiveException {
        archiveStreamFactory.createArchiveInputStream(null, inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws ArchiveException {
        archiveStreamFactory.createArchiveInputStream("zip", null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownArchiverName() throws ArchiveException {
        archiveStreamFactory.createArchiveInputStream("unknown", inputStream);
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream("zip", outputStream);
        assertNotNull("Archive output stream should not be null", archiveOutputStream);
        assertTrue("Created archive output stream should be an instance of ZipArchiveOutputStream",
                archiveOutputStream instanceof ZipArchiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws ArchiveException {
        archiveStreamFactory.createArchiveOutputStream(null, outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws ArchiveException {
        archiveStreamFactory.createArchiveOutputStream("zip", null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnknownArchiverName() throws ArchiveException {
        archiveStreamFactory.createArchiveOutputStream("unknown", outputStream);
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetect() throws ArchiveException, IOException {
        byte[] zipSignature = new byte[]{0x50, 0x4b, 0x03, 0x04};
        InputStream zipInputStream = new ByteArrayInputStream(zipSignature);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(zipInputStream);
        assertNotNull("Archive input stream should not be null", archiveInputStream);
        assertTrue("Created archive input stream should be an instance of ZipArchiveInputStream",
                archiveInputStream instanceof ZipArchiveInputStream);

        byte[] tarSignature = new byte[]{0x75, 0x73, 0x74, 0x61, 0x72};
        InputStream tarInputStream = new ByteArrayInputStream(tarSignature);
        archiveInputStream = archiveStreamFactory.createArchiveInputStream(tarInputStream);
        assertNotNull("Archive input stream should not be null", archiveInputStream);
        assertTrue("Created archive input stream should be an instance of TarArchiveInputStream",
                archiveInputStream instanceof TarArchiveInputStream);

        byte[] jarSignature = new byte[]{0x50, 0x4b, 0x03, 0x04};
        InputStream jarInputStream = new ByteArrayInputStream(jarSignature);
        archiveInputStream = archiveStreamFactory.createArchiveInputStream(jarInputStream);
        assertNotNull("Archive input stream should not be null", archiveInputStream);
        assertTrue("Created archive input stream should be an instance of JarArchiveInputStream",
                archiveInputStream instanceof JarArchiveInputStream);

        byte[] arSignature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        InputStream arInputStream = new ByteArrayInputStream(arSignature);
        archiveInputStream = archiveStreamFactory.createArchiveInputStream(arInputStream);
        assertNotNull("Archive input stream should not be null", archiveInputStream);
        assertTrue("Created archive input stream should be an instance of ArArchiveInputStream",
                archiveInputStream instanceof ArArchiveInputStream);

        byte[] cpioSignature = new byte[]{0x30, 0x37, 0x30, 0x37, 0x30, 0x30};
        InputStream cpioInputStream = new ByteArrayInputStream(cpioSignature);
        archiveInputStream = archiveStreamFactory.createArchiveInputStream(cpioInputStream);
        assertNotNull("Archive input stream should not be null", archiveInputStream);
        assertTrue("Created archive input stream should be an instance of CpioArchiveInputStream",
                archiveInputStream instanceof CpioArchiveInputStream);

        byte[] dumpSignature = new byte[]{0x3f, 0x3f, 0x3f, 0x3f, 0x3f, 0x3f, 0x3f, 0x3f};
        InputStream dumpInputStream = new ByteArrayInputStream(dumpSignature);
        archiveInputStream = archiveStreamFactory.createArchiveInputStream(dumpInputStream);
        assertNotNull("Archive input stream should not be null", archiveInputStream);
        assertTrue("Created archive input stream should be an instance of DumpArchiveInputStream",
                archiveInputStream instanceof DumpArchiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamAutoDetectWithNullInputStream() throws ArchiveException {
        archiveStreamFactory.createArchiveInputStream(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamAutoDetectWithoutMarkSupport() throws ArchiveException {
        InputStream inputStream = new FileInputStream(new File("test.txt"));
        archiveStreamFactory.createArchiveInputStream(inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamAutoDetectWithUnknownSignature() throws ArchiveException, IOException {
        byte[] unknownSignature = new byte[]{0x55, 0x4e, 0x4b, 0x4e, 0x4f, 0x57, 0x4e};
        InputStream unknownInputStream = new ByteArrayInputStream(unknownSignature);
        archiveStreamFactory.createArchiveInputStream(unknownInputStream);
    }

}