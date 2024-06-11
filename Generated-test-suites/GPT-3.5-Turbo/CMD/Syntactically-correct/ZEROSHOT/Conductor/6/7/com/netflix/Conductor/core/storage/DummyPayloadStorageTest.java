import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

@RunWith(MockitoJUnitRunner.class)
public class DummyPayloadStorageTest {

	@Test
	public void testGetLocation() {
		DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
		ExternalStorageLocation location = dummyPayloadStorage.getLocation(null, null, "test/path");
		assertNull(location);
	}

	@Test
	public void testUpload() {
		DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
		InputStream inputStream = new ByteArrayInputStream("test payload".getBytes());
		dummyPayloadStorage.upload("test/path", inputStream, 12);
		// No exceptions should be thrown
		assertTrue(true);
	}

	@Test
	public void testDownload() {
		DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
		InputStream result = dummyPayloadStorage.download("test/path");
		assertNull(result);
	}

}