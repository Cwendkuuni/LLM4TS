package org.apache.commons.compress.utils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class IOUtilsTest {

    @Test
    public void testCopy() {
        byte[] testData = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(testData);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            long count = IOUtils.copy(input, output);
            assertEquals(5, count);
            assertArrayEquals(testData, output.toByteArray());
        } catch (IOException e) {
            fail("IOException thrown unexpectedly");
        }
    }

    @Test
    public void testSkip() {
        byte[] testData = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(testData);

        try {
            long skipped = IOUtils.skip(input, 2);
            assertEquals(2, skipped);
        } catch (IOException e) {
            fail("IOException thrown unexpectedly");
        }
    }

    @Test
    public void testReadFully() {
        byte[] testData = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(testData);
        byte[] buffer = new byte[5];

        try {
            int read = IOUtils.readFully(input, buffer);
            assertEquals(5, read);
            assertArrayEquals(testData, buffer);
        } catch (IOException e) {
            fail("IOException thrown unexpectedly");
        }
    }

}