import static org.junit.Assert.*;
import org.junit.Test;

public class DummyPayloadStorageTest {

    DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.READ, PayloadType.JSON, "test.json");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        dummyPayloadStorage.upload("test.json", new InputStream() {
            @Override
            public int read() {
                return 0;
            }
        }, 10);
        // No assertion as this method doesn't return anything
    }

    @Test
    public void testDownload() {
        InputStream inputStream = dummyPayloadStorage.download("test.json");
        assertNull(inputStream);
    }
}