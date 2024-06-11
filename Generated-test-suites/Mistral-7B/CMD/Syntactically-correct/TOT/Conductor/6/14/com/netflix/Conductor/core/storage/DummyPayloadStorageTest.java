package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.Operation;
import com.netflix.conductor.common.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.core.storage.DummyPayloadStorage;

public class DummyPayloadStorageTest {

    private ExternalPayloadStorage testee;

    @Before
    public void setUp() {
        testee = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocationReturnsNull() {
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.TEMPLATE;
        String path = "/test.json";

        assertNull(testee.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUploadDoesNothing() throws IOException {
        String path = "/test.json";
        byte[] payload = new byte[1024];
        InputStream inputStream = new ByteArrayInputStream(payload);
        long payloadSize = payload.length;

        testee.upload(path, inputStream, payloadSize);
    }

    @Test
    public void testDownloadReturnsNull() {
        String path = "/test.json";

        assertNull(testee.download(path));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUploadThrowsUnsupportedOperationException() throws IOException {
        String path = "/test.json";
        InputStream inputStream = mock(InputStream.class);
        long payloadSize = 1024;

        testee.upload(path, inputStream, payloadSize);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDownloadThrowsUnsupportedOperationException() {
        String path = "/test.json";

        testee.download(path);
    }
}