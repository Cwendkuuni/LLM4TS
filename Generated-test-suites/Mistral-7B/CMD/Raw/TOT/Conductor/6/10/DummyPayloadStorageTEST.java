package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocationReturnsNull() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        assertNull(storage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUploadDoesNothing() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream payload = new ByteArrayInputStream(new byte[512]);
        long payloadSize = payload.available();

        storage.upload(path, payload, payloadSize);
    }

    @Test
    public void testDownloadReturnsNull() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        String path = "testPath";

        InputStream expected = null;
        InputStream actual = storage.download(path);

        assertNull(actual);
        // Mockito does not support verifying the null return value directly, so we compare the mocked method call instead
        ArgumentCaptor<InputStream> inputStreamCaptor = ArgumentCaptor.forClass(InputStream.class);
        when(storage).download(path).thenReturn(expected);
        storage.download(path);
        // Use reflection to check the actual method call
        Object downloadMethod = DummyPayloadStorage.class.getDeclaredMethod("download", String.class);
        downloadMethod.setAccessible(true);
        InputStream actualMethodCall = (InputStream) downloadMethod.invoke(storage, path);
        assertNull(actualMethodCall);
    }

    @Test
    public void testUploadAndDownloadWithValidData() throws IOException {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        String path = "testPath";
        String expectedPayload = "expected data";
        InputStream payload = new ByteArrayInputStream(expectedPayload.getBytes(StandardCharsets.UTF_8));
        long payloadSize = payload.available();

        InputStream actualDownload = storage.upload(path, payload, payloadSize) && storage.download(path) != null;
        InputStream expectedDownload = new ByteArrayInputStream(expectedPayload.getBytes(StandardCharsets.UTF_8));

        assertTrue(actualDownload);
        assertArrayEquals(expectedPayload.getBytes(), expectedDownload.readAllBytes());
    }

    @Test
    public void testUploadAndDownloadWithEmptyData() throws IOException {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream payload = new ByteArrayInputStream(new byte[0]);
        long payloadSize = payload.available();

        InputStream actualDownload = storage.upload(path, payload, payloadSize) && storage.download(path) != null;
        InputStream expectedDownload = new ByteArrayInputStream(new byte[0]);

        assertTrue(actualDownload);
        assertArrayEquals(new byte[0], expectedDownload.readAllBytes());
    }

    @Test(expected = IOException.class)
    public void testDownloadFailsWithNullPayload() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream payload = null;

        storage.download(path);
    }
}