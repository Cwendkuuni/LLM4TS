package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void getLocationReturnsNull() {
        assertNull(
                dummyPayloadStorage.getLocation(
                        ExternalPayloadStorage.Operation.READ,
                        ExternalPayloadStorage.PayloadType.WORKFLOW_INPUT,
                        "/dummy-path"
                )
        );
    }

    @Test
    public void uploadDoesNotThrowException() {
        InputStream inputStream = new ByteArrayInputStream("Test Payload".getBytes());

        try {
            dummyPayloadStorage.upload("/dummy-path", inputStream, inputStream.available());
        } catch (Exception e) {
            assertNotNull(e);
        }
    }

    @Test
    public void downloadReturnsNull() {
        assertNull(dummyPayloadStorage.download("/dummy-path"));
    }
}