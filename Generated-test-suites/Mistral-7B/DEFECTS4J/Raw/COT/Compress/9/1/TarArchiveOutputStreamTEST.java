import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() throws Exception {
        byteArrayOutputStream = new ByteArrayOutputStream(1024);
        outputStream = byteArrayOutputStream;
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
    }

    @After
    public void tearDown() throws Exception {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testConstructorWithBlockSizeAndRecordSize() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
        assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(new File("test.txt"));
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.finish();
        byte[] expectedResult = Arrays.copyOfRange(ArchiveUtils.toBytes(TarConstants.MAGIC), 0, TarConstants.MAGIC.length);
        assertEquals(expectedResult, byteArrayOutputStream.toByteArray().subArray(0, TarConstants.MAGIC.length));
        byte[] eofRecord = new byte[TarConstants.RECORD_LENGTH];
        Arrays.fill(eofRecord, (byte) 0);
        assertEquals(Arrays.toString(eofRecord), Arrays.toString(Arrays.copyOfRange(byteArrayOutputStream.toByteArray(), byteArrayOutputStream.size() - TarConstants.RECORD_LENGTH, byteArrayOutputStream.size())));
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(new File("test.txt"));
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
        assertFalse(tarArchiveOutputStream.isOpen());
    }

    @Test
    public void testGetRecordSize() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        assertEquals(512, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntryWithoutCloseArchiveEntry() {
        TarArchiveEntry entry = new TarArchiveEntry(new File("test.txt"));
        tarArchiveOutputStream.putArchiveEntry(entry);
        assertThrows(IOException.class, () -> tarArchiveOutputStream.finish());
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "a" + TarConstants.NAMELEN + "b";
        TarArchiveEntry entry = new TarArchiveEntry(new File(longFileName));
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] expectedResult = Arrays.copyOfRange(ArchiveUtils.toBytes(TarConstants.MAGIC), 0, TarConstants.MAGIC.length);
        assertEquals(expectedResult, byteArrayOutputStream.toByteArray().subArray(0, TarConstants.MAGIC.length));
        byte[] longFileNameRecord = new byte[1 + TarConstants.NAMELEN + 1];
        Arrays.fill(longFileNameRecord, (byte) 0);
        longFileNameRecord[0] = TarConstants.GNU_LONGLINK;
        longFileNameRecord[1] = (byte) (longFileName.length() & 0xFF);
        for (int i = 0; i < longFileName.length(); i++) {
            longFileNameRecord[1 + i] = (byte) (longFileName.charAt(i) & 0xFF);
        }
        longFileNameRecord[1 + longFileName.length()] = 0;
        assertEquals(Arrays.toString(longFileNameRecord), Arrays.copyOfRange(byteArrayOutputStream.toByteArray(), 0, TarConstants.NAMELEN + 2).subArray(1, TarConstants.NAMELEN + 1));
        assertEquals(longFileName, new String(byteArrayOutputStream.toByteArray().subArray(TarConstants.NAMELEN + 2, TarConstants.NAMELEN + 2 + longFileName.length()), StandardCharsets.UTF_8));
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameAndTruncate() throws IOException {
        String longFileName = "a" + TarConstants.NAMELEN + "b";
        TarArchiveEntry entry = new TarArchiveEntry(new File(longFileName));
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.finish();
        byte[] expectedResult = Arrays.copyOfRange(ArchiveUtils.toBytes(TarConstants.MAGIC), 0, TarConstants.MAGIC.length);
        assertEquals(expectedResult, byteArrayOutputStream.toByteArray().subArray(0, TarConstants.MAGIC.length));
        byte[] fileNameRecord = new byte[TarConstants.NAMELEN + 1];
        Arrays.fill(fileNameRecord, (byte) 0);
        fileNameRecord[0] = TarConstants.NAME_HEADER;
        for (int i = 0; i < TarConstants.NAMELEN; i++) {
            fileNameRecord[1 + i] = (byte) (longFileName.charAt(i) & 0xFF);
        }
        fileNameRecord[TarConstants.NAMELEN] = 0;
        byte[] longFileNameBytes = ArchiveUtils.toBytes(longFileName);
        System.arraycopy(longFileNameBytes, 0, fileNameRecord, 1, TarConstants.NAMELEN);
        assertEquals(Arrays.toString(fileNameRecord), Arrays.copyOfRange(byteArrayOutputStream.toByteArray(), 0, TarConstants.NAMELEN + 1));
    }
}