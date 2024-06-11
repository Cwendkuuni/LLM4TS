import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TarArchiveOutputStreamTest {

    @Test
    public void testWriteSimpleFile() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        byte[] fileContent = "Hello World!".getBytes(StandardCharsets.UTF_8);
        tarOutputStream.putNextEntry(entry);
        tarOutputStream.write(fileContent, 0, fileContent.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry entryFromArchive = new TarArchiveEntry(inputStream);
        byte[] readContent = new byte[entryFromArchive.getSize()];
        IOUtils.readFully(entryFromArchive.getInputStream(), readContent, 0, readContent.length);
        IOUtils.closeQuietly(entryFromArchive.getInputStream());

        Assert.assertArrayEquals(fileContent, readContent);
    }

    @Test(expected = RuntimeException.class)
    public void testLongFileName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/aaa.txt");
        byte[] fileContent = "Long file name test".getBytes(StandardCharsets.UTF_8);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutputStream.putNextEntry(entry);
        tarOutputStream.write(fileContent, 0, fileContent.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry entryFromArchive = new TarArchiveEntry(inputStream);
        byte[] readContent = new byte[entryFromArchive.getSize()];
        IOUtils.readFully(entryFromArchive.getInputStream(), readContent, 0, readContent.length);
        IOUtils.closeQuietly(entryFromArchive.getInputStream());

        Assert.assertArrayEquals(fileContent, readContent);
    }

    @Test(expected = IOException.class)
    public void testWriteLongFileNameWithTruncation() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/aaa.txt");
        byte[] fileContent = "Long file name test".getBytes(StandardCharsets.UTF_8);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOutputStream.putNextEntry(entry);
        tarOutputStream.write(fileContent, 0, fileContent.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry entryFromArchive = new TarArchiveEntry(inputStream);
        byte[] readContent = new byte[entryFromArchive.getSize()];
        IOUtils.readFully(entryFromArchive.getInputStream(), readContent, 0, readContent.length);
        IOUtils.closeQuietly(entryFromArchive.getInputStream());

        Assert.fail("Expected IOException but found valid archive");
    }

    @Test
    public void testWriteDirectory() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test_dir/");
        tarOutputStream.putNextEntry(entry);
        tarOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry entryFromArchive = new TarArchiveEntry(inputStream);
        Assert.assertTrue(entryFromArchive.isDirectory());

        byte[] testDirHeader = entryFromArchive.getHeader();
        long dirMode = testDirHeader[TarConstants.FILE_HEADER_ST_MODE];
        Assert.assertEquals(TarConstants.S_IFDIR, dirMode);

        TarArchiveOutputStream subTarArchiveOutputStream = new TarArchiveOutputStream(tarOutputStream);
        TarArchiveEntry subEntry = new TarArchiveEntry("test_dir/sub_dir/");
        subTarArchiveOutputStream.putNextEntry(subEntry);
        subTarArchiveOutputStream.finish();
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        inputStream.reset();
        TarArchiveEntry subEntryFromArchive = new TarArchiveEntry(inputStream);
        Assert.assertTrue(subEntryFromArchive.isDirectory());

        byte[] subDirHeader = subEntryFromArchive.getHeader();
        long subDirMode = subDirHeader[TarConstants.FILE_HEADER_ST_MODE];
        Assert.assertEquals(TarConstants.S_IFDIR, subDirMode);
    }
}