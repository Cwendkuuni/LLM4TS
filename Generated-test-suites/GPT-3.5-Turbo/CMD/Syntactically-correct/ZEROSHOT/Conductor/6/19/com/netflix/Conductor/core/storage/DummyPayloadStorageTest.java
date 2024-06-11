import org.junit.Test;
import static org.junit.Assert.*;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.GET, PayloadType.TASK_INPUT, "test_path");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        dummyPayloadStorage.upload("test_path", new InputStream() {
            @Override
            public int read() {
                return 0;
            }
        }, 10);
        // No assertion as method does not return anything
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream inputStream = dummyPayloadStorage.download("test_path");
        assertNull(inputStream);
    }
}