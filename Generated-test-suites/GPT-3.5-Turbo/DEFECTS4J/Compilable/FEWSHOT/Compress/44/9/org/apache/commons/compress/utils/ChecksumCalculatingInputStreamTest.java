
package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.util.zip.CRC32;
import java.util.zip.Adler32;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead() {
        byte[] input = "Hello, World!".getBytes();
        ByteArrayInputStream stream = new ByteArrayInputStream(input);
        ChecksumCalculatingInputStream in = new ChecksumCalculatingInputStream(new CRC32(), stream);

        for (int i = 0; i < input.length; i++) {
            try {
                int value = in.read();
                assertEquals(input[i], (byte) value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        assertEquals(in.getValue(), 547593967L);
    }

    @Test
    public void testReadByteArray() {
        byte[] input = "Hello, World!".getBytes();
        ByteArrayInputStream stream = new ByteArrayInputStream(input);
        ChecksumCalculatingInputStream in = new ChecksumCalculatingInputStream(new CRC32(), stream);
        byte[] buffer = new byte[5];
        int bytesRead;

        try {
            while ((bytesRead = in.read(buffer)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    assertEquals(buffer[i], input[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(in.getValue(), 547593967L);
    }

    @Test
    public void testReadByteArrayWithOffset() {
        byte[] input = "Hello, World!".getBytes();
        ByteArrayInputStream stream = new ByteArrayInputStream(input);
        ChecksumCalculatingInputStream in = new ChecksumCalculatingInputStream(new CRC32(), stream);
        byte[] buffer = new byte[5];
        int bytesRead;

        try {
            while ((bytesRead = in.read(buffer, 1, 3)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    assertEquals(buffer[i + 1], input[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(in.getValue(), 547593967L);
    }

    @Test
    public void testSkip() {
        byte[] input = "Hello, World!".getBytes();
        ByteArrayInputStream stream = new ByteArrayInputStream(input);
        ChecksumCalculatingInputStream in = new ChecksumCalculatingInputStream(new CRC32(), stream);

        try {
            long skipped = in.skip(5);
            assertEquals(skipped, 5);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(in.getValue(), 3497268409L);
    }

    @Test
    public void testGetValue() {
        byte[] input = "Hello, World!".getBytes();
        ByteArrayInputStream stream = new ByteArrayInputStream(input);
        ChecksumCalculatingInputStream in = new ChecksumCalculatingInputStream(new CRC32(), stream);

        try {
            while (in.read() != -1) {
                // Reading the entire input to calculate checksum
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(in.getValue(), 547593967L);
    }
}
