import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test for TarArchiveOutputStream class.
 */
public class TarArchiveOutputStreamTest {

    private static final String TEST_FILE_NAME = "testFile.txt";
    private static final String TEST_DIR_NAME = "testDir/";

    private TarArchiveOutputStream tarOut;
    private File testFile;

    @Before
    public void setUp() throws Exception {
        testFile = new File(TEST_DIR_NAME, TEST_FILE_NAME);
        tarOut = new TarArchiveOutputStream(new FileOutputStream(testFile));
    }

    @After
    public void tearDown() throws Exception {
        if (tarOut != null) {
            tarOut.finish();
            tarOut.close();
            testFile.delete();
        }
    }

    /**
     * Test for putArchiveEntry method with valid parameters.
     */
    @Test
    public void testPutArchiveEntry() throws Exception {
        TarArchiveEntry tarEntry = new TarArchiveEntry(new File("src/test/resources/testFile.txt"), "testFile.txt");
        tarOut.putArchiveEntry(tarEntry);
        tarOut.write("Test content".getBytes(StandardCharsets.UTF_8));
        tarOut.closeArchiveEntry();

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        TarArchiveInputStream tarIn = new TarArchiveInputStream(new FileInputStream(testFile));
        TarArchiveEntry entry = tarIn.getNextEntry();
        assertNotNull(entry);
        assertEquals("testFile.txt", entry.getName());

        byte[] content = new byte[entry.getSize()];
        tarIn.read(content, 0, content.length);
        assertEquals("Test content", new String(content, StandardCharsets.UTF_8));

        tarIn.close();
        IOUtils.closeQuietly(tarIn);
    }

    /**
     * Test for putArchiveEntry method with long file name.
     */
    @Test
    public void testPutArchiveEntryWithLongFileName() throws Exception {
        String longFileName = "testLongFileNameWithLengthGreaterThanNameLen.txt";
        TarArchiveEntry tarEntry = new TarArchiveEntry(new File("src/test/resources/" + longFileName), longFileName);
        tarOut.putArchiveEntry(tarEntry);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        TarArchiveInputStream tarIn = new TarArchiveInputStream(new FileInputStream(testFile));
        TarArchiveEntry entry = tarIn.getNextEntry();
        assertNotNull(entry);
        assertEquals(longFileName, entry.getName());
        tarIn.close();
        IOUtils.closeQuietly(tarIn);
    }

    /**
     * Test for putArchiveEntry method with directory.
     */
    @Test
    public void testPutArchiveEntryWithDirectory() throws Exception {
        File testDir = new File(TEST_DIR_NAME);
        TarArchiveEntry tarEntry = new TarArchiveEntry(testDir, "testDir/");
        tarOut.putArchiveEntry(tarEntry);
        tarOut.closeArchiveEntry();

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        TarArchiveInputStream tarIn = new TarArchiveInputStream(new FileInputStream(testFile));
        TarArchiveEntry entry = tarIn.getNextEntry();
        assertNotNull(entry);
        assertEquals("testDir/", entry.getName());
        tarIn.close();
        IOUtils.closeQuietly(tarIn);
    }

    /**
     * Test for putArchiveEntry method with null archiveEntry.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithNullArchiveEntry() throws Exception {
        tarOut.putArchiveEntry(null);
    }

    /**
     * Test for closeArchiveEntry method with no open entry.
     */
    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithNoOpenEntry() throws Exception {
        tarOut.closeArchiveEntry();
    }

    /**
     * Test for closeArchiveEntry method with open entry.
     */
    @Test
    public void testCloseArchiveEntryWithOpenEntry() throws Exception {
        TarArchiveEntry tarEntry = new TarArchiveEntry(new File("src/test/resources/testFile.txt"), "testFile.txt");
        tarOut.putArchiveEntry(tarEntry);
        tarOut.write("Test content".getBytes(StandardCharsets.UTF_8));
        tarOut.closeArchiveEntry();
        tarOut.finish();
        tarOut.close();

        ByteArrayInputStream byteStream = new ByteArrayInputStream("Test content".getBytes(StandardCharsets.UTF_8));
        TarArchiveInputStream tarIn = new TarArchiveInputStream(byteStream);
        TarArchiveEntry entry = tarIn.getNextEntry();
        assertNotNull(entry);
        assertEquals("testFile.txt", entry.getName());
        tarIn.close();
        IOUtils.closeQuietly(tarIn);
    }

