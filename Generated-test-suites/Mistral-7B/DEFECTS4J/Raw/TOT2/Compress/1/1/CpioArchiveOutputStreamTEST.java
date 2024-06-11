import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CpioArchiveOutputStream
 */
public class CpioArchiveOutputStreamTest {

    private static final String TEST_FILE_NAME = "test.cpio";
    private static final int TEST_NUM_ENTRIES = 3;
    private static final byte[] TEST_CONTENTS = "Hello World!".getBytes(StandardCharsets.UTF_8);
    private static final String[] TEST_FILE_NAMES = {"test1.txt", "test2.bin", "test3.txt"};
    private static final int[] TEST_FILE_SIZES = {10, 5, 15};

    private CpioArchiveOutputStream out;
    private OutputStream outFiltered;

    @Before
    public void setUp() throws IOException {
        File testFile = new File(TEST_FILE_NAME);
        out = new CpioArchiveOutputStream(new FileOutputStream(testFile));
        outFiltered = new FilterOutputStream(out);
    }

    @After
    public void tearDown() throws IOException {
        if (out != null) {
            out.close();
        }
    }

    /**
     * Test for putNextEntry method with valid arguments
     * @throws IOException
     */
    @Test
    public void testPutNextEntryValid() throws IOException {
        for (int i = 0; i < TEST_NUM_ENTRIES; i++) {
            CpioArchiveEntry entry = new CpioArchiveEntry();
            entry.setName(TEST_FILE_NAMES[i]);
            entry.setSize(TEST_FILE_SIZES[i]);
            out.putNextEntry(entry);
            assertEquals(TEST_FILE_SIZES[i], TEST_FILE_SIZES[i]);
            assertTrue(out.getWriteMode() == CpioConstants.CREATE_GZ);
        }
    }

