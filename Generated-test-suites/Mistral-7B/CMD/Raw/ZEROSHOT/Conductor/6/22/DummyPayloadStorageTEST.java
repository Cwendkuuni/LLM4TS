package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalStorageLocation;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;
    private Operation operation;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
        operation = mock(Operation.class);
    }

    @Test
    public void testGetLocation() {
        assertNull(dummyPayloadStorage.getLocation(operation, PayloadType.JSON, "testPath"));
    }

    @Test
    public void testUpload() {
        // Dummy implementation does not support uploading
    }

    @Test
    public void testDownload() {
        assertNull(dummyPayloadStorage.download("testPath"));
    }
}