
import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import java.io.*;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        try {
            tarOs.putArchiveEntry(entry);
            tarOs.closeArchiveEntry();
            tarOs.finish();
            tarOs.close();
            String output = os.toString();
            Assert.assertNotEquals(0, output.length());
        } catch (IOException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFinish() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        try {
            tarOs.finish();
            tarOs.close();
            String output = os.toString();
            Assert.assertNotEquals(0, output.length());
            Assert.assertEquals(2 * TarArchiveOutputStream.DEFAULT_RCDSIZE, output.length());
        } catch (IOException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testClose() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        try {
            tarOs.close();
            String output = os.toString();
            Assert.assertEquals(0, output.length());
        } catch (IOException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetRecordSize() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        int recordSize = tarOs.getRecordSize();
        Assert.assertEquals(TarArchiveOutputStream.DEFAULT_RCDSIZE, recordSize);
    }

    @Test
    public void testWrite() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "This is a test".getBytes());
        try {
            tarOs.putArchiveEntry(entry);
            tarOs.write("This is a test".getBytes());
            tarOs.closeArchiveEntry();
            tarOs.finish();
            tarOs.close();
            String output = os.toString();
            Assert.assertNotEquals(0, output.length());
            Assert.assertTrue(output.contains("This is a test"));
        } catch (IOException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testWriteEOFRecord() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        try {
            tarOs.writeEOFRecord();
            tarOs.finish();
            tarOs.close();
            String output = os.toString();
            Assert.assertNotEquals(0, output.length());
        } catch (IOException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFlush() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        try {
            tarOs.flush();
        } catch (IOException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveEntry() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        File inputFile = new File("test.txt");
        String entryName = "test.txt";
        try {
            ArchiveEntry entry = tarOs.createArchiveEntry(inputFile, entryName);
            Assert.assertNotNull(entry);
            Assert.assertEquals(entryName, entry.getName());
        } catch (IOException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

}