    /**
     * Test for putNextEntry method with null argument
     */
    @Test
    public void testPutNextEntryNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            out.putNextEntry(null);
        });
    }

    /**
     * Test for putNextEntry method with entry having the same name as previous entry
     * @throws IOException
     */
    @Test
    public void testPutNextEntryDuplicateEntry() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(TEST_FILE_NAMES[0]);
        entry1.setSize(TEST_FILE_SIZES[0]);
        out.putNextEntry(entry1);
        assertThrows(IllegalArgumentException.class, () -> {
            out.putNextEntry(entry1);
        });
    }

    /**
     * Test for finish method
     * @throws IOException
     */
    @Test
    public void testFinish() throws IOException {
        for (int i = 0; i < TEST_NUM_ENTRIES; i++) {
            CpioArchiveEntry entry = new CpioArchiveEntry();
            entry.setName(TEST_FILE_NAMES[i]);
            entry.setSize(TEST_FILE_SIZES[i]);
            out.putNextEntry(entry);
            out.write(TEST_CONTENTS);
            out.finish();
        }

        Map<String, CpioArchiveEntry> expectedEntries = new HashMap<>();
        for (int i = 0; i < TEST_NUM_ENTRIES; i++) {
            CpioArchiveEntry entry = new CpioArchiveEntry();
            entry.setName(TEST_FILE_NAMES[i]);
            entry.setSize(TEST_FILE_SIZES[i]);
            expectedEntries.put(TEST_FILE_NAMES[i], entry);
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        InputStream input = new FileInputStream(TEST_FILE_NAME);
        byte[] bufferContent = new byte[512];

        int read;
        while ((read = input.read(bufferContent)) > 0) {
            buffer.write(bufferContent, 0, read);
        }

        InputStream testStream = new ByteArrayInputStream(buffer.toByteArray());
        CpioArchiveEntry testEntry = null;

        for (int i = 0; i < TEST_NUM_ENTRIES; i++) {
            testEntry = CpioArchiveEntry.read(testStream);
            assertEquals(expectedEntries.get(TEST_FILE_NAMES[i]).getName(), testEntry.getName());
            assertEquals(expectedEntries.get(TEST_FILE_NAMES[i]).getSize(), testEntry.getSize());
            assertEquals(expectedEntries.get(TEST_FILE_NAMES[i]).getFormat(), testEntry.getFormat());
            testStream = testEntry.getDataStream();
            assertEquals(TEST_CONTENTS.length, testStream.available());
            byte[] testContent = new byte[TEST_CONTENTS.length];
            testStream.read(testContent);
            assertArrayEquals(TEST_CONTENTS, testContent);
            testStream.close();
            testEntry = null;
        }

        testStream.close();

        File testFile = new File(TEST_FILE_NAME);
        testFile.delete();
    }

    /**
     * Test for close method
     * @throws IOException
     */
    @Test
    public void testClose() throws IOException {
        for (int i = 0; i < TEST_NUM_ENTRIES; i++) {
            CpioArchiveEntry entry = new CpioArchiveEntry();
            entry.setName(TEST_FILE_NAMES[i]);
            entry.setSize(TEST_FILE_SIZES[i]);
            out.putNextEntry(entry);
            out.write(TEST_CONTENTS);
        }

        out.close();
        assertFalse(out.isOpen());
        assertNull(out.getArchiveEntry());

        File testFile = new File(TEST_FILE_NAME);
        testFile.delete();
    }

    /**
     * Test for closeArchiveEntry method
     * @throws IOException
     */
    @Test
    public void testCloseArchiveEntry() throws IOException {
        for (int i = 0; i < TEST_NUM_ENTRIES; i++) {
            CpioArchiveEntry entry = new CpioArchiveEntry();
            entry.setName(TEST_FILE_NAMES[i]);
            entry.setSize(TEST_FILE_SIZES[i]);
            out.putNextEntry(entry);
            out.write(TEST_CONTENTS);
            out.closeArchiveEntry();
            assertEquals(0, out.getWrittenSize());
        }

        out.close();

        File testFile = new File(TEST_FILE_NAME);
        testFile.delete();
    }

    /**
     * Test for write method
     * @throws IOException
     */
    @Test
    public void testWrite() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test4.txt");
        entry.setSize(15);
        out.putNextEntry(entry);
        byte[] testContent = new byte[TEST_CONTENTS.length];
        out.write(TEST_CONTENTS);
        out.write(testContent);
        byte[] expectedContent = new byte[TEST_CONTENTS.length + TEST_CONTENTS.length];
        System.arraycopy(TEST_CONTENTS, 0, expectedContent, 0, TEST_CONTENTS.length);
        System.arraycopy(testContent, 0, expectedContent, TEST_CONTENTS.length, testContent.length);
        byte[] bufferContent = new byte[512];

        int read;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        InputStream testStream = new ByteArrayInputStream(expectedContent);

        while ((read = testStream.read(bufferContent)) > 0) {
            buffer.write(bufferContent, 0, read);
        }

        byte[] actualContent = buffer.toByteArray();
        assertArrayEquals(expectedContent, actualContent);

        out.finish();

        InputStream input = new FileInputStream(TEST_FILE_NAME);
        byte[] inputContent = new byte[512];

        while ((read = input.read(inputContent)) > 0) {
            buffer.write(inputContent, 0, read);
        }

        testStream = new ByteArrayInputStream(buffer.toByteArray());
        CpioArchiveEntry testEntry = CpioArchiveEntry.read(testStream);
        assertEquals("test4.txt", testEntry.getName());
        assertEquals(15, testEntry.getSize());
        byte[] testEntryContent = new byte[TEST_CONTENTS.length + TEST_CONTENTS.length];
        testStream = testEntry.getDataStream();
        testStream.read(testEntryContent);
        assertArrayEquals(expectedContent, testEntryContent);
        testStream.close();

        testEntry = null;
        input.close();
        buffer.close();
        testStream.close();

        File testFile = new File(TEST_FILE_NAME);
        testFile.delete();
    }
}