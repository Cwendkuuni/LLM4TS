
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import org.junit.Assert;
import org.junit.Test;

public class DummyPayloadStorageTest {
    @Test
    public void testLocation() {
        DummyPayloadStorage dps = new DummyPayloadStorage();
        ExternalStorageLocation esl = dps.getLocation(null, null, null);
        Assert.assertNull(esl);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dps = new DummyPayloadStorage();
        try {
            dps.upload(null, null, -1);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testDownload_nullPath() {
        DummyPayloadStorage dps = new DummyPayloadStorage();
        InputStream is = dps.download(null);
        Assert.assertNull(is);
    }

}
