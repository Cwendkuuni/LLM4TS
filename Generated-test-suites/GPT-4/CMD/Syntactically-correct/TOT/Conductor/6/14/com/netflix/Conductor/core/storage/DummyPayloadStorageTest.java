package com.netflix.conductor.core.storage;

import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.*;
import java.io.InputStream;

import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation_ReturnsNull() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        ExternalStorageLocation location = storage.getLocation(null, null, "");
        assertNull(location);
    }

    @Test
    public void testUpload_DoesNothing() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        try {
            storage.upload("", null, 0);
        } catch (Exception e) {
            Assert.fail("Exception " + e + " thrown");
        }
    }

    @Test
    public void testDownload_ReturnsNull() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        InputStream stream = storage.download("");
        assertNull(stream);
    }
}
