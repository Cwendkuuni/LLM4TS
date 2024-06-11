import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import org.junit.Test;

public class DummyPayloadStorageTest {

    DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void getLocationTest() {
        ExternalPayloadStorage.Operation operation = ExternalPayloadStorage.Operation.READ;
        ExternalPayloadStorage.PayloadType payloadType = ExternalPayloadStorage.PayloadType.TASK_INPUT;
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(operation,payloadType,"dummy");
        assertNull(location);
    }

    @Test 
    public void uploadTest() {
        try {
            InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
            dummyPayloadStorage.upload("dummyPath",inputStream, 4);
            assertEquals(4,inputStream.available());
        }catch (Exception e){
            e.printStackTrace();
        }        
    }

    @Test
    public void downloadTest() {
        InputStream inputStream = dummyPayloadStorage.download("dummyPath");
        assertNull(inputStream);
    }

}