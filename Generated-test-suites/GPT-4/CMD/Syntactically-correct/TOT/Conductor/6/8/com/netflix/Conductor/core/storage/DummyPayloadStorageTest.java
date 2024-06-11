package com.netflix.conductor.core.storage;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.Before;
import java.io.InputStream;

public class DummyPayloadStorageTest {
    private DummyPayloadStorage dummyPayloadStorage;
    
    @Before 
    public void initialize() {
       dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
        assertEquals("The method should return null as it is a dummy implementation", null,
                        dummyPayloadStorage.getLocation(null, null, null));
    }

    @Test
    public void testUpload() {
        //Since no action is performed by the method and there is no return statement.
        //We do not need an assertion. It is just for coverage
        dummyPayloadStorage.upload(null, null, 0);
    }

    @Test
    public void testDownload() {
        assertEquals("The method should return null as it is a dummy implementation", null,
                        dummyPayloadStorage.download(null));
    }
}