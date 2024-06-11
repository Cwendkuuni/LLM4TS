import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream out;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        byteArrayOutputStream = new ByteArrayOutputStream();
        out = new CpioArchiveOutputStream(new FileOutputStream(temporaryFolder.newFile("test.cpio")), CpioConstants.FORMAT_NEW);
        out.putArchiveEntry(new CpioArchiveEntry("test.txt", "test.txt"));
        out.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        out.closeArchiveEntry();
        out.finish();
        out.close();
    }

    @Test
    public void testPutArchiveEntryWithValidArgument() throws IOException {
        ArchiveEntry entry = new CpioArchiveEntry("test2.txt", "test2.txt");
        out = new CpioArchiveOutputStream(byteArrayOutputStream, CpioConstants.FORMAT_NEW);
        out.putArchiveEntry(entry);
        assertEquals(6, out.available());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithNullArgument() throws IOException {
        out = new CpioArchiveOutputStream(byteArrayOutputStream, CpioConstants.FORMAT_NEW);
        out.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateName() throws IOException {
        out = new CpioArchiveOutputStream(byteArrayOutputStream, CpioConstants.FORMAT_NEW);
        out.putArchiveEntry(new CpioArchiveEntry("test.txt", "test.txt"));
        out.putArchiveEntry(new CpioArchiveEntry("test.txt", "test.txt"));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithUnsupportedFormat() throws IOException {
        out = new CpioArchiveOutputStream(byteArrayOutputStream, (short) 0x01);
        ArchiveEntry entry = new CpioArchiveEntry("test.txt", "test.txt");
        out.putArchiveEntry(entry);
    }

    @Test
    public void testWriteWithValidArgument() throws IOException {
        out = new CpioArchiveOutputStream(byteArrayOutputStream, CpioConstants.FORMAT_NEW);
        byte[] testData = "Test Data".getBytes(StandardCharsets.UTF_8);
        out.putArchiveEntry(new CpioArchiveEntry("test2.txt", "test2.txt"));
        out.write(testData);
        out.closeArchiveEntry();
        byte[] resultData = out.toByteArray();
        assertEquals(testData.length + 1, resultData.length);
        assertEquals(testData, IOUtils.toByteArray(new File(temporaryFolder.getRoot(), "test2.cpio")));
    }

    @Test(expected = IOException.class)
    public void testWriteWithClosedStream() throws IOException {
        out = new CpioArchiveOutputStream(byteArrayOutputStream, CpioConstants.FORMAT_NEW);
        out.putArchiveEntry(new CpioArchiveEntry("test2.txt", "test2.txt"));
        out.finish();
        out.close();
        out.write("Hello World!".getBytes(StandardCharsets.UTF_8));
    }

    @Test(expected = IOException.class)
    public void testWriteWithNullByteArray() throws IOException {
        out = new CpioArchiveOutputStream(byteArrayOutputStream, CpioConstants.FORMAT_NEW);
        out.write((byte[]) null);
    }

    @Test(expected = IOException.class)
    public void testWriteWithOffsetOrLengthArgumentOutOfBound() throws IOException {
        out = new CpioArchiveOutputStream(byteArrayOutputStream, CpioConstants.FORMAT_NEW);
        byte[] testData = "Test Data".getBytes(StandardCharsets.UTF_8);
        out.putArchiveEntry(new CpioArchiveEntry("test2.txt", "test2.txt"));
        out.write(testData, 10, testData.length + 5);
    }

    @Test(expected = IOException.class)
    public void testWriteWithClosedEntry() throws IOException {
        out = new CpioArchiveOutputStream(byteArrayOutputStream, CpioConstants.FORMAT_NEW);
        byte[] testData = "Test Data".getBytes(StandardCharsets.UTF_8);
        out.putArchiveEntry(new CpioArchiveEntry("test2.txt", "test2.txt"));
        out.write(testData);
        out.closeArchiveEntry();
        out.write(testData);
    }

    @Test
    public void testCloseArchiveEntryWithValidArgument() throws IOException {
        out = new CpioArchiveOutputStream(byteArrayOutputStream, CpioConstants.FORMAT_NEW);
        byte[] testData = "Test Data".getBytes(StandardCharsets.UTF_8);
        out.putArchiveEntry(new CpioArchiveEntry("test2.txt", "test2.txt"));
        out.write(testData);
        out.closeArchiveEntry();
        assertNull(out.getNextEntry());
        assertTrue(out.available() == 0);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithClosedStream() throws IOException {
        out = new CpioArchiveOutputStream(byteArrayOutputStream, CpioConstants.FORMAT_NEW);
        out.finish();
        out.close();
        out.closeArchiveEntry();
    }

    @Test
    public void testFinishWithValidArgument() throws IOException {
        out = new CpioArchiveOutputStream(byteArrayOutputStream, CpioConstants.FORMAT_NEW);
        byte[] testData1 = "Test Data 1".getBytes(StandardCharsets.UTF_8);
        byte[] testData2 = "Test Data 2".getBytes(StandardCharsets.UTF_8);
        ArchiveEntry entry1 = new CpioArchiveEntry("test1.txt", "test1.txt");
        ArchiveEntry entry2 = new CpioArchiveEntry("test2.txt", "test2.txt");
        out.putArchiveEntry(entry1);
        out.write(testData1);
        out.putArchiveEntry(entry2);
        out.write(testData2);
        out.finish();
        byte[] resultData = out.toByteArray();
        assertEquals(testData1.length + testData2.length + 1, resultData.length);
        byte[] resultData1 = IOUtils.toByteArray(new File(temporaryFolder.getRoot(), "test1.cpio"));
        byte[] resultData2 = IOUtils.toByteArray(new File(temporaryFolder.getRoot(), "test2.cpio"));
        assertEquals(testData1.length + 1, resultData1.length);
        assertEquals(testData2.length + 1, resultData2.length);
        assertEquals(testData1, IOUtils.toByteArray(new File(temporaryFolder.getRoot(), "test1.cpio").getInputStream()));
        assertEquals(testData2, IOUtils.toByteArray(new File(temporaryFolder.getRoot(), "test2.cpio").getInputStream()));
    }

    @Test(expected = IOException.class)
    public void testFinishWithClosedStream() throws IOException {
        out = new CpioArchiveOutputStream(byteArrayOutputStream, CpioConstants.FORMAT_NEW);
        byte[] testData = "Test Data".getBytes(StandardCharsets.UTF_8);
        out.putArchiveEntry(new CpioArchiveEntry("test.txt", "test.txt"));
        out.write(testData);
        out.close();
        out.finish();
    }

    @Test
    public void testCloseWithValidArgument() throws IOException {
        out = new CpioArchiveOutputStream(byteArrayOutputStream, CpioConstants.FORMAT_NEW);
        out.finish();
        out.close();
        assertTrue(out.closed);
    }

    @Test(expected = IOException.class)
    public void testCloseWithOpenStream() throws IOException {
        out = new CpioArchiveOutputStream(byteArrayOutputStream, CpioConstants.FORMAT_NEW);
        out.close();
    }

    @Test
    public void testToByteArrayWithValidArgument() throws IOException {
        out = new CpioArchiveOutputStream(byteArrayOutputStream, CpioConstants.FORMAT_NEW);
        byte[] testData1 = "Test Data 1".getBytes(StandardCharsets.UTF_8);
        byte[] testData2 = "Test Data 2".getBytes(StandardCharsets.UTF_8);
        ArchiveEntry entry1 = new CpioArchiveEntry("test1.txt", "test1.txt");
        ArchiveEntry entry2 = new CpioArchiveEntry("test2.txt", "test2.txt");
        out.putArchiveEntry(entry1);
        out.write(testData1);
        out.putArchiveEntry(entry2);
        out.write(testData2);
        out.finish();
        byte[] resultData = out.toByteArray();
        assertEquals(testData1.length + testData2.length + 1, resultData.length);
    }

    @Test(expected = IOException.class)
    public void testToByteArrayWithClosedStream() throws IOException {
        out = new CpioArchiveOutputStream(byteArrayOutputStream, CpioConstants.FORMAT_NEW);
        out.finish();
        out.close();
        out.toByteArray();
    }
}