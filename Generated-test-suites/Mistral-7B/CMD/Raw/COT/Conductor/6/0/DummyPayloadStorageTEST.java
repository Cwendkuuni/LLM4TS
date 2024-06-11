package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalStorageLocation;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        ExternalStorageLocation expectedResult = null;
        ExternalStorageLocation actualResult = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertNull(actualResult);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLocationWithNullOperation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        Operation operation = null;
        ExternalStorageLocation expectedException = null;

        try {
            dummyPayloadStorage.getLocation(operation, payloadType, path);
        } catch (IllegalArgumentException e) {
            expectedException = e;
        }

        assertNotNull(expectedException);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLocationWithNullPayloadType() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        String path = "testPath";

        PayloadType payloadType = null;
        ExternalStorageLocation expectedException = null;

        try {
            dummyPayloadStorage.getLocation(operation, payloadType, path);
        } catch (IllegalArgumentException e) {
            expectedException = e;
        }

        assertNotNull(expectedException);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLocationWithNullPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;

        String path = null;
        ExternalStorageLocation expectedException = null;

        try {
            dummyPayloadStorage.getLocation(operation, payloadType, path);
        } catch (IllegalArgumentException e) {
            expectedException = e;
        }

        assertNotNull(expectedException);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream payload = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0;

        dummyPayloadStorage.upload(path, payload, payloadSize);

        // As this method doesn't do anything, there's no need for an assertion here
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUploadWithNullPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream payload = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0;

        String path = null;
        ExternalStorageLocation expectedException = null;

        try {
            dummyPayloadStorage.upload(path, payload, payloadSize);
        } catch (IllegalArgumentException e) {
            expectedException = e;
        }

        assertNotNull(expectedException);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUploadWithNullPayload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        long payloadSize = 0;

        InputStream payload = null;
        ExternalStorageLocation expectedException = null;

        try {
            dummyPayloadStorage.upload(path, payload, payloadSize);
        } catch (IllegalArgumentException e) {
            expectedException = e;
        }

        assertNotNull(expectedException);
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";

        InputStream expectedResult = null;
        InputStream actualResult = dummyPayloadStorage.download(path);

        assertNull(actualResult);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDownloadWithEmptyPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        String path = "";
        InputStream expectedResult = null;
        InputStream actualResult = dummyPayloadStorage.download(path);

        assertNull(actualResult);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IOException.class)
    public void testDownloadWithNullInputStream() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";

        InputStream inputStream = null;
        InputStream expectedException = null;

        try {
            dummyPayloadStorage.download(path, inputStream);
        } catch (IOException e) {
            expectedException = e;
        }

        assertNotNull(expectedException);
    }
}