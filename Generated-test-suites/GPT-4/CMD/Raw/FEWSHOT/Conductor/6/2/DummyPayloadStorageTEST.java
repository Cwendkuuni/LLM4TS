package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import java.io.InputStream;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {
    @Test
    public void testGetLocation() {
        DummyPayloadStorage dps = new DummyPayloadStorage();
        assertNull(dps.getLocation(ExternalPayloadStorage.Operation.ADD, ExternalPayloadStorage.PayloadType.S3, "dummy/path"));
        assertNull(dps.getLocation(ExternalPayloadStorage.Operation.UPDATE, ExternalPayloadStorage.PayloadType.S3, "dummy/path"));
        assertNull(dps.getLocation(ExternalPayloadStorage.Operation.DELETE, ExternalPayloadStorage.PayloadType.S3, "dummy/path"));
    }

    @Test(expected = RuntimeException.class)
    public void testUpload() {
        DummyPayloadStorage dps = new DummyPayloadStorage();
        dps.upload("dummy/path", InputStream.nullInputStream(), 500L);
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dps = new DummyPayloadStorage();
        assertNull(dps.download("dummy/path"));
    }
}