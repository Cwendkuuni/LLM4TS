import static org.junit.Assert.assertNull;

import java.io.InputStream;
import org.junit.Test;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;


public class DummyPayloadStorageTest {
    private final DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
    
    @Test
    public void testGetLocationReturnsNull() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.READ, PayloadType.TASK_OUTPUT, "dummyPath");
        assertNull(location);
    }
    
    @Test
    public void testUploadDoesNothing() {
        dummyPayloadStorage.upload("dummyPath", null, 0);
        // pass if no exception thrown
    }
    
    @Test
    public void testDownloadReturnsNull() {
        InputStream istream = dummyPayloadStorage.download("dummyPath");
        assertNull(istream);
    }
    
    @Test
    public void testGetLocationWithNullInput() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(null, null, null);
        assertNull(location);
    }
    
    @Test
    public void testUploadWithNullInput() {
        dummyPayloadStorage.upload(null, null, 0);
        // pass if no exception thrown
    }
    
    @Test
    public void testDownloadWithNullInput() {
        InputStream istream = dummyPayloadStorage.download(null);
        assertNull(istream);
    }
}