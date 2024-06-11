import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CpioArchiveOutputStreamTest {

    private File outputFile;
    private CpioArchiveOutputStream out;

    @Before
    public void setUp() throws IOException {
        outputFile = new File("target/test-output.cpio");
        out = new CpioArchiveOutputStream(new FileOutputStream(outputFile), CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        if (outputFile.exists()) {
            outputFile.delete();
        }
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        String contents = "12345";
        entry.setFileSize(contents.length());
        entry.setMode(CpioConstants.C_ISREG); // regular file

        out.putArchiveEntry(entry);
        out.write(contents.getBytes(StandardCharsets.UTF_8));
        out.closeArchiveEntry();

        ArchiveEntry readEntry = out.getNextEntry();
        assertNotNull(readEntry);
        assertEquals(entry.getName(), readEntry.getName());
        assertEquals(entry.getSize(), readEntry.getSize());
        assertEquals(entry.getMode(), readEntry.getMode());

        out.close();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateName() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        String contents = "12345";
        entry.setFileSize(contents.length());
        entry.setMode(CpioConstants.C_ISREG); // regular file

        out.putArchiveEntry(entry);
        out.write(contents.getBytes(StandardCharsets.UTF_8));
        out.closeArchiveEntry();

        out.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        String contents = "12345";
        entry.setFileSize(contents.length());
        entry.setMode(CpioConstants.C_ISREG); // regular file

        out.putArchiveEntry(entry);
        out.setFormat(CpioConstants.FORMAT_OLD_ASCII);

        try {
            out.write(contents.getBytes(StandardCharsets.UTF_8));
            fail("Expected an IOException");
        } catch (IOException e) {
            // Expected
        }

        out.closeArchiveEntry();
        out.close();
    }

    @Test
    public void testWriteBinaryLong() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CpioArchiveOutputStream archOut = new CpioArchiveOutputStream(bos, CpioConstants.FORMAT_OLD_BINARY);

        archOut.writeBinaryLong(1, 4, false);
        byte[] bytes = bos.toByteArray();
        byte expected[] = new byte[]{0x00, 0x01, 0x00, 0x00, 0x01, 0x00, 0x00, 0x01};
        assertArrayEquals(expected, bytes);

        archOut.close();
    }

    @Test
    public void testWriteAsciiLong() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CpioArchiveOutputStream archOut = new CpioArchiveOutputStream(bos, CpioConstants.FORMAT_OLD_ASCII);

        archOut.writeAsciiLong(1, 4, 10);
        byte[] bytes = bos.toByteArray();
        byte expected[] = new byte[]{0x00, '1', ' ', ' ', '1', '\r', '\n'};
        assertArrayEquals(expected, bytes);

        archOut.close();
    }

    @Test
    public void testWriteCString() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CpioArchiveOutputStream archOut = new CpioArchiveOutputStream(bos, CpioConstants.FORMAT_OLD_ASCII);

        archOut.writeCString("test");
        byte[] bytes = bos.toByteArray();
        byte expected[] = new byte[]{"t", "e", "s", "t", '\0'};
        assertArrayEquals(expected, bytes);

        archOut.close();
    }

    @Test
    public void testFinishWithNoEntries() throws IOException {
        out.finish();

        ArchiveEntry readEntry = out.getNextEntry();
        assertNull(readEntry);

        out.close();
    }

    @Test
    public void testFinishWithOneEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        String contents = "12345";
        entry.setFileSize(contents.length());
        entry.setMode(CpioConstants.C_ISREG); // regular file

        out.putArchiveEntry(entry);
        out.write(contents.getBytes(StandardCharsets.UTF_8));
        out.closeArchiveEntry();

        out.finish();

        ArchiveEntry readEntry = out.getNextEntry();
        assertNotNull(readEntry);
        assertEquals(entry.getName(), readEntry.getName());
        assertEquals(entry.getSize(), readEntry.getSize());
        assertEquals(entry.getMode(), readEntry.getMode());

        out.close();
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        String contents = "12345";
        entry.setFileSize(contents.length());
        entry.setMode(CpioConstants.C_ISREG); // regular file

        out.putArchiveEntry(entry);
        out.write(contents.getBytes(StandardCharsets.UTF_8));

        // Test that we can write more than the size of the entry
        out.write("extra".getBytes(StandardCharsets.UTF_8));

        out.closeArchiveEntry();

        ArchiveEntry readEntry = out.getNextEntry();
        assertSame(entry, readEntry);

        out.close();
    }

    @Test
    public void testCloseArchiveEntryWithInvalidFormat() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        String contents = "12345";
        entry.setFileSize(contents.length());
        entry.setMode(CpioConstants.C_ISREG); // regular file

        out.putArchiveEntry(entry);
        out.write(contents.getBytes(StandardCharsets.UTF_8));

        out.setFormat(CpioConstants.FORMAT_OLD_ASCII);

        try {
            out.closeArchiveEntry();
            fail("Expected an IOException");
        } catch (IOException e) {
            // Expected
        }

        out.close();
    }

    @Test
    public void testCloseWithInvalidFormat() throws IOException {
        out.setFormat(CpioConstants.FORMAT_OLD_ASCII);

        try {
            out.close();
            fail("Expected an IOException");
        } catch (IOException e) {
            // Expected
        }

        out.close();
    }

    @Test
    public void testClose() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        String contents = "12345";
        entry.setFileSize(contents.length());
        entry.setMode(CpioConstants.C_ISREG); // regular file

        out.putArchiveEntry(entry);
        out.write(contents.getBytes(StandardCharsets.UTF_8));
        out.closeArchiveEntry();

        out.finish();
        out.close();

        ArchiveEntry readEntry = out.getNextEntry();
        assertNull(readEntry);

        assertTrue(outputFile.length() > 0);

        File extractedFile = File.createTempFile("test", null);
        ArchiveUtils.unmarshal(outputFile, extractedFile);
        String extractedContents = new String(Files.readAllBytes(extractedFile.toPath()), StandardCharsets.UTF_8);
        assertEquals(contents, extractedContents);

        extractedFile.delete();
    }
}