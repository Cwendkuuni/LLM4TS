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
        
        InputStream inputStream = new ByteArrayInputStream("Test payload".getBytes());
        
        dummyPayloadStorage.upload("dummy/path", inputStream, 12);
        
        // No assertion as it doesn't return any value, just checking for exceptions
    }
    
    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        
        InputStream downloadedPayload = dummyPayloadStorage.download("dummy/path");
        
        assertNull(downloadedPayload);
    }

    @Test(expected = NullPointerException.class)
    public void testGetLocationWithNullPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        
        dummyPayloadStorage.getLocation(Operation.READ, PayloadType.TASK_INPUT, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testUploadWithNegativePayloadSize() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        
        InputStream inputStream = new ByteArrayInputStream("Test payload".getBytes());
        
        dummyPayloadStorage.upload("dummy/path", inputStream, -1);
    }
    
}