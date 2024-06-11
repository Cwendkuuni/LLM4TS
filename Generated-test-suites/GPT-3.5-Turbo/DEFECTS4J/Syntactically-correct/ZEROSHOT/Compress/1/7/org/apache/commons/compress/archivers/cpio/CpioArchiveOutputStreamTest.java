import org.junit.Test;

import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutNextEntry() {
        // Test case 1
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(null);
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile");
        entry1.setFileSize(5);
        try {
            outputStream.putNextEntry(entry1);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        
        // Test case 2
        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile");
        entry2.setFormat((short) 2);
        try {
            outputStream.putNextEntry(entry2);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }

        // Test case 3
        CpioArchiveEntry entry3 = new CpioArchiveEntry();
        entry3.setName("testfile");
        entry3.setSize(0);
        try {
            outputStream.putNextEntry(entry3);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCloseArchiveEntry() {
        // Test case 1
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(null);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(5);
        try {
            outputStream.putNextEntry(entry);
            outputStream.closeArchiveEntry();
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        
        // Test case 2
        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile");
        entry2.setSize(10);
        try {
            outputStream.putNextEntry(entry2);
            outputStream.write(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
            outputStream.closeArchiveEntry();
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testWrite() {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(null);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(5);
        try {
            outputStream.putNextEntry(entry);
            outputStream.write(new byte[]{1, 2, 3, 4, 5});
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testFinish() {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(null);
        try {
            outputStream.finish();
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testClose() {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(null);
        try {
            outputStream.close();
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
}