import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(ExternalPayloadStorage.Operation.UPLOAD, ExternalPayloadStorage.PayloadType.INPUT, "test.txt");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        String testPayload = "Test payload";
        InputStream inputStream = new ByteArrayInputStream(testPayload.getBytes());
        dummyPayloadStorage.upload("test.txt", inputStream, testPayload.length());
        // Add assertions here if needed
    }

    @Test
    public void testDownload() {
        InputStream downloadedPayload = dummyPayloadStorage.download("test.txt");
        assertNull(downloadedPayload);
    }
}