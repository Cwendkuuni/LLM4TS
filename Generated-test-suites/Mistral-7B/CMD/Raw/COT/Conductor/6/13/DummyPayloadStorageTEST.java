package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.assertNull;

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

        ExternalStorageLocation expectedResult = null;
        ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertNull(result);
        assertNull(expectedResult);
    }

    /**
     * Test method for {@link DummyPayloadStorage#upload(String, InputStream, long)}
     */
    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream payload = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0;

        dummyPayloadStorage.upload(path, payload, payloadSize);
        // No verification needed as the method is void
    }

    /**
     * Test method for {@link DummyPayloadStorage#download(String)}
     */
    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";

        InputStream expectedResult = null;
        InputStream result = dummyPayloadStorage.download(path);

        assertNull(result);
        assertNull(expectedResult);
    }

    /**
     * Test method for {@link DummyPayloadStorage#getLocation(Operation, PayloadType, String)} with null operation
     */
    @Test(expected = NullPointerException.class)
    public void testGetLocationWithNullOperation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        ExternalStorageLocation result = dummyPayloadStorage.getLocation(null, payloadType, path);
    }

    /**
     * Test method for {@link DummyPayloadStorage#getLocation(Operation, PayloadType, String)} with null payloadType
     */
    @Test(expected = NullPointerException.class)
    public void testGetLocationWithNullPayloadType() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        String path = "testPath";

        ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, null, path);
    }

    /**
     * Test method for {@link DummyPayloadStorage#download(String)} with null path
     */
    @Test(expected = NullPointerException.class)
    public void testDownloadWithNullPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        InputStream result = dummyPayloadStorage.download(null);
    }
}