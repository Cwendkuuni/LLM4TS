import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DummyPayloadStorageTest {
    private DummyPayloadStorage dummy;
    
    @Before
    public void setUp(){
        dummy = new DummyPayloadStorage();
    }

    //Testing getLocation method. Since it always returns null, we can only validate that.
    @Test
    public void getLocationTest() {
        assertNull(dummy.getLocation(null, null, null));
    }
    
    //Testing upload method. Since it doesn't return or change anything, testing this method doesn't prove anything significant.
    @Test
    public void uploadTest() {
        dummy.upload(null, null, 0);
        assertTrue(true); //Passed if no exception thrown
    }

    //Testing download method. Since it always returns null, we can only validate that.
    @Test
    public void downloadTest() {
        assertNull(dummy.download(null));
    }
}