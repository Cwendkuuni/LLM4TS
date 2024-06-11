import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.ChecksumUtils;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChecksumCalculatingInputStreamTest {

 private InputStream input;
 private ChecksumCalculatingInputStream checksumInputStream;
 private byte[] testData = new byte[]{'A', 'B', 'C'};
 private Checksum expectedChecksum;

 @Before
 public void setup() throws Exception {
 input = new ByteArrayInputStream(testData);
 checksumInputStream = new ChecksumCalculatingInputStream(ChecksumUtils.checksum(Checksum.ADler.instance()), input);
 expectedChecksum = checksumInputStream.getValue();
 }

 @Test
 public void testReadByte() throws Exception {
 int readByte = checksumInputStream.read();
 assertEquals(-1, readByte); // EOF
 assertEquals(expectedChecksum.getValue(), checksumInputStream.getValue());
 }

 @Test
 public void testReadByteArray() throws Exception {
 byte[] byteArray = new byte[3];
 int length = checksumInputStream.read(byteArray, 0, byteArray.length);
 assertArrayEquals(testData, byteArray);
 assertEquals(expectedChecksum.getValue(), checksumInputStream.getValue());
 }

 @Test
 public void testReadByteArrayWithOffset() throws Exception {
 byte[] byteArray = new byte[5];
 int length = checksumInputStream.read(byteArray, 2, byteArray.length - 2);
 assertArrayEquals(new byte[]{'C', 'B'}, byteArray, 2);
 assertEquals(expectedChecksum.getValue(), checksumInputStream.getValue());
 }

 @Test(expected = IOException.class)
 public void testIOException() throws Exception {
 checksumInputStream.skip(Long.MAX_VALUE);
 assertEquals(expectedChecksum.getValue(), checksumInputStream.getValue());
 }

 @Test
 public void testGetChecksum() throws Exception {
 long checksum = checksumInputStream.getValue();
 assertEquals(expectedChecksum.getValue(), checksum);
 }

 @After
 public void teardown() throws Exception {
 if (input != null) {
 input.close();
 }
 if (checksumInputStream != null) {
 checksumInputStream.close();
 }
 }
}