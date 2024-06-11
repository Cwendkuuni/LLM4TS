import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;

public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, TarConstants.DEFAULT_BLKSIZE, TarConstants.DEFAULT_RCDSIZE);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    @After
    public void tearDown() throws Exception {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithEmptyFileName() throws IOException {
        // Create a new entry with an empty fileName
        TarArchiveEntry emptyFileNameEntry = new TarArchiveEntry("", "emptyFileName.txt");

        // Put the entry in the archive
        tarArchiveOutputStream.putArchiveEntry(emptyFileNameEntry);

        // Write some bytes to the entry
        tarArchiveOutputStream.write("Hello, World!".getBytes());

        // Close the entry
        tarArchiveOutputStream.closeArchiveEntry();

        // Ensure that the archive contains the expected data
        byte[] expectedData = "Hello, World!\0".getBytes();
        byte[] actualData = ((ByteArrayOutputStream) outputStream).toByteArray();
        assertEquals(expectedData.length, actualData.length);
        for (int i = 0; i < expectedData.length; i++) {
            assertEquals(expectedData[i], actualData[i]);
        }
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        // Create a long fileName with a length greater than TarConstants.NAMELEN
        String longFileName = "a" + new String(new char[TarConstants.NAMELEN + 1]) + "b";
        TarArchiveEntry longFileNameEntry = new TarArchiveEntry(new File("src/test/resources/testFile.txt"), longFileName);

        // Put the entry in the archive
        tarArchiveOutputStream.putArchiveEntry(longFileNameEntry);

        // Write some bytes to the entry
        tarArchiveOutputStream.write("Hello, World!".getBytes());

        // Close the entry
        tarArchiveOutputStream.closeArchiveEntry();

        // Ensure that the archive contains the expected data
        byte[] expectedData = "Hello, World!\0".getBytes();
        byte[] actualData = ((ByteArrayOutputStream) outputStream).toByteArray();
        assertEquals(expectedData.length, actualData.length);
        for (int i = 0; i < expectedData.length; i++) {
            assertEquals(expectedData[i], actualData[i]);
        }
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithLongFileNameAndLongFileModeError() throws IOException {
        // Set the longFileMode to LONGFILE_ERROR
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);

        // Create a long fileName with a length greater than TarConstants.NAMELEN
        String longFileName = "a" + new String(new char[TarConstants.NAMELEN + 1]) + "b";
        TarArchiveEntry longFileNameEntry = new TarArchiveEntry(new File("src/test/resources/testFile.txt"), longFileName);

        // Put the entry in the archive
        tarArchiveOutputStream.putArchiveEntry(longFileNameEntry);

        // Write some bytes to the entry
        tarArchiveOutputStream.write("Hello, World!".getBytes());

        // Close the entry
        tarArchiveOutputStream.closeArchiveEntry();

        // This test should throw a RuntimeException
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameAndLongFileModeGnu() throws IOException {
        // Set the longFileMode to LONGFILE_GNU
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        // Create a long fileName with a length greater than TarConstants.NAMELEN
        String longFileName = "a" + new String(new char[TarConstants.NAMELEN + 1]) + "b";
        TarArchiveEntry longFileNameEntry = new TarArchiveEntry(new File("src/test/resources/testFile.txt"), longFileName);

        // Put the entry in the archive
        tarArchiveOutputStream.putArchiveEntry(longFileNameEntry);

        // Write some bytes to the entry
        tarArchiveOutputStream.write("Hello, World!".getBytes());

        // Close the entry
        tarArchiveOutputStream.closeArchiveEntry();

        // Ensure that the archive contains the expected data
        byte[] expectedDataLongLink = new byte[TarConstants.NAMELEN + 1 + "Hello, World!".length() + 1]; // +1 for NUL terminator
        System.arraycopy(longFileName.getBytes(), 0, expectedDataLongLink, 0, longFileName.length());
        expectedDataLongLink[longFileName.length()] = 0;
        System.arraycopy("Hello, World!".getBytes(), 0, expectedDataLongLink, longFileName.length() + 1, "Hello, World!".length());
        byte[] actualDataLongLink = ((ByteArrayOutputStream) outputStream).toByteArray();

        // Check the longlink record
        assertEquals(expectedDataLongLink.length, actualDataLongLink.length);
        for (int i = 0; i < expectedDataLongLink.length; i++) {
            assertEquals(expectedDataLongLink[i], actualDataLongLink[i]);
        }

        // Ensure that the archive contains the expected data
        byte[] expectedData = "Hello, World!\0".getBytes();
        byte[] actualData = new byte[expectedData.length + 1]; // +1 for NUL terminator
        System.arraycopy(actualDataLongLink, longFileName.length() + 1, actualData, 0, expectedData.length);
        assertEquals(expectedData.length, actualData.length);
        for (int i = 0; i < expectedData.length; i++) {
            assertEquals(expectedData[i], actualData[i]);
        }
    }

    @Test(expected = IOException.class)
    public void testWriteWithBytesBeyondSizeSpecifiedInHeader() throws IOException {
        // Create a new entry with a size of 5
        TarArchiveEntry smallEntry = new TarArchiveEntry(new File("src/test/resources/testFile.txt"), "smallEntry.txt");
        smallEntry.setSize(5);
        tarArchiveOutputStream.putArchiveEntry(smallEntry);

        // Write some bytes to the entry
        tarArchiveOutputStream.write("Hello, World!".getBytes());

        // Close the entry
        tarArchiveOutputStream.closeArchiveEntry();

        // Attempt to write more bytes than the size specified in the header
        byte[] moreBytes = new byte[10];
        tarArchiveOutputStream.write(moreBytes);

        // This test should throw an IOException
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testFinish() throws IOException {
        // Create a new entry with a size of 5
        TarArchiveEntry smallEntry = new TarArchiveEntry(new File("src/test/resources/testFile.txt"), "smallEntry.txt");
        smallEntry.setSize(5);
        tarArchiveOutputStream.putArchiveEntry(smallEntry);

        // Write some bytes to the entry
        tarArchiveOutputStream.write("Hello, World!".getBytes());

        // Close the entry
        tarArchiveOutputStream.closeArchiveEntry();

        // Finish the archive
        tarArchiveOutputStream.finish();
    }
}