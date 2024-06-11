import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream ais;
    private byte[] header;
    private byte[] trailer;
    private ByteArrayInputStream testInput;

    @Before
    public void setUp() {
        header = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        trailer = new byte[] {0x65, 0x6E, 0x64, 0x74, 0x72, 0x61, 0x69, 0x6C, 0x65, 0x72, 0x20, 0x20};
    }

    @Test
    public void testGetNextArEntry_validEntry() throws IOException {
        String fileName = "file.txt";
        long fileSize = 10;
        byte[] entryContent = new byte[16];
        entryContent[0] = 0x41;
        entryContent[1] = 0x42;
        entryContent[2] = 0x43;
        entryContent[3] = 0x44;
        entryContent[4] = 0x45;
        entryContent[5] = 0x46;
        entryContent[6] = 0x47;
        entryContent[7] = 0x48;
        entryContent[8] = 0x49;
        entryContent[9] = 0x4A;
        entryContent[10] = 0x64;
        entryContent[11] = 0x65;
        entryContent[12] = 0x66;
        entryContent[13] = 0x67;
        entryContent[14] = 0x68;
        entryContent[15] = 0x69;

        testInput = new ByteArrayInputStream(concatByteArrays(header, entryContent, trailer));
        ais = new ArArchiveInputStream(testInput);

        ArArchiveEntry entry = ais.getNextArEntry();
        assertEquals(fileName, entry.getName());
        assertEquals(fileSize, entry.getSize());
        assertArrayEquals(entryContent, readEntryContentIntoByteArray(entry));
    }

    @Test
    public void testGetNextArEntry_emptyEntry() throws IOException {
        String fileName = "";
        long fileSize = 0;
        byte[] entryContent = new byte[16];

        testInput = new ByteArrayInputStream(concatByteArrays(header, entryContent, trailer));
        ais = new ArArchiveInputStream(testInput);

        ArArchiveEntry entry = ais.getNextArEntry();
        assertEquals(fileName, entry.getName());
        assertEquals(fileSize, entry.getSize());
        assertArrayEquals(entryContent, readEntryContentIntoByteArray(entry));
    }

    @Test
    public void testGetNextArEntry_invalidEntry() {
        byte[] invalidHeader = {0x40, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        testInput = new ByteArrayInputStream(concatByteArrays(invalidHeader));
        ais = new ArArchiveInputStream(testInput);

        assertThrows(IOException.class, () -> {
            ais.getNextArEntry();
        });
    }

    @Test
    public void testGetNextEntry_endOfStream() throws IOException {
        byte[] emptyContent = new byte[16];
        testInput = new ByteArrayInputStream(concatByteArrays(header, emptyContent));
        ais = new ArArchiveInputStream(testInput);

        ArArchiveEntry entry = ais.getNextEntry();
        assertNull(entry);
    }

    private byte[] concatByteArrays(byte[]... arrays) {
        int totalLength = 0;
        for (byte[] array : arrays) {
            totalLength += array.length;
        }

        byte[] result = new byte[totalLength];
        int currentIndex = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, currentIndex, array.length);
            currentIndex += array.length;
        }
        return result;
    }

    private byte[] readEntryContentIntoByteArray(ArArchiveEntry entry) throws IOException {
        byte[] content = new byte[(int) entry.getSize()];
        ais.read(content);
        return content;
    }

    @After
    public void tearDown() throws IOException {
        if (ais != null) {
            ais.close();
        }
        if (testInput != null) {
            testInput.close();
        }
    }
}