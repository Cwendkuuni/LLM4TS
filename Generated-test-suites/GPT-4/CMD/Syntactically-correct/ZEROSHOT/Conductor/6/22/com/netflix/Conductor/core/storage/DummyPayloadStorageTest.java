package com.netflix.conductor.core.storage;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
        Operation operation = Operation.READ;
        PayloadType payloadType = PayloadType.TASK_INPUT;
        String path = "test-path";

        ExternalStorageLocation location = dummyPayloadStorage.getLocation(operation, payloadType, path);
        Assert.assertNull(location);
    }

    @Test
    public void testUpload() {
        String path = "test-path";
        InputStream payload = null;
        long size = 0L;

        // This method does not throw exception nor returns anything
        // So the successful execution is a pass
        dummyPayloadStorage.upload(path, payload, size);
    }

    @Test
    public void testDownload() {
        String path = "test-path";

        InputStream result = dummyPayloadStorage.download(path);
        Assert.assertNull(result);
    }
}