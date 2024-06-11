package com.netflix.conductor.core.storage;

import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
        ExternalStorageLocation storageLocation = dummyPayloadStorage.getLocation(
                ExternalPayloadStorage.Operation.READ, 
                ExternalPayloadStorage.PayloadType.TASK_INPUT, 
                "test_path"
        );
        assertNull("Expecting null as DummyPayloadStorage does not implement storage", storageLocation);
    }

    @Test
    public void testUpload() {
        InputStream inputStream = new ByteArrayInputStream("test_payload".getBytes());
        // Expecting no exception here as DummyPayloadStorage does not implement storage
        dummyPayloadStorage.upload("test_path", inputStream, 11);
    }

    @Test
    public void testDownload() {
        InputStream inputStream = dummyPayloadStorage.download("test_path");
        assertNull("Expecting null as DummyPayloadStorage does not implement storage", inputStream);
    }
}
