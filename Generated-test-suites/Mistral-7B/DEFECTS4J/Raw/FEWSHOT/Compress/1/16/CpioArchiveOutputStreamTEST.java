package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutNextEntryWithValidEntry() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new MockOutputStream(), CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);
        entry.setSize(10);
        entry.setTime(System.currentTimeMillis());

        out.putNextEntry(entry);

        assertEquals(10, out.getNextEntry().getSize());
        assertEquals("testfile", out.getNextEntry().getName());
    }

    @Test
    public void testPutNextEntryWithNullEntry() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new MockOutputStream(), CpioConstants.FORMAT_NEW);

        out.putNextEntry(null);

        assertNull(out.getNextEntry());
    }

    @Test
    public void testPutNextEntryWithDuplicateEntry() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new MockOutputStream(), CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile");
        entry1.setFormat(CpioConstants.FORMAT_NEW);
        entry1.setSize(10);
        entry1.setTime(System.currentTimeMillis());

        out.putNextEntry(entry1);
        out.putNextEntry(entry1);

        assertEquals(entry1, out.getNextEntry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNextEntryWithInvalidFormat() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new MockOutputStream(), (short) 0xff);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);
        entry.setSize(10);
        entry.setTime(System.currentTimeMillis());

        out.putNextEntry(entry);
    }

    @Test
    public void testCloseArchiveEntry() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new MockOutputStream(), CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);
        entry.setSize(10);
        entry.setTime(System.currentTimeMillis());

        out.putNextEntry(entry);
        out.write(new byte[] { 0x01, 0x02, 0x03 });

        out.closeArchiveEntry();

        assertEquals(0, out.getNextEntry().getSize());
        assertNull(out.getNextEntry());
    }

    @Test
    public void testWriteToCurrentEntry() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new MockOutputStream(), CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);
        entry.setSize(10);
        entry.setTime(System.currentTimeMillis());

        out.putNextEntry(entry);

        out.write(new byte[] { 0x01, 0x02, 0x03 });

        byte[] expectedData = new byte[] { 0x01, 0x02, 0x03 };
        byte[] actualData = out.getNextEntry().getData();

        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testFinish() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new MockOutputStream(), CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile1");
        entry1.setFormat(CpioConstants.FORMAT_NEW);
        entry1.setSize(10);
        entry1.setTime(System.currentTimeMillis());

        out.putNextEntry(entry1);
        out.write(new byte[] { 0x01, 0x02, 0x03 });

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile2");
        entry2.setFormat(CpioConstants.FORMAT_NEW);
        entry2.setSize(20);
        entry2.setTime(System.currentTimeMillis());

        out.putNextEntry(entry2);
        out.write(new byte[] { 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b });

        out.finish();

        CpioArchiveEntry trailer = out.getNextEntry();
        assertEquals("TRAILER!!!", trailer.getName());
        assertEquals(0, trailer.getSize());
    }

    @Test
    public void testClose() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new MockOutputStream(), CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);
        entry.setSize(10);
        entry.setTime(System.currentTimeMillis());

        out.putNextEntry(entry);

        out.close();

        assertFalse(out.isOpen());
    }

    @Test
    public void testIsOpen() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new MockOutputStream(), CpioConstants.FORMAT_NEW);

        assertTrue(out.isOpen());

        out.close();

        assertFalse(out.isOpen());
    }

    private static class MockOutputStream extends OutputStream {
        @Override
        public void write(int b) throws IOException {
            // do nothing
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            // do nothing
        }

        @Override
        public void close() throws IOException {
            // do nothing
        }

        @Override
        public void flush() throws IOException {
            // do nothing
        }
    }
}