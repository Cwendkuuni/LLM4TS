
package com.netflix.conductor.core.storage;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;

import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void getLocation() {
        assertNull(dummyPayloadStorage.getLocation(Operation.READ, PayloadType.CONDUCTOR_TASK_TYPE, "path"));
        assertNull(dummyPayloadStorage.getLocation(Operation.WRITE, PayloadType.CONDUCTOR_WORKFLOW_TYPE, "path"));
    }

    @Test
    public void upload() {
        InputStream stream = null;
        // should not throw any exceptions
        dummyPayloadStorage.upload("path", stream, 0);
    }

    @Test
    public void download() {
        assertNull(dummyPayloadStorage.download("path"));
    }
}
