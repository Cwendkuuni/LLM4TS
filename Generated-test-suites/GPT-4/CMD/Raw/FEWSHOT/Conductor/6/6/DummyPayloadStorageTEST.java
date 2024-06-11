
package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyStorage = new DummyPayloadStorage();
        assertNull(dummyStorage.getLocation(ExternalPayloadStorage.Operation.READ, ExternalPayloadStorage.PayloadType.TASK_OUTPUT, ""));
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyStorage = new DummyPayloadStorage();
        InputStream is = new ByteArrayInputStream("test".getBytes());
        dummyStorage.upload("path", is, 4);
        // The test passes if no exception is thrown
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyStorage = new DummyPayloadStorage();
        assertNull(dummyStorage.download("path"));
    }
}
 