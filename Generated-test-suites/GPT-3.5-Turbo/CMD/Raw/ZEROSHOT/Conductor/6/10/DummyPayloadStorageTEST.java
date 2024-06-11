import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.InputStream;

import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.READ, PayloadType.TASK_INPUT, "path/to/file.txt");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream payload = mock(InputStream.class);
        dummyPayloadStorage.upload("path/to/file.txt", payload, 100L);
        // No assertion as this method is void
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream payload = dummyPayloadStorage.download("path/to/file.txt");
        assertNull(payload);
    }
}