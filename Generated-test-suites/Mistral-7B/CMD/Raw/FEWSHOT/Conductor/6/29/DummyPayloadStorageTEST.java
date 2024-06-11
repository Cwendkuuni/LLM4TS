package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.common.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.run.Payload;
import com.netflix.conductor.common.utils.ExternalStorageLocation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.AnnotatedClassLoader;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RunWith(PowerMockRunner.class)
public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.json;
        String path = "testPath";

        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUpload() throws IOException {
        byte[] payloadData = new byte[1024];
        InputStream payloadStream = new ByteArrayInputStream(payloadData);
        long payloadSize = payloadData.length;
        String path = "testPath";

        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        dummyPayloadStorage.upload(path, payloadStream, payloadSize);

        // Verify no side-effects
        // The dummy implementation does not store or upload anything.
    }

    @Test
    public void testDownload() throws IOException {
        String path = "testPath";
        InputStream expectedInputStream = mock(InputStream.class);

        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        when(dummyPayloadStorage.download(path)).thenReturn(expectedInputStream);

        InputStream actualInputStream = dummyPayloadStorage.download(path);

        Mockito.verify(dummyPayloadStorage).download(path);
        Mockito.verify(dummyPayloadStorage).getLocation(Mockito.any(Operation.class), Mockito.eq(PayloadType.json), Mockito.eq(path))
                .thenReturn(new ExternalStorageLocation("", ""));
        Mockito.verify(dummyPayloadStorage).upload(Mockito.eq(""), Mockito.any(InputStream.class), Mockito.anyLong());
        Mockito.verify(expectedInputStream).close();

        assertEquals(expectedInputStream, actualInputStream);
    }
}