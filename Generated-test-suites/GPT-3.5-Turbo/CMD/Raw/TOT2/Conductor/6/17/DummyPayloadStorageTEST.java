import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.READ, PayloadType.TASK_INPUT, "dummy/path");

        assertNull(location);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "dummy/path";
        InputStream payload = new ByteArrayInputStream("Test Payload".getBytes());
        long payloadSize = 11;

        dummyPayloadStorage.upload(path, payload, payloadSize);

        // No assertion as this is a void method
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "dummy/path";

        InputStream result = dummyPayloadStorage.download(path);

        assertNull(result);
    }

    @Test
    public void testGetLocationWithNullPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.WRITE, PayloadType.TASK_OUTPUT, null);

        assertNull(location);
    }

    @Test
    public void testUploadWithEmptyPayload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "dummy/path";
        InputStream payload = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0;

        dummyPayloadStorage.upload(path, payload, payloadSize);

        // No assertion as this is a void method
    }

    @Test
    public void testDownloadNonExistingPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "non-existing/path";

        InputStream result = dummyPayloadStorage.download(path);

        assertNull(result);
    }

}