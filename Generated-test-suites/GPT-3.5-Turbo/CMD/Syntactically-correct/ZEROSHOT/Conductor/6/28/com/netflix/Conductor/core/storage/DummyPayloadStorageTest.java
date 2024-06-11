import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import java.io.InputStream;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(null, null, null);
        assertNull(location);
    }

    @Test
    public void testUpload() {
        dummyPayloadStorage.upload("test/path", null, 0);
        // No assertion as this method has void return type
    }

    @Test
    public void testDownload() {
        InputStream inputStream = dummyPayloadStorage.download("test/path");
        assertNull(inputStream);
    }
}