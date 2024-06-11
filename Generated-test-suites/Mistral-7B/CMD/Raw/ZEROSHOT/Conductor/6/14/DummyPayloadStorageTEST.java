package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.Operation;
import com.netflix.conductor.common.PayloadType;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.FileUtils;
import com.netflix.conductor.common.utils.TestFileUtils;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage payloadStorage;
    private ExternalStorageLocation externalStorageLocation;

    @Before
    public void setup() {
        payloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocationNull() {
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        assertNull(payloadStorage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUploadNull() {
        String path = "testPath";
        InputStream inputStream = TestFileUtils.getTestInputStream("test.json");
        long payloadSize = 1024;

        payloadStorage.upload(path, inputStream, payloadSize);

        // No verification needed as the method does not interact with any external system
    }

    @Test
    public void testDownloadNull() {
        String path = "testPath";

        InputStream inputStream = payloadStorage.download(path);

        assertNull(inputStream);
    }

    @Test
    public void testGetLocationWithMockedInputStream() {
        ExternalStorageLocation expectedLocation = new ExternalStorageLocation("testBucket", "testPrefix");
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        InputStream inputStream = mock(InputStream.class);
        when(inputStream.available()).thenReturn(1024);

        when(payloadStorage.getLocation(operation, payloadType, path)).thenReturn(expectedLocation);
        when(payloadStorage.download(path)).thenReturn(inputStream);

        ExternalStorageLocation actualLocation = payloadStorage.getLocation(operation, payloadType, path);

        assertNull(actualLocation);
    }
}