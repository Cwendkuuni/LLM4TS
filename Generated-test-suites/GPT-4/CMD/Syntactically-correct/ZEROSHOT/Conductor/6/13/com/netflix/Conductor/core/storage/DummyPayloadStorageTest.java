package com.netflix.conductor.core.storage;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.Assert.*;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;
    private ByteArrayInputStream payloadStream;
    private Operation operation;
    private PayloadType payloadType;
    private String path;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
        payloadStream = new ByteArrayInputStream("dummy payload".getBytes());
        operation = Operation.WRITE;
        payloadType = PayloadType.TASK_INPUT;
        path = "dummy/path";
    }


    @Test
    public void testGetLocation() {
        ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);
        assertNull(result);
    }

    @Test
    public void testUpload() {
        try {
            dummyPayloadStorage.upload(path, payloadStream, payloadStream.available());
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    public void testDownload() {
        InputStream result = dummyPayloadStorage.download(path);
        assertNull(result);
    }
}
