package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.common.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.run.PayloadTypeRegistry;
import com.netflix.conductor.common.utils.ExternalStorageLocation;
import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation_nullOperation() {
        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();
        assertNull(payloadStorage.getLocation(null, PayloadType.JSON, "testPath"));
    }

    @Test
    public void testGetLocation_nullPayloadType() {
        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();
        Operation operation = mock(Operation.class);
        PayloadTypeRegistry payloadTypeRegistry = mock(PayloadTypeRegistry.class);
        when(operation.getPayloadTypeRegistry()).thenReturn(payloadTypeRegistry);
        when(payloadTypeRegistry.getPayloadType("testType")).thenReturn(PayloadType.JSON);
        assertNull(payloadStorage.getLocation(operation, null, "testPath"));
    }

    @Test
    public void testGetLocation_nullPath() {
        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        assertNull(payloadStorage.getLocation(operation, payloadType, null));
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();
        payloadStorage.upload("testPath", null, 1);
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();
        assertNull(payloadStorage.download("testPath"));
    }
}