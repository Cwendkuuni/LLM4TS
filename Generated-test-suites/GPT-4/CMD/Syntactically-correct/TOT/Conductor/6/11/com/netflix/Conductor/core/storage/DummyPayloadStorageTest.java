package com.netflix.conductor.core.storage;
import org.junit.Before;
import org.junit.Test;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;

import java.io.InputStream;
import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest  {

    private DummyPayloadStorage dps;

    @Before
    public void setUp() throws Exception {
        dps = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
        ExternalStorageLocation location = dps.getLocation(Operation.DELETE, PayloadType.SIMPLE, "/dummy/path");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        String dummyPath = "/dummy/path";
        InputStream dummyPayload = new ByteArrayInputStream("dummyPayload".getBytes());
        dps.upload(dummyPath, dummyPayload, dummyPayload.available());
    }

    @Test
    public void testDownload() {
        InputStream downloadedStream = dps.download("/dummy/path");
        assertNull(downloadedStream);
    }
}