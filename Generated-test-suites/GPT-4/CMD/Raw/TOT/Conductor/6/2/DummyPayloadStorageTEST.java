import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;
import org.junit.Test;
import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

	private DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

	@Test
	public void getLocationTest() {
		ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.READ, PayloadType.TASK_INPUT, "path/to/file");
		assertNull(location);
	}

	@Test
	public void uploadTest() {
		try {
			dummyPayloadStorage.upload("path/to/file", null, 0);
		} catch (Exception e) {
			fail("Exception should not be thrown");
		}
	}

	@Test
	public void downloadTest() {
		try {
			InputStream stream = dummyPayloadStorage.download("path/to/file");
			assertNull(stream);
		} catch (Exception e) {
			fail("Exception should not be thrown");
		}
	}
}