import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyStorage = new DummyPayloadStorage();
        ExternalStorageLocation location = dummyStorage.getLocation(Operation.WRITE, PayloadType.TASK_INPUT, "/test/path");

        assertNull(location);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyStorage = new DummyPayloadStorage();
        String path = "/test/path";
        InputStream payload = mock(InputStream.class);

        dummyStorage.upload(path, payload, 100);

        // No validation for upload in dummy implementation
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyStorage = new DummyPayloadStorage();
        String path = "/test/path";

        InputStream result = dummyStorage.download(path);

        assertNull(result);
    }
}