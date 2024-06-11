import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

    private File testFile;
    private ByteArrayOutputStream outStream;
    private CpioArchiveOutputStream cpioOutputStream;

    @Before
    public void setUp() throws Exception {
        testFile = File.createTempFile("junit-test-", null);
        outStream = new ByteArrayOutputStream();
        cpioOutputStream = new CpioArchiveOutputStream(outStream, CpioConstants.FORMAT_NEW);
    }

    @Test
    public void testPutArchiveEntry() throws Exception {
        String testFileContent = "test content";
        File testFileToBeAdded = new File("testFileToBeAdded.txt");
        testFileToBeAdded.createNewFile();
        String testFileName = "testFileName.txt";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setFileSize(testFileContent.length());
        entry.setMode(CpioConstants.C_ISREG);

        cpioOutputStream.putArchiveEntry(new File(".").toPath().toAbsolutePath() + File.separator + testFileToBeAdded.getAbsolutePath());
        cpioOutputStream.write(testFileContent.getBytes());
        cpioOutputStream.closeArchiveEntry();

        byte[] bytesFromOutput = outStream.toByteArray();
        byte[] expectedBytes = new byte[bytesFromOutput.length + testFileContent.length() + 1];

        System.arraycopy(bytesFromOutput, 0, expectedBytes, 0, bytesFromOutput.length);
        byte[] testFileContentBytes = testFileContent.getBytes();
        System.arraycopy(testFileContentBytes, 0, expectedBytes, bytesFromOutput.length, testFileContent.length());
        System.arraycopy(testFileContentBytes, testFileContent.length(), expectedBytes, bytesFromOutput.length + testFileContent.length(), 1);

        assertArrayEquals("Invalid test output", expectedBytes, bytesFromOutput);

        testFileToBeAdded.delete();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullEntry() throws Exception {
        cpioOutputStream.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateEntry() throws Exception {
        String testFileContent = "test content";
        File testFileToBeAdded = new File("testFileToBeAdded.txt");
        testFileToBeAdded.createNewFile();

        String testFileName = "testFileName.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setFileSize(testFileContent.length());
        entry.setMode(CpioConstants.C_ISREG);

        cpioOutputStream.putArchiveEntry(new File(".").toPath().toAbsolutePath() + File.separator + testFileToBeAdded.getAbsolutePath());
        cpioOutputStream.write(testFileContent.getBytes());
        cpioOutputStream.closeArchiveEntry();

        try {
            cpioOutputStream.putArchiveEntry(entry);
            fail("expected IOException for a duplicate entry");
        } catch (IOException e) {
            // expected
        }

        testFileToBeAdded.delete();
    }

    @Test
    public void testPutArchiveEntryWithNullFile() throws Exception {
        String testFileContent = "test content";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testFileName.txt");
        entry.setFileSize(testFileContent.length());
        entry.setMode(CpioConstants.C_ISREG);

        try {
            cpioOutputStream.putArchiveEntry(null);
            fail("expected IOException for a null file");
        } catch (IOException e) {
            // expected
        }

        try {
            cpioOutputStream.putArchiveEntry(entry);
            fail("expected IOException for a null file");
        } catch (IOException e) {
            // expected
        }

        cpioOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidFormat() throws Exception {
        OutputStream os = new FilterOutputStream(new FileOutputStream(testFile));
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(os, (short) 5);

        String testFileContent = "test content";
        File testFileToBeAdded = new File("testFileToBeAdded.txt");
        testFileToBeAdded.createNewFile();
        String testFileName = "testFileName.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setFileSize(testFileContent.length());
        entry.setMode(CpioConstants.C_ISREG);

        try {
            cpioOutputStream.putArchiveEntry(new File(".").toPath().toAbsolutePath() + File.separator + testFileToBeAdded.getAbsolutePath());
            fail("expected IOException for an invalid format");
        } catch (IOException e) {
            // expected
        }

        try {
            cpioOutputStream.putArchiveEntry(entry);
            fail("expected IOException for an invalid format");
        } catch (IOException e) {
            // expected
        }

        testFileToBeAdded.delete();
        cpioOutputStream.close();
        testFile.delete();
    }

    @Test
    public void testWrite() throws Exception {
        String testFileContent = "test content";
        File testFileToBeAdded = new File("testFileToBeAdded.txt");
        testFileToBeAdded.createNewFile();
        String testFileName = "testFileName.txt";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setFileSize(testFileContent.length());
        entry.setMode(CpioConstants.C_ISREG);

        cpioOutputStream.putArchiveEntry(new File(".").toPath().toAbsolutePath() + File.separator + testFileToBeAdded.getAbsolutePath());

        cpioOutputStream.write(testFileContent.getBytes());
        cpioOutputStream.closeArchiveEntry();

        byte[] bytesFromOutput = outStream.toByteArray();
        byte[] expectedBytes = new byte[bytesFromOutput.length + testFileContent.length() + 1];

        System.arraycopy(bytesFromOutput, 0, expectedBytes, 0, bytesFromOutput.length);
        byte[] testFileContentBytes = testFileContent.getBytes();
        System.arraycopy(testFileContentBytes, 0, expectedBytes, bytesFromOutput.length, testFileContent.length());
        System.arraycopy(testFileContentBytes, testFileContent.length(), expectedBytes, bytesFromOutput.length + testFileContent.length(), 1);

        assertArrayEquals("Invalid test output", expectedBytes, bytesFromOutput);

        testFileToBeAdded.delete();
    }

    @Test(expected = IOException.class)
    public void testWriteWithClosedStream() throws Exception {
        String testFileContent = "test content";
        File testFileToBeAdded = new File("testFileToBeAdded.txt");
        testFileToBeAdded.createNewFile();
        String testFileName = "testFileName.txt";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setFileSize(testFileContent.length());
        entry.setMode(CpioConstants.C_ISREG);

        cpioOutputStream.putArchiveEntry(new File(".").toPath().toAbsolutePath() + File.separator + testFileToBeAdded.getAbsolutePath());
        cpioOutputStream.write(testFileContent.getBytes());
        cpioOutputStream.closeArchiveEntry();
        cpioOutputStream.close();

        try {
            cpioOutputStream.write(testFileContent.getBytes());
            fail("expected IOException for a closed stream");
        } catch (IOException e) {
            // expected
        }

        testFileToBeAdded.delete();
    }

    @Test(expected = IOException.class)
    public void testWriteWithNullData() throws Exception {
        String testFileName = "testFileName.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setFileSize(0);
        entry.setMode(CpioConstants.C_ISREG);

        cpioOutputStream.putArchiveEntry(entry);

        try {
            cpioOutputStream.write(null);
            fail("expected IOException for a null data");
        } catch (IOException e) {
            // expected
        }

        cpioOutputStream.closeArchiveEntry();
        cpioOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testWriteWithInvalidLength() throws Exception {
        String testFileContent = "test content";
        File testFileToBeAdded = new File("testFileToBeAdded.txt");
        testFileToBeAdded.createNewFile();
        String testFileName = "testFileName.txt";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setFileSize(testFileContent.length() - 1);
        entry.setMode(CpioConstants.C_ISREG);

        cpioOutputStream.putArchiveEntry(new File(".").toPath().toAbsolutePath() + File.separator + testFileToBeAdded.getAbsolutePath());

        try {
            cpioOutputStream.write(testFileContent.getBytes());
            fail("expected IOException for an invalid length");
        } catch (IOException e) {
            // expected
        }

        testFileToBeAdded.delete();
        cpioOutputStream.closeArchiveEntry();
        cpioOutputStream.close();
    }

    @Test
    public void testFinish() throws Exception {
        String testFileContent = "test content";
        File testFileToBeAdded = new File("testFileToBeAdded.txt");
        testFileToBeAdded.createNewFile();
        String testFileName = "testFileName.txt";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setFileSize(testFileContent.length());
        entry.setMode(CpioConstants.C_ISREG);

        cpioOutputStream.putArchiveEntry(new File(".").toPath().toAbsolutePath() + File.separator + testFileToBeAdded.getAbsolutePath());
        cpioOutputStream.write(testFileContent.getBytes());
        cpioOutputStream.closeArchiveEntry();

        cpioOutputStream.finish();

        byte[] bytesFromFile = CpioUtil.readAllBytes(testFile);

        byte[] expectedBytes = new byte[bytesFromOutput.length + testFileContent.length() + 1];

        System.arraycopy(bytesFromFile, 0, expectedBytes, 0, bytesFromFile.length);
        byte[] testFileContentBytes = testFileContent.getBytes();
        System.arraycopy(testFileContentBytes, 0, expectedBytes, bytesFromFile.length, testFileContent.length());
        System.arraycopy(testFileContentBytes, testFileContent.length(), expectedBytes, bytesFromFile.length + testFileContent.length(), 1);

        assertArrayEquals("Invalid test output", expectedBytes, bytesFromFile);

        testFileToBeAdded.delete();
        cpioOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testFinishWithClosedStream() throws Exception {
        String testFileContent = "test content";
        File testFileToBeAdded = new File("testFileToBeAdded.txt");
        testFileToBeAdded.createNewFile();
        String testFileName = "testFileName.txt";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setFileSize(testFileContent.length());
        entry.setMode(CpioConstants.C_ISREG);

        cpioOutputStream.putArchiveEntry(new File(".").toPath().toAbsolutePath() + File.separator + testFileToBeAdded.getAbsolutePath());
        cpioOutputStream.write(testFileContent.getBytes());
        cpioOutputStream.closeArchiveEntry();
        cpioOutputStream.close();

        try {
            cpioOutputStream.finish();
            fail("expected IOException for a closed stream");
        } catch (IOException e) {
            // expected
        }

        testFileToBeAdded.delete();
    }

    @After
    public void tearDown() throws Exception {
        cpioOutputStream.close();
        testFile.delete();
    }
}