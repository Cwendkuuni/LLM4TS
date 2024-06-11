
package com.netflix.conductor.core.storage;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocationShouldReturnNull() {
        assertNull(dummyPayloadStorage.getLocation(
                ExternalPayloadStorage.Operation.READ,
                ExternalPayloadStorage.PayloadType.TASK_INPUT,
                "testPath"));
    }

    @Test
    public void testUploadShouldNotThrowException() {
        String testPath = "testPath";
        byte[] testPayload = new byte[10];
        InputStream inputStream = new ByteArrayInputStream(testPayload);
        long payloadSize = testPayload.length;

        // Since the upload method does nothing, there's no exception expected or value returned
        dummyPayloadStorage.upload(testPath, inputStream, payloadSize);
    }

    @Test
    public void testDownloadShouldReturnNull() {
        assertNull(dummyPayloadStorage.download("testPath"));
    }
}
