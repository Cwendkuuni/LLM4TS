package com.netflix.conductor.core.storage;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.InputStream;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {
    private DummyPayloadStorage underTest;

    @Before
    public void setup() {
        underTest = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocationReturnsNull() {
        assertNull(underTest.getLocation(ExternalPayloadStorage.Operation.READ, ExternalPayloadStorage.PayloadType.TASK_OUTPUT, "testPath"));
    }

    @Test
    public void testUploadDoesNotThrowException() {
        try {
            underTest.upload("testPath", null, 0);
        } catch (Exception e) {
            fail("Exception should not be thrown.");
        }
    }

    @Test
    public void testDownloadReturnsNull() {
        assertNull(underTest.download("testPath"));
    }
}