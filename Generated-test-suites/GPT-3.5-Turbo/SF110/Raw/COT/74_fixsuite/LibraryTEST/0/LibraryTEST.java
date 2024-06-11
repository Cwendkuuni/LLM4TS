
import static org.junit.Assert.*;
import org.junit.Test;
import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo;

public class LibraryTest {
    
    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        boolean result = library.loadFromDirectory("path/to/library");
        assertTrue(result);
    }
    
    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
    }
    
    @Test
    public void testGetDictionary() {
        Library library = new Library();
        DictionaryInfo dictionary = library.getDictionary("1.0");
        assertNull(dictionary);
    }

}
