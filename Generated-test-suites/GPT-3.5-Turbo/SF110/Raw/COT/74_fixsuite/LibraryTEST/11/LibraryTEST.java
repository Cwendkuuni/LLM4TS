
import org.junit.Test;
import static org.junit.Assert.*;

public class LibraryTest {

    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        boolean result = library.loadFromDirectory("path/to/directory");
        assertTrue(result);
        
        // Test loading from an empty directory
        result = library.loadFromDirectory("empty/directory");
        assertFalse(result);
        
        // Test loading from a non-existent directory
        result = library.loadFromDirectory("nonexistent/directory");
        assertFalse(result);
    }

    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        
        // Test getting dictionaries after loading from a directory
        library.loadFromDirectory("path/to/directory");
        dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertEquals(1, dictionaries.size());
    }

    @Test
    public void testGetDictionary() {
        Library library = new Library();
        DictionaryInfo dictionary = library.getDictionary("1.0");
        assertNull(dictionary);
        
        // Test getting a dictionary after loading from a directory
        library.loadFromDirectory("path/to/directory");
        dictionary = library.getDictionary("1.0");
        assertNotNull(dictionary);
        assertEquals("1.0", dictionary.getVersion());
    }

}
