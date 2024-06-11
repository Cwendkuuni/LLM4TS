
package com.netflix.conductor.core.storage;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation_ReturnsNull() {
        ExternalStorageLocation result = dummyPayloadStorage.getLocation(
                ExternalPayloadStorage.Operation.READ, 
                ExternalPayloadStorage.PayloadType.TASK_INPUT, 
                "dummyPath");

        assertNull(result);
    }

    @Test
    public void testUpload_DoesNothing() {
        InputStream dummyInputStream = new ByteArrayInputStream("dummyData".getBytes());

        try {
            dummyPayloadStorage.upload("dummyPath", dummyInputStream, 8);
        } catch (Exception e) {
            assertNotNull("Exception should not have been thrown", e);
        }
    }

    @Test
    public void testDownload_ReturnsNull() {
        InputStream result = dummyPayloadStorage.download("dummyPath");

        assertNull(result);
    }
}
