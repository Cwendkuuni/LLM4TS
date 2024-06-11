package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;

import java.io.InputStream;

import org.junit.Test;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.core.storage.DummyPayloadStorage;

public class DummyPayloadStorageTest {

    DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        // Given
        ExternalPayloadStorage.Operation operation = ExternalPayloadStorage.Operation.READ;
        ExternalPayloadStorage.PayloadType payloadType = ExternalPayloadStorage.PayloadType.TASK_INPUT;
        String path = "dummy/path";

        // When
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(operation, payloadType, path);

        // Then
        assertNull(location);
    }

    @Test
    public void testUpload() {
        // Given
        String path = "dummy/path";
        InputStream payload = null;
        long payloadSize = 0L;

        // When
        // No exceptions should be thrown
        dummyPayloadStorage.upload(path, payload, payloadSize);
    }

    @Test
    public void testDownload() {
        // Given
        String path = "dummy/path";

        // When
        InputStream downloadedPayload = dummyPayloadStorage.download(path);

        // Then
        assertNull(downloadedPayload);
    }
}
