import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() throws Exception {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() throws Exception {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.READ, PayloadType.TASK_INPUT,
          "testPath");
        assertNull(location);
    }

    @Test
    public void testUpload() throws Exception {
        InputStream nullInputStream = null;
        dummyPayloadStorage.upload("testUpload", nullInputStream, 0);
        // No exception means a successful negative test, as DummyPayloadStorage.upload() should have no effect
    }

    @Test
    public void testDownload() throws Exception {
        InputStream downloadedStream = dummyPayloadStorage.download("testDownload");
        assertNull(downloadedStream);
    }
}