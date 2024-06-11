import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream testee;
    private InputStream input;
    private ByteArrayOutputStream output;

    @BeforeEach
    public void setUp() throws IOException {
        input = new ByteArrayInputStream("MIME-Version: 1.0\n" +
                " multipart/x-ar\n" +
                "--31415926535827209850086879878492\r\n" +
                "Content-Type: application/ar\r\n" +
                "Content-Disposition: attachment; filename=test.ar\r\n\r\n" +
                "16 06266356724823981836275816033215\r\n" +
                "test.txt\r\n" +
                "15 40960111205305688335587072113383\r\n" +
                "test.bin\r\n" +
                "--31415926535827209850086879878492\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Transfer-Encoding: 7bit\r\n\r\n" +
                "Hello World!\r\n" +
                "--31415926535827209850086879878492\r\n" +
                "Content-Type: application/octet-stream\r\n" +
                "Content-Transfer-Encoding: binary\r\n\r\n" +
                "test data\r\n" +
                "--31415926535827209850086879878492--\r\n");
        testee = new ArArchiveInputStream(input);
        output = new ByteArrayOutputStream();
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry nextEntry = testee.getNextArEntry();
        Assertions.assertNotNull(nextEntry);
        Assertions.assertEquals("test.txt", nextEntry.getName());
        Assertions.assertEquals(16L, nextEntry.getSize());

        nextEntry = testee.getNextArEntry();
        Assertions.assertNotNull(nextEntry);
        Assertions.assertEquals("test.bin", nextEntry.getName());
        Assertions.assertEquals(15L, nextEntry.getSize());

        nextEntry = testee.getNextArEntry();
        Assertions.assertNull(nextEntry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry nextEntry = testee.getNextEntry();
        Assertions.assertSame(nextEntry, testee.getNextArEntry());

        nextEntry = testee.getNextEntry();
        Assertions.assertNull(nextEntry);
    }

    @Test
    public void testClose() throws IOException {
        testee.close();
        Assertions.assertThrows(IOException.class, testee::getNextArEntry);
    }

    @Test
    public void testMatchesSignature() {
        byte[] signature = new byte[8];
        ArchiveStreamFactory.readMagic(input, signature);
        Assertions.assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[10];

        int actual = testee.read(buffer, 0, buffer.length);
        Assertions.assertEquals(16, actual);
        Assertions.assertArrayEquals("Hello World!".getBytes(StandardCharsets.UTF_8), Arrays.copyOfRange(buffer, 0, 16));

        actual = testee.read(buffer, 0, buffer.length);
        Assertions.assertEquals(15, actual);
        Assertions.assertArrayEquals("test data".getBytes(StandardCharsets.UTF_8), Arrays.copyOfRange(buffer, 16, buffer.length));
    }
}