import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.*;
import static org.apache.commons.compress.archivers.ar.ArArchiveInputStream.matches;

public class ArArchiveInputStreamTest {

 private InputStream inputStream;
 private ArArchiveInputStream archiveInputStream;

 @Before
 public void setup() throws IOException {
 inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x21, (byte) 0x3c, (byte) 0x61, (byte) 0x72, (byte) 0x63, (byte) 0x68, (byte) 0x3e, (byte) 0x0a});
 archiveInputStream = new ArArchiveInputStream(inputStream);
 }

 @Test
 public void testGetNextArEntry() throws IOException {
 ArArchiveEntry entry = archiveInputStream.getNextArEntry();
 assertEquals("test entry name", "test entry", entry.getName());
 assertEquals("test entry length", 12345L, entry.getSize());
 entry = archiveInputStream.getNextArEntry();
 assertNull("no more entries", entry);
 }

 @Test(expected = IOException.class)
 public void testGetNextArEntryNoEntryFound() throws IOException {
 archiveInputStream.getNextArEntry();
 archiveInputStream.getNextArEntry();
 inputStream.close();
 archiveInputStream.getNextArEntry();
 }

 @Test
 public void testGetNextEntry() throws IOException {
 ArchiveEntry archiveEntry = archiveInputStream.getNextEntry();
 assertEquals("test entry name", "test entry", archiveEntry.getName());
 assertEquals("test entry class", ArArchiveEntry.class, archiveEntry.getClass());
 archiveEntry = archiveInputStream.getNextEntry();
 assertNull("no more entries", archiveEntry);
 }

 @Test
 public void testClose() throws IOException {
 archiveInputStream.close();
 try {
 archiveInputStream.getNextArEntry();
 fail("expected IOException when closed");
 } catch (IOException e) {
 // expected
 }
}

 @Test
 public void testMatches() {
 byte[] signature = new byte[]{(byte) 0x21, (byte) 0x3c, (byte) 0x61, (byte) 0x72, (byte) 0x63, (byte) 0x68, (byte) 0x3e, (byte) 0x0a};
 assertTrue("matches should return true", matches(signature, signature.length));
 byte[] wrongSignature = new byte[]{(byte) 0x21, (byte) 0x3c, (byte) 0x61, (byte) 0x72, (byte) 0x63, (byte) 0x68, (byte) 0x3e, (byte) 0x0b};
 assertFalse("matches should return false", matches(wrongSignature, signature.length));
}

 @Test
 public void testRead() throws IOException {
 byte[] buffer = new byte[10];
 int read = archiveInputStream.read(buffer, 0, buffer.length);
 assertNotNull("read should return non-null value", read);
 assertEquals("length of read data", buffer.length, read);
}

 @Test
 public void testReadByte() throws IOException {
 int read = archiveInputStream.read();
 assertNotNull("read should return non-null value", read);
}

 @Test
 public void testReadBytes() throws IOException {
 byte[] buffer = new byte[10];
 int read = archiveInputStream.read(buffer);
 assertNotNull("read should return non-null value", read);
 assertEquals("length of read data", buffer.length, read);
}

 @Test
 public void testCloseAfterReading() throws IOException {
 int read = archiveInputStream.read();
 archiveInputStream.close();
 try {
 archiveInputStream.getNextArEntry();
 fail("expected IOException when closed");
 } catch (IOException e) {
 // expected
 }
}
}