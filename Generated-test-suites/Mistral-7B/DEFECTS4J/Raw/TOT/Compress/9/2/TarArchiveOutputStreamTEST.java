import org.apache.commons.compress.archivers.tar.*;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.utils.ByteStreams;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * Tests for {@link TarArchiveOutputStream}.
 */
public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
    }

    @After
    public void tearDown() throws Exception {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
    }

    /**
     * Test writing a single file with a short name.
     * @throws IOException if an error occurs
     */
    @Test
    public void testWriteShortNameFile() throws IOException {
        String fileName = "file1.txt";
        File inputFile = new File("src/test/resources/" + fileName);
        byte[] fileContent = "Hello World!".getBytes(StandardCharsets.UTF_8);

        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(inputFile, fileName);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        // Check the final tar archive content
        byte[] tarContent = outputStream.toByteArray();
        ByteArrayInputStream testInput = new ByteArrayInputStream(tarContent);
        ArchiveStreamFactory factory = new ArchiveStreamFactoryImpl();
        TarInputStream tarInputStream = (TarInputStream) factory.createArchiveInputStream(testInput);

        TarEntry entryRead = tarInputStream.getNextEntry();
        assertEquals(fileName, entryRead.getName());
        assertArrayEquals(fileContent, ByteStreams.toByteArray(tarInputStream));
    }

    /**
     * Test writing a single file with a long name.
     * @throws IOException if an error occurs
     */
    @Test
    public void testWriteLongNameFile() throws IOException {
        String fileName = "long_file_name_with_spaces_and_special_characters.txt";
        File inputFile = new File("src/test/resources/" + fileName);
        byte[] fileContent = "Hello World!".getBytes(StandardCharsets.UTF_8);

        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(inputFile, fileName);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        // Check the final tar archive content
        byte[] tarContent = outputStream.toByteArray();
        ByteArrayInputStream testInput = new ByteArrayInputStream(tarContent);
        ArchiveStreamFactory factory = new ArchiveStreamFactoryImpl();
        TarInputStream tarInputStream = (TarInputStream) factory.createArchiveInputStream(testInput);

        TarEntry entryRead = tarInputStream.getNextEntry();
        assertEquals(fileName, entryRead.getName());
        assertArrayEquals(fileContent, ByteStreams.toByteArray(tarInputStream));
    }

    /**
     * Test writing multiple files.
     * @throws IOException if an error occurs
     */
    @Test
    public void testWriteMultipleFiles() throws IOException {
        String fileName1 = "file1.txt";
        String fileName2 = "file2.txt";
        File inputFile1 = new File("src/test/resources/" + fileName1);
        File inputFile2 = new File("src/test/resources/" + fileName2);
        byte[] fileContent1 = "Hello World!".getBytes(StandardCharsets.UTF_8);
        byte[] fileContent2 = "Hello Again!".getBytes(StandardCharsets.UTF_8);

        ArchiveEntry entry1 = tarArchiveOutputStream.createArchiveEntry(inputFile1, fileName1);
        tarArchiveOutputStream.putArchiveEntry(entry1);
        tarArchiveOutputStream.write(fileContent1, 0, fileContent1.length);
        tarArchiveOutputStream.closeArchiveEntry();

        ArchiveEntry entry2 = tarArchiveOutputStream.createArchiveEntry(inputFile2, fileName2);
        tarArchiveOutputStream.putArchiveEntry(entry2);
        tarArchiveOutputStream.write(fileContent2, 0, fileContent2.length);
        tarArchiveOutputStream.closeArchiveEntry();

        // Check the final tar archive content
        byte[] tarContent = outputStream.toByteArray();
        ByteArrayInputStream testInput = new ByteArrayInputStream(tarContent);
        ArchiveStreamFactory factory = new ArchiveStreamFactoryImpl();
        TarInputStream tarInputStream = (TarInputStream) factory.createArchiveInputStream(testInput);

        TarEntry entry1Read = tarInputStream.getNextEntry();
        assertEquals(fileName1, entry1Read.getName());
        assertArrayEquals(fileContent1, ByteStreams.toByteArray(tarInputStream));

        TarEntry entry2Read = tarInputStream.getNextEntry();
        assertEquals(fileName2, entry2Read.getName());
        assertArrayEquals(fileContent2, ByteStreams.toByteArray(tarInputStream));
    }

    /**
     * Test writing a long file with a truncated name.
     * @throws IOException if an error occurs
     */
    @Test(expected = RuntimeException.class)
    public void testWriteLongNameFileTruncated() throws IOException {
        String fileName = "long_file_name_with_" + TarConstants.NAMELEN + "_characters.txt";
        File inputFile = new File("src/test/resources/" + fileName);
        byte[] fileContent = "Hello World!".getBytes(StandardCharsets.UTF_8);

        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(inputFile, fileName);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        // Should throw a RuntimeException
        tarArchiveOutputStream.finish();
    }

    /**
     * Test writing a long file with an error mode.
     * @throws IOException if an error occurs
     */
    @Test(expected = IOException.class)
    public void testWriteLongNameFileError() throws IOException {
        String fileName = "long_file_name_with_" + TarConstants.NAMELEN + "_characters.txt";
        File inputFile = new File("src/test/resources/" + fileName);
        byte[] fileContent = "Hello World!".getBytes(StandardCharsets.UTF_8);

        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);

        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(inputFile, fileName);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        // Should throw an IOException
        tarArchiveOutputStream.finish();
    }
}