package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void getLocationReturnsNull() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.READ, PayloadType.TASK_INPUT, "path");
        assertNull(location);
    }

    @Test
    public void uploadDoesNothing() {
        InputStream input = InputStream.nullInputStream();
        try {
            // It shouldn't throw any exception, so no assertion
            dummyPayloadStorage.upload("path", input, 0);
        } finally {
            try { input.close(); } catch (Exception ignored) {}
        }
    }

    @Test
    public void downloadReturnsNull() {
        InputStream downloadStream = dummyPayloadStorage.download("a_path");
        assertNull(downloadStream);
    }
  
    @Test(expected=NullPointerException.class)
    public void downloadShouldThrowExceptionWhenPathIsNull() {
        dummyPayloadStorage.download(null);
    }
  
    @Test(expected=NullPointerException.class)
    public void uploadShouldThrowExceptionWhenPathIsNull() {
        InputStream input = InputStream.nullInputStream();
        try {
            dummyPayloadStorage.upload(null, input, 0);
        }
        finally {
            try { input.close(); } catch (Exception ignored) {}
        }
    }
  
    @Test(expected=NullPointerException.class)
    public void locationShouldThrowExceptionWhenOperationIsNull() {
        dummyPayloadStorage.getLocation(null, PayloadType.TASK_OUTPUT, "path");
    }

}