import org.junit.Test;
import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        assertNull(dummyPayloadStorage.getLocation(new Operation(), PayloadType.JSON, ""));
    }
}