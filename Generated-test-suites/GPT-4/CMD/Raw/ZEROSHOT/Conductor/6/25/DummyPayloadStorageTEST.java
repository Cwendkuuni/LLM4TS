
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.io.InputStream;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.READ, PayloadType.TASK_INPUT, "testPath");
        assertNull(location);
    }

    @Test(expected=NullPointerException.class)
    public void testUpload() {
        InputStream payload = null;
        dummyPayloadStorage.upload("testPath", payload, 1050);
    }

    @Test
    public void testDownload() {
        InputStream downloadStream = dummyPayloadStorage.download("testPath");
        assertNull(downloadStream);
    }
}
