
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
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(
                Operation.WRITE, PayloadType.TASK_INPUT, "test-path");

        // As the DummyPayloadStorage#getLocation is hardcoded to return null,
        // we assert that the result is indeed null
        assertNull(location);
    }

    @Test(expected = NullPointerException.class)
    public void testUpload() {
        // As the DummyPayloadStorage#upload does not do anything, 
        // trying to upload a null InputStream should logically throw a NullPointerException.
        dummyPayloadStorage.upload("test-path", null, 100);
    }

    @Test
    public void testDownload() {
        InputStream inputStream = dummyPayloadStorage.download("test-path");

        // As the DummyPayloadStorage#download is hardcoded to return null,
        // we assert that the result is indeed null
        assertNull(inputStream);
    }
}
