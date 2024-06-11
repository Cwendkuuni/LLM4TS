
/*
 * DummyPayloadStorageTest.java
 */
package com.netflix.conductor.core.storage;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.io.InputStream;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {
    DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
        assertNotNull(dummyPayloadStorage);
    }

    @Test
    public void getLocationTest() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(
                ExternalPayloadStorage.Operation.WRITE, 
                ExternalPayloadStorage.PayloadType.TASK_INPUT, 
                "dummyPath"
        );
        assertNull(location);
    }

    @Test
    public void uploadTest() {
        InputStream dummyInputStream = new InputStream() {
            public int read() {
                return -1;
            }
        };
        // This check mostly ensures nothing crashes during the operation
        dummyPayloadStorage.upload("dummyPath", dummyInputStream, 0L);
    }

    @Test
    public void downloadTest() {
        InputStream inputStream = dummyPayloadStorage.download("dummyPath");
        assertNull(inputStream);
    }
}
