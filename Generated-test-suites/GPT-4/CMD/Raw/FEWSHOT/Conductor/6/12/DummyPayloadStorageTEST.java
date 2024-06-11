package com.netflix.conductor.core.storage;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class DummyPayloadStorageTest {

    DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        assertNull(dummyPayloadStorage.getLocation(Operation.WRITE, PayloadType.TASK_INPUT, "dummyPath"));
        assertNull(dummyPayloadStorage.getLocation(Operation.READ, PayloadType.TASK_OUTPUT, "dummyPath"));
    }

    @Test
    public void testUpload() {
        InputStream dummyStream = InputStream.nullInputStream();
        dummyPayloadStorage.upload("dummyPath", dummyStream, 100L);
        // Since the DummyPayloadStorage does not actually implement any logic in the "upload" method, there's nothing to assert.
    }

    @Test
    public void testDownload() {
        assertNull(dummyPayloadStorage.download("dummyPath"));
    }
}