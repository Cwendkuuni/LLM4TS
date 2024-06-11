
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

public class DummyPayloadStorageTest {

    DummyPayloadStorage dummy = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        assertNull(dummy.getLocation(Operation.WRITE, PayloadType.TASK_INPUT, "testPath"));
    }

    @Test
    public void testUpload() {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        dummy.upload("testPath", inputStream, 4);
    }

    @Test
    public void testDownload() {
        assertNull(dummy.download("testPath"));
    }

    @Test
    public void testUploadAndDownload() {
        String testContent = "This is a test";
        InputStream inputStream = new ByteArrayInputStream(testContent.getBytes());
        dummy.upload("testPath", inputStream, testContent.length());
        InputStream downloadedStream = dummy.download("testPath");
        assertNotNull(downloadedStream);

        StringBuilder downloadedContent = new StringBuilder();
        int data;
        try {
            while ((data = downloadedStream.read()) != -1) {
                downloadedContent.append((char) data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(testContent, downloadedContent.toString());
    }

}
