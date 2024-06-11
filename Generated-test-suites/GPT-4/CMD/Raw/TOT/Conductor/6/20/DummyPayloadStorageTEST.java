
package com.netflix.conductor.core.storage;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.io.InputStream;

import static junit.framework.Assert.assertNull;

@RunWith(JUnit4.class)
public class DummyPayloadStorageTest {

    private ExternalPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void getLocationTest(){
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(null, null, null);
        assertNull(location);
    }

    @Test
    public void uploadTest(){
        try {
            dummyPayloadStorage.upload(null, null, 0);
        } catch (Exception e) {
            fail("Expected no exceptions, but got: " + e.getMessage());
        }
    }

    @Test
    public void downloadTest(){
        InputStream result = dummyPayloadStorage.download(null);
        assertNull(result);
    }
}
