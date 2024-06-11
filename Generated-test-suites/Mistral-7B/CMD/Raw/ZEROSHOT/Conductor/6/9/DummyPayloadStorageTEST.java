package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

import com.netflix.conductor.common.Operation;
import com.netflix.conductor.common.RunStatus;
import com.netflix.conductor.common.external.storage.ExternalPayload;
import com.netflix.conductor.common.external.storage.ExternalPayloadType;
import com.netflix.conductor.common.external.storage.ExternalStorageLocation;
import com.netflix.conductor.common.utils.Payloader;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.core.storage.DummyPayloadStorage.DummyPayload;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = "test-path";

        assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUpload() throws IOException {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "test-path";
        byte[] payload = "test-payload".getBytes();
        long payloadSize = payload.length;

        dummyPayloadStorage.upload(path, new ByteArrayInputStream(payload), payloadSize);

        //Since this is a dummy implementation, we cannot test the actual uploading to an external storage
        //Instead, we can test if the method does not throw any exception
        assertTrue(true);
    }

    @Test
    public void testDownload() throws IOException {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "test-path";

        InputStream inputStream = dummyPayloadStorage.download(path);

        assertNull(inputStream);
    }

    @Test
    public void testGetAndSetDummyPayload() throws IOException {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "test-path";
        byte[] payload = "test-payload".getBytes();
        long payloadSize = payload.length;

        //Upload test payload
        dummyPayloadStorage.upload(path, new ByteArrayInputStream(payload), payloadSize);

        //Download test payload
        InputStream inputStream = dummyPayloadStorage.download(path);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] expectedPayload = new byte[payloadSize];
        int bytesRead = -1;

        while ((bytesRead = inputStream.read(expectedPayload, 0, expectedPayload.length)) != -1) {
            outputStream.write(expectedPayload, 0, bytesRead);
        }

        outputStream.flush();
        byte[] actualPayload = outputStream.toByteArray();

        //Compare expected and actual payloads
        assertArrayEquals(payload, actualPayload);

        //Cleanup test payload
        dummyPayloadStorage.delete(path);
    }

    @Test
    public void testDelete() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "test-path";

        dummyPayloadStorage.delete(path);

        //Since this is a dummy implementation, we cannot test if the actual deletion from external storage happens
        //Instead, we can test if the method does not throw any exception
        assertTrue(true);
    }

    @Test
    public void testGetAndSetDummyPayloadWithInvalidPath() throws IOException {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = null;
        byte[] payload = "test-payload".getBytes();
        long payloadSize = payload.length;

        try {
            dummyPayloadStorage.upload(path, new ByteArrayInputStream(payload), payloadSize);
        } catch (NullPointerException e) {
            //Expected since path is null
            assertEquals("path", "Cannot upload null path", e.getMessage());
        }

        try {
            dummyPayloadStorage.download(path);
        } catch (NullPointerException e) {
            //Expected since path is null
            assertEquals("path", "Cannot download null path", e.getMessage());
        }

        try {
            dummyPayloadStorage.delete(path);
        } catch (NullPointerException e) {
            //Expected since path is null
            assertEquals("path", "Cannot delete null path", e.getMessage());
        }
    }

    @Test
    public void testGetAndSetDummyPayloadWithInvalidPayload() throws IOException {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "test-path";
        byte[] payload = null;

        try {
            dummyPayloadStorage.upload(path, new ByteArrayInputStream(payload), -1);
        } catch (NullPointerException e) {
            //Expected since payload is null
            assertEquals("payload", "Cannot upload null payload", e.getMessage());
        }

        try {
            dummyPayloadStorage.download(path);
        } catch (IOException e) {
            //Expected since there is no payload to download
            assertEquals("Unexpected exception", "No payload found for path [" + path + "]", e.getMessage());
        }

        try {
            dummyPayloadStorage.delete(path);
        } catch (IOException e) {
            //Expected since there is no payload to delete
            assertEquals("Unexpected exception", "No payload found for path [" + path + "]", e.getMessage());
        }

        try {
            dummyPayloadStorage.upload(path, new ByteArrayInputStream(new byte[0]), 0);
        } catch (IOException e) {
            //Expected since payload size is zero
            assertEquals("payload size", "Payload size cannot be zero", e.getMessage());
        }
    }

    @Test
    public void testGetAndSetDummyPayloadWithInvalidOperation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = null;

        try {
            dummyPayloadStorage.getLocation(operation, PayloadType.JSON, "test-path");
        } catch (NullPointerException e) {
            //Expected since operation is null
            assertEquals("operation", "Cannot get location with null operation", e.getMessage());
        }

        try {
            dummyPayloadStorage.upload("test-path", new ByteArrayInputStream("test-payload".getBytes()), 1);
            ExternalPayload externalPayload = dummyPayloadStorage.getPayload(operation, "test-path");

            assertNull(externalPayload);

            dummyPayloadStorage.delete("test-path");
        } catch (NullPointerException e) {
            //Expected since operation is null
            assertEquals("operation", "Cannot get/set/delete payload with null operation", e.getMessage());
        }
    }

    @Test
    public void testGetAndSetDummyPayloadWithInvalidPayloadType() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        String path = "test-path";
        PayloadType invalidPayloadType = PayloadType.XML;

        try {
            dummyPayloadStorage.getLocation(operation, invalidPayloadType, path);
        } catch (IllegalArgumentException e) {
            //Expected since invalid payload type is passed
            assertEquals("payload type", "Unsupported payload type [" + invalidPayloadType + "]", e.getMessage());
        }

        try {
            dummyPayloadStorage.upload(path, new ByteArrayInputStream("test-payload".getBytes()), 1);
            ExternalPayload externalPayload = dummyPayloadStorage.getPayload(operation, path);

            assertNull(externalPayload);

            dummyPayloadStorage.delete(path);
        } catch (IllegalArgumentException e) {
            //Expected since invalid payload type is passed
            assertEquals("payload type", "Unsupported payload type [" + invalidPayloadType + "]", e.getMessage());
        }
    }

    @Test
    public void testGetAndSetDummyPayloadWithInvalidPathAndPayloadType() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        String invalidPath = null;
        PayloadType invalidPayloadType = PayloadType.JSON;

        try {
            dummyPayloadStorage.getLocation(operation, invalidPayloadType, invalidPath);
        } catch (NullPointerException e) {
            //Expected since path is null
            assertEquals("path", "Cannot get location with null path", e.getMessage());
        } catch (IllegalArgumentException e) {
            //Expected since invalid payload type is passed
            assertEquals("payload type", "Unsupported payload type [" + invalidPayloadType + "]", e.getMessage());
        }

        try {
            dummyPayloadStorage.upload(invalidPath, new ByteArrayInputStream("test-payload".getBytes()), 1);
        } catch (NullPointerException e) {
            //Expected since path is null
            assertEquals("path", "Cannot upload null path", e.getMessage());
        } catch (IllegalArgumentException e) {
            //Expected since invalid payload type is passed
            assertEquals("payload type", "Unsupported payload type [" + invalidPayloadType + "]", e.getMessage());
        }

        try {
            dummyPayloadStorage.delete(invalidPath);
        } catch (NullPointerException e) {
            //Expected since path is null
            assertEquals("path", "Cannot delete null path", e.getMessage());
        }

        try {
            dummyPayloadStorage.getPayload(operation, invalidPath);
        } catch (NullPointerException e) {
            //Expected since path is null
            assertEquals("path", "Cannot get payload with null path", e.getMessage());
        } catch (IllegalArgumentException e) {
            //Expected since invalid payload type is passed
            assertEquals("payload type", "Unsupported payload type [" + PayloadType.JSON + "]", e.getMessage());
        }

        try {
            dummyPayloadStorage.upload("test-path", new ByteArrayInputStream("test-payload".getBytes()), PayloadType.XML.getId());
            ExternalPayload externalPayload = dummyPayloadStorage.getPayload(operation, "test-path");

            assertNull(externalPayload);

            dummyPayloadStorage.delete("test-path");
        } catch (IllegalArgumentException e) {
            //Expected since invalid payload type is passed
            assertEquals("payload type", "Unsupported payload type [" + PayloadType.XML.getId() + "]", e.getMessage());
        }
    }

    @Test
    public void testGetPayload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        String path = "test-path";
        byte[] payload = "test-payload".getBytes();
        long payloadSize = payload.length;

        dummyPayloadStorage.upload(path, new ByteArrayInputStream(payload), payloadSize);

        ExternalPayload externalPayload = dummyPayloadStorage.getPayload(operation, path);

        assertNotNull(externalPayload);
        assertEquals(ExternalPayloadType.FILE, externalPayload.getType());
        assertEquals(payload, externalPayload.getData());
        assertEquals(path, externalPayload.getPath());
        assertEquals(RunStatus.SUCCEEDED, externalPayload.getStatus());

        dummyPayloadStorage.delete(path);
    }
}