
package com.netflix.conductor.core.storage;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dps = new DummyPayloadStorage();
        ExternalStorageLocation location = dps.getLocation(ExternalPayloadStorage.Operation.READ,
                ExternalPayloadStorage.PayloadType.DATA, "dummyPath");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dps = new DummyPayloadStorage();
        InputStream payload = new ByteArrayInputStream("test_payload".getBytes());

        try {
            // Try to upload. The function should run without exceptions,
            // given that the DummyPayloadStorage just has an empty implementation for this method
            dps.upload("dummyPath", payload, payload.available());

        } catch (Exception e) {
            // The method should have not thrown any exception
            assertEquals("", e.getMessage());
        }
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dps = new DummyPayloadStorage();
        // Try to download. DummyPayloadStorage download always returns null.
        assertNull(dps.download("dummyPath"));
    }
}
