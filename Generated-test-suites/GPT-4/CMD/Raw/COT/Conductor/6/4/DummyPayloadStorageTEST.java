import org.junit.Before;
import org.junit.Test;

public class DummyPayloadStorageTest {
    private DummyPayloadStorage storage;

    @Before
    public void setUp() {
        storage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
        assertNull(storage.getLocation(null, null, null));
    }

    @Test
    public void testUpload() {
        storage.upload(null, null, 0);
    }

    @Test
    public void testDownload() {
        assertNull(storage.download(null));
    }
}