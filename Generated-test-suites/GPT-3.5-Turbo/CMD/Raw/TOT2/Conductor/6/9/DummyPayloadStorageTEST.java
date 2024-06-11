import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.GET, PayloadType.TASK_INPUT, "dummyPath");
        
        assertNull(location);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream inputStream = null;
        dummyPayloadStorage.upload("dummyPath", inputStream, 0);
        
        // No exception expected, so test passes
        assertEquals(true, true);
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream downloadedPayload = dummyPayloadStorage.download("dummyPath");
        
        assertNull(downloadedPayload);
    }

}