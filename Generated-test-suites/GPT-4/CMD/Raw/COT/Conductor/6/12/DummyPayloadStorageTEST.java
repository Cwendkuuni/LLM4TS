import static org.junit.Assert.assertNull;
import org.junit.Test;

public class DummyPayloadStorageTest {
    @Test
    public void testGetLocation(){
        DummyPayloadStorage storage = new DummyPayloadStorage();
        assertNull(storage.getLocation(null, null, null));
    }
    
    @Test
    public void testUpload(){
        DummyPayloadStorage storage = new DummyPayloadStorage();
        storage.upload(null, null, 0);
    }
    
    @Test
    public void testDownload(){
        DummyPayloadStorage storage = new DummyPayloadStorage();
        assertNull(storage.download(null));
    }
    
    @Test(expected = Exception.class)
    public void testGetLocation_exception(){
        DummyPayloadStorage storage = new DummyPayloadStorage();
        storage.getLocation(null, null, null);
        throw new Exception();
    }
    
    @Test(expected = Exception.class)
    public void testUpload_exception(){
        DummyPayloadStorage storage = new DummyPayloadStorage();
        storage.upload(null, null, 0);
        throw new Exception();
    }
    
    @Test(expected = Exception.class)
    public void testDownload_exception(){
        DummyPayloadStorage storage = new DummyPayloadStorage();
        storage.download(null);
        throw new Exception();
    }
}