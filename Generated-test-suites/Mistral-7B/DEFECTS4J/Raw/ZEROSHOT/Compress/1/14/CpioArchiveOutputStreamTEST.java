import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Tests for the CpioArchiveOutputStream class
 */
public class CpioArchiveOutputStreamTest {

    @Test
    public void testCpioArchiveOutputStream() throws IOException {
        // Test construction with new format
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW);

        // Test construction with default format
        CpioArchiveOutputStream cpioOutDefault = new CpioArchiveOutputStream(baos);

        // Test writing entries and data
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("test1.txt");
        entry1.setSize(10);
        cpioOut.putNextEntry(entry1);
        String contents1 = "Hello World!";
        cpioOut.write(contents1.getBytes());
        cpioOut.finish();

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("test2.txt");
        entry2.setSize(20);
        cpioOut.putNextEntry(entry2);
        String contents2 = "Test entry 2.";
        cpioOut.write(contents2.getBytes());
        cpioOut.finish();

        byte[] expectedData = ("Hello World!\0" + "Test entry 2." + "\0").getBytes();
        byte[] actualData = baos.toByteArray();
        Assert.assertArrayEquals("Unexpected data written to stream", expectedData, actualData);

        // Test writing duplicate entry
        try {
            cpioOut.putNextEntry(entry1);
            Assert.fail("Expected an IOException");
        } catch (IOException e) {
            // Expected
        }

        // Test writing entry with invalid format
        CpioArchiveEntry entry3 = new CpioArchiveEntry();
        entry3.setFormat(CpioConstants.FORMAT_OLD_BINARY + 1);
        try {
            cpioOutDefault.putNextEntry(entry3);
            Assert.fail("Expected an IOException");
        } catch (IOException e) {
            // Expected
        }

        // Test writing entry with invalid name length
        CpioArchiveEntry entry4 = new CpioArchiveEntry();
        entry4.setName("test".getBytes());
        try {
            cpioOutDefault.putNextEntry(entry4);
            Assert.fail("Expected an IOException");
        } catch (IOException e) {
            // Expected
        }

        // Test writing entry with invalid size
        CpioArchiveEntry entry5 = new CpioArchiveEntry();
        entry5.setName("test5.txt");
        entry5.setSize(-10);
        try {
            cpioOutDefault.putNextEntry(entry5);
            Assert.fail("Expected an IOException");
        } catch (IOException e) {
            // Expected
        }

        // Test writing entry with invalid time
        CpioArchiveEntry entry6 = new CpioArchiveEntry();
        entry6.setName("test6.txt");
        entry6.setTime(-1);
        try {
            cpioOutDefault.putNextEntry(entry6);
            Assert.fail("Expected an IOException");
        } catch (IOException e) {
            // Expected
        }

        // Test write past end of entry
        CpioArchiveEntry entry7 = new CpioArchiveEntry();
        entry7.setName("test7.txt");
        entry7.setSize(5);
        cpioOutDefault.putNextEntry(entry7);
        String contents7 = "Test entry 7.";
        byte[] data7 = contents7.getBytes();
        cpioOutDefault.write(data7, 0, data7.length + 1);
        try {
            cpioOutDefault.write(data7, 0, data7.length + 1);
            Assert.fail("Expected an IOException");
        } catch (IOException e) {
            // Expected
        }

        cpioOut.close();
        cpioOutDefault.close();
    }
}