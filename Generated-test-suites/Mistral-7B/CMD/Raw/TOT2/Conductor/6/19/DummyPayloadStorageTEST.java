import org.junit.Test;
import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {
    // 

    @Test
    public void testGetLocation_whenOperationIsNull_thenReturnNull() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = null;
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertNull(result);
    }

    // 
    
    // Repeat the process for upload and download methods
}