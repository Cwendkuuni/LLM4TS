package com.netflix.conductor.core.storage;

import org.junit.Test;
import java.io.InputStream;
import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {
    
    private final DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.READ, PayloadType.TASK_INPUT, "any_path");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        // Given there is no actual implementation for upload, there's no point of testing. 
        // For the sake of coverage, call the method anyway.
        dummyPayloadStorage.upload("any_path", InputStream.nullInputStream(), 0);
    }

    @Test
    public void testDownload() {
        InputStream inputStream = dummyPayloadStorage.download("any_path");
        assertNull(inputStream);
    }

    @Test(expected = NullPointerException.class)
    public void testUpload_NullPath() {
        // Given string arguments could be null, test if exception is thrown if string parameter is null
        // other parameters are written as arbitrary values, it could be tested for null as well
        dummyPayloadStorage.upload(null, InputStream.nullInputStream(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpload_NegativePayloadSize() {
        // Given payloadSize could be negative, test if the exception is thrown in that situation
        // other parameters are written as arbitrary values
        dummyPayloadStorage.upload("any_path", InputStream.nullInputStream(), -1);
    }

    @Test(expected = NullPointerException.class)
    public void testDownload_NullPath() {
        // Check if the method handles null input
        dummyPayloadStorage.download(null);
    }
}
