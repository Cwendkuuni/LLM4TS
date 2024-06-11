package com.netflix.conductor.core.storage.test;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.common.utils.PayloadType;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = "/test/path";

        ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);
        assertNull(result);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "/test/path";
        InputStream payload = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0;

        try {
            dummyPayloadStorage.upload(path, payload, payloadSize);
            fail("expected exception");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "/test/path";

        InputStream result = dummyPayloadStorage.download(path);
        assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetLocationWithNullOperation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = null;
        PayloadType payloadType = PayloadType.JSON;
        String path = "/test/path";

        dummyPayloadStorage.getLocation(operation, payloadType, path);
    }

    @Test(expected = NullPointerException.class)
    public void testGetLocationWithNullPayloadType() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = null;
        String path = "/test/path";

        dummyPayloadStorage.getLocation(operation, payloadType, path);
    }

    @Test(expected = NullPointerException.class)
    public void testGetLocationWithNullPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = null;

        dummyPayloadStorage.getLocation(operation, payloadType, path);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUploadWithNegativePayloadSize() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "/test/path";
        InputStream payload = new ByteArrayInputStream(new byte[0]);
        long payloadSize = -1;

        dummyPayloadStorage.upload(path, payload, payloadSize);
    }

    @Test(expected = IOException.class)
    public void testUploadWithIOException() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "/test/path";
        InputStream payload = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0;

        InputStream inputStream = new ByteArrayInputStream(new byte[]{"Test"});
        InputStream expectedException = mock(InputStream.class);
        when(expectedException.read()).thenThrow(new IOException("Test Exception"));
        doReturn(expectedException).when(dummyPayloadStorage).getInputStreamFromExternalStorage(path);

        try {
            dummyPayloadStorage.upload(path, payload, payloadSize);
            fail("expected exception");
        } catch (IOException e) {
            // expected
        }
    }
}