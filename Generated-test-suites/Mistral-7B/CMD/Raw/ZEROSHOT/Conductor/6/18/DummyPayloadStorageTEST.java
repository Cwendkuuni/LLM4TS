package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.common.operation.Status;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.PayloadType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Unit test for {@link DummyPayloadStorage}
 */
public class DummyPayloadStorageTest {

    private ExternalPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUpload() throws IOException {
        String path = "testPath";
        byte[] payload = "testPayload".getBytes(StandardCharsets.UTF_8);
        long payloadSize = payload.length;

        InputStream inputStream = new ByteArrayInputStream(payload);

        DummyPayloadStorage dummyPayloadStorage = mock(DummyPayloadStorage.class);
        when(dummyPayloadStorage.upload(eq(path), any(InputStream.class), anyLong()))
                .thenReturn(Status.SUCCESS);

        InputStream result = dummyPayloadStorage.upload(path, inputStream, payloadSize);

        Mockito.verify(dummyPayloadStorage).upload(eq(path), any(InputStream.class), anyLong());
        assertNull(result);
    }

    @Test
    public void testDownload() {
        String path = "testPath";

        InputStream inputStream = new ByteArrayInputStream("testDownloadPayload".getBytes(StandardCharsets.UTF_8));
        when(dummyPayloadStorage.download(eq(path)))
                .thenReturn(inputStream);

        InputStream result = dummyPayloadStorage.download(path);

        Mockito.verify(dummyPayloadStorage).download(eq(path));
        assertEquals("testDownloadPayload", readAllBytes(result));
    }

    private byte[] readAllBytes(InputStream inputStream) throws IOException {
        int byteArraySize = StreamUtils.copyToByteArray(inputStream).length;
        byte[] byteArray = new byte[byteArraySize];
        inputStream.read(byteArray);
        inputStream.close();
        return byteArray;
    }
}