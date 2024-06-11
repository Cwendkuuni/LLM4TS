package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TarArchiveOutputStreamTest {

 private static final int LONG_FILE_NAME_LENGTH = 256;
 private static final int RECORD_SIZE = 512;
 private static final int TOTAL_DATA_TO_WRITE = 2 * RECORD_SIZE;
 private static final int BUFFER_SIZE = 1024;

 @Test
 public void testConstructor() {
 TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream(BUFFER_SIZE));
 assertTrue(tarStream instanceof TarArchiveOutputStream);
 }

 @Test
 public void testSetLongFileMode() {
 TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream(BUFFER_SIZE));
 tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
 assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarStream.getLongFileMode());
 }

 @Test
 public void testPutArchiveEntryWithValidEntry() throws IOException {
 TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream(BUFFER_SIZE));
 TarArchiveEntry entry = new TarArchiveEntry("testEntry.txt");
 tarStream.putArchiveEntry(entry);

 byte[] expectedRecordHeader = entry.writeHeader(new byte[TarConstants.NAMELEN]);
 byte[] expectedRecordData = "Test data for the test entry.".getBytes();

 assertEquals(expectedRecordHeader.length, tarStream.getRecordSize());
 tarStream.write(expectedRecordHeader);
 tarStream.write(expectedRecordData);
 tarStream.finish();

 InputStream testInputStream = tarStream.getInputStream();
 byte[] readRecordHeader = new byte[expectedRecordHeader.length];
 byte[] readRecordData = new byte[expectedRecordData.length];
 int read = 0;
 while (read < expectedRecordData.length) {
 read += testInputStream.read(readRecordData, read, expectedRecordData.length - read);
 }
 InputStream inputStream = new ByteArrayInputStream(expectedRecordData);
 byte[] temp = new byte[expectedRecordData.length];
 int length = 0;
 while((length = inputStream.read(temp)) > 0) {
 assertEquals(temp[0], readRecordData[read]);
 read += length;
 }
 }

 @Test
 public void testPutArchiveEntryWithLongFileName() throws IOException {
 TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream(BUFFER_SIZE));
 TarArchiveEntry entry = new TarArchiveEntry("testEntry_" + new String(new char[LONG_FILE_NAME_LENGTH]).getBytes());
 tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
 tarStream.putArchiveEntry(entry);

 byte[] expectedRecordHeader = entry.writeHeader(new byte[TarConstants.NAMELEN]);
 byte[] expectedLongFileNameRecord = ArchiveUtils.toAsciiBytes(entry.getName());
 byte[] expectedLongFileNameData = new byte[LONG_FILE_NAME_LENGTH];
 System.arraycopy(expectedLongFileNameRecord, 0, expectedLongFileNameData, 0, LONG_FILE_NAME_LENGTH);

 assertEquals(expectedRecordHeader.length, tarStream.getRecordSize());
 tarStream.write(expectedRecordHeader);
 tarStream.write(expectedLongFileNameRecord);
 tarStream.write(expectedLongFileNameData);
 tarStream.write(expectedRecordData);
 tarStream.finish();

 InputStream testInputStream = tarStream.getInputStream();
 byte[] readRecordHeader = new byte[expectedRecordHeader.length];
 byte[] readRecordData = new byte[expectedRecordData.length];
 int read = 0;
 while (read < expectedRecordData.length) {
 read += testInputStream.read(readRecordData, read, expectedRecordData.length - read);
 }
 InputStream inputStream = new ByteArrayInputStream(expectedRecordData);
 byte[] temp = new byte[expectedRecordData.length];
 int length = 0;
 while((length = inputStream.read(temp)) > 0) {
 assertEquals(temp[0], readRecordData[read]);
 read += length;
 }
 }

 @Test
 public void testPutArchiveEntryWithInvalidLongFileNameMode() throws IOException {
 TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream(BUFFER_SIZE));
 TarArchiveEntry entry = new TarArchiveEntry("testEntry.txt");
 tarStream.putArchiveEntry(entry);

 assertThrows(RuntimeException.class, () -> tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR));
 }

 @Test
 public void testCloseArchiveEntry() throws IOException {
 TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream(BUFFER_SIZE));
 TarArchiveEntry entry = new TarArchiveEntry("testEntry.txt");
 tarStream.putArchiveEntry(entry);
 tarStream.write(new byte[TarConstants.NAMELEN]);
 tarStream.finish();
 tarStream.closeArchiveEntry();

 byte[] expectedEOFRecord = new byte[2 * TarConstants.BLOCKSIZE];
 for (int i = 0; i < expectedEOFRecord.length; ++i) {
 expectedEOFRecord[i] = 0;
 }

 assertEquals(expectedEOFRecord.length, tarStream.getRecordSize());
 tarStream.write(expectedEOFRecord);
 tarStream.write(expectedEOFRecord);
 tarStream.close();
 }

 @Test
 public void testPutArchiveEntryWithMultipleEntries() throws IOException {
 TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream(BUFFER_SIZE));
 TarArchiveEntry entry1 = new TarArchiveEntry("testEntry1.txt");
 TarArchiveEntry entry2 = new TarArchiveEntry("testEntry2.txt");
 tarStream.putArchiveEntry(entry1);
 tarStream.putArchiveEntry(entry2);
 tarStream.finish();

 byte[] expectedRecord1Header = entry1.writeHeader(new byte[TarConstants.NAMELEN]);
 byte[] expectedRecord1Data = "Test data for the first entry.".getBytes();
 byte[] expectedRecord2Header = entry2.writeHeader(new byte[TarConstants.NAMELEN]);
 byte[] expectedRecord2Data = "Test data for the second entry.".getBytes();

 assertEquals(expectedRecord1Header.length, tarStream.getRecordSize());
 tarStream.write(expectedRecord1Header);
 tarStream.write(expectedRecord1Data);
 tarStream.write(expectedRecord2Header);
 tarStream.write(expectedRecord2Data);
 tarStream.write(expectedRecord1Header);
 tarStream.write(expectedRecord1Data);
 tarStream.write(expectedRecord2Header);
 tarStream.write(expectedRecord2Data);
 tarStream.write(expectedRecord1Header);
 tarStream.write(expectedRecord1Data);
 tarStream.write(expectedRecord2Header);
 tarStream.write(expectedRecord2Data);
 tarStream.write(expectedEOFRecord);
 tarStream.write(expectedEOFRecord);
 tarStream.close();
 }

 @Test
 public void testWriteWithValidData() throws IOException {
 TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream(BUFFER_SIZE));
 byte[] dataToWrite = "Test data for the test write method.".getBytes();
 tarStream.write(dataToWrite);
 tarStream.finish();

 byte[] expectedRecordData = dataToWrite;
 byte[] readRecordData = new byte[dataToWrite.length];
 InputStream testInputStream = tarStream.getInputStream();
 int read = 0;
 while (read < expectedRecordData.length) {
 read += testInputStream.read(readRecordData, read, dataToWrite.length - read);
 }
 InputStream inputStream = new ByteArrayInputStream(dataToWrite);
 byte[] temp = new byte[expectedRecordData.length];
 int length = 0;
 while((length = inputStream.read(temp)) > 0) {
 assertEquals(temp[0], readRecordData[read]);
 read += length;
 }
 }

 @Test
 public void testWriteWithInvalidDataLength() throws IOException {
 TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream(BUFFER_SIZE));
 byte[] dataToWrite = "Test data for the test write method.".getBytes();
 tarStream.write(dataToWrite);
 tarStream.write(dataToWrite);

 assertThrows(IOException.class, () -> tarStream.write(dataToWrite));
 }

 @Test
 public void testClose() throws IOException {
 TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream(BUFFER_SIZE));
 byte[] dataToWrite = "Test data for the test close method.".getBytes();
 tarStream.write(dataToWrite);
 tarStream.finish();
 tarStream.close();

 assertEquals(0, tarStream.getRecordSize());
 assertNull(tarStream.getInputStream());
 assertFalse(tarStream.isFinished());
 assertFalse(tarStream.isWritable());
 }

 @Test
 public void testFinish() throws IOException {
 TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream(BUFFER_SIZE));
 byte[] dataToWrite = "Test data for the test finish method.".getBytes();
 tarStream.write(dataToWrite);
 tarStream.finish();

 InputStream testInputStream = tarStream.getInputStream();
 byte[] expectedRecordData = dataToWrite;
 byte[] readRecordData = new byte[dataToWrite.length];
 int read = 0;
 while (read < expectedRecordData.length) {
 read += testInputStream.read(readRecordData, read, dataToWrite.length - read);
 }
 InputStream inputStream = new ByteArrayInputStream(dataToWrite);
 byte[] temp = new byte[expectedRecordData.length];
 int length = 0;
 while((length = inputStream.read(temp)) > 0) {
 assertEquals(temp[0], readRecordData[read]);
 read += length;
 }
 tarStream.close();
 }
}