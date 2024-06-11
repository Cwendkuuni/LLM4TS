
import org.junit.Before; 
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.common.run.ExternalStorageLocation;

/**
 * DummyPayloadStorageTest - This class contains test cases for DummyPayloadStorage class
 */
public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;
    
    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    /**
     * Test method for {@link com.netflix.conductor.core.storage.DummyPayloadStorage#getLocation(Operation, PayloadType, String)}.
     */
    @Test
    public void testGetLocation() {
        assertNull(dummyPayloadStorage.getLocation( ExternalPayloadStorage.Operation.READ, ExternalPayloadStorage.PayloadType.TASK_INPUT,
               "/path/to/file"));
    }

    /**
     * Test method for {@link com.netflix.conductor.core.storage.DummyPayloadStorage#upload(String, InputStream, long)}.
     */
    @Test
    public void testUpload() {
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        
        //As the method is void, this just checks that an exception is not thrown
        try {
            dummyPayloadStorage.upload("/path/to/file", inputStream, 1234L);
            assertTrue(true); // If it makes it this far, it succeeded.
        } catch(Exception e) {
            fail("Exception should not be thrown");
        }
    }

    /**
     * Test method for {@link com.netflix.conductor.core.storage.DummyPayloadStorage#download(String)}.
     */
    @Test
    public void testDownload() {
        assertNull(dummyPayloadStorage.download("/path/to/file"));
    }
    
    /**
     * Test method for {@link com.netflix.conductor.core.storage.DummyPayloadStorage#upload(String, InputStream, null)}.
     */
    @Test(expected = NullPointerException.class)
    public void testUploadNullInputStream() {
        dummyPayloadStorage.upload("/path/to/file", null, 1234L);
    }
    
    /**
     * Test method for {@link com.netflix.conductor.core.storage.DummyPayloadStorage#upload(null, InputStream, long)}.
     */
    @Test(expected = NullPointerException.class)
    public void testUploadNullPath() {
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        dummyPayloadStorage.upload(null, inputStream, 1234L);
    }
    
    /**
     * Test method for {@link com.netflix.conductor.core.storage.DummyPayloadStorage#download(null)}.
     */
    @Test(expected = NullPointerException.class)
    public void testDownloadNullPath() {
        dummyPayloadStorage.download(null);
    }
}