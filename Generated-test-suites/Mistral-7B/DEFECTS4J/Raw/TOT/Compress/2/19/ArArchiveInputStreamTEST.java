import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream inputStream;
    private ArchiveInputStream expectedArchive;

    @BeforeEach
    public void setUp() throws IOException {
        InputStream input = new ByteArrayInputStream(ArchiveTestUtils.getTestData("test-ar.tar"));
        expectedArchive = new ArArchiveInputStream(input);
        inputStream = new ArArchiveInputStream(new GZIPInputStream(input));
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry actualEntry = inputStream.getNextArEntry();
        ArArchiveEntry expectedEntry = expectedArchive.getNextEntry();

        Assertions.assertNotNull(actualEntry);
        Assertions.assertEquals(expectedEntry.getName(), actualEntry.getName());
        Assertions.assertEquals(expectedEntry.getSize(), actualEntry.getSize());

        inputStream.close();
        expectedArchive.close();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry actualEntry = inputStream.getNextEntry();
        ArchiveEntry expectedEntry = expectedArchive.getNextEntry();

        Assertions.assertNotNull(actualEntry);
        Assertions.assertEquals(expectedEntry.getName(), actualEntry.getName());
        Assertions.assertEquals(expectedEntry.getSize(), actualEntry.getSize());

        inputStream.close();
        expectedArchive.close();
    }

    @Test
    public void testInvalidSignature() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream inputStream = new ArArchiveInputStream(input);

        Assertions.assertThrows(IOException.class, () -> {
            inputStream.getNextArEntry();
        });

        input.close();
    }

    @Test
    public void testEndOfStream() throws IOException {
        ArArchiveInputStream inputStream = new ArArchiveInputStream(new ByteArrayInputStream(new byte[0]));

        Assertions.assertNull(inputStream.getNextArEntry());
        inputStream.close();
    }
}