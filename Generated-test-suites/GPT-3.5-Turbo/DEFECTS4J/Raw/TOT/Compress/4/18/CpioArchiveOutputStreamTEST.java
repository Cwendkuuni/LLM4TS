import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream cpioOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @BeforeEach
    public void setup() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        cpioOutputStream = new CpioArchiveOutputStream(byteArrayOutputStream);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        // Typical use case
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        entry.setMode(CpioConstants.C_ISREG);
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write("12345".getBytes());
        cpioOutputStream.closeArchiveEntry();
        assertTrue(byteArrayOutputStream.toString().startsWith("07070"));
    }

    @Test
    public void testPutArchiveEntryDuplicateName() throws IOException {
        // Error scenario - Entry with duplicate name
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile");
        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile");
        cpioOutputStream.putArchiveEntry(entry1);
        assertThrows(IOException.class, () -> cpioOutputStream.putArchiveEntry(entry2));
    }

    @Test
    public void testPutArchiveEntryInvalidSize() {
        // Error scenario - Invalid size
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG);
        cpioOutputStream.putArchiveEntry(entry);
        assertThrows(IOException.class, () -> cpioOutputStream.write("12345".getBytes()));
    }
  
    @Test
    public void testCloseArchiveEntry() throws IOException {
        // Typical use case
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        entry.setMode(CpioConstants.C_ISREG);
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write("12345".getBytes());
        cpioOutputStream.closeArchiveEntry();
        cpioOutputStream.finish();
        cpioOutputStream.close();
    }

    @Test
    public void testCloseArchiveEntryInvalidSize() {
        // Error scenario - Invalid entry size
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG);
        cpioOutputStream.putArchiveEntry(entry);
        assertThrows(IOException.class, () -> cpioOutputStream.closeArchiveEntry());
    }

    @Test
    public void testFinish() throws IOException {
        // Typical use case
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        entry.setMode(CpioConstants.C_ISREG);
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write("12345".getBytes());
        cpioOutputStream.closeArchiveEntry();
        cpioOutputStream.finish();
        assertTrue(byteArrayOutputStream.toString().contains("07070707070707000100011345442b02testfile"));
    }

    @Test
    public void testClose() throws IOException {
        // Typical use case
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        entry.setMode(CpioConstants.C_ISREG);
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write("12345".getBytes());
        cpioOutputStream.closeArchiveEntry();
        cpioOutputStream.finish();
        cpioOutputStream.close();
    }

}