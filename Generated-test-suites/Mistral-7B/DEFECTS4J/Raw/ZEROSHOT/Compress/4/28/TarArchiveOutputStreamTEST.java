import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarArchiveOutputStream;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testConstructor() {
        Assertions.assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assertions.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] expectedEOFRecord = new byte[TarConstants.STANDARD_RECORD_SIZE];
        Arrays.fill(expectedEOFRecord, (byte) 0);
        Assertions.assertArrayEquals(expectedEOFRecord, outputStream.toByteArray(), 0, expectedEOFRecord.length);
        Assertions.assertArrayEquals(expectedEOFRecord, outputStream.toByteArray(), outputStream.size() - TarConstants.STANDARD_RECORD_SIZE, expectedEOFRecord.length);
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();

        byte[] expectedEOFRecord = new byte[TarConstants.STANDARD_RECORD_SIZE * 2];
        Arrays.fill(expectedEOFRecord, (byte) 0);
        Assertions.assertArrayEquals(expectedEOFRecord, outputStream.toByteArray(), 0, expectedEOFRecord.length);
        Assertions.assertEquals(0, outputStream.size(), "Output stream size should be zero after closing it.");
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedHeaderRecord = new byte[TarConstants.STANDARD_RECORD_SIZE];
        tarArchiveEntry.writeEntryHeader(expectedHeaderRecord);

        byte[] expectedDataRecord = "Hello World!".getBytes(StandardCharsets.UTF_8);

        Assertions.assertArrayEquals(expectedHeaderRecord, outputStream.toByteArray(), 0, expectedHeaderRecord.length);
        Assertions.assertArrayEquals(expectedDataRecord, outputStream.toByteArray(), expectedHeaderRecord.length, expectedDataRecord.length);
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        byte[] dataToWrite = "Hello World!".getBytes(StandardCharsets.UTF_8);

        int bytesWritten = tarArchiveOutputStream.write(dataToWrite, 0, dataToWrite.length);
        Assertions.assertEquals(dataToWrite.length, bytesWritten);

        byte[] expectedDataRecord = new byte[dataToWrite.length + TarConstants.STANDARD_RECORD_SIZE];
        System.arraycopy(tarArchiveEntry.getHeaderRecord(), 0, expectedDataRecord, 0, TarConstants.STANDARD_RECORD_SIZE);
        System.arraycopy(dataToWrite, 0, expectedDataRecord, TarConstants.STANDARD_RECORD_SIZE, dataToWrite.length);

        Assertions.assertArrayEquals(expectedDataRecord, outputStream.toByteArray(), 0, expectedDataRecord.length);
    }

    @Test
    public void testWriteWithAssembling() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        byte[] dataToWrite = "Hello World!".getBytes(StandardCharsets.UTF_8);

        int bytesWritten = tarArchiveOutputStream.write(dataToWrite, 0, dataToWrite.length / 2);
        Assertions.assertEquals(dataToWrite.length / 2, bytesWritten);

        byte[] expectedPartialDataRecord = new byte[dataToWrite.length / 2];
        System.arraycopy(dataToWrite, 0, expectedPartialDataRecord, 0, dataToWrite.length / 2);

        Assertions.assertArrayEquals(expectedPartialDataRecord, outputStream.toByteArray(), 0, expectedPartialDataRecord.length);

        bytesWritten = tarArchiveOutputStream.write(dataToWrite, dataToWrite.length / 2, dataToWrite.length / 2);
        Assertions.assertEquals(dataToWrite.length / 2, bytesWritten);

        byte[] expectedFullDataRecord = new byte[dataToWrite.length + TarConstants.STANDARD_RECORD_SIZE];
        System.arraycopy(tarArchiveEntry.getHeaderRecord(), 0, expectedFullDataRecord, 0, TarConstants.STANDARD_RECORD_SIZE);
        System.arraycopy(dataToWrite, 0, expectedFullDataRecord, TarConstants.STANDARD_RECORD_SIZE, dataToWrite.length);

        Assertions.assertArrayEquals(expectedFullDataRecord, outputStream.toByteArray(), 0, expectedFullDataRecord.length);
    }

    @Test
    public void testWriteWithAssemblingAndClosingEntry() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        byte[] dataToWrite = "Hello World!".getBytes(StandardCharsets.UTF_8);

        int bytesWritten = tarArchiveOutputStream.write(dataToWrite, 0, dataToWrite.length / 2);
        Assertions.assertEquals(dataToWrite.length / 2, bytesWritten);

        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedPartialDataRecord = new byte[dataToWrite.length / 2];
        System.arraycopy(dataToWrite, 0, expectedPartialDataRecord, 0, dataToWrite.length / 2);

        Assertions.assertArrayEquals(expectedPartialDataRecord, outputStream.toByteArray(), 0, expectedPartialDataRecord.length);

        bytesWritten = tarArchiveOutputStream.write(dataToWrite, dataToWrite.length / 2, dataToWrite.length / 2);
        Assertions.assertEquals(dataToWrite.length / 2, bytesWritten);

        tarArchiveOutputStream.close();

        byte[] expectedFullDataRecord = new byte[dataToWrite.length + TarConstants.STANDARD_RECORD_SIZE];
        System.arraycopy(tarArchiveEntry.getHeaderRecord(), 0, expectedFullDataRecord, 0, TarConstants.STANDARD_RECORD_SIZE);
        System.arraycopy(dataToWrite, 0, expectedFullDataRecord, TarConstants.STANDARD_RECORD_SIZE, dataToWrite.length);

        Assertions.assertArrayEquals(expectedFullDataRecord, outputStream.toByteArray(), 0, expectedFullDataRecord.length);
    }
}