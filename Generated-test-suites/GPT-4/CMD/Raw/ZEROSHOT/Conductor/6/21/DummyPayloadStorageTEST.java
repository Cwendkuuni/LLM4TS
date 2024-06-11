
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.InputStream;

import org.junit.Test;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {

    DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(ExternalPayloadStorage.Operation.ADD, 
        ExternalPayloadStorage.PayloadType.TASK_INPUT, "path");

        assertNull(location);
    }

    @Test
    public void testUpload() {
        //Create mock InputStream
        InputStream mockInputStream = mock(InputStream.class);
            
        //The method doesn't return anything, so we just ensure that no exception is thrown
        dummyPayloadStorage.upload("path", mockInputStream, 20);
    }

    @Test
    public void testDownload() {
        InputStream downloadStream = dummyPayloadStorage.download("path");
        
        assertNull(downloadStream);
    }
}
