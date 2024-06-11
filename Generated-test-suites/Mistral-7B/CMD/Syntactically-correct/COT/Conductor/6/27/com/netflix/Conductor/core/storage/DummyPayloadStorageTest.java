package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.core.storage.ExternalPayloadStorage;
import com.netflix.conductor.core.storage.ExternalStorageLocation;

/**
 * Tests for {@link DummyPayloadStorage}
 */
public class DummyPayloadStorageTest {

    private final DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    /**
     * Test for getLocation method
     */
    @Test
    public void testGetLocation() {
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = "test-path";
        ExternalStorageLocation expectedResult = null;

        ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertNull(result);
        assertEquals(expectedResult, result);
    }

    /**
     * Test for upload method with null InputStream
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUploadWithNullInputStream() {
        String path = "test-path";
        InputStream payload = null;
        long payloadSize = 1024L;

        dummyPayloadStorage.upload(path, payload, payloadSize);
    }

    /**
     * Test for upload method with empty InputStream
     */
    @Test
    public void testUploadWithEmptyInputStream() {
        String path = "test-path";
        InputStream payload = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0L;

        dummyPayloadStorage.upload(path, payload, payloadSize);
    }

    /**
     * Test for upload method with non-empty InputStream
     */
    @Test
    public void testUploadWithNonEmptyInputStream() throws IOException {
        String path = "test-path";
        InputStream payload = new ByteArrayInputStream(new byte[]{"T", "e", "st", "s"});
        long payloadSize = 4L;

        dummyPayloadStorage.upload(path, payload, payloadSize);
    }

    /**
     * Test for download method with null path
     */
    @Test(expected = NullPointerException.class)
    public void testDownloadWithNullPath() {
        String path = null;

        InputStream result = dummyPayloadStorage.download(path);

        assertNull(result);
    }

    /**
     * Test for download method with empty path
     */
    @Test
    public void testDownloadWithEmptyPath() {
        String path = "";

        InputStream result = dummyPayloadStorage.download(path);

        assertNull(result);
    }

    /**
     * Test for download method with non-empty path
     */
    @Test
    public void testDownloadWithNonEmptyPath() {
        String path = "test-path";

        InputStream expectedResult = new ByteArrayInputStream(new byte[0]);
        InputStream result = dummyPayloadStorage.download(path);

        assertNull(result);
        assertEquals(expectedResult, result);
    }
}