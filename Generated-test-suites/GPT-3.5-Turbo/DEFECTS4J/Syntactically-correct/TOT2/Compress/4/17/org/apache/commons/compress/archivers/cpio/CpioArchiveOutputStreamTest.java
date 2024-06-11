import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(baos);
        
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setMode(CpioConstants.C_ISREG);
        
        cpioOut.putArchiveEntry(entry);
        cpioOut.closeArchiveEntry();
        
        cpioOut.finish();
        cpioOut.close();
        
        // Check if entry was written
        assertNotNull(baos.toByteArray());
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(baos);
        
        byte[] data = "test data".getBytes();
        
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setMode(CpioConstants.C_ISREG);
        entry.setSize(data.length);
        
        cpioOut.putArchiveEntry(entry);
        cpioOut.write(data, 0, data.length);
        cpioOut.closeArchiveEntry();
        
        cpioOut.finish();
        cpioOut.close();
        
        // Check if data was written
        assertArrayEquals(data, baos.toByteArray());
    }

    @Test
    public void testPutArchiveEntryWithCRC() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW_CRC);
        
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setMode(CpioConstants.C_ISREG);
        entry.calculateChksum();
        
        cpioOut.putArchiveEntry(entry);
        cpioOut.closeArchiveEntry();
        
        cpioOut.finish();
        cpioOut.close();
        
        // Check if CRC was calculated correctly
        CRC32 crc = new CRC32();
        crc.update(baos.toByteArray());
        assertEquals(entry.getChksum(), crc.getValue());
    }

    @Test(expected = IOException.class)
    public void testPutDuplicateEntry() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(baos);
        
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile");
        entry1.setMode(CpioConstants.C_ISREG);
        
        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile");
        entry2.setMode(CpioConstants.C_ISREG);
        
        cpioOut.putArchiveEntry(entry1);
        cpioOut.putArchiveEntry(entry2); // Should throw IOException
        
        cpioOut.finish();
        cpioOut.close();
    }

    @Test(expected = IOException.class)
    public void testWritePastEndOfEntry() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(baos);
        
        byte[] data = "test data".getBytes();
        
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setMode(CpioConstants.C_ISREG);
        entry.setSize(data.length);
        
        cpioOut.putArchiveEntry(entry);
        cpioOut.write(data, 0, data.length + 1); // Should throw IOException
    }
}