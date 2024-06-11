import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

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
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(
                Operation.READ, PayloadType.TASK_INPUT, "path/to/file");

        assertEquals(null, location);
    }

    @Test
    public void testUpload() {
        InputStream payload = mock(InputStream.class);
        long payloadSize = 100L;

        dummyPayloadStorage.upload("path/to/file", payload, payloadSize);
        // Assert that no exceptions are thrown
    }

    @Test
    public void testDownload() {
        InputStream result = dummyPayloadStorage.download("path/to/file");

        assertEquals(null, result);
    }
}