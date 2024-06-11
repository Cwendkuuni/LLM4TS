import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.*;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;

    @Before
    public void setUp() {
        factory = new ArchiveStreamFactory();
    }

    @Test(expected = IllegalArgumentException.class)
    public void createArchiveInputStream_nullArchiverName() throws ArchiveException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(null, input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createArchiveInputStream_nullInputStream() throws ArchiveException {
        factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createArchiveOutputStream_nullArchiverName() throws ArchiveException {
        OutputStream output = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(null, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createArchiveOutputStream_nullOutputStream() throws ArchiveException {
        factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createArchiveInputStream_inputStreamNotSupportingMark() throws ArchiveException {
        InputStream input = new FileInputStream(new File("test.txt"));
        factory.createArchiveInputStream(input);
    }

    @Test
    public void createArchiveInputStream_AR() throws ArchiveException, IOException {
        ByteArrayOutputStream memOut = new ByteArrayOutputStream();
        OutputStream arOut = new ArArchiveOutputStream(memOut);

        ArchiveEntry entry = new ArArchiveEntry("testFile");
        ((ArArchiveOutputStream) arOut).putArchiveEntry(entry);
        arOut.write("testData".getBytes());
        arOut.closeArchiveEntry();
        arOut.close();

        InputStream input = new ByteArrayInputStream(memOut.toByteArray());
        ArchiveInputStream in = factory.createArchiveInputStream(ArchiveStreamFactory.AR, input);

        ArchiveEntry result = in.getNextEntry();
        assertNotNull(result);
        assertEquals("testFile", result.getName());

        ByteArrayOutputStream memIn = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int count;
        while ((count = in.read(buffer)) != -1) {
            memIn.write(buffer, 0, count);
        }
        in.close();

        assertEquals("testData", memIn.toString());
    }

    @Test
    public void createArchiveInputStream_ZIP() throws ArchiveException, IOException {
        ByteArrayOutputStream memOut = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(memOut);

        ZipEntry entry = new ZipEntry("testFile.txt");
        zipOut.putNextEntry(entry);
        zipOut.write("testData".getBytes());
        zipOut.closeEntry();
        zipOut.close();

        InputStream input = new ByteArrayInputStream(memOut.toByteArray());
        ArchiveInputStream in = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, input);

        ArchiveEntry result = in.getNextEntry();
        assertNotNull(result);
        assertEquals("testFile.txt", result.getName());

        ByteArrayOutputStream memIn = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int count;
        while ((count = in.read(buffer)) != -1) {
            memIn.write(buffer, 0, count);
        }
        in.close();

        assertEquals("testData", memIn.toString());
    }

    @Test
    public void createArchiveInputStream_TAR() throws ArchiveException, IOException {
        ByteArrayOutputStream memOut = new ByteArrayOutputStream();
        OutputStream tarOut = new TarArchiveOutputStream(memOut);

        ArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        ((TarArchiveOutputStream) tarOut).putArchiveEntry(entry);
        tarOut.write("testData".getBytes());
        tarOut.closeArchiveEntry();
        tarOut.close();

        InputStream input = new ByteArrayInputStream(memOut.toByteArray());
        ArchiveInputStream in = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, input);

        ArchiveEntry result = in.getNextEntry();
        assertNotNull(result);
        assertEquals("testFile.txt", result.getName());

        ByteArrayOutputStream memIn = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int count;
        while ((count = in.read(buffer)) != -1) {
            memIn.write(buffer, 0, count);
        }
        in.close();

        assertEquals("testData", memIn.toString());
    }

    @Test
    public void createArchiveInputStream_JAR() throws ArchiveException, IOException {
        ByteArrayOutputStream memOut = new ByteArrayOutputStream();
        OutputStream jarOut = new JarArchiveOutputStream(memOut);

        ArchiveEntry entry = new JarArchiveEntry("testFile.txt");
        ((JarArchiveOutputStream) jarOut).putArchiveEntry(entry);
        jarOut.write("testData".getBytes());
        jarOut.closeArchiveEntry();
        jarOut.close();

        InputStream input = new ByteArrayInputStream(memOut.toByteArray());
        ArchiveInputStream in = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, input);

        ArchiveEntry result = in.getNextEntry();
        assertNotNull(result);
        assertEquals("testFile.txt", result.getName());

        ByteArrayOutputStream memIn = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int count;
        while ((count = in.read(buffer)) != -1) {
            memIn.write(buffer, 0, count);
        }
        in.close();

        assertEquals("testData", memIn.toString());
    }

    @Test
    public void createArchiveInputStream_CPIO() throws ArchiveException, IOException {
        ByteArrayOutputStream memOut = new ByteArrayOutputStream();
        OutputStream cpioOut = new CpioArchiveOutputStream(memOut);

        ArchiveEntry entry = new CpioArchiveEntry("testFile.txt");
        ((CpioArchiveOutputStream) cpioOut).putArchiveEntry(entry);
        cpioOut.write("testData".getBytes());
        cpioOut.closeArchiveEntry();
        cpioOut.close();

        InputStream input = new ByteArrayInputStream(memOut.toByteArray());
        ArchiveInputStream in = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, input);

        ArchiveEntry result = in.getNextEntry();
        assertNotNull(result);
        assertEquals("testFile.txt", result.getName());

        ByteArrayOutputStream memIn = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int count;
        while ((count = in.read(buffer)) != -1) {
            memIn.write(buffer, 0, count);
        }
        in.close();

        assertEquals("testData", memIn.toString());
    }

    @Test(expected = ArchiveException.class)
    public void createArchiveInputStream_unknownArchiver() throws ArchiveException, IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream("unknown", input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createArchiveInputStream_nullStream() throws ArchiveException {
        factory.createArchiveInputStream(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createArchiveInputStream_streamNotSupportingMark() throws ArchiveException {
        InputStream input = new FileInputStream("test.txt");
        factory.createArchiveInputStream(input);
    }

    @Test(expected = ArchiveException.class)
    public void createArchiveInputStream_noArchiverFoundForSignature() throws ArchiveException, IOException {
        InputStream input = new ByteArrayInputStream(new byte[12]);
        factory.createArchiveInputStream(input);
    }

}