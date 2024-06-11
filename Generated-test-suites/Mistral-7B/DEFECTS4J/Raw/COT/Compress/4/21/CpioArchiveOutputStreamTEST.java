import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CpioArchiveOutputStreamTest {

    private File outputFile;
    private ByteArrayOutputStream out;
    private CpioArchiveOutputStream cpioArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        outputFile = new File("test.cpio");
        out = new ByteArrayOutputStream();
        cpioArchiveOutputStream = new CpioArchiveOutputStream(new FileOutputStream(outputFile), CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws Exception {
        cpioArchiveOutputStream.close();
        outputFile.delete();
    }

    @Test
    public void testPutArchiveEntry() throws Exception {
        String testFileName = "testfile";
        String testFileContent = "12345";
        byte[] testFileBytes = testFileContent.getBytes();

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setFileSize(testFileContent.length());
        entry.setMode(CpioConstants.C_ISREG);
        cpioArchiveOutputStream.putArchiveEntry(entry);

        cpioArchiveOutputStream.write(testFileBytes, 0, testFileBytes.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] actualBytes = out.toByteArray();
        byte[] expectedBytes = new byte[testFileBytes.length + 1024]; // 1024 is a random number, check actual size in the output
        System.arraycopy(testFileBytes, 0, expectedBytes, 0, testFileBytes.length);

        int headerSize = ArchiveUtils.writeCpioHeader(expectedBytes, 0, testFileName, (short) 0, testFileBytes.length, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, CpioConstants.FORMAT_NEW, 0);
        System.arraycopy(ArchiveUtils.MAGIC_NEW, 0, expectedBytes, 0, ArchiveUtils.MAGIC_NEW.length);
        System.arraycopy(ArchiveUtils.toAsciiBytes(testFileName), 0, expectedBytes, 8, testFileName.length());
        System.arraycopy(ArchiveUtils.toAsciiBytes(Long.toString(testFileBytes.length, 8)), 0, expectedBytes, 32, ArchiveUtils.toAsciiBytes(Long.toString(testFileBytes.length, 8)).length);

        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateName() throws Exception {
        String testFileName = "testfile";
        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(testFileName));
        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(testFileName));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidFormat() throws Exception {
        CpioArchiveOutputStream invalidFormatOutputStream = new CpioArchiveOutputStream(new FileOutputStream(outputFile), (short) 123);
        try {
            invalidFormatOutputStream.putArchiveEntry(new CpioArchiveEntry());
        } finally {
            invalidFormatOutputStream.close();
        }
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullEntry() throws Exception {
        cpioArchiveOutputStream.putArchiveEntry(null);
    }

    @Test
    public void testCloseArchiveEntry() throws Exception {
        String testFileName = "testfile";
        String testFileContent = "12345";
        byte[] testFileBytes = testFileContent.getBytes();

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setFileSize(testFileContent.length());
        entry.setMode(CpioConstants.C_ISREG);
        cpioArchiveOutputStream.putArchiveEntry(entry);

        cpioArchiveOutputStream.write(testFileBytes, 0, testFileBytes.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        Map<String, CpioArchiveEntry> entries = cpioArchiveOutputStream.getNextEntries();
        CpioArchiveEntry actualEntry = entries.values().iterator().next();
        Assert.assertEquals(testFileName, actualEntry.getName());
        Assert.assertEquals(testFileContent.length(), actualEntry.getSize());

        byte[] actualBytes = out.toByteArray();
        byte[] expectedBytes = new byte[testFileBytes.length + 1024]; // 1024 is a random number, check actual size in the output
        System.arraycopy(testFileBytes, 0, expectedBytes, 0, testFileBytes.length);

        int headerSize = ArchiveUtils.writeCpioHeader(expectedBytes, 0, testFileName, (short) 0, testFileBytes.length, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, CpioConstants.FORMAT_NEW, 0);
        System.arraycopy(ArchiveUtils.MAGIC_NEW, 0, expectedBytes, 0, ArchiveUtils.MAGIC_NEW.length);
        System.arraycopy(ArchiveUtils.toAsciiBytes(testFileName), 0, expectedBytes, 8, testFileName.length());
        System.arraycopy(ArchiveUtils.toAsciiBytes(Long.toString(testFileBytes.length, 8)), 0, expectedBytes, 32, ArchiveUtils.toAsciiBytes(Long.toString(testFileBytes.length, 8)).length);

        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithUnclosedEntry() throws Exception {
        cpioArchiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testWriteWithNullByteArray() throws Exception {
        cpioArchiveOutputStream.write(null, 0, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testWriteWithNegativeLength() throws Exception {
        cpioArchiveOutputStream.write(new byte[]{0}, -1, 1);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNegativeOffset() throws Exception {
        cpioArchiveOutputStream.write(new byte[]{0}, -1, 1);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNegativeLengthAndNegativeOffset() throws Exception {
        cpioArchiveOutputStream.write(new byte[]{0}, -1, -1);
    }

    @Test
    public void testWrite() throws Exception {
        String testFileName = "testfile";
        String testFileContent = "12345";
        byte[] testFileBytes = testFileContent.getBytes();

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setFileSize(testFileContent.length());
        entry.setMode(CpioConstants.C_ISREG);
        cpioArchiveOutputStream.putArchiveEntry(entry);

        cpioArchiveOutputStream.write(testFileBytes, 0, testFileBytes.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] actualBytes = out.toByteArray();
        byte[] expectedBytes = new byte[testFileBytes.length + 1024]; // 1024 is a random number, check actual size in the output
        System.arraycopy(testFileBytes, 0, expectedBytes, 0, testFileBytes.length);

        int headerSize = ArchiveUtils.writeCpioHeader(expectedBytes, 0, testFileName, (short) 0, testFileBytes.length, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, CpioConstants.FORMAT_NEW, 0);
        System.arraycopy(ArchiveUtils.MAGIC_NEW, 0, expectedBytes, 0, ArchiveUtils.MAGIC_NEW.length);
        System.arraycopy(ArchiveUtils.toAsciiBytes(testFileName), 0, expectedBytes, 8, testFileName.length());
        System.arraycopy(ArchiveUtils.toAsciiBytes(Long.toString(testFileBytes.length, 8)), 0, expectedBytes, 32, ArchiveUtils.toAsciiBytes(Long.toString(testFileBytes.length, 8)).length);

        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test(expected = IOException.class)
    public void testWriteWithUnclosedEntry() throws Exception {
        cpioArchiveOutputStream.write(new byte[]{0}, 0, 1);
    }

    @Test(expected = IOException.class)
    public void testWriteWithClosedEntry() throws Exception {
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.write(new byte[]{0}, 0, 1);
    }

    @Test
    public void testFinish() throws Exception {
        String testFileName = "testfile";
        String testFileContent = "12345";
        byte[] testFileBytes = testFileContent.getBytes();

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setFileSize(testFileContent.length());
        entry.setMode(CpioConstants.C_ISREG);
        cpioArchiveOutputStream.putArchiveEntry(entry);

        cpioArchiveOutputStream.write(testFileBytes, 0, testFileBytes.length);
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.finish();

        byte[] actualBytes = out.toByteArray();
        byte[] expectedBytes = new byte[testFileBytes.length + 1024 + ArchiveUtils.TRAILER_SIZE]; // 1024 is a random number, check actual size in the output
        System.arraycopy(testFileBytes, 0, expectedBytes, 0, testFileBytes.length);
        int headerSize = ArchiveUtils.writeCpioHeader(expectedBytes, 0, testFileName, (short) 0, testFileBytes.length, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, CpioConstants.FORMAT_NEW, 0);
        System.arraycopy(ArchiveUtils.MAGIC_NEW, 0, expectedBytes, 0, ArchiveUtils.MAGIC_NEW.length);
        System.arraycopy(ArchiveUtils.toAsciiBytes(testFileName), 0, expectedBytes, 8, testFileName.length());
        System.arraycopy(ArchiveUtils.toAsciiBytes(Long.toString(testFileBytes.length, 8)), 0, expectedBytes, 32, ArchiveUtils.toAsciiBytes(Long.toString(testFileBytes.length, 8)).length);

        int trailerSize = ArchiveUtils.writeCpioTrailer(expectedBytes, headerSize + testFileBytes.length, 0, 0);
        Assert.assertEquals(expectedBytes.length, actualBytes.length);
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws Exception {
        cpioArchiveOutputStream.finish();
    }

    @Test
    public void testCreateArchiveEntry() throws Exception {
        File testFile = new File("testfile.txt");
        String testFileName = "testfile";
        CpioArchiveEntry entry = cpioArchiveOutputStream.createArchiveEntry(testFile, testFileName);
        Assert.assertEquals(testFileName, entry.getName());
        Assert.assertEquals(testFile.length(), entry.getSize());
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveEntryWithNullFile() throws Exception {
        cpioArchiveOutputStream.createArchiveEntry(null, "testfile");
    }
}