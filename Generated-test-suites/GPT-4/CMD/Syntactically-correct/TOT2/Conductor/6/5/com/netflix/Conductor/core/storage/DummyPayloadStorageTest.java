package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.InputStream;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;

import org.junit.Test;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage testDummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        ExternalStorageLocation externalStorageLocation = testDummyPayloadStorage.getLocation(
                Operation.READ, 
                PayloadType.TASK_INPUT, 
                "path"
        );
        assertNull(externalStorageLocation);
    }

    @Test
    public void testDownload() {
        InputStream inputStream = testDummyPayloadStorage.download("path");
        assertNull(inputStream);
    }

    @Test(expected = NullPointerException.class)
    public void testUpload() {
        testDummyPayloadStorage.upload("path", null, 0);
        // This will throw a NullPointerException as we are passing null as an InputStream
    }
}