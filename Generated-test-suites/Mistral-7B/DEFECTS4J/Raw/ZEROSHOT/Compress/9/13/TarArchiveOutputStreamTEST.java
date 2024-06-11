import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.CountingInputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    private File testFile;
    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;
    private CountingOutputStream countingOutputStream;

    @Before
    public void setUp() throws IOException {
        testFile = File.createTempFile("test", ".tar");
        outputStream = new ByteArrayOutputStream();
        countingOutputStream = new CountingOutputStream(outputStream);
        tarArchiveOutputStream = new TarArchiveOutputStream(countingOutputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
        testFile.delete();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry(new File("src/test/java/org/apache/commons/compress/archivers/tar/TarArchiveOutputStreamTest.java"), "TarArchiveOutputStreamTest.java");
        tarArchiveOutputStream.putArchiveEntry(testEntry);
        Assert.assertEquals("TarArchiveOutputStreamTest.java", tarArchiveOutputStream.currName);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry(new File("src/test/java/org/apache/commons/compress/archivers/tar/TarArchiveOutputStreamTest/subdirectory/testFileWithLongName.txt"), "testFileWithLongName.txt");
        tarArchiveOutputStream.putArchiveEntry(testEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutCloseArchiveEntry() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry(new File("src/test/java/org/apache/commons/compress/archivers/tar/TarArchiveOutputStreamTest.java"), "TarArchiveOutputStreamTest.java");
        tarArchiveOutputStream.putArchiveEntry(testEntry);
        Assert.assertEquals("TarArchiveOutputStreamTest.java", tarArchiveOutputStream.currName);
        tarArchiveOutputStream.write(new byte[1]);
    }

    @Test
    public void testWrite() throws IOException {
        tarArchiveOutputStream.write("Test String".getBytes());
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        byte[] buffer = new byte[1024];
        int read = 0;
        while ((read = inputStream.read(buffer)) != -1) {
            System.out.write(buffer, 0, read);
        }

        Assert.assertTrue(testFile.exists());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry(new File("src/test/java/org/apache/commons/compress/archivers/tar/TarArchiveOutputStreamTest.java"), "TarArchiveOutputStreamTest.java");
        tarArchiveOutputStream.putArchiveEntry(testEntry);
        tarArchiveOutputStream.write("Test String".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.write("Closed Entry Test String".getBytes());
    }
}