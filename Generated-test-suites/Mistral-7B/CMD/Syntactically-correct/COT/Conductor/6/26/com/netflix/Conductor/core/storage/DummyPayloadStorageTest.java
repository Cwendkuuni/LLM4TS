package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

import com.netflix.conductor.common.Operation;
import com.netflix.conductor.common.PayloadType;
import com.netflix.conductor.common.utils.ExternalStorageLocation;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.core.storage.ExternalPayloadStorage;

/**
 * Test class for {@link DummyPayloadStorage}
 *
 * @author John Doe
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
        String path = "test-path";

        ExternalStorageLocation expectedLocation = null;
        ExternalStorageLocation actualLocation = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertEquals(expectedLocation, actualLocation);
    }

    /**
     * Test method for {@link DummyPayloadStorage#upload(String, InputStream, long)}
     */
    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "test-path";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0L;

        dummyPayloadStorage.upload(path, inputStream, payloadSize);

        // No verification needed as the method does not have any side effects
    }

    /**
     * Test method for {@link DummyPayloadStorage#download(String)}
     */
    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "test-path";

        InputStream expectedInputStream = null;
        InputStream actualInputStream = dummyPayloadStorage.download(path);

        assertEquals(expectedInputStream, actualInputStream);
    }

    /**
     * Test method for {@link DummyPayloadStorage#getLocation(Operation, PayloadType, String)} with null operation
     */
    @Test(expected = NullPointerException.class)
    public void testGetLocationWithNullOperation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        PayloadType payloadType = PayloadType.JSON;
        String path = "test-path";

        dummyPayloadStorage.getLocation(null, payloadType, path);
    }

    /**
     * Test method for {@link DummyPayloadStorage#upload(String, InputStream, long)} with null path
     */
    @Test(expected = NullPointerException.class)
    public void testUploadWithNullPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0L;

        dummyPayloadStorage.upload(null, inputStream, payloadSize);
    }

    /**
     * Test method for {@link DummyPayloadStorage#download(String)} with null path
     */
    @Test(expected = NullPointerException.class)
    public void testDownloadWithNullPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        dummyPayloadStorage.download(null);
    }

    /**
     * Test method for {@link DummyPayloadStorage#getLocation(Operation, PayloadType, String)} with empty path
     */
    @Test
    public void testGetLocationWithEmptyPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = "";

        ExternalStorageLocation expectedLocation = null;
        ExternalStorageLocation actualLocation = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertEquals(expectedLocation, actualLocation);
    }

    /**
     * Test method for {@link DummyPayloadStorage#upload(String, InputStream, long)} with empty path
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUploadWithEmptyPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0L;

        dummyPayloadStorage.upload("", inputStream, payloadSize);
    }

    /**
     * Test method for {@link DummyPayloadStorage#download(String)} with empty path
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDownloadWithEmptyPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        dummyPayloadStorage.download("");
    }
}