package org.apache.commons.compress.utils;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtilsTest {

    @Test
    public void testCopy() {
        try {
            byte[] data = "Hello, World!".getBytes();
            InputStream inputStream = new ByteArrayInputStream(data);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            long count = IOUtils.copy(inputStream, outputStream);
            assertEquals(data.length, count);
            assertArrayEquals(data, outputStream.toByteArray());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testSkip() {
        try {
            byte[] data = "Hello, World!".getBytes();
            InputStream inputStream = new ByteArrayInputStream(data);
            long skipped = IOUtils.skip(inputStream, 7);
            assertEquals(7, skipped);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    // add test cases for other public methods
    
}