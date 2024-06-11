import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.exception.ArchiveFormatException;
import org.apache.commons.compress.archivers.tar.TarEntry;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArchiveStreamFactoryTest {

 private static InputStream testInputStream;
 private static OutputStream testOutputStream;

 @BeforeClass
 public static void setUp() throws IOException {
 testInputStream = new ByteArrayInputStream(new byte[1024]);
 testOutputStream = new ByteArrayOutputStream(1024);
 }

 /**
 * Test for createArchiveInputStream(String, InputStream) method
 */
 @Test
 public void testCreateArchiveInputStreamStringInputStream() throws ArchiveException, IOException {
 // Test with known archiver name and InputStream
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
 .createArchiveInputStream(ArchiveStreamFactory.ZIP, testInputStream);
 assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
 archiveInputStream.close();

 // Test with unknown archiver name and InputStream
 try {
 ArchiveInputStream archiveInputStream2 = ArchiveStreamFactory.getInstance()
 .createArchiveInputStream("unknown", testInputStream);
 fail("ArchiveException expected, but not thrown.");
 } catch (ArchiveException e) {
 // Expected
 }

 // Test with null InputStream
 try {
 ArchiveInputStream archiveInputStream3 = ArchiveStreamFactory.getInstance()
 .createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
 fail("IllegalArgumentException expected, but not thrown.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 /**
 * Test for createArchiveInputStream(InputStream) method
 */
 @Test
 public void testCreateArchiveInputStreamInputStream() throws ArchiveException, IOException {
 // Test with known archiver type detected from InputStream signature
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
 .createArchiveInputStream(testInputStream);
 assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
 archiveInputStream.close();

 // Test with unknown archiver type, should throw ArchiveException
 try {
 ArchiveInputStream archiveInputStream2 = ArchiveStreamFactory.getInstance()
 .createArchiveInputStream(new ByteArrayInputStream(new byte[10]));
 fail("ArchiveException expected, but not thrown.");
 } catch (ArchiveException e) {
 // Expected
 }
 }

 /**
 * Test for createArchiveOutputStream(String, OutputStream) method
 */
 @Test
 public void testCreateArchiveOutputStreamStringOutputStream() throws ArchiveException, IOException {
 // Test with known archiver name and OutputStream
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
 .createArchiveOutputStream(ArchiveStreamFactory.ZIP, testOutputStream);
 assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
 archiveOutputStream.finish();

 // Test with unknown archiver name and OutputStream
 try {
 ArchiveOutputStream archiveOutputStream2 = ArchiveStreamFactory.getInstance()
 .createArchiveOutputStream("unknown", testOutputStream);
 fail("ArchiveException expected, but not thrown.");
 } catch (ArchiveException e) {
 // Expected
 }

 // Test with null OutputStream
 try {
 ArchiveOutputStream archiveOutputStream3 = ArchiveStreamFactory.getInstance()
 .createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
 fail("IllegalArgumentException expected, but not thrown.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 /**
 * Test for createArchiveOutputStream(OutputStream) method
 */
 @Test
 public void testCreateArchiveOutputStreamOutputStream() throws ArchiveException, IOException {
 // Test with known archiver type detected from OutputStream
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
 .createArchiveOutputStream(testOutputStream);
 assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
 archiveOutputStream.finish();
 testOutputStream.reset();

 // Test with unknown archiver type, should throw ArchiveException
 try {
 ArchiveOutputStream archiveOutputStream2 = ArchiveStreamFactory.getInstance()
 .createArchiveOutputStream(new ByteArrayOutputStream(1024));
 fail("ArchiveException expected, but not thrown.");
 } catch (ArchiveException e) {
 // Expected
 }
 }

 /**
 * Test for createArchiveInputStream(InputStream) method with autodetection
 */
 @Test
 public void testCreateArchiveInputStreamInputStreamAutodetection() throws ArchiveException, IOException {
 // Test with ZIP archive
 ByteArrayOutputStream zipFile = new ByteArrayOutputStream(1024);
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
 .createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipFile);
 archiveOutputStream.putArchiveEntry(new ZipArchiveEntry("test.txt"));
 archiveOutputStream.write(new byte[10], 0, 10);
 archiveOutputStream.closeArchiveEntry();
 archiveOutputStream.finish();
 InputStream zipInputStream = new ByteArrayInputStream(zipFile.toByteArray());
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
 .createArchiveInputStream(zipInputStream);
 TarEntry tarEntry = (TarEntry) archiveInputStream.getNextEntry();
 assertEquals("test.txt", tarEntry.getName());
 archiveInputStream.close();
 zipFile.close();

 // Test with unknown archive format
 ByteArrayOutputStream unknownFile = new ByteArrayOutputStream(1024);
 unknownFile.write("Unknown Archive Format".getBytes());
 InputStream unknownInputStream = new ByteArrayInputStream(unknownFile.toByteArray());
 ArchiveInputStream archiveInputStream2 = ArchiveStreamFactory.getInstance()
 .createArchiveInputStream(unknownInputStream);
 assertNull(archiveInputStream2.getNextEntry());
 archiveInputStream2.close();
 unknownFile.close();
 }

 /**
 * Test for createArchiveOutputStream(OutputStream) method with autodetection
 */
 @Test
 public void testCreateArchiveOutputStreamOutputStreamAutodetection() throws ArchiveException, IOException {
 // Test with ZIP archive
 ByteArrayOutputStream zipFile = new ByteArrayOutputStream(1024);
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
 .createArchiveOutputStream(zipFile);
 archiveOutputStream.putArchiveEntry(new ZipArchiveEntry("test.txt"));
 archiveOutputStream.write(new byte[10], 0, 10);
 archiveOutputStream.closeArchiveEntry();
 archiveOutputStream.finish();
 OutputStream zipOutputStream = new ByteArrayOutputStream(1024);
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
 .createArchiveInputStream(new ByteArrayInputStream(zipFile.toByteArray()));
 TarEntry tarEntry = (TarEntry) archiveInputStream.getNextEntry();
 assertEquals("test.txt", tarEntry.getName());
 archiveInputStream.close();
 zipFile.close();

 // Test with unknown archive format, should throw ArchiveException
 ByteArrayOutputStream unknownFile = new ByteArrayOutputStream(1024);
 unknownFile.write("Unknown Archive Format".getBytes());
 OutputStream unknownOutputStream = new ByteArrayOutputStream(1024);
 ArchiveOutputStream archiveOutputStream2 = ArchiveStreamFactory.getInstance()
 .createArchiveOutputStream(unknownOutputStream);
 try {
 archiveOutputStream2.putArchiveEntry(new ZipArchiveEntry("test.txt"));
 fail("ArchiveException expected, but not thrown.");
 } catch (ArchiveException e) {
 // Expected
 }
 archiveOutputStream2.close();
 unknownFile.close();
 }
}