package org.apache.commons.compress.utils.test;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksums;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testReadMethod() throws Exception {
        // ...
    }

    // ... test cases for other read methods, empty input stream, different data types, IOException, and reading more data than available

    @Test
    public void testGetChecksumFromExhaustedInputStream() throws Exception {
        Checksum checksum = Checksums.adler32();
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, input);

        checksumInputStream.close();

        try {
            checksumInputStream.read();
            fail();
        } catch (IOException e) {
            // expected
        }

        assertEquals(0L, checksum.getValue());
    }

    @Test
    public void testGetChecksumMethod() throws Exception {
        Checksum checksum = Checksums.adler32();
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3});
        ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, input);

        checksumInputStream.close();

        assertEquals(1430622951L, checksum.getValue());
    }
}