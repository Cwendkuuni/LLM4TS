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
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.UPLOAD, PayloadType.DATA, "test");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        dummyPayloadStorage.upload("test", null, 0);
        // No assertion as this method does not return anything
    }

    @Test
    public void testDownload() {
        InputStream inputStream = dummyPayloadStorage.download("test");
        assertNull(inputStream);
    }

}