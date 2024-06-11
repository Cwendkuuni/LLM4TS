package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class DummyPayloadStorageTest {
    DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }
    
    @Test
    public void getLocationTest() {
        Operation operation = new Operation();
        PayloadType payloadType = new PayloadType();
        String path = "test/path";
        assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
    }
    
    @Test
    public void uploadTest() {
        String path = "test/path";
        InputStream payload = null;
        long payloadSize = 0;

        try {
            dummyPayloadStorage.upload(path, payload, payloadSize);
        } catch(Exception e) {
            fail("This test is failed due to an unexpected exception.");
        }
    }
    
    @Test
    public void downloadTest() {
        String path = "test/path";
        assertNull(dummyPayloadStorage.download(path));
    }
}
