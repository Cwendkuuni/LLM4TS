
import org.junit.Test;
import org.fixsuite.message.*;
import java.util.*;
import static org.junit.Assert.*;

public class LibraryTest {
  
    @Test
    public void testLoadFromDirectory() {
        // Test loading from valid directory
        Library library = new Library();
        boolean result = library.loadFromDirectory("path/to/valid/directory");
        assertTrue(result);
        
        // Test loading from invalid directory
        Library library2 = new Library();
        boolean result2 = library2.loadFromDirectory("path/to/invalid/directory");
        assertFalse(result2);
    }
    
    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        
        // Test if the returned list is empty when no dictionaries are loaded
        assertTrue(dictionaries.isEmpty());
        
        // Test if the returned list is not empty when dictionaries are loaded
        library.loadFromDirectory("path/to/valid/directory");
        dictionaries = library.getDictionaries();
        assertFalse(dictionaries.isEmpty());
    }
    
    @Test
    public void testGetDictionary() {
        Library library = new Library();
        DictionaryInfo dictionary = library.getDictionary("version");
        assertNull(dictionary);
        
        // Test if the returned dictionary is not null when it exists
        library.loadFromDirectory("path/to/valid/directory");
        dictionary = library.getDictionary("version");
        assertNotNull(dictionary);
    }
  
}
