package com.netflix.conductor.core.storage;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import junit.framework.TestCase;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class DummyPayloadStorageTest extends TestCase {

    private DummyPayloadStorage dummyPayloadStorage;

    protected void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    /* Test DummyPayloadStorage getLocation() method for valid input */
    public void testGetLocation(){
        ExternalStorageLocation esl = dummyPayloadStorage.getLocation(ExternalPayloadStorage.Operation.READ, ExternalPayloadStorage.PayloadType.TASK_INPUT, "dummyPath");
        assertNull(esl);
    }

    /* Test DummyPayloadStorage upload() method for valid input */
    public void testUpload(){
        InputStream inputStream = new ByteArrayInputStream("testData".getBytes());
        try {
            dummyPayloadStorage.upload("dummyPath", inputStream, 8);
         } catch (Exception e) {
            fail("Exception should not be thrown");
        }
     }

    /* Test DummyPayloadStorage download() method for valid input */
    public void testDownload(){
        InputStream result = dummyPayloadStorage.download("dummyPath");
        assertNull(result);
    }
}
