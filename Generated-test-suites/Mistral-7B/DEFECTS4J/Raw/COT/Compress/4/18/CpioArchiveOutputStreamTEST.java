import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CpioArchiveOutputStreamTest {

    private File outputFile;
    private CpioArchiveOutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        outputFile = File.createTempFile("test", ".cpio");
        outputStream = new CpioArchiveOutputStream(new FileOutputStream(outputFile));
    }

    @After
    public void tearDown() throws Exception {
        outputStream.close();
        outputFile.delete();
    }

    @Test
    public void testConstructor() {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                new FileOutputStream(new File("output.cpio")));
        Assert.assertEquals(CpioConstants.FORMAT_NEW, outputStream.getFormat());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatConstructor() {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                new FileOutputStream(new File("output.cpio")), (short) 12345);
    }

    @Test
    public void testPutArchiveEntry() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test");
        outputStream.putArchiveEntry(entry);
        Assert.assertEquals(entry, outputStream.getNextEntry());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryAlreadyHasEntry() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test");
        outputStream.putArchiveEntry(entry);
        outputStream.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntryWithInvalidTime() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test");
        entry.setTime(-1);
        outputStream.putArchiveEntry(entry);
        Assert.assertNotNull(outputStream.getNextEntry());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithEmptyName() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("");
        outputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullName() throws Exception {
        CpioArchiveEntry entry = null;
        outputStream.putArchiveEntry(entry);
    }

    @Test
    public void testWrite() throws Exception {
        String data = "test data";
        byte[] bytes = data.getBytes();

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test");
        outputStream.putArchiveEntry(entry);

        outputStream.write(bytes, 0, bytes.length);

        Assert.assertEquals(data, outputStream.readAllBytes());
    }

    @Test
    public void testWriteWithInvalidOffset() throws Exception {
        String data = "test data";
        byte[] bytes = data.getBytes();

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test");
        outputStream.putArchiveEntry(entry);

        int offset = bytes.length + 1;

        try {
            outputStream.write(bytes, offset, bytes.length);
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testWriteWithInvalidLength() throws Exception {
        String data = "test data";
        byte[] bytes = data.getBytes();

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test");
        outputStream.putArchiveEntry(entry);

        int length = bytes.length - 1;

        try {
            outputStream.write(bytes, 0, length);
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testWriteWithInvalidData() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test");
        outputStream.putArchiveEntry(entry);

        try {
            outputStream.write(new byte[0], 0, 1);
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testFinish() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test1");
        outputStream.putArchiveEntry(entry);
        outputStream.write("test1 data".getBytes());

        entry = new CpioArchiveEntry();
        entry.setName("test2");
        outputStream.putArchiveEntry(entry);
        outputStream.write("test2 data".getBytes());

        outputStream.finish();

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        FileInputStream inputStream = new FileInputStream(outputFile);
        byte[] bytes = new byte[1024];
        int len;
        while ((len = inputStream.read(bytes, 0, bytes.length)) != -1) {
            buffer.write(bytes, 0, len);
        }
        inputStream.close();

        String content = new String(buffer.toByteArray());

        Map<String, Integer> expected = new HashMap<>();
        expected.put("test1", 0);
        expected.put("test2", 0);
        expected.put(CpioConstants.CPIO_TRAILER, 0);

        Map<String, Integer> actual = new HashMap<>();
        String[] lines = content.split("\n");
        for (String line : lines) {
            if (line.startsWith("NAME  ")) {
                String name = line.substring(7).trim();
                int position = Integer.parseInt(line.substring(13).trim());
                actual.put(name, position);
            }
        }

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testClose() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test");
        outputStream.putArchiveEntry(entry);

        outputStream.close();

        try {
            outputStream.write("data".getBytes());
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }
}