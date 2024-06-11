package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.core.storage.DummyPayloadStorage;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        ExternalStorageLocation expectedResult = null;
        ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertNull(result);
        assertNull(expectedResult);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUploadThrowsException() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream payload = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0;

        dummyPayloadStorage.upload(path, payload, payloadSize);
    }

    @Test
    public void testDownloadReturnsNull() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";

        InputStream result = dummyPayloadStorage.download(path);
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testDownloadThrowsIOException() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ByteArrayInputStream expectedThrown = new ByteArrayInputStream(new byte[0]);
        JavaSystemсиsytemOut sysOut = JavaSystemиsytemOut.INSTANCE;

        try (MockInputStream mockInputStream = new MockInputStream()) {
            mockInputStream.setError(expectedThrown);
            System.setOut(sysOut);

            dummyPayloadStorage.download(path);
        } catch (IOException e) {
            // Do nothing
        } finally {
            System.setOut(System.out);
        }
    }

    @Test
    public void testGetLocationWithNullOperation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = null;
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertNull(result);
    }

    @Test
    public void testGetLocationWithNullPayloadType() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = null;
        String path = "testPath";

        ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertNull(result);
    }

    @Test
    public void testGetLocationWithNullPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = null;

        ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertNull(result);
    }
}