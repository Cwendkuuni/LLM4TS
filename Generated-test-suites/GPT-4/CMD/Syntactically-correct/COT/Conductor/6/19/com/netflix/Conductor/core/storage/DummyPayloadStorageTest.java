package com.netflix.conductor.core.storage;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.InputStream;

public class DummyPayloadStorageTest {

    DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        assertNull(dummyPayloadStorage.getLocation(null, null, null));
    }

    @Test
    public void testUpload() {
        try {
            dummyPayloadStorage.upload(null, null, 0L);
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    public void testDownload() {
        assertNull(dummyPayloadStorage.download(null));
    }
}