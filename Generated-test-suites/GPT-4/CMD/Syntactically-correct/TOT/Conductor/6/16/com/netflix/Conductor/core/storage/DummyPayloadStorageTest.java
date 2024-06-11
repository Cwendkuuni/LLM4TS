
package com.netflix.conductor.core.storage;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Test;
import java.io.InputStream;

import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

    // Testing getLocation method
    @Test
    public void getLocationTest() {
        DummyPayloadStorage dummyStorage = new DummyPayloadStorage();

        ExternalPayloadStorage.Operation operation = null;
        ExternalPayloadStorage.PayloadType payloadType = null;
        String path = "samplePath";

        // The getLocation method should always return null in DummyPayloadStorage
        ExternalStorageLocation location = dummyStorage.getLocation(operation, payloadType, path);
        assertNull(location);
    }

    // Testing upload method
    @Test
    public void uploadTest() {
        DummyPayloadStorage dummyStorage = new DummyPayloadStorage();

        String path = "samplePath";
        InputStream payload = null;
        long payloadSize = 1000L;

        // The upload method in DummyPayloadStorage has no effect, thus having no assertion
        // If the upload method throws an exception, the test would fail
        dummyStorage.upload(path, payload, payloadSize);
    }

    // Testing download method
    @Test
    public void downloadTest() {
        DummyPayloadStorage dummyStorage = new DummyPayloadStorage();

        String path = "samplePath";

        // The download method in DummyPayloadStorage should always return null
        InputStream stream = dummyStorage.download(path);
        assertNull(stream);
    }
}
