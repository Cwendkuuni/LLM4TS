
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.core.storage.DummyPayloadStorage;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.UPLOAD, PayloadType.FILE, "test/path");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream payload = new ByteArrayInputStream("Test data".getBytes());
        dummyPayloadStorage.upload("test/path", payload, 10);
        // No assertion as method does not return anything
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream downloadedPayload = dummyPayloadStorage.download("test/path");
        assertNull(downloadedPayload);
    }
}
