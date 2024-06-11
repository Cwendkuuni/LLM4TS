
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LibraryTest {
    
    private Library library;
    
    @Before
    public void setUp() {
        library = new Library();
    }
    
    @Test
    public void testLoadFromDirectory_withValidDirectory_shouldLoadDictionaries() {
        String libraryPath = "path/to/directory";
        
        boolean result = library.loadFromDirectory(libraryPath);
        
        assertTrue(result);
        assertTrue(library.getDictionaries().size() > 0);
    }
    
    @Test
    public void testLoadFromDirectory_withInvalidDirectory_shouldReturnFalse() {
        String libraryPath = "invalid/path/to/directory";
        
        boolean result = library.loadFromDirectory(libraryPath);
        
        assertFalse(result);
    }
    
    @Test
    public void testLoadFromDirectory_withEmptyDirectory_shouldReturnFalse() {
        String libraryPath = "path/to/empty/directory";
        
        boolean result = library.loadFromDirectory(libraryPath);
        
        assertFalse(result);
    }
    
    @Test
    public void testGetDictionaries_withLoadedDictionaries_shouldReturnListOfDictionaries() {
        String libraryPath = "path/to/directory";
        library.loadFromDirectory(libraryPath);
        
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        
        assertNotNull(dictionaries);
        assertTrue(dictionaries.size() > 0);
    }
    
    @Test
    public void testGetDictionaries_withNoLoadedDictionaries_shouldReturnEmptyList() {
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        
        assertNotNull(dictionaries);
        assertEquals(0, dictionaries.size());
    }
    
    @Test
    public void testGetDictionary_withExistingVersion_shouldReturnDictionaryInfo() {
        String libraryPath = "path/to/directory";
        library.loadFromDirectory(libraryPath);
        String version = "1.0";
        
        DictionaryInfo dictionary = library.getDictionary(version);
        
        assertNotNull(dictionary);
        assertEquals(version, dictionary.getVersion());
    }
    
    @Test
    public void testGetDictionary_withNonExistingVersion_shouldReturnNull() {
        String version = "2.0";
        
        DictionaryInfo dictionary = library.getDictionary(version);
        
        assertNull(dictionary);
    }
    
}
