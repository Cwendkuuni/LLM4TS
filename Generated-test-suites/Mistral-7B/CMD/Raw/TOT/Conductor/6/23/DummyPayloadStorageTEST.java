package com.netflix.conductor.core.storage.test;

import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalStorageLocation;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.UPLOAD;
        String path = "testPath";

        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        ExternalStorageLocation expectedResult = null;

        ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

        Assert.assertNull(result);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream payload = mock(InputStream.class);
        long payloadSize = 1024L;

        dummyPayloadStorage.upload(path, payload, payloadSize);

        // No verification needed as the method is empty
    }

    @Test
    public void testDownload() {
        String path = "testPath";

        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream expectedResult = mock(InputStream.class);

        when(dummyPayloadStorage.download(path)).thenReturn(expectedResult);

        InputStream result = dummyPayloadStorage.download(path);

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testGetLocationWithNullOperation() {
        PayloadType payloadType = PayloadType.UPLOAD;
        String path = "testPath";

        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = null;

        ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

        Assert.assertNull(result);
    }

    @Test
    public void testGetLocationWithNullPath() {
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.UPLOAD;

        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = null;

        ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

        Assert.assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testUploadWithNullPath() {
        String path = null;
        InputStream payload = mock(InputStream.class);
        long payloadSize = 1024L;

        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        dummyPayloadStorage.upload(path, payload, payloadSize);
    }
}