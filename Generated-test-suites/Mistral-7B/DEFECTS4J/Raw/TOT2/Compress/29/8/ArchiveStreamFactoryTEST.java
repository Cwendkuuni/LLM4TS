import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.*;

import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;
    private ArchiveOutputStream out;

    @Before
    public void setUp() {
        factory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveOutputStream_AR() {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream os = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, output);
        assertEquals("ar", os.getClass().getSimpleName());
        os.finish();
        assertNotNull(os.getNextEntry());
        os.close();
    }

    @Test
    public void testCreateArchiveOutputStream_ARJ() {
        String encoding = "UTF-8";
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream os = factory.createArchiveOutputStream(ArchiveStreamFactory.ARJ, output, encoding);
        assertEquals("arj", os.getClass().getSimpleName());
        os.finish();
        assertNotNull(os.getNextEntry());
        os.close();
    }

    @Test(expected = StreamingNotSupportedException.class)
    public void testCreateArchiveOutputStream_SEVEN_Z() {
        OutputStream output = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, output);
    }

    @Test
    public void testCreateArchiveInputStream_AR() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ArchiveStreamFactory.ArArchiveOutputStream arOut = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, bos);
        arOut.putArchiveEntry(new ArchiveEntry("test.txt"));
        arOut.write("Test".getBytes(), 0, "Test".length());
        arOut.finish();
        arOut.close();

        byte[] data = bos.toByteArray();
        InputStream is = new ByteArrayInputStream(data);
        ArchiveInputStream in = factory.createArchiveInputStream(ArchiveStreamFactory.AR, is);
        ArchiveEntry entry = in.getNextEntry();
        assertNotNull(entry);
        assertEquals("test.txt", entry.getName());
        InputStream testInputStream = in.getInputStream();
        byte[] buffer = new byte[10];
        assertTrue(testInputStream.read(buffer, 0, buffer.length) > 0);
        assertEquals("Test", new String(buffer, 0, buffer.length));
        in.close();
    }

    @Test(expected = StreamingNotSupportedException.class)
    public void testCreateArchiveInputStream_SEVEN_Z() throws Exception {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, is);
    }

    @Test
    public void testCreateArchiveInputStream_autodetect() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ArchiveStreamFactory.ZipArchiveOutputStream zipOut = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, bos);
        zipOut.putNextEntry(new ZipEntry("test.txt"));
        zipOut.write("Test".getBytes(), 0, "Test".length());
        zipOut.finish();
        zipOut.close();

        byte[] data = bos.toByteArray();
        InputStream is = new ByteArrayInputStream(data);
        ArchiveInputStream in = factory.createArchiveInputStream(null, is);
        ArchiveEntry entry = in.getNextEntry();
        assertNotNull(entry);
        assertEquals("test.txt", entry.getName());
        InputStream testInputStream = in.getInputStream();
        byte[] buffer = new byte[10];
        assertTrue(testInputStream.read(buffer, 0, buffer.length) > 0);
        assertEquals("Test", new String(buffer, 0, buffer.length));
        in.close();
    }

    @Test
    public void testSetEntryEncoding() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory("UTF-8");
        assertEquals("UTF-8", factory.getEntryEncoding());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetEntryEncoding_OnceSetInContructor() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.setEntryEncoding("UTF-8");
        try {
            factory.setEntryEncoding("UTF-16");
        } finally {
            Mockito.verify(factory, Mockito.times(1)).setEncoding(Mockito.eq("UTF-8"));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullOutputStream() {
        factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArchiverName() {
        factory.createArchiveOutputStream(null, new ByteArrayOutputStream());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStream() {
        factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
    }

    @Test
    public void testCreateArchiveInputStream_EmptyInputStream() {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream in = factory.createArchiveInputStream(ArchiveStreamFactory.AR, is);
        assertNull(in.getNextEntry());
        in.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_UnknownArchiver() {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream("unknown", is);
    }

    @Test
    public void testCreateArchiveOutputStream_Zip() {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream os = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, output);
        assertEquals("ZipArchiveOutputStream", os.getClass().getSimpleName());
        os.finish();
        assertNotNull(os.getNextEntry());
        os.close();
    }

    @Test
    public void testCreateArchiveOutputStream_Tar() {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream os = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, output);
        assertEquals("TarArchiveOutputStream", os.getClass().getSimpleName());
        os.finish();
        assertNotNull(os.getNextEntry());
        os.close();
    }

    @Test
    public void testCreateArchiveOutputStream_Jar() {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream os = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, output);
        assertEquals("JarArchiveOutputStream", os.getClass().getSimpleName());
        os.finish();
        assertNotNull(os.getNextEntry());
        os.close();
    }

    @Test
    public void testCreateArchiveOutputStream_Cpio() {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream os = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, output);
        assertEquals("CpioArchiveOutputStream", os.getClass().getSimpleName());
        os.finish();
        assertNotNull(os.getNextEntry());
        os.close();
    }

    @Test
    public void testCreateArchiveOutputStream_Arj() {
        String encoding = "UTF-8";
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream os = factory.createArchiveOutputStream(ArchiveStreamFactory.ARJ, output, encoding);
        assertEquals("ArjArchiveOutputStream", os.getClass().getSimpleName());
        os.finish();
        assertNotNull(os.getNextEntry());
        os.close();
    }

    @Test
    public void testCreateArchiveOutputStream_Dump() {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream os = factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, output);
        assertEquals("DumpArchiveOutputStream", os.getClass().getSimpleName());
        os.finish();
        assertNotNull(os.getNextEntry());
        os.close();
    }

    @Test
    public void testCreateArchiveOutputStream_SevenZ() {
        OutputStream output = new ByteArrayOutputStream();
        ArgumentCaptor<OutputStream> outputCaptor = ArgumentCaptor.forClass(OutputStream.class);
        SevenZFile sevenZFile = Mockito.mock(SevenZFile.class);
        Mockito.when(sevenZFile.createOutput(outputCaptor.capture())).thenReturn(sevenZFile);
        ArchiveOutputStream os = factory.createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, output);
        assertEquals("SevenZFile", os.getClass().getSimpleName());
        assertEquals(output, outputCaptor.getValue());
        os.finish();
        assertNotNull(os.getNextEntry());
        os.close();
    }

    @Test
    public void testCreateArchiveOutputStream_AR_NullOutputStream() {
        try {
            factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream_AR_NullArchiverName() {
        try {
            factory.createArchiveOutputStream(null, new ByteArrayOutputStream());
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStream_AR_NullInputStream() {
        try {
            factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}