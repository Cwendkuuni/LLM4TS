import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class LibraryTest {
    
    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        assertTrue(library.loadFromDirectory("path/to/library"));
    }
    
    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        assertTrue(library.loadFromDirectory("path/to/library"));
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertFalse(dictionaries.isEmpty());
    }
    
    @Test
    public void testGetDictionary() {
        Library library = new Library();
        assertTrue(library.loadFromDirectory("path/to/library"));
        DictionaryInfo dictionary = library.getDictionary("version");
        assertNotNull(dictionary);
    }
    
    // Additional test cases for edge cases and exception handling scenarios can be added here
    
}

