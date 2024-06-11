import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.archiveentry.ArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ArArchiveInputStreamTest {

 private InputStream input;
 private ArArchiveInputStream arStream;
 private ArArchiveEntry entry;

 @Before
 public void setUp() throws Exception {
 InputStream testInputStream = new ByteArrayInputStream(
 "3c21 7261 6863 0a3e 000000000100 4100 4200 5652 4553 494e 4453 5554 5341 4d50 5f54 5845 4354 0a78 0100 0000 0000 0000 0100 0200 5441 544d 502d 3230 3136 0a3c 2172 6168 630a 3e0a".getBytes(StandardCharsets.UTF_8));
 input = testInputStream;
 arStream = new ArArchiveInputStream(input);
 entry = arStream.getNextArEntry();
 }

 @After
 public void tearDown() throws Exception {
 arStream.close();
 input.close();
 }

 @Test
 public void testGetNextArEntry() throws Exception {
 ArArchiveEntry nextEntry = arStream.getNextArEntry();
 assertNotNull(nextEntry);
 assertEquals("name.txt", nextEntry.getName());
 assertEquals(1L, nextEntry.getSize());
 }

 @Test(expected = IOException.class)
 public void testEndOfStream() throws Exception {
 ArArchiveEntry nextEntry = arStream.getNextArEntry();
 nextEntry = arStream.getNextArEntry();
 nextEntry = arStream.getNextArEntry();
 // no more entries should throw an exception
 arStream.getNextArEntry();
 }

 @Test(expected = IOException.class)
 public void testInvalidHeader() throws Exception {
 InputStream testInputStream = new ByteArrayInputStream(
 "3c21 7261 6863 0a3e 000000000100 4100 4200 5652 4553 494e 4453 5554 5341 4d50 5f54 4543 0a78 0100 0000 0000 0000 0100 0200 5441 544d 502d 3230 3136 0a3c 2172 6168 630a 3e0a".getBytes(StandardCharsets.UTF_8));
 ArArchiveInputStream arStream = new ArArchiveInputStream(testInputStream);
 arStream.getNextArEntry();
 arStream.getNextArEntry();
 arStream.getNextArEntry();
 // modify the header to be invalid
 testInputStream = new ByteArrayInputStream(
 "3c21 7261 6863 0a3e 000000000100 4100 4200 5652 4553 494e 4453 5554 5341 4d50 5f54 4543 0a78 0100 0000 0000 0000 0100 0200 5441 544d 502d 3230 3136 0a3c 2172 6168 630a 3e0a".getBytes(StandardCharsets.UTF_8));
 arStream = new ArArchiveInputStream(testInputStream);
 // getting next entry should now throw an exception
 arStream.getNextArEntry();
 }

 @Test
 public void testGetNextEntry() throws Exception {
 ArchiveEntry nextEntry = arStream.getNextEntry();
 assertEquals(entry.getName(), nextEntry.getName());
 assertEquals(entry.getSize(), nextEntry.getSize());
 }

 @Test(expected = IOException.class)
 public void testInvalidName() throws Exception {
 input = new ByteArrayInputStream(
 "3c21 7261 6863 0a3e 000000000100 4100 4200 5652 4553 494e 4453 5554 5341 4d50 5f54 4543 0a78 0100 0000 01 00 0200 5441 544d 502d 3230 3136 0a3c 2172 6168 630a 3e0a".getBytes(StandardCharsets.UTF_8));
 ArArchiveInputStream arStream = new ArArchiveInputStream(input);
 arStream.getNextArEntry();
 arStream.getNextArEntry();
 arStream.getNextArEntry();
 // name has an invalid length
 arStream.getNextArEntry();
 }

 @Test
 public void testRead() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] buffer = new byte[1024];
 int bytesRead;
 String expected = "Hello World!";
 while ((bytesRead = arStream.read(buffer)) > 0) {
 outputStream.write(buffer, 0, bytesRead);
 }
 String actual = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
 assertEquals(expected, actual);
 }

 @Test(expected = IOException.class)
 public void testReadPastEndOfStream() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] buffer = new byte[1024];
 int bytesRead;
 String expected = "Hello World!";
 while ((bytesRead = arStream.read(buffer)) > 0) {
 outputStream.write(buffer, 0, bytesRead);
 }
 arStream.read(buffer);
 }

}