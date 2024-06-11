import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class LibraryTest {

    Library library;

    @Before
    public void setUp() {
        library = new Library();
    }

    @Test
    public void testLoadFromDirectory() {
        // Typical use case
        assertTrue(library.loadFromDirectory("path/to/directory"));
        
        // Edge case: Empty directory
        assertFalse(library.loadFromDirectory("empty/directory"));
        
        // Edge case: Non-existing directory
        assertFalse(library.loadFromDirectory("nonexisting/directory"));
        
        // Error scenario: Invalid directory path
        assertFalse(library.loadFromDirectory("invalid_path"));
    }

    @Test
    public void testGetDictionaries() {
        // Typical use case
        assertNotNull(library.getDictionaries());
        
        // Edge case: No dictionaries loaded
        assertEquals(0, library.getDictionaries().size());
    }

    @Test
    public void testGetDictionary() {
        final String version = "1.0";
        
        // Edge case: Dictionary not found
        assertNull(library.getDictionary(version));
        
        // Typical use case
        DictionaryInfo info = new DictionaryInfo(version);
        library.dictionaries.put(version, info);
        assertEquals(info, library.getDictionary(version));
    }

    @Test
    public void testArrangeFiles() {
        // Typical use case
        List<File> files = new ArrayList<>();
        files.add(new File("Fields.xml"));
        files.add(new File("Enums.xml"));
        List<File> arrangedFiles = library.arrangeFiles(files.toArray(new File[0]));
        assertEquals(files.size(), arrangedFiles.size());
        assertEquals("Fields.xml", arrangedFiles.get(0).getName());
        assertEquals("Enums.xml", arrangedFiles.get(1).getName());
        
        // Edge case: Unordered files
        files.add(new File("MsgContents.xml"));
        arrangedFiles = library.arrangeFiles(files.toArray(new File[0]));
        assertEquals("Fields.xml", arrangedFiles.get(0).getName());
        assertEquals("Enums.xml", arrangedFiles.get(1).getName());
        assertEquals("MsgContents.xml", arrangedFiles.get(2).getName());
    }

    // Additional test cases can be added for edge cases and error scenarios

}