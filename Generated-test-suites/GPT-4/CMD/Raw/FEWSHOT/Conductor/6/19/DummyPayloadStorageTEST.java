
package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import java.io.ByteArrayInputStream;
import org.junit.Test;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

// JUnit 4 Test Cases
public class DummyPayloadStorageTest {
    
    DummyPayloadStorage dummyStorage = new DummyPayloadStorage();
    
    @Test
    public void testGetLocation() {
        // All of these should return null, regardless of the inputs
        for(ExternalPayloadStorage.Operation op : ExternalPayloadStorage.Operation.values()) {
            for(ExternalPayloadStorage.PayloadType pt : ExternalPayloadStorage.PayloadType.values()) {
                assertNull(dummyStorage.getLocation(op, pt, "dummyPath"));
            }
        }
    }

    @Test
    public void testUpload() {
        // This should not throw any exceptions regardless of the inputs
        ByteArrayInputStream payload = new ByteArrayInputStream(new byte[] {1, 2, 3});
        dummyStorage.upload("dummyPath", payload, payload.available());
    }

    @Test
    public void testDownload() {
        // This should always return null, regardless of the input
        assertNull(dummyStorage.download("dummyPath"));
    }
}
