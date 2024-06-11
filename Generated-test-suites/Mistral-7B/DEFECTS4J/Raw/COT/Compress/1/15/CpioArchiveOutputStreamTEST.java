import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @Test
    public void testConstructorWithFormat() throws IOException {
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        Assert.assertEquals(CpioConstants.FORMAT_NEW, cpioOutputStream.getFormat());
    }

    @Test
    public void testConstructorWithoutFormat() throws IOException {
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        Assert.assertEquals(CpioConstants.FORMAT_NEW, cpioOutputStream.getFormat());
    }

    @Test
    public void testEnsureOpen() throws IOException {
        cpioOutputStream.ensureOpen();
        cpioOutputStream.close();
        try {
            cpioOutputStream.ensureOpen();
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testPutNextEntryWithValidEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(10);
        cpioOutputStream.putNextEntry(entry);
        byte[] headerBytes = outputStream.toByteArray();
        byte[] magicBytes = CpioConstants.MAGIC_NEW.getBytes();
        Assert.assertArrayEquals(magicBytes, Arrays.copyOfRange(headerBytes, 0, magicBytes.length));
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithDuplicateEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioOutputStream.putNextEntry(entry);
        try {
            cpioOutputStream.putNextEntry(entry);
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testWrite() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(10);
        cpioOutputStream.putNextEntry(entry);
        byte[] data = "test data".getBytes();
        cpioOutputStream.write(data, 0, data.length);
        byte[] expectedBytes = new byte[data.length + 10 + 1];
        System.arraycopy(data, 0, expectedBytes, 0, data.length);
        for (int i = 0; i < 10; i++) {
            expectedBytes[data.length + i] = (byte) 0xFF;
        }
        byte[] actualBytes = outputStream.toByteArray();
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test(expected = IOException.class)
    public void testWriteWithClosedStream() throws IOException {
        cpioOutputStream.close();
        byte[] data = "test data".getBytes();
        cpioOutputStream.write(data, 0, data.length);
        Assert.fail();
    }

    @Test
    public void testFinish() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(10);
        cpioOutputStream.putNextEntry(entry);
        byte[] data = "test data".getBytes();
        cpioOutputStream.write(data, 0, data.length);
        cpioOutputStream.finish();
        byte[] expectedBytes = new byte[10 + data.length + 1 + CpioConstants.MAGIC_NEW.length];
        System.arraycopy(CpioConstants.MAGIC_NEW.getBytes(), 0, expectedBytes, 0, CpioConstants.MAGIC_NEW.length);
        System.arraycopy(data, 0, expectedBytes, CpioConstants.MAGIC_NEW.length, data.length);
        for (int i = 0; i < 10; i++) {
            expectedBytes[CpioConstants.MAGIC_NEW.length + data.length + i] = (byte) 0xFF;
        }
        byte[] actualBytes = outputStream.toByteArray();
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test(expected = IOException.class)
    public void testFinishWithOpenEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.finish();
        try {
            cpioOutputStream.finish();
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }
}