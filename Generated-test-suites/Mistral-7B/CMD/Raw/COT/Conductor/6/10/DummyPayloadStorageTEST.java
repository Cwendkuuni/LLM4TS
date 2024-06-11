package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalStorageLocation;
import com.netflix.conductor.core.storage.DummyPayloadStorage;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";
        ExternalStorageLocation expectedLocation = null;
        ExternalStorageLocation actualLocation = dummyPayloadStorage.getLocation(operation, payloadType, path);
        assertNull(actualLocation);
        assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream payload = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0L;
        dummyPayloadStorage.upload(path, payload, payloadSize);
        // verifying that the method does not throw any exception
        // as it is a void method and does not return any value
        assertTrue(true);
    }

    @Test(expected = IOException.class)
    public void testDownload_NullPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = null;
        try {
            dummyPayloadStorage.download(path);
        } catch (IOException e) {
            // expected exception
        }
    }

    @Test
    public void testDownload_NullInputStream() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream inputStream = null;
        try {
            dummyPayloadStorage.download(path);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            dummyPayloadStorage.download(path, outputStream, 0L);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected exception
        }
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0L;
        try {
            dummyPayloadStorage.download(path, outputStream, payloadSize);
            byte[] expectedBytes = new byte[0];
            byte[] actualBytes = outputStream.toByteArray();
            assertArrayEquals(expectedBytes, actualBytes);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testDownload_UnexpectedPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath/test";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0L;
        try {
            dummyPayloadStorage.download(path, outputStream, payloadSize);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected exception
        }
    }

    @Test
    public void testDownload_UnexpectedPayloadSize() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        long payloadSize = Long.MAX_VALUE;
        try {
            dummyPayloadStorage.download(path, outputStream, payloadSize);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected exception
        }
    }
}