
import static org.junit.Assert.assertNull;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import org.junit.Test;
import org.junit.Before;

import java.io.InputStream;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(ExternalPayloadStorage.Operation.READ, ExternalPayloadStorage.PayloadType.TASK_INPUT, "/dummyPath");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        InputStream stream = new InputStream() {
            @Override
            public int read() {
                return 123;
            }
        };
        // Since upload does nothing, we just need to confirm whether it runs without any exceptions
        dummyPayloadStorage.upload("/dummyPath", stream, 1);
    }

    @Test
    public void testDownload() {
        InputStream stream = dummyPayloadStorage.download("/dummyPath");
        assertNull(stream);
    }
}
