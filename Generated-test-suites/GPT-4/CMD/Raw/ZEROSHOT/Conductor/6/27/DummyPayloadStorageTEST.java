
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.READ, PayloadType.TASK_INPUT, "path");
        Assert.assertNull(location);
    }

    @Test
    public void testUpload() {
        // There's nothing to assert as the method does not yield any output or exception
        InputStream payload = InputStream.nullInputStream();
        dummyPayloadStorage.upload("path", payload, 123L);
    }

    @Test
    public void testDownload() {
        InputStream downloadStream = dummyPayloadStorage.download("path");
        Assert.assertNull(downloadStream);
    }
}
