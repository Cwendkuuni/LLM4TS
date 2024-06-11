package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalStorageLocation;
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
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";
        ExternalStorageLocation expectedLocation = null;
        ExternalStorageLocation actualLocation = dummyPayloadStorage.getLocation(operation, payloadType, path);
        assertEquals(expectedLocation, actualLocation);
    }

    /**
     * Test method for {@link DummyPayloadStorage#upload(String, InputStream, long)}
     */
    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0;
        dummyPayloadStorage.upload(path, inputStream, payloadSize);
        // No verification needed as the method does not have any side effect
    }

    /**
     * Test method for {@link DummyPayloadStorage#download(String)}
     */
    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream expectedInputStream = null;
        try {
            expectedInputStream = new ByteArrayInputStream(new byte[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream actualInputStream = dummyPayloadStorage.download(path);
        assertNull(actualInputStream);
        assertEquals(expectedInputStream, actualInputStream);
    }

    /**
     * Test method for {@link DummyPayloadStorage#getLocation(Operation, PayloadType, String)} with null operation
     */
    @Test(expected = NullPointerException.class)
    public void testGetLocationWithNullOperation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";
        Operation operation = null;
        dummyPayloadStorage.getLocation(operation, payloadType, path);
    }

    /**
     * Test method for {@link DummyPayloadStorage#getLocation(Operation, PayloadType, String)} with null path
     */
    @Test(expected = NullPointerException.class)
    public void testGetLocationWithNullPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = null;
        dummyPayloadStorage.getLocation(operation, payloadType, path);
    }

    /**
     * Test method for {@link DummyPayloadStorage#upload(String, InputStream, long)} with null inputStream
     */
    @Test(expected = NullPointerException.class)
    public void testUploadWithNullInputStream() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        long payloadSize = 0;
        InputStream inputStream = null;
        dummyPayloadStorage.upload(path, inputStream, payloadSize);
    }

    /**
     * Test method for {@link DummyPayloadStorage#upload(String, InputStream, long)} with negative payloadSize
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUploadWithNegativePayloadSize() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        long payloadSize = -1;
        dummyPayloadStorage.upload(path, inputStream, payloadSize);
    }
}