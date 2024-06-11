package org.apache.commons.compress.utils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;

import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead_singleByte() {
        byte[] testData = { 1, 2, 3 };
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(checksum, bais);

        try {
            int firstByte = ccis.read();
            assertEquals(1, firstByte);
            assertEquals(1, checksum.getValue());
        } catch (IOException e) {
            fail("Exception thrown when not expected");
        }
    }

    @Test
    public void testRead_byteArray() {
        byte[] testData = { 1, 2, 3, 4, 5 };
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(checksum, bais);
        byte[] readData = new byte[3];

        try {
            int bytesRead = ccis.read(readData);
            assertEquals(3, bytesRead);
            assertEquals(0xD6DC6ACC, checksum.getValue());
        } catch (IOException e) {
            fail("Exception thrown when not expected");
        }
    }

    @Test
    public void testRead_byteArrayWithOffsetAndLength() {
        byte[] testData = { 1, 2, 3, 4, 5 };
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(checksum, bais);
        byte[] readData = new byte[3];

        try {
            int bytesRead = ccis.read(readData, 0, 3);
            assertEquals(3, bytesRead);
            assertEquals(0xD6DC6ACC, checksum.getValue());
        } catch (IOException e) {
            fail("Exception thrown when not expected");
        }
    }

    @Test
    public void testSkip() {
        byte[] testData = { 1, 2, 3 };
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(checksum, bais);

        try {
            long skipped = ccis.skip(2);
            assertEquals(0, skipped);
            assertEquals(0, checksum.getValue());
        } catch (IOException e) {
            fail("Exception thrown when not expected");
        }
    }

    @Test
    public void testGetValue() {
        byte[] testData = { 1, 2, 3, 4, 5 };
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(checksum, bais);

        try {
            while (ccis.read() != -1) {
                // Reading the whole stream to calculate checksum
            }
            assertEquals(0xD6DC6ACC, ccis.getValue());
        } catch (IOException e) {
            fail("Exception thrown when not expected");
        }
    }

    @Test
    public void testIOExceptionOnRead() {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[0]) {
            @Override
            public synchronized int read() {
                throw new RuntimeException("Fake exception");
            }
        };
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(checksum, bais);

        try {
            ccis.read();
            fail("Exception not thrown when expected");
        } catch (IOException e) {
            assertTrue(true); // Expected behavior
        }
    }

}