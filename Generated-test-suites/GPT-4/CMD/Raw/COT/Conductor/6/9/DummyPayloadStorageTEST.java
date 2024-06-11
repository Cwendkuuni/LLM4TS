
import org.junit.Test;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;
import com.netflix.conductor.core.storage.DummyPayloadStorage;

public class DummyPayloadStorageTest {

    DummyPayloadStorage dummyStorage = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        assertNull(dummyStorage.getLocation(Operation.READ, PayloadType.TASK_OUTPUT, "path"));
    }

    @Test
    public void testUpload() {
        InputStream testInput = new ByteArrayInputStream("test data".getBytes());
        try {
            dummyStorage.upload("path", testInput, 10L);
        } catch (Exception e) {
            throw new AssertionError("Unexpected exception thrown during upload.");
        }
    }

    @Test
    public void testDownload() {
        assertNull(dummyStorage.download("path"));
    }

}
