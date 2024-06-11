import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setup() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.WRITE, PayloadType.JSON, "test.json");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        dummyPayloadStorage.upload("test.json", new InputStream(), 100);
        // Test if no exceptions are thrown
    }

    @Test
    public void testDownload() {
        InputStream inputStream = dummyPayloadStorage.download("test.json");
        assertNull(inputStream);
    }
}