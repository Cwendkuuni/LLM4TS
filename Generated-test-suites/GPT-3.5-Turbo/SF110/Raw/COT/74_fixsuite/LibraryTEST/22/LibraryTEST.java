
import org.junit.Test;
import static org.junit.Assert.*;

public class LibraryTest {

    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        boolean result = library.loadFromDirectory("path/to/directory");
        assertTrue(result);
        // Test that dictionaries are loaded correctly
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertEquals(1, dictionaries.size());
        DictionaryInfo dictionary = dictionaries.get(0);
        assertEquals("version", dictionary.getVersion());
    }
    
    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        // Test that empty list is returned when no dictionaries loaded
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertTrue(dictionaries.isEmpty());
        
        // Test that list contains loaded dictionaries
        DictionaryInfo dictionary1 = new DictionaryInfo("version1");
        library.addDictionary(dictionary1);
        DictionaryInfo dictionary2 = new DictionaryInfo("version2");
        library.addDictionary(dictionary2);
        dictionaries = library.getDictionaries();
        assertEquals(2, dictionaries.size());
        assertTrue(dictionaries.contains(dictionary1));
        assertTrue(dictionaries.contains(dictionary2));
    }
    
    @Test
    public void testGetDictionary() {
        Library library = new Library();
        DictionaryInfo dictionary1 = new DictionaryInfo("version1");
        library.addDictionary(dictionary1);
        DictionaryInfo dictionary2 = new DictionaryInfo("version2");
        library.addDictionary(dictionary2);
        
        // Test that correct dictionary is retrieved
        DictionaryInfo retrievedDictionary1 = library.getDictionary("version1");
        assertEquals(dictionary1, retrievedDictionary1);
        
        DictionaryInfo retrievedDictionary2 = library.getDictionary("version2");
        assertEquals(dictionary2, retrievedDictionary2);
        
        // Test that null is returned when dictionary with given version is not present
        DictionaryInfo retrievedDictionary3 = library.getDictionary("version3");
        assertNull(retrievedDictionary3);
    }
}

