import org.junit.Test;
import static org.junit.Assert.*;

public class DummyPayloadStorageTest {

    DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.UPLOAD, PayloadType.TASK_INPUT, "dummy/path");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        dummyPayloadStorage.upload("dummy/path", null, 0);
        // No assertion as this method does not return anything
    }

    @Test
    public void testDownload() {
        InputStream inputStream = dummyPayloadStorage.download("dummy/path");
        assertNull(inputStream);
    }

    // Edge case: empty path
    @Test
    public void testGetLocationEmptyPath() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.DOWNLOAD, PayloadType.TASK_OUTPUT, "");
        assertNull(location);
    }

    // Exception handling: uploading null payload
    @Test(expected = NullPointerException.class)
    public void testUploadNullPayload() {
        dummyPayloadStorage.upload("dummy/path", null, 10);
    }

    // Exception handling: downloading from non-existing path
    @Test
    public void testDownloadNonExistingPath() {
        assertNull(dummyPayloadStorage.download("non-existing/path"));
    }
}