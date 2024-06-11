
package com.netflix.conductor.core.storage;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNull;

import java.io.InputStream;

import com.netflix.conductor.common.run.ExternalStorageLocation;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void getLocationReturnsNullTest() {
        ExternalStorageLocation result = dummyPayloadStorage.getLocation(null, null, null);
        assertNull(result);
    }

    @Test
    public void uploadDoesNothingTest() {
        // Since the method is void and expected to do nothing, 
        // the best we can do is to call the method and see if it throws any unexpected exception.
        dummyPayloadStorage.upload("", null, 0L);
    }

    @Test
    public void downloadReturnsNullTest() {
        InputStream result = dummyPayloadStorage.download("");
        assertNull(result);
   }
}
