
package com.netflix.conductor.core.storage;

import java.io.InputStream;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(
                ExternalPayloadStorage.Operation.READ, 
                ExternalPayloadStorage.PayloadType.TASK_INPUT, 
                "dummyPath");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        // since it is a dummy method, no assertion here
        dummyPayloadStorage.upload("testPath", null, 10);
    }

    @Test
    public void testDownload() {
        InputStream downloaded = dummyPayloadStorage.download("testPath");
        assertNull(downloaded);
    }
}
