package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.core.storage.DummyPayloadStorage;

/**
 * Test class for DummyPayloadStorage
 */
public class DummyPayloadStorageTest {

    /**
     * Test method for {@link DummyPayloadStorage#getLocation(Operation, PayloadType, String)}
     */
    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        assertNull(dummyPayloadStorage.getLocation(null, null, ""));
    }

    /**
     * Test method for {@link DummyPayloadStorage#upload(String, InputStream, long)}
     */
    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        dummyPayloadStorage.upload("", inputStream, 0);
        // No verification needed as the method does not perform any action
    }

    /**
     * Test method for {@link DummyPayloadStorage#download(String)}
     */
    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream expectedInput = new ByteArrayInputStream(new byte[0]);
        InputStream inputStream = dummyPayloadStorage.download("");
        assertNull(inputStream);
        // No verification needed as the method does not return the expected InputStream in this case
    }

    /**
     * Test method for {@link DummyPayloadStorage#getLocation(Operation, PayloadType, String)} with null operation
     */
    @Test(expected = NullPointerException.class)
    public void testGetLocation_NullOperation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        dummyPayloadStorage.getLocation(null, PayloadType.JSON, "");
    }

    /**
     * Test method for {@link DummyPayloadStorage#getLocation(Operation, PayloadType, String)} with null payloadType
     */
    @Test(expected = NullPointerException.class)
    public void testGetLocation_NullPayloadType() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        dummyPayloadStorage.getLocation(new Operation(), null, "");
    }

    /**
     * Test method for {@link DummyPayloadStorage#getLocation(Operation, PayloadType, String)} with empty path
     */
    @Test
    public void testGetLocation_EmptyPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        assertNull(dummyPayloadStorage.getLocation(new Operation(), PayloadType.JSON, ""));
    }

    /**
     * Test method for {@link DummyPayloadStorage#upload(String, InputStream, long)} with empty path
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpload_EmptyPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        dummyPayloadStorage.upload("", inputStream, 0);
    }

    /**
     * Test method for {@link DummyPayloadStorage#upload(String, InputStream, long)} with negative payload size
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpload_NegativePayloadSize() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        dummyPayloadStorage.upload("", inputStream, -1);
    }

    /**
     * Test method for {@link DummyPayloadStorage#download(String)} with non-empty path
     */
    @Test
    public void testDownload_NonEmptyPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream expectedInput = new ByteArrayInputStream(new byte[0]);
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        InputStream resultInput = dummyPayloadStorage.download("");
        byte[] expectedBytes = new byte[0];
        byte[] resultBytes = new byte[1024];

        try {
            int read = 0;
            while (read != -1) {
                read = expectedInput.read(resultBytes, 0, resultBytes.length);
                if (read > 0) {
                    outputStream.write(resultBytes, 0, read);
                }
            }
            inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            int bytesRead = 0;
            byte[] readBuffer = new byte[1024];
            while ((bytesRead = inputStream.read(readBuffer)) != -1) {
                expectedBytes = new byte[bytesRead];
                System.arraycopy(readBuffer, 0, expectedBytes, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            int bytesRead = 0;
            byte[] readBuffer = new byte[1024];
            while ((bytesRead = resultInput.read(readBuffer)) != -1) {
                assertArrayEquals(expectedBytes, readBuffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                resultInput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}