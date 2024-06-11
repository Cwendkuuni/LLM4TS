package com.netflix.conductor.core.storage;

import com.netflix.conductor.common.Operation;
import com.netflix.conductor.common.payload.PayloadType;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocationReturnsNull() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = Mockito.mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        ExternalStorageLocation expectedResult = null;
        ExternalStorageLocation actualResult = dummyPayloadStorage.getLocation(operation, payloadType, path);

        Assert.assertNull(actualResult);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testUploadDoesNotDoAnything() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        byte[] testData = "testData".getBytes();
        long payloadSize = testData.length;

        InputStream inputStream = new ByteArrayInputStream(testData);

        dummyPayloadStorage.upload(path, inputStream, payloadSize);

        // No verification needed as the method does not do anything
    }

    @Test
    public void testDownloadReturnsNull() throws IOException {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";

        InputStream expectedResult = null;
        InputStream actualResult = dummyPayloadStorage.download(path);

        Assert.assertNull(actualResult);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testUploadAndDownloadFlow() throws IOException {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        byte[] testData = "testData".getBytes();
        long payloadSize = testData.length;

        InputStream inputStream = new ByteArrayInputStream(testData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] expectedResult = testData;
        byte[] actualResult = new byte[1024];

        // Upload the data
        dummyPayloadStorage.upload(path, inputStream, payloadSize);

        // Download the data
        InputStream downloadedData = dummyPayloadStorage.download(path);

        int read = 0;
        while ((read = downloadedData.read(actualResult, 0, actualResult.length)) != -1) {
            outputStream.write(actualResult, 0, read);
        }

        byte[] downloadedDataAsArray = outputStream.toByteArray();

        Assert.assertArrayEquals(expectedResult, downloadedDataAsArray);
    }
}