package com.netflix.conductor.core.storage;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dps = new DummyPayloadStorage();
        ExternalStorageLocation location = dps.getLocation(
                ExternalPayloadStorage.Operation.READ,
                ExternalPayloadStorage.PayloadType.TASK_INPUT,
                "dummyPath"
        );
        assertNull(location);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dps = new DummyPayloadStorage();
        InputStream dummyInputStream = InputStream.nullInputStream();
        dps.upload("path", dummyInputStream, 100L);
        // Upload operation in DummyPayloadStorage doesn't affect anything, so no assertions are needed
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dps = new DummyPayloadStorage();
        InputStream resultInputStream = dps.download("dummyPath");
        assertNull(resultInputStream);
    }
}