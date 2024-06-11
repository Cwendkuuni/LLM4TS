import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyStorage = new DummyPayloadStorage();
        String path = "testPath";
        ExternalStorageLocation location = dummyStorage.getLocation(Operation.READ, PayloadType.TASK_OUTPUT, path);
        assertNull(location);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream payload = null;
        long payloadSize = 0;
        dummyStorage.upload(path, payload, payloadSize);
        // No exception thrown means successful upload
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream downloadedPayload = dummyStorage.download(path);
        assertNull(downloadedPayload);
    }
}