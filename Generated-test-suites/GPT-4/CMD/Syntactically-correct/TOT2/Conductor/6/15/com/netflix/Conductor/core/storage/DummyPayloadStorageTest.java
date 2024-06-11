package com.netflix.conductor.core.storage;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;

public class DummyPayloadStorageTest {

    DummyPayloadStorage dps = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        Operation dummyOperation = Operation.WRITE;
        PayloadType dummyPayloadType = PayloadType.TASK_INPUT;
        String dummyPath = "dummy-path";
        ExternalStorageLocation actualLocation = dps.getLocation(dummyOperation, dummyPayloadType, dummyPath);
        assertNull(actualLocation);
    }

    @Test
    public void testUpload() {
        String dummyPath = "dummy-path";
        long dummyPayloadSize = 1024;
        InputStream dummyPayload = new ByteArrayInputStream("dummy-payload".getBytes());
        // As upload() method does nothing, there is no assertion to be made here.
        // This test case is mainly to verify that no exception is thrown during execution.
        dps.upload(dummyPath, dummyPayload, dummyPayloadSize);
    }

    @Test
    public void testDownload() {
        String dummyPath = "dummy-path";
        InputStream downloadedPayload = dps.download(dummyPath);
        assertNull(downloadedPayload);
    }
}