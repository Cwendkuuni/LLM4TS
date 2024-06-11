import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = byteArrayOutputStream;
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        if (cpioArchiveOutputStream != null) {
            cpioArchiveOutputStream.close();
        }
    }

    @Test
    public void testCreateAndWriteEntry() throws IOException {
        // create and write an entry
        String entryName = "testFile.txt";
        byte[] entryContent = "Hello, world!".getBytes();

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(entryContent.length);
        cpioArchiveOutputStream.putArchiveEntry(entry);

        // write the content
        cpioArchiveOutputStream.write(entryContent, 0, entryContent.length);

        // close the entry
        cpioArchiveOutputStream.closeArchiveEntry();

        // verify the output
        byte[] expectedOutput = getExpectedOutput("test-create-and-write-entry.cpio");
        assertEquals(ArchiveUtils.toByteArray(new File(expectedOutput)), byteArrayOutputStream.toByteArray());
    }

    @Test
    public void testCreateAndWriteMultipleEntries() throws IOException {
        // create and write multiple entries
        String entryName1 = "testFile1.txt";
        byte[] entryContent1 = "Hello, world!".getBytes();

        String entryName2 = "testFile2.txt";
        byte[] entryContent2 = "Hello, JUnit!".getBytes();

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(entryName1);
        entry1.setSize(entryContent1.length);
        cpioArchiveOutputStream.putArchiveEntry(entry1);

        cpioArchiveOutputStream.write(entryContent1, 0, entryContent1.length);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName(entryName2);
        entry2.setSize(entryContent2.length);
        cpioArchiveOutputStream.putArchiveEntry(entry2);

        cpioArchiveOutputStream.write(entryContent2, 0, entryContent2.length);

        // close all entries
        cpioArchiveOutputStream.closeArchiveEntry();

        // verify the output
        byte[] expectedOutput = getExpectedOutput("test-create-and-write-multiple-entries.cpio");
        assertEquals(ArchiveUtils.toByteArray(new File(expectedOutput)), byteArrayOutputStream.toByteArray());
    }

    @Test(expected = IOException.class)
    public void testWriteAfterEntryClosed() throws IOException {
        // create and write an entry
        String entryName = "testFile.txt";
        byte[] entryContent = "Hello, world!".getBytes();

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(entryContent.length);
        cpioArchiveOutputStream.putArchiveEntry(entry);

        // write the content
        cpioArchiveOutputStream.write(entryContent, 0, entryContent.length);

        // close the entry
        cpioArchiveOutputStream.closeArchiveEntry();

        // try to write after closing the entry
        byte[] newContent = "Hello, JUnit!".getBytes();
        cpioArchiveOutputStream.write(newContent, 0, newContent.length);
    }

    @Test
    public void testFinish() throws IOException {
        // create and write an entry
        String entryName = "testFile.txt";
        byte[] entryContent = "Hello, world!".getBytes();

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(entryContent.length);
        cpioArchiveOutputStream.putArchiveEntry(entry);

        cpioArchiveOutputStream.write(entryContent, 0, entryContent.length);

        // finish the output
        cpioArchiveOutputStream.finish();

        // verify the output
        byte[] expectedOutput = getExpectedOutput("test-finish.cpio");
        assertEquals(ArchiveUtils.toByteArray(new File(expectedOutput)), byteArrayOutputStream.toByteArray());
    }

    @Test
    public void testClose() throws IOException {
        // create and write an entry
        String entryName = "testFile.txt";
        byte[] entryContent = "Hello, world!".getBytes();

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(entryContent.length);
        cpioArchiveOutputStream.putArchiveEntry(entry);

        cpioArchiveOutputStream.write(entryContent, 0, entryContent.length);

        // close the output
        cpioArchiveOutputStream.close();

        // verify that the output is closed
        assertEquals(0, byteArrayOutputStream.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() throws IOException {
        // create an invalid output stream
        OutputStream outputStream = new FilterOutputStream(new ByteArrayOutputStream()){
            @Override
            public void write(int b) throws IOException {
                throw new IOException("Invalid format");
            }
        };

        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_INVALID);
    }

    private byte[] getExpectedOutput(String fileName) {
        return ArchiveUtils.toByteArray(new File(fileName));
    }
}