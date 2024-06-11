import org.junit.Assert;
import org.junit.Test;
import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.test.TestFiles;

public class DummyPayloadStorageTest {

 @Test
 public void testGetLocation_whenOperationIsNull_thenReturnNull() {
 Operation operation = null;
 PayloadType payloadType = PayloadType.FILE;
 String path = "test.txt";
 ExternalStorageLocation expectedLocation = null;
 ExternalStorageLocation actualLocation = new DummyPayloadStorage().getLocation(operation, payloadType, path);
 Assert.assertNull(actualLocation);
 Assert.assertEquals(expectedLocation, actualLocation);
 }

 @Test
 public void testGetLocation_whenPayloadTypeIsInvalid_thenReturnNull() {
 Operation operation = new Operation();
 PayloadType payloadType = null;
 String path = "test.txt";
 ExternalStorageLocation expectedLocation = null;
 ExternalStorageLocation actualLocation = new DummyPayloadStorage().getLocation(operation, payloadType, path);
 Assert.assertNull(actualLocation);
 Assert.assertEquals(expectedLocation, actualLocation);
 }

 @Test
 public void testGetLocation_whenPathIsEmpty_thenReturnNull() {
 Operation operation = new Operation();
 PayloadType payloadType = PayloadType.FILE;
 String path = "";
 ExternalStorageLocation expectedLocation = null;
 ExternalStorageLocation actualLocation = new DummyPayloadStorage().getLocation(operation, payloadType, path);
 Assert.assertNull(actualLocation);
 Assert.assertEquals(expectedLocation, actualLocation);
 }

 @Test
 public void testUpload_whenPayloadIsNull_thenDoNothing() {
 String path = "test.txt";
 InputStream payload = null;
 long payloadSize = 0;
 new DummyPayloadStorage().upload(path, payload, payloadSize);
 }

 @Test
 public void testUpload_whenPayloadIsEmpty_thenDoNothing() {
 String path = "test.txt";
 InputStream payload = TestFiles.createTestInputStream();
 long payloadSize = 0;
 new DummyPayloadStorage().upload(path, payload, payloadSize);
 }

 @Test
 public void testUpload_whenPayloadIsValid_thenDoNothing() {
 String path = "test.txt";
 InputStream payload = TestFiles.createTestInputStream();
 long payloadSize = payload.available();
 new DummyPayloadStorage().upload(path, payload, payloadSize);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDownload_whenPathIsNull_thenThrowException() {
 String path = null;
 new DummyPayloadStorage().download(path);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDownload_whenPathIsEmpty_thenThrowException() {
 String path = "";
 new DummyPayloadStorage().download(path);
 }

 @Test
 public void testDownload_whenPathIsValid_thenReturnNullInputStream() {
 String path = "test.txt";
 InputStream expectedInputStream = null;
 InputStream actualInputStream = new DummyPayloadStorage().download(path);
 Assert.assertNull(actualInputStream);
 Assert.assertEquals(expectedInputStream, actualInputStream);
 }
}