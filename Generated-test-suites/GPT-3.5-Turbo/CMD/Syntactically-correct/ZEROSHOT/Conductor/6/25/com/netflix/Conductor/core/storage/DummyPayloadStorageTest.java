import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.UPLOAD, PayloadType.TASK_INPUT, "test_path");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        dummyPayloadStorage.upload("test_path", null, 0);
        // No assertion as this method doesn't return anything
    }

    @Test
    public void testDownload() {
        InputStream inputStream = dummyPayloadStorage.download("test_path");
        assertNull(inputStream);
    }
}