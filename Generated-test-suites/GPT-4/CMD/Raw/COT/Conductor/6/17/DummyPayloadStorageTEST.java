
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {
    DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(null, null, "");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        // Nothing to assert in this test case since upload method has an empty implementation.
        dummyPayloadStorage.upload("", null, 0);
    }

    @Test
    public void testDownload() {
        InputStream result = dummyPayloadStorage.download("");
        assertNull(result);
    }

    //Testing Edge Cases
    @Test
    public void testGetLocationWithNulls() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(null, null, null);
        assertNull(location);
    }

    @Test
    public void testUploadWithNullPath() {
        // Nothing to assert in this test case since upload method has an empty implementation.
        dummyPayloadStorage.upload(null, null, 0);
    }

    @Test
    public void testUploadWithNegativePayloadSize() {
        // Nothing to assert in this test case since upload method has an empty implementation.
        dummyPayloadStorage.upload("", null, -1);
    }

    @Test
    public void testDownloadWithNullPath() {
        InputStream result = dummyPayloadStorage.download(null);
        assertNull(result);
    }
}
