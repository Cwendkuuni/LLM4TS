package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.operation.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.common.utils.TestUtils;
import com.netflix.conductor.common.utils.serialization.DeserializationException;
import com.netflix.conductor.common.utils.serialization.SerializationException;

/**
 * Test class for DummyPayloadStorage
 */
public class DummyPayloadStorageTest {

    private ExternalPayloadStorage storage;
    private Operation operation;

    @Before
    public void setUp() {
        storage = new DummyPayloadStorage();
        operation = mock(Operation.class);
    }

    /**
     * Test getLocation method with null input
     */
    @Test
    public void testGetLocation_NullInput() {
        assertNull(storage.getLocation(null, null, null));
    }

    /**
     * Test getLocation method with valid input
     */
    @Test
    public void testGetLocation_ValidInput() {
        assertNull(storage.getLocation(operation, PayloadType.JSON, "testPath"));
    }

    /**
     * Test upload method with empty payload
     */
    @Test
    public void testUpload_EmptyPayload() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(operation.getUniqueId()).thenReturn("testId");

        storage.upload("testPath", TestUtils.getEmptyByteArrayInputStream(), 0);

        // No verification is needed since the method does not throw any exception
    }

    /**
     * Test upload method with non-empty payload
     */
    @Test
    public void testUpload_NonEmptyPayload() throws IOException, SerializationException, DeserializationException {
        String testData = "Test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());

        when(operation.getUniqueId()).thenReturn("testId");

        storage.upload("testPath", inputStream, testData.length());

        // No verification is needed since the method does not throw any exception
    }

    /**
     * Test download method with null input
     */
    @Test
    public void testDownload_NullInput() {
        assertNull(storage.download(null));
    }

    /**
     * Test download method with empty path
     */
    @Test
    public void testDownload_EmptyPath() {
        assertNull(storage.download(""));
    }

    /**
     * Test download method with valid input
     */
    @Test
    public void testDownload_ValidInput() {
        String testData = "Test data";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        when(operation.getUniqueId()).thenReturn("testId");
        when(storage.download("testPath")).thenReturn(new ByteArrayInputStream(testData.getBytes()));

        byte[] downloadedData = TestUtils.toByteArray(storage.download("testPath"));

        assert(TestUtils.compare(downloadedData, testData.getBytes()));
    }
}