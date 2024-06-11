import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream out;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws Exception {
        out = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(out);
    }

    @After
    public void tearDown() throws Exception {
        tarOutputStream.close();
    }

    @Test
    public void testConstructor() {
        // verify default constructor works
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullOutputStream() {
        new TarArchiveOutputStream(null);
    }

    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertThat(tarOutputStream.getLongFileMode(), is(TarArchiveOutputStream.LONGFILE_TRUNCATE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeWithInvalidMode() {
        tarOutputStream.setLongFileMode(3);
    }

    @Test
    public void testFinish() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();

        byte[] expectedHeader = entry.getHeaderBytes();
        byte[] expectedEof = new byte[TarConstants.STANDARD_MAGIC + TarConstants.STANDARD_MAGIC + 2 * TarConstants.RECORD_LENGTH];

        byte[] output = out.toByteArray();

        for (int i = 0; i < expectedHeader.length; i++) {
            assertThat(output[i], is(expectedHeader[i]));
        }

        for (int i = expectedHeader.length; i < expectedEof.length; i++) {
            assertThat(output[i], is((byte) 0));
        }
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);

        tarOutputStream.close(); // should throw an exception
        tarOutputStream.finish();
    }

    @Test
    public void testClose() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.close();

        byte[] output = out.toByteArray();
        byte[] expectedHeader = entry.getHeaderBytes();

        for (int i = 0; i < expectedHeader.length; i++) {
            assertThat(output[i], is(expectedHeader[i]));
        }

        // there should be no extra bytes after the header
        assertThat(output.length, is(expectedHeader.length));
    }

    @Test
    public void testWrite() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);

        byte[] data = "Hello, World!".getBytes();
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();

        byte[] output = out.toByteArray();

        byte[] expectedHeader = entry.getHeaderBytes();
        byte[] expectedData = entry.getBodyBytes("Hello, World!".getBytes());

        for (int i = 0; i < expectedHeader.length; i++) {
            assertThat(output[i], is(expectedHeader[i]));
        }

        for (int i = expectedHeader.length; i < expectedHeader.length + expectedData.length; i++) {
            assertThat(output[i], is(expectedData[i - expectedHeader.length]));
        }
    }

    @Test(expected = IOException.class)
    public void testWriteWithUnclosedEntry() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);

        byte[] data = "Hello, World!".getBytes();
        tarOutputStream.write(data, 0, data.length);

        tarOutputStream.close(); // should throw an exception
    }

    @Test
    public void testWriteWithBuffer() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);

        byte[] buffer = new byte[10];
        tarOutputStream.write(buffer, 0, 5);
        tarOutputStream.write(buffer, 5, 5);
        tarOutputStream.closeArchiveEntry();

        byte[] output = out.toByteArray();

        byte[] expectedHeader = entry.getHeaderBytes();
        byte[] expectedData = new byte[10];
        System.arraycopy(buffer, 0, expectedData, 0, 10);

        for (int i = 0; i < expectedHeader.length; i++) {
            assertThat(output[i], is(expectedHeader[i]));
        }

        for (int i = expectedHeader.length; i < expectedHeader.length + expectedData.length; i++) {
            assertThat(output[i], is(expectedData[i - expectedHeader.length]));
        }
    }

    @Test(expected = IOException.class)
    public void testWriteWithUnclosedEntryAndBuffer() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);

        byte[] buffer = new byte[10];
        tarOutputStream.write(buffer, 0, 5);
        tarOutputStream.write(buffer, 5, 5);

        tarOutputStream.close(); // should throw an exception
    }

    @Test
    public void testCreateArchiveEntry() throws Exception {
        File tempFile = File.createTempFile("test", null);
        TarArchiveEntry entry = tarOutputStream.createArchiveEntry(tempFile, "test.txt");

        assertThat(entry.getName(), is("test.txt"));
        assertThat(entry.getSize(), is(tempFile.length()));
    }

    @Test
    public void testCreateArchiveEntryWithNullFileName() {
        try {
            tarOutputStream.createArchiveEntry(null, "test.txt");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveEntryWithEmptyFileName() {
        try {
            tarOutputStream.createArchiveEntry(new File(""), "test.txt");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveEntryWithNullFile() {
        try {
            tarOutputStream.createArchiveEntry(null, "");
        } catch (NullPointerException e) {
            // expected
        }
    }
}