import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class LibraryTest {
    
    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        assertTrue(library.loadFromDirectory("testDirectory"));
        assertFalse(library.loadFromDirectory("emptyDirectory"));
    }
    
    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        assertEquals(0, library.getDictionaries().size());
    }
    
    @Test
    public void testGetDictionary() {
        Library library = new Library();
        assertNull(library.getDictionary("nonExistentVersion"));
    }
    
    @Test
    public void testLoadDirectory() {
        Library library = new Library();
        // Create test files
        List<File> testFiles = new ArrayList<>();
        File testFile1 = new File("test1.xml");
        testFiles.add(testFile1);
        File testFile2 = new File("test2.xml");
        testFiles.add(testFile2);
        
        library.loadDirectory(testFiles, "TestVersion");
        assertNotNull(library.getDictionary("TestVersion"));
    }
    
    @Test
    public void testArrangeFiles() {
        Library library = new Library();
        // Create test files to be arranged
        File file1 = new File("Fields.xml");
        File file2 = new File("MsgType.xml");
        File file3 = new File("Components.xml");
        
        List<File> testFiles = new ArrayList<>();
        testFiles.add(file1);
        testFiles.add(file2);
        testFiles.add(file3);
        
        List<File> arrangedFiles = library.arrangeFiles(testFiles);
        assertEquals(3, arrangedFiles.size());
    }
}