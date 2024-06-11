package com.netflix.conductor.core.storage;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.netflix.conductor.common.Operation;
import com.netflix.conductor.common.PayloadType;
import com.netflix.conductor.common.utils.ExternalStorageLocation;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.FILE;
        String path = "test-path";

        ExternalStorageLocation location = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertNull(location);
    }

    @Test
    public void testUpload() throws IOException {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "test-path";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0;

        dummyPayloadStorage.upload(path, inputStream, payloadSize);

        // Since it's a dummy implementation, there's no need to check the result
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "test-path";

        InputStream expectedInput = new ByteArrayInputStream(new byte[0]);
        InputStream actualInput = dummyPayloadStorage.download(path);

        assertSame(expectedInput, actualInput);
    }
}