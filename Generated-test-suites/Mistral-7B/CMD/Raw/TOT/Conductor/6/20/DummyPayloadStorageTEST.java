package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.Operation;
import com.netflix.conductor.common.PayloadType;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;
    private Operation operation;

    @Before
    public void setup() {
        dummyPayloadStorage = new DummyPayloadStorage();
        operation = mock(Operation.class);
    }

    @Test
    public void testGetLocation_whenOperationNull_thenReturnNull() {
        ExternalStorageLocation expected = null;
        ExternalStorageLocation actual = dummyPayloadStorage.getLocation(null, PayloadType.JSON, "testPath");
        assertNull(actual);
        assertNull(expected);
    }

    @Test
    public void testGetLocation_whenOperationNotNull_thenReturnNull() {
        ExternalStorageLocation expected = null;
        when(operation.getId()).thenReturn("testId");
        ExternalStorageLocation actual = dummyPayloadStorage.getLocation(operation, PayloadType.JSON, "testPath");
        assertNull(actual);
        assertNull(expected);
    }

    @Test
    public void testUpload_whenPayloadIsNull_thenDoNothing() {
        InputStream payload = null;
        dummyPayloadStorage.upload("testPath", payload, 0);
    }

    @Test(expected = IOException.class)
    public void testDownload_whenPayloadIsNull_thenThrowException() throws IOException {
        InputStream expected = mock(InputStream.class);
        when(expected.readAllBytes()).thenReturn(new byte[0]);

        InputStream actual = dummyPayloadStorage.download("testPath");
        assertNull(actual);

        byte[] bytes = new byte[1];
        actual.read(bytes, 0, bytes.length);
    }

    @Test
    public void testDownload_whenPayloadIsEmpty_thenReturnEmptyInputStream() {
        InputStream expected = new ByteArrayInputStream(new byte[0]);
        InputStream actual = dummyPayloadStorage.download("testPath");
        assertSame(expected, actual);
    }

    @Test
    public void testDownload_whenPayloadIsNotEmpty_thenReturnInputStreamWithExpectedData() throws IOException {
        String data = "test data";
        InputStream expected = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));

        when(operation.getId()).thenReturn("testId");
        when(dummyPayloadStorage.upload("testPath", new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)), data.length()))
                .thenReturn(new ExternalStorageLocation("testPath", "testId"));

        InputStream actual = dummyPayloadStorage.download("testPath");
        assertSame(expected, actual);

        byte[] bytes = new byte[data.length()];
        int length = actual.read(bytes, 0, bytes.length);
        assertEquals(data.length(), length);
        assertEquals(data, new String(bytes, StandardCharsets.UTF_8));
    }
}