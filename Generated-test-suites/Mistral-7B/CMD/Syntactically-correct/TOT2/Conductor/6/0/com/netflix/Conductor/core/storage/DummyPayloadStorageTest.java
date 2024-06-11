import static org.junit.Assert.assertNull;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.utils.PayloadType;

public class DummyPayloadStorageTest {

    @org.junit.Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        assertNull(dummyPayloadStorage.getLocation(new Operation(), PayloadType.JSON, "testPath"));
    }
}