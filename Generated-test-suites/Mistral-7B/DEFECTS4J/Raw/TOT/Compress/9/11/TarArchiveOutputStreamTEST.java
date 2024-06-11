import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    @Test
    public void testTarArchiveOutputStream() throws IOException {
        // Test creating a TarArchiveOutputStream with default parameters
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        // Test setting long file mode
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        // Test writing a file entry
        File testFile = new File("test.txt");
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt", "Test content.");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        IOUtils.write("Test content.", testFile.getAbsoluteFile().getOutputStream(), "UTF-8");
        tarArchiveOutputStream.write(testFile.getName().getBytes(), 0, testFile.getName().length());
        tarArchiveOutputStream.writeTo(testFile.getAbsoluteFile().getInputStream(), tarArchiveOutputStream.getSize());
        tarArchiveOutputStream.closeArchiveEntry();
        testFile.delete();
        tarArchiveOutputStream.close();

        // Test reading the written archive
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveOutputStream testTarArchiveOutputStream = new TarArchiveOutputStream(inputStream);
        TarArchiveEntry testTarArchiveEntry = (TarArchiveEntry) testTarArchiveOutputStream.getNextEntry();
        Assert.assertEquals("test.txt", testTarArchiveEntry.getName());
        Assert.assertEquals("Test content.", IOUtils.toString(testTarArchiveOutputStream, "UTF-8"));
        testTarArchiveOutputStream.close();

        // Test writing a directory entry
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry directoryEntry = new TarArchiveEntry("test_dir/", "");
        directoryEntry.setDirectory(true);
        tarArchiveOutputStream.putArchiveEntry(directoryEntry);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();

        // Test reading the written archive (directory)
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        testTarArchiveOutputStream = new TarArchiveOutputStream(inputStream);
        testTarArchiveEntry = (TarArchiveEntry) testTarArchiveOutputStream.getNextEntry();
        Assert.assertEquals("test_dir/", testTarArchiveEntry.getName());
        Assert.assertTrue(testTarArchiveEntry.isDirectory());
        testTarArchiveOutputStream.close();

        // Test writing multiple file entries
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        testFile = new File("test1.txt");
        TarArchiveEntry testFileEntry1 = new TarArchiveEntry("test1.txt", "Test content 1.");
        tarArchiveOutputStream.putArchiveEntry(testFileEntry1);
        IOUtils.write("Test content 1.", testFile.getAbsoluteFile().getOutputStream(), "UTF-8");
        tarArchiveOutputStream.write(testFile.getName().getBytes(), 0, testFile.getName().length());
        tarArchiveOutputStream.writeTo(testFile.getAbsoluteFile().getInputStream(), tarArchiveOutputStream.getSize());
        tarArchiveOutputStream.closeArchiveEntry();
        testFile.delete();

        testFile = new File("test2.txt");
        TarArchiveEntry testFileEntry2 = new TarArchiveEntry("test2.txt", "Test content 2.");
        tarArchiveOutputStream.putArchiveEntry(testFileEntry2);
        IOUtils.write("Test content 2.", testFile.getAbsoluteFile().getOutputStream(), "UTF-8");
        tarArchiveOutputStream.write(testFile.getName().getBytes(), 0, testFile.getName().length());
        tarArchiveOutputStream.writeTo(testFile.getAbsoluteFile().getInputStream(), tarArchiveOutputStream.getSize());
        tarArchiveOutputStream.closeArchiveEntry();
        testFile.delete();

        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        // Test reading the written archive (multiple entries)
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        testTarArchiveOutputStream = new TarArchiveOutputStream(inputStream);

        testTarArchiveEntry = (TarArchiveEntry) testTarArchiveOutputStream.getNextEntry();
        Assert.assertEquals("test1.txt", testTarArchiveEntry.getName());
        Assert.assertEquals("Test content 1.", IOUtils.toString(testTarArchiveOutputStream, "UTF-8"));
        testTarArchiveOutputStream.closeArchiveEntry();

        testTarArchiveEntry = (TarArchiveEntry) testTarArchiveOutputStream.getNextEntry();
        Assert.assertEquals("test2.txt", testTarArchiveEntry.getName());
        Assert.assertEquals("Test content 2.", IOUtils.toString(testTarArchiveOutputStream, "UTF-8"));
        testTarArchiveOutputStream.closeArchiveEntry();

        testTarArchiveEntry = (TarArchiveEntry) testTarArchiveOutputStream.getNextEntry();
        Assert.assertNull(testTarArchiveEntry);
        testTarArchiveOutputStream.close();

        // Test writing a long file name
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        testFile = new File("long_test.txt");
        TarArchiveEntry longFileNameEntry = new TarArchiveEntry("long_test_entry/test_entry_with_very_long_name.txt", "Test content.");
        tarArchiveOutputStream.putArchiveEntry(longFileNameEntry);
        IOUtils.write("Test content.", testFile.getAbsoluteFile().getOutputStream(), "UTF-8");
        tarArchiveOutputStream.writeTo(testFile.getAbsoluteFile().getInputStream(), tarArchiveOutputStream.getSize());
        tarArchiveOutputStream.closeArchiveEntry();
        testFile.delete();
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        // Test reading the written archive (long file name)
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        testTarArchiveOutputStream = new TarArchiveOutputStream(inputStream);

        testTarArchiveEntry = (TarArchiveEntry) testTarArchiveOutputStream.getNextEntry();
        Assert.assertEquals("long_test_entry/test_entry_with_very_long_name.txt", testTarArchiveEntry.getName());
        Assert.assertEquals("Test content.", IOUtils.toString(testTarArchiveOutputStream, "UTF-8"));
        testTarArchiveOutputStream.closeArchiveEntry();
        testTarArchiveOutputStream.close();
    }
}