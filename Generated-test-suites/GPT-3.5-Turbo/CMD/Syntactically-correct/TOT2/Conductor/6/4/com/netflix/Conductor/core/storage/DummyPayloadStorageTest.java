import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        ExternalStorageLocation location = storage.getLocation(Operation.READ, PayloadType.TASK_INPUT, "test/path");

        assertNull(location);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        InputStream payload = new InputStream() {
            @Override
            public int read() {
                return -1;
            }
        };
        storage.upload("test/path", payload, 10);

        // No exception should be thrown
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        InputStream downloadedPayload = storage.download("test/path");

        assertNull(downloadedPayload);
    }
}