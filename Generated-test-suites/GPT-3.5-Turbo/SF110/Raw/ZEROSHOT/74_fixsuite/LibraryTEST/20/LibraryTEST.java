import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class LibraryTest {
    
    private Library library;
    
    @Before
    public void setUp() {
        library = new Library();
    }
    
    @Test
    public void testLoadFromDirectory() {
        assertTrue(library.loadFromDirectory("path/to/library"));
        assertFalse(library.loadFromDirectory("invalid/directory"));
    }
    
    @Test
    public void testGetDictionaries() {
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertTrue(dictionaries.isEmpty());
        
        library.loadFromDirectory("path/to/library");
        dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertFalse(dictionaries.isEmpty());
    }
    
    @Test
    public void testGetDictionary() {
        assertNull(library.getDictionary("1.0.0"));
        
        library.loadFromDirectory("path/to/library");
        assertNotNull(library.getDictionary("1.0.0"));
        assertNull(library.getDictionary("2.0.0"));
    }
}