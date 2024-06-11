
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {
    private DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void getLocationTest() {
        assertNull(dummyPayloadStorage.getLocation(
            ExternalPayloadStorage.Operation.READ,
            ExternalPayloadStorage.PayloadType.TASK_INPUT,
            "dummyPath"));
    }

    @Test(expected = Exception.class)
    public void uploadTest() {
        dummyPayloadStorage.upload("dummyPath", null, 1024);
        
        // Although the upload method doesn't do anything, it is still good to call
        // it in the test to see if it throws an exception or has unwanted behaviors.
    }

    @Test
    public void downloadTest() {
        assertNull(dummyPayloadStorage.download("dummyPath"));
    }
}
