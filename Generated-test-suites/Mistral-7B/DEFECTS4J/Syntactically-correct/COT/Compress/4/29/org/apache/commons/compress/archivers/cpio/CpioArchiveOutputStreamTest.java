import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;

public class CpioArchiveOutputStreamTest {

    @org.junit.Test
    public void testCpioArchiveOutputStream_Create() {
        OutputStream outputStream = new FileOutputStream(new File("test.cpio"));
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        assertEquals(CpioConstants.FORMAT_NEW, cpioArchiveOutputStream.getFormat());
    }

    @org.junit.Test
    public void testPutArchiveEntry_ValidEntry() throws IOException {
        byte[] testData = "Test Data".getBytes();
        OutputStream outputStream = new FileOutputStream(new File("test.cpio"));
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        ArchiveEntry entry = new CpioArchiveEntry("testfile.txt", "test/data.txt");
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write("Test Data".getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.close();

        File testFile = new File("test.cpio");
        ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();
        CpioArchiveOutputStream expectedOutputStream = new CpioArchiveOutputStream(expectedOutput, CpioConstants.FORMAT_NEW);
        expectedOutputStream.putArchiveEntry(new CpioArchiveEntry("testfile.txt", "test/data.txt"));
        expectedOutputStream.write("Test Data".getBytes());
        expectedOutputStream.closeArchiveEntry();
        expectedOutputStream.finish();
        expectedOutputStream.close();

        File testFileActual = new File("test.cpio");
        ByteArrayInputStream actualInput = new ByteArrayInputStream(fileToByteArray(testFileActual));
        CpioArchiveInputStream cpioArchiveInputStream = new CpioArchiveInputStream(actualInput);
        ArchiveEntry nextEntry = cpioArchiveInputStream.getNextEntry();
        byte[] actualData = new byte[nextEntry.getSize()];
        nextEntry.getInputStream().read(actualData);

        assertEquals(expectedOutput.toByteArray(), fileToByteArray(testFileActual));
        assertEquals(testData, actualData);
    }

    @org.junit.Test
    public void testPutArchiveEntry_DuplicateEntry() throws IOException {
        OutputStream outputStream = new FileOutputStream(new File("test.cpio"));
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        ArchiveEntry entry = new CpioArchiveEntry("testfile.txt", "test/data.txt");
        cpioArchiveOutputStream.putArchiveEntry(entry);

        assertThrows(IOException.class, () -> {
            cpioArchiveOutputStream.putArchiveEntry(entry);
        });
    }

    @org.junit.Test
    public void testPutArchiveEntry_InvalidFormat() throws IOException {
        OutputStream outputStream = new FileOutputStream(new File("test.cpio"));
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, (short) 123);

        ArchiveEntry entry = new CpioArchiveEntry("testfile.txt", "test/data.txt");
        assertThrows(IOException.class, () -> {
            cpioArchiveOutputStream.putArchiveEntry(entry);
        });
    }

    @org.junit.Test
    public void testWrite_ValidWrite() throws IOException {
        OutputStream outputStream = new FileOutputStream(new File("test.cpio"));
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        ArchiveEntry entry = new CpioArchiveEntry("testfile.txt", "test/data.txt");
        cpioArchiveOutputStream.putArchiveEntry(entry);

        byte[] testData = "Test Data".getBytes();
        cpioArchiveOutputStream.write(testData);
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();

        File testFile = new File("test.cpio");
        ByteArrayInputStream expectedInput = new ByteArrayInputStream(testData);
        CpioArchiveInputStream cpioArchiveInputStream = new CpioArchiveInputStream(expectedInput);
        ArchiveEntry nextEntry = cpioArchiveInputStream.getNextEntry();
        byte[] actualData = new byte[nextEntry.getSize()];
        nextEntry.getInputStream().read(actualData);

        assertEquals(testData, actualData);
    }

    @org.junit.Test
    public void testWrite_InvalidWrite() throws IOException {
        OutputStream outputStream = new FileOutputStream(new File("test.cpio"));
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        ArchiveEntry entry = new CpioArchiveEntry("testfile.txt", "test/data.txt");
        cpioArchiveOutputStream.putArchiveEntry(entry);

        byte[] invalidTestData = null;
        assertThrows(NullPointerException.class, () -> {
            cpioArchiveOutputStream.write(invalidTestData);
        });

        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();
    }

    @org.junit.Test
    public void testCloseArchiveEntry_ValidClose() throws IOException {
        OutputStream outputStream = new FileOutputStream(new File("test.cpio"));
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        ArchiveEntry entry = new CpioArchiveEntry("testfile.txt", "test/data.txt");
        cpioArchiveOutputStream.putArchiveEntry(entry);

        byte[] testData = "Test Data".getBytes();
        cpioArchiveOutputStream.write(testData);

        cpioArchiveOutputStream.closeArchiveEntry();
        assertEquals(testData.length, cpioArchiveOutputStream.written);
        assertNull(cpioArchiveOutputStream.entry);
    }

    @org.junit.Test
    public void testFinish_ValidFinish() throws IOException {
        OutputStream outputStream = new FileOutputStream(new File("test.cpio"));
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        ArchiveEntry entry = new CpioArchiveEntry("testfile.txt", "test/data.txt");
        cpioArchiveOutputStream.putArchiveEntry(entry);

        byte[] testData = "Test Data".getBytes();
        cpioArchiveOutputStream.write(testData);
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.finish();
        assertEquals(testData.length + entry.getHeaderSize() + entry.getDataPadCount() + CpioConstants.CPIO_TRAILER_SIZE, cpioArchiveOutputStream.written);
    }

    @org.junit.Test
    public void testFinish_InvalidFinish() throws IOException {
        OutputStream outputStream = new FileOutputStream(new File("test.cpio"));
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        ArchiveEntry entry = new CpioArchiveEntry("testfile.txt", "test/data.txt");
        cpioArchiveOutputStream.putArchiveEntry(entry);

        byte[] testData = "Test Data".getBytes();
        cpioArchiveOutputStream.write(testData);

        assertFalse(cpioArchiveOutputStream.finished);
        assertThrows(IllegalStateException.class, () -> {
            cpioArchiveOutputStream.finish();
        });

        cpioArchiveOutputStream.close();
    }

    @org.junit.Test
    public void testClose_ValidClose() throws IOException {
        OutputStream outputStream = new FileOutputStream(new File("test.cpio"));
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        ArchiveEntry entry = new CpioArchiveEntry("testfile.txt", "test/data.txt");
        cpioArchiveOutputStream.putArchiveEntry(entry);

        byte[] testData = "Test Data".getBytes();
        cpioArchiveOutputStream.write(testData);
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.finish();

        cpioArchiveOutputStream.close();
        assertTrue(cpioArchiveOutputStream.closed);
    }

    @org.junit.Test
    public void testClose_InvalidClose() throws IOException {
        OutputStream outputStream = new FileOutputStream(new File("test.cpio"));
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        ArchiveEntry entry = new CpioArchiveEntry("testfile.txt", "test/data.txt");
        cpioArchiveOutputStream.putArchiveEntry(entry);

        byte[] testData = "Test Data".getBytes();
        cpioArchiveOutputStream.write(testData);
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.finish();

        assertThrows(IllegalStateException.class, () -> {
            cpioArchiveOutputStream.close();
        });
    }

    private byte[] fileToByteArray(File file) throws IOException {
        FileInputStream input = new FileInputStream(file);
        byte[] byteArray = new byte[file.length()];
        input.read(byteArray);
        input.close();
        return byteArray;
    }
}