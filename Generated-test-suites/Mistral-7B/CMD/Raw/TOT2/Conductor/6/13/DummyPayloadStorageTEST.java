import org.junit.Test;
import static org.junit.Assert.*;
import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.utils.PayloadType;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();
        assertNull(payloadStorage.getLocation(new Operation(), PayloadType.JSON, "testPath"));
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();
        payloadStorage.upload("testPath", null, 0);
    }

    @Test(expected = NullPointerException.class)
    public void testUploadNullOperation() {
        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();
        payloadStorage.upload(null, PayloadType.JSON, "testPath".getBytes());
    }

    @Test(expected = NullPointerException.class)
    public void testUploadNullPayloadType() {
        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();
        payloadStorage.upload(new Operation(), null, "testPath".getBytes());
    }

    @Test(expected = NullPointerException.class)
    public void testUploadNullInputStream() {
        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();
        payloadStorage.upload("testPath", null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUploadNegativePayloadSize() {
        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();
        payloadStorage.upload("testPath", new byte[0][], -1);
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();
        assertNull(payloadStorage.download("testPath"));
    }

    @Test(expected = NullPointerException.class)
    public void testDownloadNullPath() {
        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();
        payloadStorage.download(null);
    }
}