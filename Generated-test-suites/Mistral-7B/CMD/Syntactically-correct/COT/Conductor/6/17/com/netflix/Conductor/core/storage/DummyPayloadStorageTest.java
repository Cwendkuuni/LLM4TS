package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.core.storage.ExternalPayloadStorage;
import com.netflix.conductor.core.storage.ExternalStorageLocation;

/**
 * Test class for DummyPayloadStorage
 */
public class DummyPayloadStorageTest {

    /**
     * Test method for {@link DummyPayloadStorage#getLocation(Operation, PayloadType, String)}
     */
    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";
        ExternalStorageLocation expectedResult = null;
        ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);
        assertEquals(expectedResult, result);
    }

    /**
     * Test method for {@link DummyPayloadStorage#upload(String, InputStream, long)}
     */
    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0;
        dummyPayloadStorage.upload(path, inputStream, payloadSize);
        // As this method is a no-op, no verification is needed here
    }

    /**
     * Test method for {@link DummyPayloadStorage#download(String)}
     */
    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream expectedResult = null;
        InputStream result = dummyPayloadStorage.download(path);
        assertEquals(expectedResult, result);
    }

    /**
     * Test method for {@link DummyPayloadStorage#getLocation(Operation, PayloadType, String)} with null operation
     */
    @Test(expected = NullPointerException.class)
    public void testGetLocationNullOperation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";
        Operation operation = null;
        dummyPayloadStorage.getLocation(operation, payloadType, path);
    }

    /**
     * Test method for {@link DummyPayloadStorage#getLocation(Operation, PayloadType, String)} with null path
     */
    @Test(expected = NullPointerException.class)
    public void testGetLocationNullPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = null;
        dummyPayloadStorage.getLocation(operation, payloadType, path);
    }

    /**
     * Test method for {@link DummyPayloadStorage#upload(String, InputStream, long)} with null path
     */
    @Test(expected = NullPointerException.class)
    public void testUploadNullPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0;
        String path = null;
        dummyPayloadStorage.upload(path, inputStream, payloadSize);
    }

    /**
     * Test method for {@link DummyPayloadStorage#upload(String, InputStream, long)} with null inputStream
     */
    @Test(expected = NullPointerException.class)
    public void testUploadNullInputStream() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream inputStream = null;
        long payloadSize = 0;
        dummyPayloadStorage.upload(path, inputStream, payloadSize);
    }

    /**
     * Test method for {@link DummyPayloadStorage#upload(String, InputStream, long)} with negative payload size
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUploadNegativePayloadSize() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        long payloadSize = -1;
        dummyPayloadStorage.upload(path, inputStream, payloadSize);
    }
}