    /**
     * Test for write method with valid parameters.
     */
    @Test
    public void testWrite() throws Exception {
        TarArchiveEntry tarEntry = new TarArchiveEntry(new File("src/test/resources/testFile.txt"), "testFile.txt");
        tarOut.putArchiveEntry(tarEntry);
        tarOut.write("Test content".getBytes(StandardCharsets.UTF_8));
        tarOut.closeArchiveEntry();

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        TarArchiveInputStream tarIn = new TarArchiveInputStream(new FileInputStream(testFile));
        TarArchiveEntry entry = tarIn.getNextEntry();
        assertNotNull(entry);
        assertEquals("testFile.txt", entry.getName());
        byte[] content = new byte[entry.getSize()];
        tarIn.read(content, 0, content.length);
        assertEquals("Test content", new String(content, StandardCharsets.UTF_8));

        tarIn.close();
        IOUtils.closeQuietly(tarIn);
    }

    /**
     * Test for write method with null input.
     */
    @Test(expected = NullPointerException.class)
    public void testWriteWithNullInput() throws Exception {
        TarArchiveEntry tarEntry = new TarArchiveEntry(new File("src/test/resources/testFile.txt"), "testFile.txt");
        tarOut.putArchiveEntry(tarEntry);
        tarOut.write(null);
    }

    /**
     * Test for write method with wrong input size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWriteWithWrongInputSize() throws Exception {
        TarArchiveEntry tarEntry = new TarArchiveEntry(new File("src/test/resources/testFile.txt"), "testFile.txt");
        tarOut.putArchiveEntry(tarEntry);
        byte[] input = new byte[10];
        tarOut.write(input, 11, 3);
    }

    /**
     * Test for write method with open entry.
     */
    @Test
    public void testWriteWithOpenEntry() throws Exception {
        TarArchiveEntry tarEntry = new TarArchiveEntry(new File("src/test/resources/testFile.txt"), "testFile.txt");
        tarOut.putArchiveEntry(tarEntry);
        tarOut.write("Test content".getBytes(StandardCharsets.UTF_8));
        tarOut.closeArchiveEntry();
        tarOut.write("Append content".getBytes(StandardCharsets.UTF_8));
        tarOut.finish();
        tarOut.close();

        ByteArrayInputStream byteStream = new ByteArrayInputStream("Test contentAppend content".getBytes(StandardCharsets.UTF_8));
        TarArchiveInputStream tarIn = new TarArchiveInputStream(byteStream);
        TarArchiveEntry entry = tarIn.getNextEntry();
        assertNotNull(entry);
        assertEquals("testFile.txt", entry.getName());
        byte[] content = new byte[entry.getSize()];
        tarIn.read(content, 0, content.length);
        assertEquals("Test contentAppend content", new String(content, StandardCharsets.UTF_8));

        tarIn.close();
        IOUtils.closeQuietly(tarIn);
    }

    /**
     * Test for finish method with no open entry.
     */
    @Test(expected = IOException.class)
    public void testFinishWithNoOpenEntry() throws Exception {
        tarOut.finish();
    }

    /**
     * Test for finish method with open entry.
     */
    @Test
    public void testFinishWithOpenEntry() throws Exception {
        TarArchiveEntry tarEntry = new TarArchiveEntry(new File("src/test/resources/testFile.txt"), "testFile.txt");
        tarOut.putArchiveEntry(tarEntry);
        tarOut.write("Test content".getBytes(StandardCharsets.UTF_8));
        tarOut.closeArchiveEntry();
        tarOut.finish();
        tarOut.close();

        ByteArrayInputStream byteStream = new ByteArrayInputStream("Test content".getBytes(StandardCharsets.UTF_8));
        TarArchiveInputStream tarIn = new TarArchiveInputStream(byteStream);
        TarArchiveEntry entry = tarIn.getNextEntry();
        assertNotNull(entry);
        assertEquals("testFile.txt", entry.getName());
        byte[] content = new byte[entry.getSize()];
        tarIn.read(content, 0, content.length);
        assertEquals("Test content", new String(content, StandardCharsets.UTF_8));

        tarIn.close();
        IOUtils.closeQuietly(tarIn);
    }

    /**
     * Test for setLongFileMode method.
     */
    @Test
    public void testSetLongFileMode() throws Exception {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        TarArchiveEntry tarEntry = new TarArchiveEntry(new File("src/test/resources/testFile.txt"), "testFile.txt");
        tarOut.putArchiveEntry(tarEntry);
        tarOut.write("Test content".getBytes(StandardCharsets.UTF_8));
        tarOut.closeArchiveEntry();
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        TarArchiveInputStream tarIn = new TarArchiveInputStream(new ByteArrayInputStream(tarOut.toByteArray()));
        assertNull(tarIn.getNextEntry());
        tarIn.close();
        IOUtils.closeQuietly(tarIn);
    }
}