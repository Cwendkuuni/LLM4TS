package com.netflix.conductor.core.storage;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp(){
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(null, null, null);
        assertNull(location);
    }

    @Test
    public void testUpload() {
        try {
            dummyPayloadStorage.upload(null, null, 0);
        } catch (Exception e) {
            fail("Upload method should not throw exception");
        }
    }

    @Test
    public void testDownload() {
        InputStream inputStream = dummyPayloadStorage.download(null);
        assertNull(inputStream);
    }
}