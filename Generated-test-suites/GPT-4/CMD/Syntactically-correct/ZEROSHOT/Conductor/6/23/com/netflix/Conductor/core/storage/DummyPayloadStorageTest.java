package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import java.io.InputStream;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import org.junit.Before;
import org.junit.Test;

public class DummyPayloadStorageTest {
    DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setup() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
        assertNull(dummyPayloadStorage.getLocation(Operation.READ, PayloadType.TASK_INPUT, "path"));
    }

    @Test
    public void testUpload() {
        InputStream payload = mock(InputStream.class);
        dummyPayloadStorage.upload("path", payload, 100L);
        // Since upload operation does nothing in DummyPayloadStorage, we don't assert anything here.
    }

    @Test
    public void testDownload() {
        assertNull(dummyPayloadStorage.download("path"));
    }
}
