import org.junit.Test;
import static org.junit.Assert.*;

public class DummyPayloadStorageTest {

    DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.READ, PayloadType.JSON, "test/path");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        dummyPayloadStorage.upload("test/path", new InputStream(), 10);
        // No assertions as method does not return anything
    }

    @Test
    public void testDownload() {
        InputStream inputStream = dummyPayloadStorage.download("test/path");
        assertNull(inputStream);
    }

    @Test(expected = NullPointerException.class)
    public void testNullPathGetLocation() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.READ, PayloadType.JSON, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativePayloadSizeUpload() {
        dummyPayloadStorage.upload("test/path", new InputStream(), -10);
    }
}