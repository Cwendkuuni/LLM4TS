
import org.junit.Before;
import org.junit.Test;
import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo;

import java.io.File;
import java.util.List;
import static org.junit.Assert.*;

public class LibraryTest {

    private Library library;

    @Before
    public void setUp() {
        library = new Library();
    }

    @Test
    public void testLoadFromDirectory_ValidPath_Success() {
        boolean result = library.loadFromDirectory("path/to/valid/directory");
        assertTrue(result);
        
        // Ensure dictionaries are loaded
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertTrue(dictionaries.size() > 0);
    }
    
    @Test
    public void testLoadFromDirectory_EmptyDirectory_Failure() {
        boolean result = library.loadFromDirectory("path/to/empty/directory");
        assertFalse(result);
        
        // Ensure no dictionaries are loaded
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertTrue(dictionaries.isEmpty());
    }
    
    @Test
    public void testLoadFromDirectory_InvalidPath_Failure() {
        boolean result = library.loadFromDirectory("path/to/non/existent/directory");
        assertFalse(result);
        
        // Ensure no dictionaries are loaded
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertTrue(dictionaries.isEmpty());
    }
    
    @Test
    public void testGetDictionaries_NotEmpty_Success() {
        // Manually load dictionaries for testing
        library.loadFromDirectory("path/to/directory/with/dictionaries");
        
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertTrue(dictionaries.size() > 0);
    }
    
    @Test
    public void testGetDictionaries_Empty_Success() {
        // Ensure no dictionaries are loaded
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertTrue(dictionaries.isEmpty());
    }
    
    @Test
    public void testGetDictionary_ValidVersion_Success() {
        // Manually load dictionaries for testing
        library.loadFromDirectory("path/to/directory/with/dictionaries");
        
        DictionaryInfo dictionaryInfo = library.getDictionary("1.0");
        assertNotNull(dictionaryInfo);
        assertEquals("1.0", dictionaryInfo.getVersion());
    }
    
    @Test
    public void testGetDictionary_InvalidVersion_ReturnNull() {
        // Manually load dictionaries for testing
        library.loadFromDirectory("path/to/directory/with/dictionaries");
        
        DictionaryInfo dictionaryInfo = library.getDictionary("2.0");
        assertNull(dictionaryInfo);
    }
    
    // Testing private methods is not possible in JUnit, so we are skipping the loadDirectory() and arrangeFiles() methods.

}
