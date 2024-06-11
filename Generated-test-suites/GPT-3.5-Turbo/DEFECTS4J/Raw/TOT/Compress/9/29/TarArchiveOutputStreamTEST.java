import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutputStream;
    private String testFolderPath = "testFolder";
    private String testFilePath = testFolderPath + "/testFile.txt";

    @Before
    public void setup() {
        File testFolder = new File(testFolderPath);
        testFolder.mkdir();
    }

    @Test
    public void testPutArchiveEntry_Success() throws IOException {
        FileOutputStream fos = new FileOutputStream(testFilePath);
        byte[] testData = "Hello, world!".getBytes();
        fos.write(testData);
        fos.close();

        File file = new File(testFilePath);
        ArchiveEntry entry = new TarArchiveEntry(file, file.getName());

        tarOutputStream = new TarArchiveOutputStream(new FileOutputStream("test.tar"));
        tarOutputStream.putArchiveEntry(entry);

        assertTrue(tarOutputStream.haveUnclosedEntry);
        assertEquals(tarOutputStream.currSize, file.length());
        assertEquals(tarOutputStream.currName, file.getName());
        assertEquals(tarOutputStream.currBytes, 0);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntry_Failure_FileNotFound() throws IOException {
        File file = new File("fileNotFound.txt");
        ArchiveEntry entry = new TarArchiveEntry(file, file.getName());

        tarOutputStream = new TarArchiveOutputStream(new FileOutputStream("test.tar"));
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testCloseArchiveEntry_Success() throws IOException {
        FileOutputStream fos = new FileOutputStream(testFilePath);
        byte[] testData = "Hello, world!".getBytes();
        fos.write(testData);
        fos.close();

        File file = new File(testFilePath);
        ArchiveEntry entry = new TarArchiveEntry(file, file.getName());

        tarOutputStream = new TarArchiveOutputStream(new FileOutputStream("test.tar"));
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();

        assertFalse(tarOutputStream.haveUnclosedEntry);
        assertEquals(tarOutputStream.assemLen, 0);
        assertEquals(tarOutputStream.currBytes, testData.length);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntry_Failure_NoCurrentEntry() throws IOException {
        tarOutputStream = new TarArchiveOutputStream(new FileOutputStream("test.tar"));
        tarOutputStream.closeArchiveEntry();
    }

    @After
    public void cleanup() throws IOException {
        Path path = Paths.get(testFolderPath);
        Files.walk(path)
                .map(Path::toFile)
                .forEach(File::delete);
    }

}