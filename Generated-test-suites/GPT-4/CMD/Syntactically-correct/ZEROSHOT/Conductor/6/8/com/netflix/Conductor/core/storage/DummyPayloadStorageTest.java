package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.InputStream;

import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

import org.junit.Before;
import org.junit.Test;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;
    private ExternalPayloadStorage.Operation operation;
    private ExternalPayloadStorage.PayloadType payloadType;
    private String path;
    private InputStream payload;
    private long payloadSize;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
        operation = mock(ExternalPayloadStorage.Operation.class); // mock Operation enum
        payloadType = mock(ExternalPayloadStorage.PayloadType.class); // mock PayloadType enum
        path = "test-path";
        payload = mock(InputStream.class);
        payloadSize = 1024L;
    }

    @Test
    public void getLocationTest() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(operation, payloadType, path);
        assertNull("Expected null location", location);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void uploadTest() {
        dummyPayloadStorage.upload(path, payload, payloadSize);
    }

    @Test
    public void downloadTest() {
        InputStream downloadStream = dummyPayloadStorage.download(path);
        assertNull("Expected null download stream", downloadStream);
    }
}
