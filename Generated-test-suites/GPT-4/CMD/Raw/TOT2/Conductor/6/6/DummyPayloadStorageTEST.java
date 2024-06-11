import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

    private ExternalPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp(){
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void getLocationShouldReturnNull() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(null, null, "");
        assertNull(location);
    }

    @Test
    public void downloadShouldReturnNull() {
        InputStream inputStream = dummyPayloadStorage.download("");
        assertNull(inputStream);
    }
}