import static org.junit.Assert.assertNull;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.core.storage.DummyPayloadStorage;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class DummyPayloadStorageTest {
    private final DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        assertNull(dummyPayloadStorage.getLocation(ExternalPayloadStorage.Operation.READ, ExternalPayloadStorage.PayloadType.TASK_OUTPUT, "dummyPath"));
        assertNull(dummyPayloadStorage.getLocation(ExternalPayloadStorage.Operation.WRITE, ExternalPayloadStorage.PayloadType.TASK_INPUT, "dummyPath"));
    }

    @Test
    public void testUpload() {
        InputStream payload = new ByteArrayInputStream("dummyPayload".getBytes());
        dummyPayloadStorage.upload("dummyPath", payload, payload.available());
        // Since no actual uploading is performed, there's no assert check needed
    }

    @Test
    public void testDownload() {
        assertNull(dummyPayloadStorage.download("dummyPath"));
    }
}