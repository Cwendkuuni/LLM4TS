//Corresponding JUnit 4 Test Cases:

package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummy = new DummyPayloadStorage();
        assertNull(dummy.getLocation(Operation.GET, PayloadType.TASK_INPUT, "test/path"));
        assertNull(dummy.getLocation(Operation.POST, PayloadType.TASK_OUTPUT, "test/path"));
        assertNull(dummy.getLocation(Operation.PUT, PayloadType.WORKFLOW_INPUT, "test/path"));
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummy = new DummyPayloadStorage();
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        dummy.upload("test/path", inputStream, 4);
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummy = new DummyPayloadStorage();
        assertNull(dummy.download("test/path"));
    }
}