import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TarArchiveOutputStreamTest {

    private OutputStream out;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        out = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(out, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testConstructorWithOutputStream() throws Exception {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(out);
        assertThat(tarArchiveOutputStream, is(instanceOf(TarArchiveOutputStream.class)));
    }

    @Test
    public void testConstructorWithOutputStreamBlockSize() throws Exception {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(out, TarBuffer.DEFAULT_BLKSIZE);
        assertThat(tarArchiveOutputStream, is(instanceOf(TarArchiveOutputStream.class)));
    }

    @Test
    public void testConstructorWithOutputStreamBlockSizeRecordSize() throws Exception {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(out, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        assertThat(tarArchiveOutputStream, is(instanceOf(TarArchiveOutputStream.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullOutputStream() throws Exception {
        new TarArchiveOutputStream(null);
    }

    @Test
    public void testSetLongFileMode() throws Exception {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertThat(tarArchiveOutputStream.getLongFileMode(), is(TarArchiveOutputStream.LONGFILE_TRUNCATE));
    }

    @Test
    public void testFinish() throws Exception {
        tarArchiveOutputStream.finish();
        byte[] expectedEOFRecord = new byte[TarBuffer.RECORD_SIZE * 2];
        byte[] actualEOFRecord = out.toByteArray();

        System.arraycopy(expectedEOFRecord, 0, actualEOFRecord, 0, expectedEOFRecord.length);

        assertThat(actualEOFRecord, is(equalTo(expectedEOFRecord)));
    }

    @Test
    public void testClose() throws Exception {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        byte[] actualEOFRecord = out.toByteArray();
        byte[] expectedEOFRecord = new byte[TarBuffer.RECORD_SIZE * 2];
        System.arraycopy(expectedEOFRecord, 0, actualEOFRecord, 0, expectedEOFRecord.length);

        assertThat(actualEOFRecord, is(equalTo(expectedEOFRecord)));
        out.close();
    }

    @Test
    public void testGetRecordSize() throws Exception {
        assertThat(tarArchiveOutputStream.getRecordSize(), is(TarBuffer.RECORD_SIZE));
    }

    @Test
    public void testPutArchiveEntry() throws Exception {
        byte[] testFileContent = "Test file content".getBytes();
        File testFile = new File("testFile.txt");
        ArchiveEntry archiveEntry = new TarArchiveEntry(testFile, "testFile.txt");

        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(testFileContent, 0, testFileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedTarEntry = new byte[TarConstants.NAMELEN + 1 + testFileContent.length];
        byte[] actualTarEntry = out.toByteArray();

        System.arraycopy(expectedTarEntry, 0, actualTarEntry, 0, expectedTarEntry.length);

        assertThat(actualTarEntry, is(not(equalTo(expectedTarEntry))));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryLongFileName() throws Exception {
        String longFileName = "longFileNameThatIsLongerThanTarConstants.NAMELEN.length()";
        ArchiveEntry archiveEntry = new TarArchiveEntry(new File("longFileNameThatIsLongerThanTarConstants.NAMELEN.length()"), longFileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testCloseArchiveEntry() throws Exception {
        byte[] testFileContent = "Test file content".getBytes();
        File testFile = new File("testFile.txt");
        ArchiveEntry archiveEntry = new TarArchiveEntry(testFile, "testFile.txt");

        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(testFileContent, 0, testFileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedEOFRecord = new byte[TarBuffer.RECORD_SIZE];
        byte[] actualEOFRecord = out.toByteArray();

        System.arraycopy(expectedEOFRecord, 0, actualEOFRecord, 0, expectedEOFRecord.length);

        assertThat(actualEOFRecord, is(equalTo(expectedEOFRecord)));
    }
}