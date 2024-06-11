import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.test.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    @Test
    public void testValidArEntry() throws IOException {
        final byte[] header = ArArchiveInputStream.HEADER.getBytes();
        final byte[] name = "test.txt".getBytes();
        final byte[] length = "123456789012345".getBytes();
        final byte[] trailer = ArArchiveInputStream.TRAILER.getBytes();

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(header);
        output.write(name);
        output.write(length);
        output.write(trailer);

        final ArchiveInputStream input = new ArArchiveInputStream(new ByteArrayInputStream(output.toByteArray()));
        final ArchiveEntry entry = input.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertTrue(entry instanceof ArArchiveEntry);
        Assert.assertEquals("test.txt", ((ArArchiveEntry) entry).getName());
        Assert.assertEquals(123456789012345L, ((ArArchiveEntry) entry).getSize());

        input.close();
    }

    @Test
    public void testInvalidHeader() throws IOException {
        final byte[] invalidHeader = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x01}; // different length
        final ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{0x00, 0x01, 0x02});

        try {
            new ArArchiveInputStream(input);
            Assert.fail();
        } catch (IOException e) {
            Assert.assertEquals("failed to read header", e.getMessage());
        }
    }

    @Test
    public void testNoNextEntry() throws IOException {
        final byte[] header = ArArchiveInputStream.HEADER.getBytes();
        final byte[] trailer = ArArchiveInputStream.TRAILER.getBytes();

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(header);
        output.write(trailer);

        final ArchiveInputStream input = new ArArchiveInputStream(new ByteArrayInputStream(output.toByteArray()));

        final ArchiveEntry entry = input.getNextEntry();
        Assert.assertNotNull(entry);
        input.close();

        final ArchiveEntry noEntry = input.getNextEntry();
        Assert.assertNull(noEntry);
    }

    @Test
    public void testGetNextEntryMultipleTimes() throws IOException {
        final byte[] header = ArArchiveInputStream.HEADER.getBytes();
        final byte[] name = "test1.txt".getBytes();
        final byte[] length = "123456789012345".getBytes();
        final byte[] trailer = ArArchiveInputStream.TRAILER.getBytes();

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(header);
        output.write(name);
        output.write(length);
        output.write(trailer);

        final ByteArrayOutputStream output2 = new ByteArrayOutputStream();
        output2.write(header);
        output2.write(name);
        output2.write(length);
        output2.write(trailer);

        final ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());
        final ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);

        final ArchiveEntry entry1 = arInputStream.getNextEntry();
        Assert.assertNotNull(entry1);
        final ArchiveEntry entry2 = arInputStream.getNextEntry();
        Assert.assertNotNull(entry2);

        final ArchiveInputStream testInputStream = new ArArchiveInputStream(new ByteArrayInputStream(output2.toByteArray()));
        final ArchiveEntry entry3 = testInputStream.getNextEntry();
        Assert.assertNotNull(entry3);

        arInputStream.close();
        testInputStream.close();
    }

    @Test
    public void testInvalidTrailer() throws IOException {
        final byte[] header = ArArchiveInputStream.HEADER.getBytes();
        final byte[] name = "test.txt".getBytes();
        final byte[] length = "123456789012345".getBytes();
        final byte[] invalidTrailer = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x02}; // different last byte

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(header);
        output.write(name);
        output.write(length);
        output.write(invalidTrailer);

        try {
            final ArchiveInputStream input = new ArArchiveInputStream(new ByteArrayInputStream(output.toByteArray()));
            input.getNextEntry();
            Assert.fail();
        } catch (IOException e) {
            Assert.assertEquals("invalid entry header. not read the content?", e.getMessage());
        }
    }

    @Test
    public void testReadAfterGetNextEntry() throws IOException {
        final byte[] header = ArArchiveInputStream.HEADER.getBytes();
        final byte[] name = "test.txt".getBytes();
        final byte[] length = "123456789012345".getBytes();
        final byte[] trailer = ArArchiveInputStream.TRAILER.getBytes();

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(header);
        output.write(name);
        output.write(length);
        output.write(trailer);

        final ArchiveInputStream input = new ArArchiveInputStream(new ByteArrayInputStream(output.toByteArray()));

        final ArchiveEntry entry = input.getNextEntry();
        Assert.assertNotNull(entry);

        final int read = input.read();
        Assert.assertEquals(-1, read);
    }
}