import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * TarArchiveOutputStreamTest
 */
public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeWithInvalidArgument() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(-1);
    }

    @Test
    public void testPutArchiveEntryWithValidEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String validEntryName = "testValidEntry.txt";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(validEntryName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] buffer = new byte[10];
        byteArrayOutputStream.write(buffer, 0, buffer.length);
        tarArchiveOutputStream.write(byteArrayOutputStream.toByteArray(), 0, buffer.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        byte[] result = tarArchiveOutputStream.toByteArray();
        Assert.assertArrayEquals(new byte[]{(byte) 0x14, (byte) 0x00, 0x00, 0x00, (byte) 'T', (byte) 'a', (byte) 'r', (byte) '\0'}, result, 0, 8);
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String longFileName = "testLongFileName.txt";
        for (int i = 0; i < TarConstants.NAMELEN - longFileName.length(); i++) {
            longFileName += "a";
        }
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(longFileName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        Assert.assertTrue(tarArchiveOutputStream.isLongFileModeSet());
        tarArchiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        Object invalidArchiveEntry = new Object();
        tarArchiveOutputStream.putArchiveEntry(invalidArchiveEntry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithNullArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testWriteWithTooMuchData() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String validEntryName = "testValidEntry.txt";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(validEntryName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] buffer = new byte[10];
        byteArrayOutputStream.write(buffer, 0, buffer.length);
        byte[] tooMuchData = new byte[11];
        tarArchiveOutputStream.write(tooMuchData, 0, buffer.length + 1);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String validEntryName = "testValidEntry.txt";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(validEntryName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] buffer = new byte[10];
        byteArrayOutputStream.write(buffer, 0, buffer.length);
        tarArchiveOutputStream.write(byteArrayOutputStream.toByteArray(), 0, buffer.length);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] result = tarArchiveOutputStream.toByteArray();
        Assert.assertArrayEquals(new byte[]{(byte) 0x14, (byte) 0x00, 0x00, 0x00, (byte) 'T', (byte) 'a', (byte) 'r', (byte) '\0'}, result, 0, 8);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testValidEntry.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        byte[] result = tarArchiveOutputStream.toByteArray();
        Assert.assertArrayEquals(new byte[]{(byte) 0x14, (byte) 0x00, 0x00, 0x00, (byte) 'T', (byte) 'a', (byte) 'r', (byte) '\0', (byte) 0x00}, result, 0, 9);
    }

    @Test(expected = IllegalStateException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testValidEntry.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testValidEntry.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.close();
        tarArchiveOutputStream.finish();
    }

    @Test(expected = IllegalStateException.class)
    public void testCloseWithUnclosedEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testValidEntry.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveEntryWithNullFileName() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.createArchiveEntry(null, "testValidEntry.txt");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveEntryWithNullFile() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.createArchiveEntry(null, null);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        File file = new File("testValidFile.txt");
        String fileName = "testValidEntry.txt";
        TarArchiveEntry tarArchiveEntry = tarArchiveOutputStream.createArchiveEntry(file, fileName);
        Assert.assertEquals(fileName, tarArchiveEntry.getName());
        Assert.assertEquals(file.length(), tarArchiveEntry.getSize());
    }

    @Test(expected = IOException.class)
    public void testWriteWithNullBuffer() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.write(null, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteWithNegativeOffset() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        byte[] buffer = new byte[10];
        tarArchiveOutputStream.write(buffer, -1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteWithNegativeLength() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        byte[] buffer = new byte[10];
        tarArchiveOutputStream.write(buffer, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testWriteWithOffsetOutOfBounds() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        byte[] buffer = new byte[10];
        tarArchiveOutputStream.write(buffer, 12, 1);
    }

    @Test
    public void testFlush() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testValidEntry.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] buffer = new byte[10];
        tarArchiveOutputStream.write(buffer, 0, buffer.length);
        tarArchiveOutputStream.flush();
        byte[] result = tarArchiveOutputStream.toByteArray();
        Assert.assertArrayEquals(new byte[]{(byte) 0x14, (byte) 0x00, 0x00, 0x00, (byte) 'T', (byte) 'a', (byte) 'r', (byte) '\0'}, result, 0, 8);
    }
}