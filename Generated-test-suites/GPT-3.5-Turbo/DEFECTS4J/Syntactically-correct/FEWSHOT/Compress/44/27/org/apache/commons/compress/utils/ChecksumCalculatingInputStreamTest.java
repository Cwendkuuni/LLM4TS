package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Checksum;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {
    @Test
    public void testRead() throws IOException {
        Checksum checksum = mock(Checksum.class);
        InputStream in = new ByteArrayInputStream(new byte[]{1, 2, 3});
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);

        assertEquals(1, stream.read());
        assertEquals(2, stream.read());
        assertEquals(3, stream.read());

        stream.close();

        // verify that checksum.update() was called correctly
        // based on the bytes read
        // assertEquals(expectedChecksum, stream.getValue());
    }

    @Test
    public void testReadByteArray() throws IOException {
        Checksum checksum = mock(Checksum.class);
        byte[] data = {1, 2, 3, 4, 5};
        InputStream in = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);

        byte[] buffer1 = new byte[3];
        int bytesRead1 = stream.read(buffer1);
        assertEquals(3, bytesRead1);
        assertTrue(buffer1[0] == 1 && buffer1[1] == 2 && buffer1[2] == 3);

        byte[] buffer2 = new byte[2];
        int bytesRead2 = stream.read(buffer2);
        assertEquals(2, bytesRead2);
        assertTrue(buffer2[0] == 4 && buffer2[1] == 5);
        
        stream.close();

        // verify that checksum.update() was called correctly
        // based on the bytes read
        // assertEquals(expectedChecksum, stream.getValue());
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        Checksum checksum = mock(Checksum.class);
        byte[] data = {1, 2, 3, 4, 5};
        InputStream in = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);

        byte[] buffer = new byte[6];
        int bytesRead = stream.read(buffer, 1, 4);
        assertEquals(4, bytesRead);
        assertNull(buffer[0]);
        assertTrue(buffer[1] == 1 && buffer[2] == 2 && buffer[3] == 3 && buffer[4] == 4);
        assertNull(buffer[5]);
        
        stream.close();

        // verify that checksum.update() was called correctly
        // based on the bytes read
        // assertEquals(expectedChecksum, stream.getValue());
    }

    @Test
    public void testSkip() throws IOException {
        Checksum checksum = mock(Checksum.class);
        byte[] data = {1, 2, 3, 4, 5};
        InputStream in = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);

        long bytesSkipped = stream.skip(2);
        assertEquals(2, bytesSkipped);
        assertEquals(3, stream.read());

        bytesSkipped = stream.skip(10);
        assertEquals(0, bytesSkipped);
        assertEquals(-1, stream.read());

        stream.close();

        // verify that checksum.update() was called correctly
        // based on the bytes read
        // assertEquals(expectedChecksum, stream.getValue());
    }

    @Test
    public void testGetValue() {
        Checksum checksum = mock(Checksum.class);
        when(checksum.getValue()).thenReturn(12345L);

        InputStream in = mock(InputStream.class);
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);

        assertEquals(12345L, stream.getValue());
    }
}