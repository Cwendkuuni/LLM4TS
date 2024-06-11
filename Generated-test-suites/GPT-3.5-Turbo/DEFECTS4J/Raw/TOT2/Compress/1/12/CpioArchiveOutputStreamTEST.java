import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutNextEntry() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(baos);

        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        entry.setSize(10);
        try {
            cpioOutput.putNextEntry(entry);
            cpioOutput.write(new byte[10]);
            cpioOutput.closeArchiveEntry();
        } catch (IOException e) {
            fail("Exception not expected");
        }

        String expectedEntryName = "test.txt";
        String actualEntryName = cpioOutput.getNames().keySet().iterator().next();
        assertEquals(expectedEntryName, actualEntryName);
    }

    @Test
    public void testWrite() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(baos);
        byte[] data = new byte[10];

        try {
            cpioOutput.putNextEntry(new CpioArchiveEntry("test.txt"));
            cpioOutput.write(data, 0, 5);
        } catch (IOException e) {
            fail("Exception not expected");
        }

        assertEquals(5, cpioOutput.getWritten());
    }

    @Test
    public void testFinish() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(baos);

        try {
            cpioOutput.finish();
            cpioOutput.close();
        } catch (IOException e) {
            fail("Exception not expected");
        }

        assertTrue(cpioOutput.isFinished());
    }

    @Test
    public void testClose() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(baos);

        try {
            cpioOutput.close();
        } catch (IOException e) {
            fail("Exception not expected");
        }

        assertTrue(cpioOutput.isClosed());
    }

    @Test
    public void testCRC32() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CheckedOutputStream checkedOutput = new CheckedOutputStream(baos, new CRC32());
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(checkedOutput);

        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        entry.setSize(10);
        try {
            cpioOutput.putNextEntry(entry);
            cpioOutput.write(new byte[10]);
            cpioOutput.closeArchiveEntry();
            cpioOutput.finish();
        } catch (IOException e) {
            fail("Exception not expected");
        }

        long expectedChecksum = checkedOutput.getChecksum().getValue();
        assertEquals(expectedChecksum, cpioOutput.getCrc());
    }

}