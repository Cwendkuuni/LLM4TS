import static org.junit.Assert.*;
import org.junit.Test;

public class DummyPayloadStorageTest {

  @Test
  public void testGetLocation() {
    DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
    assertNull(dummyPayloadStorage.getLocation(Operation.READ, PayloadType.TASK_INPUT, "test-path"));
  }

  @Test
  public void testUpload() {
    DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
    dummyPayloadStorage.upload("test-path", null, 0); // No exceptions expected
  }

  @Test
  public void testDownload() {
    DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
    assertNull(dummyPayloadStorage.download("test-path"));
  }
}