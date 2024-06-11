import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;

import java.io.*;
import java.util.Arrays;

import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream cpioArchiveOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] expectedBytes = ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW);
        expectedBytes = Arrays.copyOf(expectedBytes, expectedBytes.length + 110);
        byte[] actualBytes = Arrays.copyOf(outputStream.toByteArray(), 110);

        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);
        entry.setSize(5);
        entry.setData(new byte[]{49, 50, 51, 52, 53});

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(entry.getData());

        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] expectedBytes = ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW);
        Arrays.copyOf(expectedBytes, expectedBytes.length + 110);
        expectedBytes = Arrays.copyOf(expectedBytes, expectedBytes.length + 5);
        byte[] actualBytes = Arrays.copyOf(outputStream.toByteArray(), 5);

        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testWrite() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);
        entry.setSize(5);

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(new byte[]{49, 50, 51, 52, 53});

        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] expectedBytes = ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW);
        expectedBytes = Arrays.copyOf(expectedBytes, expectedBytes.length + 110);
        expectedBytes = Arrays.copyOf(expectedBytes, expectedBytes.length + 5);
        byte[] actualBytes = Arrays.copyOf(outputStream.toByteArray(), 5);

        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testFinish() throws IOException {
        cpioArchiveOutputStream.finish();

        byte[] expectedBytes = ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW);
        expectedBytes = Arrays.copyOf(expectedBytes, expectedBytes.length + 110);
        expectedBytes = Arrays.copyOf(expectedBytes, expectedBytes.length + 121);
        byte[] actualBytes = Arrays.copyOf(outputStream.toByteArray(), 121);

        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testClose() throws IOException {
        cpioArchiveOutputStream.close();

        assertTrue(cpioArchiveOutputStream.isClosed());
    }
}