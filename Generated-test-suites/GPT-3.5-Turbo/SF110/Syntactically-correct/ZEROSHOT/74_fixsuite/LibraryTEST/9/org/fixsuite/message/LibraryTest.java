import org.junit.*;
import java.util.*;
import static org.junit.Assert.*;

public class LibraryTest {


    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        
        // Test with valid directory
        boolean result1 = library.loadFromDirectory("/path/to/directory");
        assertTrue(result1);
        
        // Test with empty directory
        boolean result2 = library.loadFromDirectory("/path/to/empty/directory");
        assertFalse(result2);
        
        // Test with invalid directory
        boolean result3 = library.loadFromDirectory("/path/to/invalid/directory");
        assertFalse(result3);
    }

    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        
        // Test with empty dictionary
        List<DictionaryInfo> dictionaries1 = library.getDictionaries();
        assertTrue(dictionaries1.isEmpty());
        
        // Test with dictionaries
        DictionaryInfo dictionary1 = new DictionaryInfo("version1");
        library.getDictionary().put("version1", dictionary1);
        DictionaryInfo dictionary2 = new DictionaryInfo("version2");
        library.getDictionary().put("version2", dictionary2);
        List<DictionaryInfo> dictionaries2 = library.getDictionaries();
        assertEquals(2, dictionaries2.size());
        assertTrue(dictionaries2.contains(dictionary1));
        assertTrue(dictionaries2.contains(dictionary2));
    }

    @Test
    public void testGetDictionary() {
        Library library = new Library();
        
        // Test with empty dictionary
        DictionaryInfo dictionary1 = library.getDictionary("version1");
        assertNull(dictionary1);
        
        // Test with existing dictionary
        DictionaryInfo dictionary2 = new DictionaryInfo("version2");
        library.getDictionary().put("version2", dictionary2);
        DictionaryInfo dictionary3 = library.getDictionary("version2");
        assertSame(dictionary2, dictionary3);
    }
}