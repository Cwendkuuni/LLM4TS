package com.netflix.conductor.core.storage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() throws Exception {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    //Testing getLocation method
    @Test
    public void testGetLocation() {
        assertNull(dummyPayloadStorage.getLocation(ExternalPayloadStorage.Operation.READ, ExternalPayloadStorage.PayloadType.DATA, "somePath"));
    }
    
    //Testing upload method
    @Test
    public void testUpload() {
        InputStream payload = new ByteArrayInputStream("test payload".getBytes());
        dummyPayloadStorage.upload("somePath" , payload , 12);
        //This test doesn't have any assertion as the method has void return type and doesn't do any operation
    }
    
    //Testing download method
    @Test
    public void testDownload() {
        assertNull(dummyPayloadStorage.download("somePath"));
    }
}
