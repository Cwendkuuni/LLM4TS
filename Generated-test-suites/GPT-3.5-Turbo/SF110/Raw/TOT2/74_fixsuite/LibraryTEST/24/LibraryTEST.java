import static org.junit.Assert.*;
import org.junit.Test;

public class LibraryTest {
    
    @Test
    public void testLoadFromDirectory() {
        // Test loading from a valid directory path
        Library library = new Library();
        assertTrue(library.loadFromDirectory("test/resources"));
        
        // Test loading from an invalid directory path
        assertFalse(library.loadFromDirectory("invalidPath"));
    }
    
    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        assertNotNull(library.getDictionaries());
    }
    
    @Test
    public void testGetDictionary() {
        Library library = new Library();
        assertNull(library.getDictionary("nonExistentVersion"));
        
        // Add dictionary and test retrieval
        DictionaryInfo dictionary = new DictionaryInfo("testVersion");
        library.dictionaries.put("testVersion", dictionary);
        assertEquals(dictionary, library.getDictionary("testVersion"));
    }
    
    // Edge case: Test loading directory with empty files
    @Test
    public void testLoadDirectory_EmptyFiles() {
        Library library = new Library();
        library.loadDirectory(new ArrayList<File>(), "emptyVersion");
        assertEquals(0, library.getDictionaries().size());
    }
    
    // Error scenario: Test loading directory with invalid files
    @Test
    public void testLoadDirectory_InvalidFiles() {
        Library library = new Library();
        List<File> invalidFiles = new ArrayList<File>();
        invalidFiles.add(new File("invalidFileName"));
        library.loadDirectory(invalidFiles, "invalidVersion");
        assertEquals(0, library.getDictionaries().size());
    }

    // Edge case: Test arrangeFiles method with different file names
    @Test
    public void testArrangeFiles() {
        Library library = new Library();
        File[] files = new File[] {new File("Fields.xml"), new File("Enums.xml"), new File("Components.xml")};
        assertEquals(3, library.arrangeFiles(files).size());
    }

}