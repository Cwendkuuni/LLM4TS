import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() throws Exception {
        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = new FileOutputStream("testFile.cpio");
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @Test
    public void testPutArchiveEntry() throws Exception {
        String testFileContent = "Test content";
        byte[] testFileContentBytes = testFileContent.getBytes();

        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName("testFile.txt");
        cpioArchiveEntry.setSize(testFileContentBytes.length);
        cpioArchiveEntry.setFormat(CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.putArchiveEntry(cpioArchiveEntry);
        cpioArchiveOutputStream.write(testFileContentBytes, 0, testFileContentBytes.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        byteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toString().getBytes());
        ArchiveInputStream archiveInputStream = new CpioArchiveInputStream(inputStream);

        ArchiveEntry archiveEntry = archiveInputStream.getNextEntry();
        Assert.assertEquals("testFile.txt", archiveEntry.getName());

        byte[] bytesRead = new byte[ArchiveEntry.SIZE_BUFFER];
        int length;
        while ((length = archiveInputStream.read(bytesRead)) != -1) {
            byteArrayOutputStream.write(bytesRead, 0, length);
        }

        Assert.assertEquals(testFileContent, new String(byteArrayOutputStream.toByteArray()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithInvalidFormat() throws Exception {
        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(), CpioConstants.FORMAT_INVALID);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicatedEntry() throws Exception {
        String testFileContent = "Test content";
        byte[] testFileContentBytes = testFileContent.getBytes();

        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName("testFile.txt");
        cpioArchiveEntry.setSize(testFileContentBytes.length);
        cpioArchiveEntry.setFormat(CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.putArchiveEntry(cpioArchiveEntry);
        cpioArchiveOutputStream.write(testFileContentBytes, 0, testFileContentBytes.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName("testFile.txt");

        cpioArchiveOutputStream.putArchiveEntry(cpioArchiveEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullEntry() throws Exception {
        cpioArchiveOutputStream.putArchiveEntry(null);
    }

    @Test
    public void testPutArchiveEntryWithNullName() throws Exception {
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setSize(10);
        cpioArchiveEntry.setFormat(CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.putArchiveEntry(cpioArchiveEntry);

        byteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toString().getBytes());
        ArchiveInputStream archiveInputStream = new CpioArchiveInputStream(inputStream);

        ArchiveEntry archiveEntry = archiveInputStream.getNextEntry();
        Assert.assertEquals("", archiveEntry.getName());
    }

    @Test
    public void testPutArchiveEntryWithEmptyName() throws Exception {
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName("");
        cpioArchiveEntry.setSize(10);
        cpioArchiveEntry.setFormat(CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.putArchiveEntry(cpioArchiveEntry);

        byteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toString().getBytes());
        ArchiveInputStream archiveInputStream = new CpioArchiveInputStream(inputStream);

        ArchiveEntry archiveEntry = archiveInputStream.getNextEntry();
        Assert.assertEquals("", archiveEntry.getName());
    }

    @Test
    public void testPutArchiveEntryWithValidName() throws Exception {
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName("test.txt");
        cpioArchiveEntry.setSize(10);
        cpioArchiveEntry.setFormat(CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.putArchiveEntry(cpioArchiveEntry);

        byteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toString().getBytes());
        ArchiveInputStream archiveInputStream = new CpioArchiveInputStream(inputStream);

        ArchiveEntry archiveEntry = archiveInputStream.getNextEntry();
        Assert.assertEquals("test.txt", archiveEntry.getName());
    }

    @Test(expected = IOException.class)
    public void testWriteWithInvalidSize() throws Exception {
        String testFileContent = "Test content";
        byte[] testFileContentBytes = testFileContent.getBytes();

        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName("testFile.txt");
        cpioArchiveEntry.setSize(testFileContentBytes.length + 1);
        cpioArchiveEntry.setFormat(CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.putArchiveEntry(cpioArchiveEntry);
        cpioArchiveOutputStream.write(testFileContentBytes, 0, testFileContentBytes.length);
        cpioArchiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testWriteWithNullData() throws Exception {
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName("testFile.txt");
        cpioArchiveEntry.setSize(10);
        cpioArchiveEntry.setFormat(CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.putArchiveEntry(cpioArchiveEntry);
        cpioArchiveOutputStream.write(null, 0, 0);
        cpioArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWriteWithValidData() throws Exception {
        String testFileContent = "Test content";
        byte[] testFileContentBytes = testFileContent.getBytes();

        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName("testFile.txt");
        cpioArchiveEntry.setSize(testFileContentBytes.length);
        cpioArchiveEntry.setFormat(CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.putArchiveEntry(cpioArchiveEntry);
        cpioArchiveOutputStream.write(testFileContentBytes, 0, testFileContentBytes.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        byteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toString().getBytes());
        ArchiveInputStream archiveInputStream = new CpioArchiveInputStream(inputStream);

        ArchiveEntry archiveEntry = archiveInputStream.getNextEntry();
        Assert.assertEquals("testFile.txt", archiveEntry.getName());

        byte[] bytesRead = new byte[ArchiveEntry.SIZE_BUFFER];
        int length;
        while ((length = archiveInputStream.read(bytesRead)) != -1) {
            byteArrayOutputStream.write(bytesRead, 0, length);
        }

        Assert.assertEquals(testFileContent, new String(byteArrayOutputStream.toByteArray()));
    }

    @Test
    public void testFinish() throws Exception {
        cpioArchiveOutputStream.finish();
        outputStream.close();

        byteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toString().getBytes());
        ArchiveInputStream archiveInputStream = new CpioArchiveInputStream(inputStream);

        ArchiveEntry archiveEntry = archiveInputStream.getNextEntry();
        Assert.assertEquals(CpioConstants.CPIO_TRAILER, archiveEntry.getName());
    }

    @Test
    public void testClose() throws Exception {
        cpioArchiveOutputStream.close();
        outputStream.close();

        byteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toString().getBytes());
        ArchiveInputStream archiveInputStream = new CpioArchiveInputStream(inputStream);

        ArchiveEntry archiveEntry = archiveInputStream.getNextEntry();
        Assert.assertNull(archiveEntry);
    }

    @After
    public void tearDown() throws Exception {
        outputStream.close();
        byteArrayOutputStream.close();
    }
}