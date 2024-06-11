import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarEntry;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = new TarOutputStream(byteArrayOutputStream);
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
        outputStream.close();
    }

    @Test
    public void testPutArchiveEntryAndWriteWithDefaultParameters() throws IOException {
        // Create an entry with default parameters
        TarEntry entry = new TarEntry("test.txt");
        entry.setSize(10);
        tarArchiveOutputStream.putArchiveEntry(entry);

        // Write data
        byte[] data = "Hello World!".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(data, 0, data.length);

        // Close the entry
        tarArchiveOutputStream.closeArchiveEntry();

        // Read the data back from the output stream
        byte[] output = byteArrayOutputStream.toByteArray();
        int headerLength = TarConstants.STANDARD_HEADER_SIZE;
        int recordLength = tarArchiveOutputStream.getRecordSize();

        // Verify the header and record are correct
        int expectedPosition = 0;
        int actualPosition = 0;
        assertEquals("Unexpected header",
                TarConstants.MAGIC_NUMBER, output[actualPosition++]);
        assertEquals("Unexpected version",
                TarConstants.VERSION_NUMBER, output[actualPosition++]);
        assertEquals("Unexpected header length",
                headerLength, (int) TarConstants.toUnsignedInt(output[actualPosition],
                        output[actualPosition + 1]));
        actualPosition += 2;
        assertEquals("Unexpected name length",
                10, (int) TarConstants.toUnsignedInt(output[actualPosition],
                        output[actualPosition + 1]));

        String expectedName = "test.txt";
        String actualName = new String(output, actualPosition, 10,
                StandardCharsets.UTF_8);
        actualPosition += 10;

        assertEquals("Unexpected file size",
                10, TarConstants.toUnsignedInt(output[actualPosition],
                        output[actualPosition + 1]));
        actualPosition += 2;

        assertEquals("Unexpected modification time",
                0, TarConstants.toUnsignedInt(output[actualPosition],
                        output[actualPosition + 1]));
        actualPosition += 2;

        assertEquals("Unexpected access time",
                0, TarConstants.toUnsignedInt(output[actualPosition],
                        output[actualPosition + 1]));
        actualPosition += 2;

        assertEquals("Unexpected modification time",
                0, TarConstants.toUnsignedInt(output[actualPosition],
                        output[actualPosition + 1]));
        actualPosition += 2;

        assertEquals("Unexpected access time",
                0, TarConstants.toUnsignedInt(output[actualPosition],
                        output[actualPosition + 1]));
        actualPosition += 2;

        assertEquals("Unexpected record length",
                recordLength, TarConstants.toUnsignedInt(output[actualPosition],
                        output[actualPosition + 1]));
        actualPosition += 2;

        assertEquals("Unexpected data",
                data, new String(output, actualPosition, data.length,
                        StandardCharsets.UTF_8).getBytes(StandardCharsets.UTF_8));

        assertEquals("Unexpected EOF",
                TarConstants.EOF_MAGIC_NUMBER, output[expectedPosition++]);
        assertEquals("Unexpected EOF",
                TarConstants.EOF_MAGIC_NUMBER, output[actualPosition++]);

        assertEquals("Unexpected EOF",
                TarConstants.EOF_MAGIC_NUMBER, output[expectedPosition++]);
        assertEquals("Unexpected EOF",
                TarConstants.EOF_MAGIC_NUMBER, output[actualPosition++]);
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        // Set long file mode to LONGFILE_GNU
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        // Create an entry with a long file name
        TarArchiveEntry entry = new TarArchiveEntry("long_file_name.txt");
        entry.setSize(10);
        tarArchiveOutputStream.putArchiveEntry(entry);

        // Write data
        byte[] data = "Hello World!".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(data, 0, data.length);

        // Close the entry
        tarArchiveOutputStream.closeArchiveEntry();

        // Read the data back from the output stream
        byte[] output = byteArrayOutputStream.toByteArray();
        int headerLength = TarConstants.STANDARD_HEADER_SIZE + TarConstants.GNU_LONGLINK_HEADER_SIZE;
        int recordLength = tarArchiveOutputStream.getRecordSize();

        // Verify the header and record are correct
        int expectedPosition = 0;
        int actualPosition = 0;
        assertEquals("Unexpected header",
                TarConstants.MAGIC_NUMBER, output[actualPosition++]);
        assertEquals("Unexpected version",
                TarConstants.VERSION_NUMBER, output[actualPosition++]);
        assertEquals("Unexpected header length",
                headerLength, (int) TarConstants.toUnsignedInt(output[actualPosition],
                        output[actualPosition + 1]));
        actualPosition += 2;
        assertEquals("Unexpected name length",
                22, (int) TarConstants.toUnsignedInt(output[actualPosition],
                        output[actualPosition + 1]));

        String expectedName = "long_file_name.txt";
        String actualName = new String(output, actualPosition, 22,
                StandardCharsets.UTF_8);
        actualPosition += 22;

        assertEquals("Unexpected file size",
                10, TarConstants.toUnsignedInt(output[actualPosition],
                        output[actualPosition + 1]));
        actualPosition += 2;

        assertEquals("Unexpected modification time",
                0, TarConstants.toUnsignedInt(output[actualPosition],
                        output[actualPosition + 1]));
        actualPosition += 2;

        assertEquals("Unexpected access time",
                0, TarConstants.toUnsignedInt(output[actualPosition],
                        output[actualPosition + 1]));
        actualPosition += 2;

        assertEquals("Unexpected modification time",
                0, TarConstants.toUnsignedInt(output[actualPosition],
                        output[actualPosition + 1]));
        actualPosition += 2;

        assertEquals("Unexpected access time",
                0, TarConstants.toUnsignedInt(output[actualPosition],
                        output[actualPosition + 1]));
        actualPosition += 2;

        assertEquals("Unexpected record length",
                recordLength, TarConstants.toUnsignedInt(output[actualPosition],
                        output[actualPosition + 1]));
        actualPosition += 2;

        assertEquals("Unexpected data",
                data, new String(output, actualPosition, data.length,
                        StandardCharsets.UTF_8).getBytes(StandardCharsets.UTF_8));

        assertEquals("Unexpected EOF",
                TarConstants.EOF_MAGIC_NUMBER, output[expectedPosition++]);
        assertEquals("Unexpected EOF",
                TarConstants.EOF_MAGIC_NUMBER, output[actualPosition++]);

        assertEquals("Unexpected long file name",
                expectedName, actualName);

        assertEquals("Unexpected EOF",
                TarConstants.EOF_MAGIC_NUMBER, output[expectedPosition++]);
        assertEquals("Unexpected EOF",
                TarConstants.EOF_MAGIC_NUMBER, output[actualPosition++]);

        assertEquals("Unexpected EOF",
                TarConstants.EOF_MAGIC_NUMBER, output[expectedPosition++]);
        assertEquals("Unexpected EOF",
                TarConstants.EOF_MAGIC_NUMBER, output[actualPosition++]);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameAndTruncateMode() throws IOException {
        // Set long file mode to LONGFILE_TRUNCATE
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        // Create an entry with a long file name
        TarArchiveEntry entry = new TarArchiveEntry("long_file_name.txt");
        entry.setSize(10);
        tarArchiveOutputStream.putArchiveEntry(entry);

        // Write data
        byte[] data = "Hello World!".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(data, 0, data.length);

        // Close the entry
        tarArchiveOutputStream.closeArchiveEntry();

        // Verify an IOException is thrown
        TarEntry newEntry = new TarArchiveEntry("long_file_name_2.txt");
        tarArchiveOutputStream.putArchiveEntry(newEntry);
    }
}