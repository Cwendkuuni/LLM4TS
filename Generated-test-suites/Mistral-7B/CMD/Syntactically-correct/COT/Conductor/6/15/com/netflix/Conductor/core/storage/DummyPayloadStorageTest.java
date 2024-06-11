package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
        assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream payload = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0;
        dummyPayloadStorage.upload(path, payload, payloadSize);
        // check if the method does not throw an exception
        // as the method is empty in this class
        // adding an assertion for demonstration purpose only
        assertTrue(true);
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream expectedInput = new ByteArrayInputStream(new byte[0]);
        InputStream input = dummyPayloadStorage.download(path);
        assertSame(expectedInput, input);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLocationWithException() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = null;
        dummyPayloadStorage.getLocation(operation, payloadType, path);
    }

    @Test(expected = IOException.class)
    public void testDownloadWithException() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            dummyPayloadStorage.download(path);
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }
        fail("Expected IOException but got null");
    }

    @Test
    public void testUploadWithLargePayload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        byte[] largePayload = new byte[1024 * 1024 * 10]; // 10 MB
        InputStream payload = new ByteArrayInputStream(largePayload);
        long payloadSize = largePayload.length;
        dummyPayloadStorage.upload(path, payload, payloadSize);
        // check if the method does not throw an exception
        // as the method is empty in this class
        // adding an assertion for demonstration purpose only
        assertTrue(true);
    }
}