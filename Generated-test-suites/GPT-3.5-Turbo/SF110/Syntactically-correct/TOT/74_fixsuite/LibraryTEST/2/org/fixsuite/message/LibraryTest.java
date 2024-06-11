import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.fixsuite.message.Library;
import org.fixsuite.message.DictionaryInfo;
import java.util.List;

public class LibraryTest {

    private Library library;
    
    @Before
    public void setUp() {
        library = new Library();
    }
    
    @Test
    public void testLoadFromDirectory_typical() {
        String libraryPath = "C:/FixLibraries/";
        assertTrue(library.loadFromDirectory(libraryPath));
    }
    
    @Test
    public void testLoadFromDirectory_emptyDirectory() {
        String libraryPath = "C:/EmptyLibrary/";
        assertFalse(library.loadFromDirectory(libraryPath));
    }
    
    @Test
    public void testLoadFromDirectory_notADirectory() {
        String libraryPath = "C:/FixLibrary/Library.jar";
        assertFalse(library.loadFromDirectory(libraryPath));
    }
    
    @Test
    public void testGetDictionaries_typical() {
        library.loadFromDirectory("C:/FixLibraries/");
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertFalse(dictionaries.isEmpty());
    }
    
    @Test
    public void testGetDictionary_typical() {
        library.loadFromDirectory("C:/FixLibraries/");
        String version = "FIX.4.4";
        DictionaryInfo dictionary = library.getDictionary(version);
        assertNotNull(dictionary);
        assertEquals(version, dictionary.getVersion());
    }
    
    @Test
    public void testGetDictionary_nonExistingVersion() {
        library.loadFromDirectory("C:/FixLibraries/");
        String version = "FIX.5.0";
        DictionaryInfo dictionary = library.getDictionary(version);
        assertNull(dictionary);
    }
    
    // Edge case: Dictionary version with special characters
    @Test
    public void testGetDictionary_specialCharacters() {
        library.loadFromDirectory("C:/FixLibraries/");
        String version = "@!#%&";
        DictionaryInfo dictionary = library.getDictionary(version);
        assertNull(dictionary);
    }
    
    // Error scenario: Library not loaded, dictionaries should be empty
    @Test
    public void testGetDictionaries_notLoaded() {
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertTrue(dictionaries.isEmpty());
    }
    
    // Error scenario: Library not loaded, dictionary should be null
    @Test
    public void testGetDictionary_notLoaded() {
        String version = "FIX.4.4";
        DictionaryInfo dictionary = library.getDictionary(version);
        assertNull(dictionary);
    }
    
    // Edge case: Directory with only one of the required files
    @Test
    public void testLoadDirectory_partialFiles() {
        String libraryPath = "C:/PartialLibrary/";
        assertFalse(library.loadFromDirectory(libraryPath));
    }
    
    // Error Scenario: Directory does not exist
    @Test
    public void testLoadFromDirectory_directoryNotExists() {
        String libraryPath = "C:/NonExistingLibrary/";
        assertFalse(library.loadFromDirectory(libraryPath));
    }
}