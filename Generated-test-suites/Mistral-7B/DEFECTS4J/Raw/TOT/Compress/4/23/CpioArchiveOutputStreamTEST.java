import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioConstants;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testCpioArchiveOutputStream() throws IOException {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                new FileOutputStream("/temppath/test.cpio"));

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        String testContents = "12345";
        entry.setFileSize(testContents.length());
        entry.setMode(CpioConstants.C_ISREG);
        outputStream.putArchiveEntry(entry);
        outputStream.write(testContents.getBytes());
        outputStream.closeArchiveEntry();
        outputStream.finish();
        outputStream.close();

        File file = new File("/temppath/test.cpio");
        InputStream inputStream = new FileInputStream(file);
        CpioArchiveOutputStream inputCpioArchiveOutputStream = new CpioArchiveOutputStream(
                new ByteArrayOutputStream());

        CpioArchiveEntry readEntry = null;
        byte[] bytes = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(bytes, 0, 1024)) != -1) {
            inputCpioArchiveOutputStream.write(bytes, 0, length);
            readEntry = (CpioArchiveEntry) inputCpioArchiveOutputStream
                    .getNextEntry();
            if (readEntry != null && readEntry.getName().equals("testfile")) {
                break;
            }
        }
        Assert.assertNotNull(readEntry);
        Assert.assertEquals(testContents, new String(readEntry.getData(), 0, (int) readEntry.getSize()));

        Map<String, CpioArchiveEntry> entries = outputStream.getEntries();
        Assert.assertEquals(1, entries.size());
        CpioArchiveEntry entryFromMap = entries.values().iterator().next();
        Assert.assertEquals(testContents, new String(entryFromMap.getData(), 0, (int) entryFromMap.getSize()));

        inputStream.close();
        inputCpioArchiveOutputStream.close();
        file.delete();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongFormat() throws IOException {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                new FileOutputStream("/temppath/test.cpio"), FORMAT_INVALID);
    }

    @Test(expected = IOException.class)
    public void testDuplicateEntry() throws IOException {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                new FileOutputStream("/temppath/test.cpio"));

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        String testContents = "12345";
        entry.setFileSize(testContents.length());
        entry.setMode(CpioConstants.C_ISREG);
        outputStream.putArchiveEntry(entry);
        outputStream.write(testContents.getBytes());
        outputStream.closeArchiveEntry();

        try {
            outputStream.putArchiveEntry(entry);
            outputStream.write(testContents.getBytes());
            outputStream.closeArchiveEntry();
            outputStream.finish();
            outputStream.close();
        } catch (IOException e) {
            Assert.assertEquals("duplicate entry: testfile", e.getMessage());
        }

        File file = new File("/temppath/test.cpio");
        file.delete();
    }

    @Test(expected = IOException.class)
    public void testInvalidEntrySize() throws IOException {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                new FileOutputStream("/temppath/test.cpio"));

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        String testContents = "12345";
        entry.setFileSize(testContents.length() + 1);
        entry.setMode(CpioConstants.C_ISREG);
        outputStream.putArchiveEntry(entry);
        outputStream.write(testContents.getBytes());
        outputStream.closeArchiveEntry();

        try {
            outputStream.finish();
            outputStream.close();
        } catch (IOException e) {
            Assert.assertEquals("invalid entry size (expected 13 but got 12 bytes)", e.getMessage());
        }

        File file = new File("/temppath/test.cpio");
        file.delete();
    }

    @Test(expected = IOException.class)
    public void testInvalidCrc() throws IOException {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                new FileOutputStream("/temppath/test.cpio"), FORMAT_NEW_CRC);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        String testContents = "12345";
        entry.setFileSize(testContents.length());
        entry.setMode(CpioConstants.C_ISREG);
        outputStream.putArchiveEntry(entry);
        outputStream.write(testContents.getBytes());
        outputStream.closeArchiveEntry();

        try {
            outputStream.finish();
            outputStream.close();
            File file = new File("/temppath/test.cpio");
            InputStream inputStream = new FileInputStream(file);
            CpioArchiveOutputStream inputCpioArchiveOutputStream = new CpioArchiveOutputStream(
                    new ByteArrayOutputStream());
            CpioArchiveEntry readEntry = null;

            while ((readEntry = inputCpioArchiveOutputStream.getNextEntry()) != null) {
                if (readEntry.getName().equals("testfile")) {
                    byte[] data = readEntry.getData();
                    for (int i = 0; i < data.length; i++) {
                        data[i] = (byte) (data[i] ^ 0xFF);
                    }
                    outputStream.write(data);
                    outputStream.closeArchiveEntry();
                    break;
                }
            }
            outputStream.finish();
            outputStream.close();
            file.delete();
        } catch (IOException e) {
            Assert.assertEquals("CRC Error", e.getMessage());
        }
    }
}