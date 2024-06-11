package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStream() {
        // test empty input
        final Checksum checksum = mock(Checksum.class);
        final InputStream input = new ByteArrayInputStream(new byte[0]);
        final ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, input);
        assertEquals(0, checksumInputStream.read());
        verify(checksum).update((byte) 0);
        assertEquals(0, checksumInputStream.read());
        verify(checksum).update((byte) 0);
        assertEquals(0, checksumInputStream.read());
        verify(checksum).update((byte) 0);
        assertEquals(0, checksumInputStream.read());
        assertEquals(-1, checksumInputStream.read());
        assertEquals(Adler32.ADLER32_CHECKVAL_INIT, checksum.getValue());

        // test single byte input
        final byte[] inputData = new byte[] { 0x01 };
        final byte[] expectedData = new byte[] { 0x01 };
        final InputStream inputStream = new ByteArrayInputStream(inputData);
        final Checksum checksum = new Adler32();
        final ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] output = new byte[1];
        int read = 0;
        while ((read = checksumInputStream.read()) >= 0) {
            outputStream.write(read);
        }
        assertArrayEquals(expectedData, outputStream.toByteArray());
        assertEquals(Adler32.byteArrayToLong(expectedData), checksum.getValue());

        // test multiple byte input
        final byte[] inputDataMulti = new byte[] { 0x01, 0x02, 0x03 };
        final byte[] expectedDataMulti = new byte[] { 0x01, 0x02, 0x03 };
        final InputStream inputStreamMulti = new ByteArrayInputStream(inputDataMulti);
        final Checksum checksumMulti = new Adler32();
        final ChecksumCalculatingInputStream checksumInputStreamMulti = new ChecksumCalculatingInputStream(checksumMulti, inputStreamMulti);
        ByteArrayOutputStream outputStreamMulti = new ByteArrayOutputStream();
        byte[] outputMulti = new byte[3];
        int readMulti = 0;
        while ((readMulti = checksumInputStreamMulti.read(outputMulti, 0, 3)) >= 0) {
            outputStreamMulti.write(outputMulti, 0, readMulti);
        }
        assertArrayEquals(expectedDataMulti, outputStreamMulti.toByteArray());
        assertEquals(Adler32.byteArrayToLong(expectedDataMulti), checksumMulti.getValue());
    }
}