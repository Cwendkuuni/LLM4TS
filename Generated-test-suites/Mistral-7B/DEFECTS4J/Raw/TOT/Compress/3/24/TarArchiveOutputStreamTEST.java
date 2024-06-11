import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TarArchiveOutputStreamTest {

    private File outputFile;
    private TarOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        outputFile = Files.createTempFile("test-tar", ".tar").toFile();
        tarOutputStream = new TarOutputStream(new FileOutputStream(outputFile));
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.close();
        outputFile.delete();
    }

    @Test
    public void testEmptyArchive() throws IOException {
        tarOutputStream.putNextEntry(new TarArchiveEntry("test-empty.txt"));
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        File archiveFile = new File(outputFile.getAbsolutePath() + ".tar.gz"); // Assuming the TarOutputStream writes a gzipped tar file
        Assert.assertFalse(Files.isEmpty(Files.newInputStream(Paths.get(archiveFile.getAbsolutePath()))));

        try (InputStream inputStream = new FileInputStream(archiveFile)) {
            byte[] buffer = new byte[512];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) != -1) {
                byte[] expected = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, // Magic number
                        (byte) TarConstants.MAGIC_TAR, (byte) TarConstants.MAGIC_PAX_FILEHEADER,
                        (byte) 0x00, (byte) 0x00, // Version
                        (byte) 0x00, (byte) 0x00, // User ID
                        (byte) 0x00, (byte) 0x00, // Group ID
                        0x68, 0x74, 0x74, 0x70, // Name: test-empty.txt
                        (byte) 0x00, (byte) 0x00, (byte) 0x00, // Modification time
                        (byte) 0x00, (byte) 0x00, (byte) 0x00, // Size
                        0x00, (byte) 0x00, // Permissions
                        (byte) 0x00, (byte) 0x00, // Link name index
                        (byte) 0x00, (byte) 0x00, // Magic number for the next record (should be 0x50 0x4b)
                        (byte) 0x50, (byte) 0x4b, // Magic number for the next record (should be 0x50 0x4b)
                        (byte) 0x00, (byte) 0x00, // Version
                        (byte) 0x00, (byte) 0x00, // User ID
                        (byte) 0x00, (byte) 0x00, // Group ID
                        0x68, 0x74, 0x74, 0x70, // Name: test-empty.txt
                        0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, // Modification time
                        0x00, (byte) 0x00, // Size
                        0x00, // Permissions
                        0x00, // Link name index
                        (byte) 0x00 // End of record marker
                };
                Assert.assertArrayEquals(expected, buffer, 0, buffer.length);
            }
        }
    }

    @Test(expected = RuntimeException.class)
    public void testLongFileName() throws IOException {
        String longFileName = "a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/AAA";
        tarOutputStream.setLongFileMode(TarOutputStream.LONGFILE_GNU);
        tarOutputStream.putNextEntry(new TarArchiveEntry(longFileName));
        tarOutputStream.write("Long file name test.".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
    }

    @Test(expected = RuntimeException.class)
    public void testLongFileNameWithoutGnuExtension() throws IOException {
        String longFileName = "a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/AAA";
        tarOutputStream.setLongFileMode(TarOutputStream.LONGFILE_TRUNCATE);
        tarOutputStream.putNextEntry(new TarArchiveEntry(longFileName));
        tarOutputStream.write("Long file name test.".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
    }
}