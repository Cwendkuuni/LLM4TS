import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyStorage = new DummyPayloadStorage();
        ExternalStorageLocation location = dummyStorage.getLocation(Operation.GET, PayloadType.INPUT, "testPath");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyStorage = new DummyPayloadStorage();
        InputStream inputStream = new ByteArrayInputStream("Test Payload".getBytes());
        dummyStorage.upload("testPath", inputStream, 11);

        // No assertion as method does not return anything
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyStorage = new DummyPayloadStorage();
        InputStream downloadedPayload = dummyStorage.download("testPath");
        assertNull(downloadedPayload);
    }
}