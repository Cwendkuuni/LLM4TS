package org.apache.commons.compress.utils;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import static org.junit.Assert.*;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] input = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04};
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);

        int data;
        while ((data = stream.read()) != -1) {
            // Do nothing
        }
        
        assertEquals(checksum.getValue(), 674356850L);
        assertEquals(stream.read(), -1);

        stream.close();
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] input = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04};
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);

        byte[] buffer = new byte[3];
        stream.read(buffer);
        assertEquals(checksum.getValue(), 2678197614L);
        assertArrayEquals(buffer, new byte[]{0x00, 0x01, 0x02});

        stream.read(buffer);
        assertEquals(checksum.getValue(), 118817503L);
        assertArrayEquals(buffer, new byte[]{0x03, 0x04, 0x02});

        stream.close();
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] input = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04};
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);

        byte[] buffer = new byte[5];
        stream.read(buffer, 1, 3);
        assertEquals(checksum.getValue(), 268920001L);
        assertArrayEquals(buffer, new byte[]{0x00, 0x00, 0x01, 0x02, 0x00});

        stream.read(buffer, 2, 2);
        assertEquals(checksum.getValue(), 119709474L);
        assertArrayEquals(buffer, new byte[]{0x00, 0x00, 0x03, 0x04, 0x00});

        stream.close();
    }

    @Test
    public void testSkip() throws IOException {
        byte[] input = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04};
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);

        long skipped = stream.skip(3);
        assertEquals(skipped, 3);
        assertEquals(checksum.getValue(), 67261231L);

        skipped = stream.skip(3);
        assertEquals(skipped, 0);
        assertEquals(checksum.getValue(), 67261231L);

        stream.close();
    }

    @Test
    public void testGetValue() throws IOException {
        byte[] input = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04};
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);

        int data;
        while ((data = stream.read()) != -1) {
            // Do nothing
        }

        assertEquals(stream.getValue(), checksum.getValue());

        stream.close();
    }

}