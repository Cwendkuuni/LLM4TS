import org.junit.Before;
import org.junit.Test;
import java.io.InputStream;
import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {
    
    DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() throws Exception {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void getLocation() {
        assertNull(dummyPayloadStorage.getLocation(null, null, null));
    }

    @Test
    public void upload() {
        dummyPayloadStorage.upload(null, null, 0);
        // No assertion needed here as the method does nothing
    }

    @Test
    public void download() {
        assertNull(dummyPayloadStorage.download(null));
    }
    
    @Test(expected = NullPointerException.class) 
    public void nullPointerExceptionForUpload() {
        dummyPayloadStorage.upload(null, InputStream.nullInputStream(), 0);
    }
    
    @Test(expected = NullPointerException.class) 
    public void nullPointerExceptionForDownload() {
        dummyPayloadStorage.download(null);
    }

}