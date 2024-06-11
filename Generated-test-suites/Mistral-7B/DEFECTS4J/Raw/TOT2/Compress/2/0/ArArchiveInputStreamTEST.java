import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArFormatException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArArchiveInputStreamTest {

 private InputStream input;
 private ArArchiveInputStream ais;

 @Before
 public void setUp() throws Exception {
 input = new ByteArrayInputStream(new byte[]{(byte) 0x21, (byte) 0x3c, (byte) 0x61, (byte) 0x72, (byte) 0x63, (byte) 0x68, (byte) 0x3e, (byte) 0x0a, // AR header
 (byte) 0x68, (byte) 0x65, (byte) 0x6c, (byte) 0x6c, (byte) 0x6f, (byte) 0x20, (byte) 0x74, (byte) 0x68, (byte) 0x65, (byte) 0x72, (byte) 0x65, // name: test.txt
 (byte) 0x31, (byte) 0x30, (byte) 0x31, (byte) 0x36, (byte) 0x31, (byte) 0x32, (byte) 0x35, (byte) 0x37, (byte) 0x33, (byte) 0x31, // length: 102573125
 (byte) 0x68, (byte) 0x65, (byte) 0x6c, (byte) 0x6c, (byte) 0x6f, (byte) 0x20, (byte) 0x73, (byte) 0x74, (byte) 0x61, (byte) 0x72, // name: gzip
 (byte) 0x0a, (byte) 0x68, (byte) 0x65, (byte) 0x6c, (byte) 0x6c, (byte) 0x6f, (byte) 0x20, (byte) 0x75, (byte) 0x73, (byte) 0x68, // name: urlz
 (byte) 0x31, (byte) 0x30, (byte) 0x31, (byte) 0x36, (byte) 0x31, (byte) 0x32, (byte) 0x35, (byte) 0x37, (byte) 0x33, (byte) 0x31, // length: 102573125
 (byte) 0x68, (byte) 0x65, (byte) 0x6c, (byte) 0x6c, (byte) 0x6f, (byte) 0x20, (byte) 0x70, (byte) 0x75, (byte) 0x62, (byte) 0x6c, // name: plain
 (byte) 0x0a, (byte) 0x68, (byte) 0x65, (byte) 0x6c, (byte) 0x6c, (byte) 0x6f, (byte) 0x20, (byte) 0x63, (byte) 0x61, (byte) 0x6d, (byte) 0x65, // name: cake
 (byte) 0x31, (byte) 0x30, (byte) 0x31, (byte) 0x36, (byte) 0x31, (byte) 0x32, (byte) 0x35, (byte) 0x37, (byte) 0x33, (byte) 0x31, // length: 102573125
 (byte) 0x0a, (byte) 0x68, (byte) 0x6d, (byte) 0x65, (byte) 0x74, (byte) 0x68, (byte) 0x6f, (byte) 0x75, (byte) 0x74, (byte) 0x20, // name: test.data
 (byte) 0x6c, (byte) 0x61, (byte) 0x6e, (byte) 0x67, (byte) 0x20, (byte) 0x61, (byte) 0x73, (byte) 0x73, (byte) 0x20, (byte) 0x74, (byte) 0x61, // name: data.ascii
 (byte) 0x31, (byte) 0x30, (byte) 0x31, (byte) 0x36, (byte) 0x31, (byte) 0x32, (byte) 0x35, (byte) 0x37, (byte) 0x33, (byte) 0x31, // length: 102573125
 (byte) 0x0a, (byte) 0x68, (byte) 0x65, (byte) 0x78, (byte) 0x61, (byte) 0x77, (byte) 0x20, (byte) 0x63, (byte) 0x6f, (byte) 0x6d, (byte) 0x70, // name: xz
 (byte) 0x31, (byte) 0x30, (byte) 0x31, (byte) 0x36, (byte) 0x31, (byte) 0x32, (byte) 0x35, (byte) 0x37, (byte) 0x33, (byte) 0x31, // length: 102573125
 (byte) 0x0a, (byte) 0x75, (byte) 0x73, (byte) 0x68, (byte) 0x6f, (byte) 0x75, (byte) 0x74, (byte) 0x20, (byte) 0x61, (byte) 0x73, (byte) 0x73, // signature: urlz.ascii
 (byte) 0x20, (byte) 0x69, (byte) 0x6f, (byte) 0x6e, (byte) 0x66, (byte) 0x6f, (byte) 0x0a, (byte) 0});
 ais = new ArArchiveInputStream(input);
 }

 @Test
 public void testGetNextArEntry() throws IOException {
 ArArchiveEntry entry;
 while ((entry = ais.getNextArEntry()) != null) {
 System.out.println(entry.getName() + " " + entry.getSize());
 }
 }

 @Test(expected = IOException.class)
 public void testGetNextArEntry_IOException() throws IOException {
 ais.getNextArEntry();
 input.close();
 ArArchiveEntry entry = ais.getNextArEntry();
 fail("Expected IOException");
 }

 @Test(expected = ArFormatException.class)
 public void testGetNextArEntry_ArFormatException() throws IOException {
 input = new ByteArrayInputStream(new byte[]{(byte) 0x3c, (byte) 0x61, (byte) 0x72, (byte) 0x63, (byte) 0x68, (byte) 0x3e, (byte) 0x0a, // AR header
 (byte) 0x68, (byte) 0x65, (byte) 0x6c, (byte) 0x6c, (byte) 0x6f, (byte) 0x20, (byte) 0x74, (byte) 0x68, (byte) 0x65, (byte) 0x72, (byte) 0x65, // name: test.txt
 (byte) 0x31, (byte) 0x30, (byte) 0x31, (byte) 0x36, (byte) 0x31, (byte) 0x32, (byte) 0x35, (byte) 0x37, (byte) 0x33, (byte) 0x31, // length: 102573125
 (byte) 0x0a, (byte) 0x68, (byte) 0x65, (byte) 0x6c, (byte) 0x6c, (byte) 0x6f, (byte) 0x20, (byte) 0x63, (byte) 0x61, (byte) 0x6d, (byte) 0x65, // name: cake
 (byte) 0x31, (byte) 0x30, (byte) 0x31, (byte) 0x36, (byte) 0x31, (byte) 0x32, (byte) 0x35, (byte) 0x37, (byte) 0x33, (byte) 0x31, // length: 102573125
 (byte) 0x0a, (byte) 0x68, (byte) 0x65, (byte) 0x78, (byte) 0x61, (byte) 0x77, (byte) 0x20, (byte) 0x63, (byte) 0x6f, (byte) 0x6d, (byte) 0x70, // name: xz
 (byte) 0x31, (byte) 0x30, (byte) 0x31, (byte) 0x36, (byte) 0x31, (byte) 0x32, (byte) 0x35, (byte) 0x37, (byte) 0x33, (byte) 0x31, // length: 102573125
 (byte) 0x0a, (byte) 0x75, (byte) 0x73, (byte) 0x68, (byte) 0x6f, (byte) 0x75, (byte) 0x74, (byte) 0x65, (byte) 0x6e, (byte) 0x63, (byte) 0x6F, // signature: urlz.data.xz.ascii
 (byte) 0x6d, (byte) 0x65, (byte) 0x73, (byte) 0x73, (byte) 0x61, (byte) 0x72, (byte) 0x74, (byte) 0x65, (byte) 0x64, (byte) 0x0a, (byte) 0});
 ais = new ArArchiveInputStream(input);
 ArArchiveEntry entry = ais.getNextArEntry();
 fail("Expected ArFormatException");
 }

 @Test
 public void testGetNextArEntry_EmptyInputStream() throws IOException {
 input = new ByteArrayInputStream(new byte[0]);
 ais = new ArArchiveInputStream(input);
 ArArchiveEntry entry = ais.getNextArEntry();
 assertNull(entry);
 }

 @Test
 public void testClose() throws IOException {
 ais.close();
 ArArchiveEntry entry = ais.getNextArEntry();
 assertNull(entry);
 }

 @Test
 public void testRead() throws IOException {
 byte[] buffer = new byte[10];
 int len = ais.read(buffer);
 assertEquals(len, ais.read(buffer, 0, buffer.length));
 }

 @Test(expected = IOException.class)
 public void testRead_IOException() throws IOException {
 byte[] buffer = new byte[10];
 ais.read(buffer);
 input.close();
 int len = ais.read(buffer);
 fail("Expected IOException");
 }
}