import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream arInputStream;

    @Before
    public void setUp() throws IOException {
        byte[] data = buildTestData();
        InputStream input = new ByteArrayInputStream(data);
        arInputStream = new ArArchiveInputStream(input);
    }

    @After
    public void tearDown() throws IOException {
        arInputStream.close();
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry entry = arInputStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("TestFile1.txt", entry.getName());
        assertEquals(10, entry.getLength());

        entry = arInputStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("TestFile2.txt", entry.getName());
        assertEquals(5, entry.getLength());

        entry = arInputStream.getNextArEntry();
        assertNull(entry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = arInputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("TestFile1.txt", entry.getName());
        assertEquals(10, entry.getSize());

        entry = arInputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("TestFile2.txt", entry.getName());
        assertEquals(5, entry.getSize());

        entry = arInputStream.getNextEntry();
        assertNull(entry);
    }

    @Test
    public void testRead() throws IOException {
        int value = arInputStream.read();
        assertEquals(84, value);
        value = arInputStream.read();
        assertEquals(101, value);
        value = arInputStream.read();
        assertEquals(115, value);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[5];
        int bytesRead = arInputStream.read(buffer);
        assertEquals(5, bytesRead);
        assertArrayEquals(buildExpectedByteArray(), buffer);
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = arInputStream.read(buffer, 3, 5);
        assertEquals(5, bytesRead);
        assertEquals(0, buffer[0]);
        assertEquals(0, buffer[1]);
        assertEquals(0, buffer[2]);
        assertEquals(84, buffer[3]);
        assertEquals(101, buffer[4]);
        assertEquals(115, buffer[5]);
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        boolean result = ArArchiveInputStream.matches(signature, 8);
        assertTrue(result);
    }

    private byte[] buildTestData() {
        String file1 = "TestFile1.txt";
        byte[] file1Bytes = file1.getBytes();
        byte[] file1Length = String.format("%-10s", String.valueOf(file1Bytes.length)).getBytes();

        String file2 = "TestFile2.txt";
        byte[] file2Bytes = file2.getBytes();
        byte[] file2Length = String.format("%-10s", String.valueOf(file2Bytes.length)).getBytes();

        byte[] header = "!<arch>\n".getBytes();
        byte[] trailer = "`\n".getBytes();

        byte[] testData = new byte[header.length + ArArchiveEntry.HEADER.length() + file1Bytes.length +
                                    ArArchiveEntry.HEADER.length() + file2Bytes.length +
                                    ArArchiveEntry.TRAILER.length() + trailer.length];

        System.arraycopy(header, 0, testData, 0, header.length);
        System.arraycopy(ArArchiveEntry.HEADER.getBytes(), 0, testData, header.length, ArArchiveEntry.HEADER.length());
        System.arraycopy(file1Bytes, 0, testData, header.length + ArArchiveEntry.HEADER.length(), file1Bytes.length);
        System.arraycopy(file1Length, 0, testData, header.length + ArArchiveEntry.HEADER.length() + file1Bytes.length, file1Length.length);
        System.arraycopy(ArArchiveEntry.HEADER.getBytes(), 0, testData, header.length + ArArchiveEntry.HEADER.length() + file1Bytes.length + file1Length.length, ArArchiveEntry.HEADER.length());
        System.arraycopy(file2Bytes, 0, testData, header.length + ArArchiveEntry.HEADER.length() + file1Bytes.length + file1Length.length + ArArchiveEntry.HEADER.length(), file2Bytes.length);
        System.arraycopy(file2Length, 0, testData, header.length + ArArchiveEntry.HEADER.length() + file1Bytes.length + file1Length.length + ArArchiveEntry.HEADER.length() + file2Bytes.length, file2Length.length);
        System.arraycopy(ArArchiveEntry.TRAILER.getBytes(), 0, testData, header.length + ArArchiveEntry.HEADER.length() + file1Bytes.length + file1Length.length + ArArchiveEntry.HEADER.length() + file2Bytes.length + file2Length.length, ArArchiveEntry.TRAILER.length());
        System.arraycopy(trailer, 0, testData, header.length + ArArchiveEntry.HEADER.length() + file1Bytes.length + file1Length.length + ArArchiveEntry.HEADER.length() + file2Bytes.length + file2Length.length + ArArchiveEntry.TRAILER.length(), trailer.length);

        return testData;
    }

    private byte[] buildExpectedByteArray() {
        String expected = "TestF";
        return expected.getBytes();
    }
}