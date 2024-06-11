package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ArArchiveInputStreamTest {

 @Test
 public void testGetNextArEntry() throws Exception {
 ArArchiveInputStream inputStream = new ArArchiveInputStream(getClass().getResourceAsStream("/test.ar"));

 ArArchiveEntry entry1 = inputStream.getNextArEntry();
 assertEquals("test1.txt", entry1.getName());
 assertEquals(123L, entry1.getSize());

 ArArchiveEntry entry2 = inputStream.getNextArEntry();
 assertEquals("test2.txt", entry2.getName());
 assertEquals(456L, entry2.getSize());

 ArArchiveEntry entry3 = inputStream.getNextArEntry();
 assertNull(entry3);
 }

 @Test(expected = IOException.class)
 public void testGetNextArEntryWithInvalidHeader() throws Exception {
 InputStream input = new ByteArrayInputStream(new byte[] { 0x1, 0x2, 0x3 });
 ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);
 arInputStream.getNextArEntry();
 }

 @Test(expected = IOException.class)
 public void testGetNextArEntryWithMissingTrailer() throws Exception {
 InputStream input = new ByteArrayInputStream(new byte[] { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x12, 0x34, 0x56, 0x78, 0x90, 0x12, 0x34, 0x56, 0x78, 0x90, 0x0a });
 ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);
 arInputStream.getNextArEntry();
 }

 @Test
 public void testMatchesSignature() {
 byte[] signature = new byte[] { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x12, 0x34, 0x56, 0x78, 0x90, 0x12, 0x34, 0x56, 0x78, 0x90, 0x0a };
 assertTrue(ArArchiveInputStream.matches(signature, signature.length));
 }

 @Test
 public void testMatchesInvalidSignature() {
 byte[] signature = new byte[] { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x12, 0x34, 0x56, 0x78, 0x90, 0x12, 0x34, 0x56, 0x78, 0x91, 0x0a }; // extra byte at the end
 assertFalse(ArArchiveInputStream.matches(signature, signature.length));
 }
}