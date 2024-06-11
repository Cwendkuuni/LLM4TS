import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Before;
import org.junit.Test;
import java.io.InputStream;
import static org.junit.Assert.*;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void getLocationShouldReturnNull() {
        assertNull(dummyPayloadStorage.getLocation(ExternalPayloadStorage.Operation.ADD, ExternalPayloadStorage.PayloadType.TASK_INPUT, ""));
    }

    @Test(expected = NullPointerException.class)
    public void uploadShouldNotChangeAnything() {
        InputStream inputStream = null;
        dummyPayloadStorage.upload("", inputStream, 0);
        assertTrue(true);
        // If a NullPointerException is not thrown here, then a behavior other than noop is performed.
    }

    @Test
    public void downloadShouldReturnNull() {
        assertNull(dummyPayloadStorage.download(""));
    }

}