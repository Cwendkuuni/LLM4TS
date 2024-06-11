package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {

    private ExternalPayloadStorage underTest;
    private Operation operation;

    @Before
    public void setup() {
        underTest = new DummyPayloadStorage();
        operation = mock(Operation.class);
    }

    @Test
    public void testGetLocationReturnsNull() {
        PayloadType payloadType = PayloadType.JSON;
        String path = "/testPath";

        assertNull(underTest.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUploadDoesNotDoAnything() throws IOException {
        String path = "/testPath";
        InputStream payload = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0;

        underTest.upload(path, payload, payloadSize);

        // No assertions here, as the method does not do anything
    }

    @Test
    public void testDownloadReturnsNull() {
        String path = "/testPath";

        InputStream expectedInput = new ByteArrayInputStream(new byte[0]);

        when(underTest.download(path)).thenReturn(expectedInput);

        InputStream actualInput = underTest.download(path);

        assertNull(actualInput);
    }

    @Test(expected = NullPointerException.class)
    public void testDownloadWithNullPath() {
        underTest.download(null);
    }

    @Test(expected = NullPointerException.class)
    public void testDownloadWithNullInputStream() {
        String path = "/testPath";

        when(underTest.download(path)).thenReturn(null);

        underTest.download(path);
    }

    @Test
    public void testUploadWithNullPath() {
        InputStream payload = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0;

        underTest.upload(null, payload, payloadSize);
    }

    @Test
    public void testUploadWithNullPayload() {
        String path = "/testPath";
        long payloadSize = 0;

        underTest.upload(path, null, payloadSize);
    }

    @Test
    public void testUploadWithNegativePayloadSize() {
        String path = "/testPath";
        long payloadSize = -1;

        underTest.upload(path, new ByteArrayInputStream(new byte[0]), payloadSize);
    }

    @Test
    public void testDownloadUsesMockedDownloadMethod() throws IOException {
        String path = "/testPath";
        InputStream expectedInput = new ByteArrayInputStream(new byte[0]);

        when(underTest.download(path)).thenReturn(expectedInput);

        InputStream actualInput = underTest.download(path);

        assertSame(expectedInput, actualInput);
    }

    @Test
    public void testUploadUsesMockedUploadMethod() throws IOException {
        String path = "/testPath";
        InputStream payload = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream expectedInput = new ByteArrayInputStream(outputStream.toByteArray());

        when(underTest.upload(path, payload, payloadSize)).thenReturn(null);
        when(underTest.download(path)).thenReturn(expectedInput);

        underTest.upload(path, payload, payloadSize);

        InputStream actualInput = underTest.download(path);

        assertSame(expectedInput, actualInput);
    }
}