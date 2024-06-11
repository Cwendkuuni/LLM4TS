import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class LibraryTest {
    
    private Library library;
    
    @Before
    public void setUp() {
        library = new Library();
    }
    
    @Test
    public void testLoadFromDirectory() {
        assertTrue(library.loadFromDirectory("path/to/library"));
    }
    
    @Test
    public void testGetDictionaries() {
        library.loadFromDirectory("path/to/library");
        assertNotNull(library.getDictionaries());
    }
    
    @Test
    public void testGetDictionary() {
        library.loadFromDirectory("path/to/library");
        assertNotNull(library.getDictionary("version"));
    }
    
    @Test
    public void testLoadDirectory() {
        library.loadDirectory(someFileList, "version");
        assertNotNull(library.getDictionary("version"));
    }
    
    @Test
    public void testArrangeFiles() {
        File[] files = {fieldsFile, enumsFile, componentsFile, msgTypeFile, msgContentsFile};
        List<File> arrangedFiles = library.arrangeFiles(files);
        assertEquals(5, arrangedFiles.size());
    }
}