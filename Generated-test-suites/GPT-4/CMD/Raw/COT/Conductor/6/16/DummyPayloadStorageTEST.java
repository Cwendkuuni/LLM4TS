/**  */
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import java.io.InputStream;

public class DummyPayloadStorageTest {

    DummyPayloadStorage dps = new DummyPayloadStorage();

    @Test
    public void testGetLocation(){
        ExternalStorageLocation esl = dps.getLocation(null, null, null);
        assertEquals(esl, null); //since getLocation always return null
    }

    @Test
    public void testUpload(){
        dps.upload(null, null, 0); //since upload doesn't do anything
        // No assert statement needed here as this function doesn't return anything 
    }

    @Test
    public void testDownload(){
        InputStream input = dps.download(null);
        assertEquals(input, null); //since download always returns null
    }
}
/**  */