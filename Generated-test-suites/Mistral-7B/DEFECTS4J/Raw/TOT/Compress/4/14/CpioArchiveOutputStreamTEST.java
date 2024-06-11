import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test the {@link CpioArchiveOutputStream} class
 */
public class CpioArchiveOutputStreamTest {

    private File outputFile;
    private CpioArchiveOutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        outputFile = new File("test.cpio");
        outputStream = new CpioArchiveOutputStream(new FileOutputStream(outputFile));
    }

    @After
    public void tearDown() throws Exception {
        outputStream.close();
        outputFile.delete();
    }

    @Test
    public void testNewInstance() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(new FileOutputStream(new File("test.cpio")));
        assertNotNull(out);
    }

    @Test
    public void testNewInstanceWithFormat() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(new FileOutputStream(new File("test.cpio")), CpioConstants.FORMAT_NEW);
        assertSame(CpioConstants.FORMAT_NEW, out.getEntryFormat());
    }

    @Test
    public void testPutArchiveEntryWithInvalidEntry() throws Exception {
        assertThrows(ClassCastException.class, () -> outputStream.putArchiveEntry(null));
    }

    @Test
    public void testPutArchiveEntryWithDuplicateEntry() throws Exception {
        outputStream.putArchiveEntry(new CpioArchiveEntry("test.txt"));
        assertThrows(IllegalStateException.class, () -> outputStream.putArchiveEntry(new CpioArchiveEntry("test.txt")));
    }

    @Test
    public void testPutArchiveEntryWithInvalidFormat() throws Exception {
        outputStream = new CpioArchiveOutputStream(new FileOutputStream(new File("test.cpio")), (short) 99);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        assertThrows(IllegalArgumentException.class, () -> outputStream.putArchiveEntry(entry));
    }

    @Test
    public void testPutArchiveEntryWithValidEntry() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        outputStream.putArchiveEntry(entry);
        assertSame(entry, outputStream.getNextEntry());
        assertNull(outputStream.getNextEntry());
    }

    @Test
    public void testPutArchiveEntryWithValidEntryWithData() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        outputStream.putArchiveEntry(entry);

        ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
        byte[] data = "Hello World".getBytes();
        dataStream.write(data, 0, data.length);
        dataStream.close();

        outputStream.write(data, 0, data.length);
        outputStream.closeArchiveEntry();

        InputStream inputStream = new ByteArrayInputStream(data);
        InputStream testInput = outputStream.getInputStream();
        byte[] readData = new byte[data.length];
        testInput.read(readData);
        testInput.close();
        inputStream.close();

        assertArrayEquals(data, readData);
    }

    @Test
    public void testFinish() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        outputStream.putArchiveEntry(entry);
        outputStream.finish();

        InputStream inputStream = new FileInputStream(outputFile);
        byte[] testData = new byte[42];
        inputStream.read(testData);
        inputStream.close();

        Map expectedMap = new HashMap<>();
        expectedMap.put("test.txt", entry);
        Map actualMap = (Map) outputStream.getNames();

        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testClose() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        outputStream.putArchiveEntry(entry);
        outputStream.close();

        assertFalse(outputStream.isOpen());
    }
}