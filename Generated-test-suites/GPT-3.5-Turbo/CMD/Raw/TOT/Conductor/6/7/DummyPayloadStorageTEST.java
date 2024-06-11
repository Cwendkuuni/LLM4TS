
import org.junit.Test;
import static org.junit.Assert.*;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        Operation operation = Operation.READ;
        PayloadType payloadType = PayloadType.INPUT;
        String path = "testPath";

        ExternalStorageLocation location = storage.getLocation(operation, payloadType, path);

        assertNull(location);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream payload = new ByteArrayInputStream("test payload".getBytes());
        long payloadSize = 12;

        storage.upload(path, payload, payloadSize);
        // no assertion as the method has void return type
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        String path = "testPath";

        InputStream result = storage.download(path);

        assertNull(result);
    }

}
