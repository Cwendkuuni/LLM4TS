package com.netflix.conductor.core.storage;

import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class DummyPayloadStorageTest {
    private DummyPayloadStorage storage = new DummyPayloadStorage();

    @Test
    public void getLocationReturnsNull() {
        assertNull(storage.getLocation(ExternalPayloadStorage.Operation.READ,
                ExternalPayloadStorage.PayloadType.TASK_INPUT, "path"));
    }

    @Test
    public void uploadDoesNothing() {
        try (InputStream input = new ByteArrayInputStream("payload".getBytes())) {
            storage.upload("path", input, "payload".length());

            // Here we're assuming that a failure would cause an exception, so the test passes
            // if no exceptions are thrown. This may need to be adjusted based on the real 
            // implementation.
        } catch (IOException e) {
            fail("Upload threw an exception, and it shouldn't have.");
        }
    }

    @Test
    public void downloadReturnsNull() {
        assertNull(storage.download("path"));
    }

    @Test(expected = NullPointerException.class)
    public void uploadFailsWithNullPayload() {
        storage.upload("path", null, 0);
    }

    @Test(expected = NullPointerException.class)
    public void downloadFailsWithNullPath() {
        storage.download(null);
    }
}