import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        if (tarArchiveOutputStream != null) {
            tarArchiveOutputStream.close();
        }
    }

    @Test
    public void testEmptyTarArchive() throws IOException {
        tarArchiveOutputStream.finish();
        byte[] expectedBytes = new byte[TarConstants.HEADER_LENGTH * 2];
        Assert.assertArrayEquals(expectedBytes, outputStream.toByteArray());
    }

    @Test
    public void testPutSingleEntryWithShortFileName() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Hello World!".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry readEntry = new TarArchiveEntry(inputStream);
        byte[] bytes = new byte[10];
        inputStream.read(bytes);
        Assert.assertArrayEquals("Hello World!".getBytes(), bytes);
    }

    @Test
    public void testPutSingleEntryWithLongFileName() throws IOException {
        String fileName = "longFileNameThatIsLongerThanNameLength" +
                "SpecifiedInTarConstants.NAMELEN.txt";
        TarArchiveEntry entry = new TarArchiveEntry(fileName);
        entry.setSize(10);

        if (tarArchiveOutputStream.getLongFileMode() != TarArchiveOutputStream.LONGFILE_GNU) {
            tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        }

        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Hello World!".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry readEntry = new TarArchiveEntry(inputStream);
        byte[] bytes = new byte[10];
        inputStream.read(bytes);
        Assert.assertArrayEquals("Hello World!".getBytes(), bytes);
    }

    @Test(expected = IOException.class)
    public void testPutSingleEntryWithLongFileNameAndErrorMode() throws IOException {
        String fileName = "longFileNameThatIsLongerThanNameLength" +
                "SpecifiedInTarConstants.NAMELEN.txt";
        TarArchiveEntry entry = new TarArchiveEntry(fileName);
        entry.setSize(10);

        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Hello World!".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        if (tarArchiveOutputStream.getLongFileMode() != TarArchiveOutputStream.LONGFILE_ERROR) {
            tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        }

        TarArchiveEntry entryToWrite = new TarArchiveEntry("testError.txt");
        tarArchiveOutputStream.putArchiveEntry(entryToWrite);
        tarArchiveOutputStream.write("Error!".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
    }
}