import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() throws IOException {
        String entryName = "test.txt";
        String expectedHeader = createExpectedHeader(TarConstants.REGULAR_FILE, entryName, (long) 10);

        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry tarEntry = new TarArchiveEntry(new File(entryName), entryName);
        tarOutput.putArchiveEntry(tarEntry);
        tarOutput.writeBytes("Hello World!".getBytes());
        tarOutput.closeArchiveEntry();
        tarOutput.finish();

        byte[] outputBytes = outputStream.toByteArray();

        int index = 0;
        for (int i = 0; i < 512 && index < outputBytes.length; i++) {
            if (i == 0) {
                Assert.assertEquals(expectedHeader.charAt(i), outputBytes[index]);
            } else if (i >= 512 && i < 1024) {
                Assert.assertEquals(0, outputBytes[index]);
            } else {
                Assert.assertEquals("Hello World!".charAt(i - 512), outputBytes[index]);
            }
            index++;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String entryName = "test/subdirectory/longFileNameThatIsLongerThanNameLength.txt";
        TarArchiveEntry tarEntry = new TarArchiveEntry(new File(""), entryName);
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutput.putArchiveEntry(tarEntry);
        tarOutput.finish();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidLongFileName() throws IOException {
        String entryName = "test/subdirectory/longFileNameThatIsLongerThanNameLength.txt";
        TarArchiveEntry tarEntry = new TarArchiveEntry(new File(""), entryName);
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOutput.putArchiveEntry(tarEntry);
        tarOutput.finish();
    }

    @Test
    public void testWrite() throws IOException {
        String entryName = "test.txt";
        String expectedHeader = createExpectedHeader(TarConstants.REGULAR_FILE, entryName, (long) 10);

        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry tarEntry = new TarArchiveEntry(new File(entryName), entryName);
        tarOutput.putArchiveEntry(tarEntry);

        byte[] data = "Hello World!".getBytes();
        byte[] buffer = new byte[TarArchiveOutputStream.TarBuffer.DEFAULT_BLKSIZE];

        int bytesWritten = tarOutput.write(data, 0, data.length);
        Assert.assertEquals(data.length, bytesWritten);

        int index = 0;
        for (int i = 0; i < 512 && index < data.length; i++) {
            if (i < data.length - 512) {
                Assert.assertEquals(data[i], tarOutput.getBuffer()[index]);
                index++;
            }
            if (i >= 512) {
                Assert.assertEquals(0, tarOutput.getBuffer()[index]);
                index++;
            }
        }

        tarOutput.closeArchiveEntry();
        tarOutput.finish();
    }

    @Test
    public void testCloseArchiveEntryAfterWritingData() throws IOException {
        String entryName = "test.txt";
        String expectedHeader = createExpectedHeader(TarConstants.REGULAR_FILE, entryName, (long) 10);

        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry tarEntry = new TarArchiveEntry(new File(entryName), entryName);
        tarOutput.putArchiveEntry(tarEntry);

        byte[] data = "Hello World!".getBytes();
        byte[] buffer = new byte[TarArchiveOutputStream.TarBuffer.DEFAULT_BLKSIZE];

        int bytesWritten = tarOutput.write(data, 0, data.length);
        Assert.assertEquals(data.length, bytesWritten);

        tarOutput.closeArchiveEntry();

        byte[] outputBytes = outputStream.toByteArray();

        int index = 0;
        for (int i = 0; i < 512 && index < outputBytes.length; i++) {
            if (i == 0) {
                Assert.assertEquals(expectedHeader.charAt(i), outputBytes[index]);
            } else if (i >= 512 && i < 1024) {
                Assert.assertEquals(0, outputBytes[index]);
            } else {
                Assert.assertEquals(data[i - 512], outputBytes[index]);
            }
            index++;
        }

        tarOutput.finish();
    }

    @Test(expected = IOException.class)
    public void testWriteAfterCloseArchiveEntry() throws IOException {
        String entryName = "test.txt";
        String expectedHeader = createExpectedHeader(TarConstants.REGULAR_FILE, entryName, (long) 10);

        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry tarEntry = new TarArchiveEntry(new File(entryName), entryName);
        tarOutput.putArchiveEntry(tarEntry);
        tarOutput.write(new byte[10]);
        tarOutput.closeArchiveEntry();

        tarOutput.write(new byte[10]);
    }

    @Test(expected = IOException.class)
    public void testFinishWithoutCloseArchiveEntry() throws IOException {
        String entryName = "test.txt";
        String expectedHeader = createExpectedHeader(TarConstants.REGULAR_FILE, entryName, (long) 10);

        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry tarEntry = new TarArchiveEntry(new File(entryName), entryName);
        tarOutput.putArchiveEntry(tarEntry);

        tarOutput.finish();
        tarOutput.write(new byte[10]);
    }

    private String createExpectedHeader(int fileType, String fileName, long fileSize) {
        StringBuilder sb = new StringBuilder();

        sb.append((char) 0x16);
        sb.append((char) 0x00);
        sb.append((char) 0x00);

        sb.append((char) fileType << 4 | TarConstants.MAGIC_NUMBER);
        sb.append((char) (fileSize >>> 24 & 0xFF));
        sb.append((char) (fileSize >>> 16 & 0xFF));
        sb.append((char) (fileSize >>> 8 & 0xFF));
        sb.append((char) (fileSize & 0xFF));

        int fileNameLength = fileName.length() > TarConstants.NAMELEN ? fileName.length() : TarConstants.NAMELEN;
        sb.append((char) fileNameLength);
        for (int i = 0; i < fileNameLength; i++) {
            sb.append((char) fileName.charAt(i));
        }

        sb.append((char) 0);
        for (int i = 0; i < 14 - fileNameLength; i++) {
            sb.append((char) 0);
        }

        sb.append((char) 0x00);
        sb.append((char) 0x00);

        return sb.toString();
    }
}