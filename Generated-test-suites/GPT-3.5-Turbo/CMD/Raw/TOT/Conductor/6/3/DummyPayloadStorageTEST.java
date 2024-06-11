import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = Operation.READ;
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        ExternalStorageLocation location = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertNull(location);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream payload = new ByteArrayInputStream("test data".getBytes());
        long payloadSize = 10;

        dummyPayloadStorage.upload(path, payload, payloadSize);
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";

        InputStream result = dummyPayloadStorage.download(path);

        assertNull(result);
    }
}