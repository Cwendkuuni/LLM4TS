
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

public class DummyPayloadStorageTest {

    @Test
    public void getLocationTest() throws Exception{
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        ExternalStorageLocation result = dummyPayloadStorage.getLocation(ExternalPayloadStorage.Operation.WRITE, ExternalPayloadStorage.PayloadType.TASK_INPUT, "/path");
        assertNull(result);
    }

    @Test
    public void uploadTest() throws Exception{
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream payload = null;
        dummyPayloadStorage.upload("/path", payload, 0);
        // As the upload method doesn't return anything and only accepts inputs, there's no direct assert we can make here
        // We could however check for exceptions, as in its current state it's not supposed to throw any.
        assertTrue(true);
    }

    @Test
    public void downloadTest() throws Exception{
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream result = dummyPayloadStorage.download("/path");
        assertNull(result);
    }
}
