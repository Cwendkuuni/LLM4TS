import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {
    
    private TarArchiveOutputStream tarOutput;
    private final String testDirectoryName = "testFolder";
    private final String testFileName = "testFile.txt";
    private final String testFileContent = "This is a test file.";
    
    @Before
    public void setUp() throws IOException {
        FileOutputStream fos = new FileOutputStream("test.tar");
        tarOutput = new TarArchiveOutputStream(fos);
    }

    @After
    public void tearDown() throws IOException {
        tarOutput.close();
        File file = new File("test.tar");
        file.delete();
    }

    @Test
    public void testPutArchiveEntryAndCloseArchiveEntryWithDirectory() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(testDirectoryName);
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();
        
        // Verify
        // The tar entry should be a directory
        // The tar entry name should match the given directory name
        // The tar entry should have zero size
        TarArchiveEntry tarEntry = (TarArchiveEntry) tarOutput.createArchiveEntry(new File(testDirectoryName), testDirectoryName);
        assert(tarEntry.isDirectory());
        assert(tarEntry.getName().equals(testDirectoryName));
        assert(tarEntry.getSize() == 0);
    }
    
    @Test
    public void testPutArchiveEntryAndCloseArchiveEntryWithFile() throws IOException {
        File testFile = new File(testFileName);
        FileOutputStream fos = new FileOutputStream(testFile);
        fos.write(testFileContent.getBytes());
        fos.close();
        
        TarArchiveEntry entry = new TarArchiveEntry(testFile);
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();
        
        // Verify
        // The tar entry should not be a directory
        // The tar entry name should match the given file name
        // The tar entry size should match the size of the file content
        TarArchiveEntry tarEntry = (TarArchiveEntry) tarOutput.createArchiveEntry(testFile, testFileName);
        assert(!tarEntry.isDirectory());
        assert(tarEntry.getName().equals(testFileName));
        assert(tarEntry.getSize() == testFileContent.length());
        
        testFile.delete();
    }
    
    @Test
    public void testPutArchiveEntryLongFileNameTruncate() throws IOException {
        String longFileName = generateLongFileName(500);
        
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOutput.putArchiveEntry(entry);
        
        // Verify
        // The tar entry should not be a directory
        // The tar entry name should match the given long file name, but truncated
        // The tar entry size should be zero
        TarArchiveEntry tarEntry = (TarArchiveEntry) tarOutput.createArchiveEntry(new File(longFileName), longFileName);
        assert(!tarEntry.isDirectory());
        assert(tarEntry.getName().equals(longFileName.substring(0, 99) + "..."));
        assert(tarEntry.getSize() == 0);

        tarOutput.closeArchiveEntry();
    }
    
    @Test
    public void testPutArchiveEntryLongFileNameGNU() throws IOException {
        String longFileName = generateLongFileName(500);

        TarArchiveOutputStream tarOutputGNU = new TarArchiveOutputStream(new FileOutputStream("test-gnu.tar"));
        tarOutputGNU.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOutputGNU.putArchiveEntry(entry);

        // Verify
        // The tar entry should not be a directory
        // The tar entry name should match the given long file name
        // The tar entry size should be zero
        TarArchiveEntry tarEntry = (TarArchiveEntry) tarOutputGNU.createArchiveEntry(new File(longFileName), longFileName);
        assert(!tarEntry.isDirectory());
        assert(tarEntry.getName().equals(longFileName));
        assert(tarEntry.getSize() == 0);

        tarOutputGNU.closeArchiveEntry();
        tarOutputGNU.close();
        
        File file = new File("test-gnu.tar");
        file.delete();
    }
    
    @Test(expected = IOException.class)
    public void testPutArchiveEntryLongFileNameError() throws IOException {
        String longFileName = generateLongFileName(500);

        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOutput.putArchiveEntry(entry);
        
        // An IOException should be thrown due to the long file name

        tarOutput.closeArchiveEntry();
    }
    
    private String generateLongFileName(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("a");
        }
        return sb.toString();
    }
}