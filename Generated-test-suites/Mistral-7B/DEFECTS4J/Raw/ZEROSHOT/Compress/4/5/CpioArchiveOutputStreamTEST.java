import org.apache.commons.compress.archivers.CpioArchiveEntry;
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
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = new FileOutputStream(new File("temp.cpio"));
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        byteArrayOutputStream.reset();
    }

    @After
    public void tearDown() throws IOException {
        if (cpioArchiveOutputStream != null) {
            cpioArchiveOutputStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
        if (new File("temp.cpio").exists()) {
            new File("temp.cpio").delete();
        }
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        String testFileName = "testFile.txt";
        String testContent = "Test content";
        File testFile = new File("testFile.txt");
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry(testFile, testFileName);

        cpioArchiveOutputStream.putArchiveEntry(cpioArchiveEntry);
        cpioArchiveOutputStream.write(testContent.getBytes(StandardCharsets.UTF_8));
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] bytes = byteArrayOutputStream.toByteArray();
        String expectedMagicNumber = ArchiveUtils.toString(ArchiveUtils.toByteArray(ArchiveUtils.MAGIC_NEW), StandardCharsets.UTF_8);
        String expectedTestFileName = ArchiveUtils.toString(CpioArchiveEntry.getNameBytes(testFileName), StandardCharsets.UTF_8);

        Assert.assertEquals(expectedMagicNumber, ArchiveUtils.toString(bytes, 0, ArchiveUtils.MAGIC_NEW.length, StandardCharsets.UTF_8));
        int entryIndex = ArchiveUtils.readUnsignedShort(bytes, 16);
        Assert.assertEquals(0, entryIndex);
        String actualTestFileName = ArchiveUtils.toString(bytes, 18, ArchiveUtils.NAME_LENGTH + 1, StandardCharsets.UTF_8);
        Assert.assertEquals(expectedTestFileName, actualTestFileName);
        long actualFileSize = ArchiveUtils.readUnsignedLong(bytes, 25);
        Assert.assertEquals(testContent.length(), actualFileSize);
        Assert.assertEquals(CpioConstants.FORMAT_NEW, cpioArchiveOutputStream.getFormat());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryException() throws IOException {
        String testFileName = "testFile.txt";
        String testContent = "Test content";
        File testFile = new File("testFile.txt");
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry(testFile, testFileName);

        cpioArchiveOutputStream.putArchiveEntry(cpioArchiveEntry);
        cpioArchiveOutputStream.putArchiveEntry(cpioArchiveEntry);
    }

    @Test
    public void testPutNextEntry() throws IOException {
        String testFileName1 = "testFile1.txt";
        String testContent1 = "Test content 1";
        File testFile1 = new File("testFile1.txt");

        String testFileName2 = "testFile2.txt";
        String testContent2 = "Test content 2";
        File testFile2 = new File("testFile2.txt");

        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(testFile1, testFileName1));
        cpioArchiveOutputStream.write(testContent1.getBytes(StandardCharsets.UTF_8));
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(testFile2, testFileName2));
        cpioArchiveOutputStream.write(testContent2.getBytes(StandardCharsets.UTF_8));
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] bytes = byteArrayOutputStream.toByteArray();
        String expectedMagicNumber = ArchiveUtils.toString(ArchiveUtils.toByteArray(ArchiveUtils.MAGIC_NEW), StandardCharsets.UTF_8);
        int entriesCount = ArchiveUtils.readUnsignedShort(bytes, 0);

        Assert.assertEquals(expectedMagicNumber, ArchiveUtils.toString(bytes, 0, ArchiveUtils.MAGIC_NEW.length, StandardCharsets.UTF_8));
        Assert.assertEquals(2, entriesCount);

        int firstEntryIndex = ArchiveUtils.readUnsignedShort(bytes, 2);
        String firstTestFileName = ArchiveUtils.toString(bytes, 18, ArchiveUtils.NAME_LENGTH + 1, StandardCharsets.UTF_8);
        long firstFileSize = ArchiveUtils.readUnsignedLong(bytes, 25);

        Assert.assertEquals(0, firstEntryIndex);
        Assert.assertEquals(testFileName1, firstTestFileName);
        Assert.assertEquals(testContent1.length(), firstFileSize);

        int secondEntryIndex = ArchiveUtils.readUnsignedShort(bytes, 31);
        String secondTestFileName = ArchiveUtils.toString(bytes, 39, ArchiveUtils.NAME_LENGTH + 1, StandardCharsets.UTF_8);
        long secondFileSize = ArchiveUtils.readUnsignedLong(bytes, 48);

        Assert.assertEquals(1, secondEntryIndex);
        Assert.assertEquals(testFileName2, secondTestFileName);
        Assert.assertEquals(testContent2.length(), secondFileSize);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryException() throws IOException {
        String testFileName = "testFile.txt";
        String testContent = "Test content";
        File testFile = new File("testFile.txt");
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry(testFile, testFileName);

        cpioArchiveOutputStream.putArchiveEntry(cpioArchiveEntry);
        cpioArchiveOutputStream.write(testContent.getBytes(StandardCharsets.UTF_8));
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
    }

    @Test
    public void testFinish() throws IOException {
        String testFileName = "testFile.txt";
        String testContent = "Test content";
        File testFile = new File("testFile.txt");

        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry(testFile, testFileName);

        cpioArchiveOutputStream.putArchiveEntry(cpioArchiveEntry);
        cpioArchiveOutputStream.write(testContent.getBytes(StandardCharsets.UTF_8));
        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();

        byte[] bytes = byteArrayOutputStream.toByteArray();
        String expectedMagicNumber = ArchiveUtils.toString(ArchiveUtils.toByteArray(ArchiveUtils.MAGIC_NEW), StandardCharsets.UTF_8);
        int entriesCount = ArchiveUtils.readUnsignedShort(bytes, 0);
        int lastEntryIndex = ArchiveUtils.readUnsignedShort(bytes, 2);
        String lastTestFileName = ArchiveUtils.toString(bytes, 18, ArchiveUtils.NAME_LENGTH + 1, StandardCharsets.UTF_8);
        long lastFileSize = ArchiveUtils.readUnsignedLong(bytes, 25);

        Assert.assertEquals(expectedMagicNumber, ArchiveUtils.toString(bytes, 0, ArchiveUtils.MAGIC_NEW.length, StandardCharsets.UTF_8));
        Assert.assertEquals(1, entriesCount);
        Assert.assertEquals(lastEntryIndex, 0);
        Assert.assertEquals(testFileName, lastTestFileName);
        Assert.assertEquals(testContent.length(), lastFileSize);

        Map<String, CpioArchiveEntry> names = cpioArchiveOutputStream.getNames();
        Assert.assertEquals(1, names.size());
        CpioArchiveEntry cpioArchiveEntryFromMap = names.values().iterator().next();
        Assert.assertEquals(testFileName, cpioArchiveEntryFromMap.getName());
    }
}