
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;
    private static final String OPERATION = "TestOperation";
    private static final String PATH = "TestPath";

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
       ExternalStorageLocation location = dummyPayloadStorage.getLocation(
               ExternalPayloadStorage.Operation.valueOf(OPERATION), 
               ExternalPayloadStorage.PayloadType.valueOf("DATA"), 
               PATH
        );
        assertNull(location);
    }

    @Test
    public void testUpload() {
        InputStream payload = new ByteArrayInputStream("TestPayload".getBytes());
        dummyPayloadStorage.upload(PATH, payload, payload.available());
        // As the upload method doesn't return anything, there is no direct way to test this.
    }

    @Test
    public void testDownload() {
        InputStream downloadedPayload = dummyPayloadStorage.download(PATH);
        assertNull(downloadedPayload);
    }
}
