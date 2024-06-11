
package com.netflix.conductor.core.storage;
import java.io.InputStream;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DummyPayloadStorageTest {

    DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        Assert.assertNull(dummyPayloadStorage.getLocation(Operation.READ, PayloadType.TASK_INPUT, "/path"));
        Assert.assertNull(dummyPayloadStorage.getLocation(null, PayloadType.TASK_INPUT, "/path"));
        Assert.assertNull(dummyPayloadStorage.getLocation(Operation.READ, null, "/path"));
        Assert.assertNull(dummyPayloadStorage.getLocation(Operation.READ, PayloadType.TASK_INPUT, null));
    }

    @Test
    public void testUpload() {
        InputStream is = null;
        dummyPayloadStorage.upload("/path", is, 0);
        dummyPayloadStorage.upload(null, is, 0);
    }

    @Test
    public void testDownload() {
        Assert.assertNull(dummyPayloadStorage.download("/path"));
        Assert.assertNull(dummyPayloadStorage.download(null));
    }
}
