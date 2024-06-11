
import org.junit.Test;
import static org.junit.Assert.*;

public class LibraryTest {

    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        assertTrue(library.loadFromDirectory("path/to/library"));
        // Additional assertions for edge cases and exception handling scenarios
    }

    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        // Additional assertions for edge cases and exception handling scenarios
    }

    @Test
    public void testGetDictionary() {
        Library library = new Library();
        DictionaryInfo dictionary = library.getDictionary("version");
        assertNull(dictionary);
        // Additional assertions for edge cases and exception handling scenarios
    }

    // Additional test cases for other public methods

    // Test case for private method "loadDirectory" can be omitted as it is already covered indirectly

    // Test case for private method "arrangeFiles" can be omitted as it is already covered indirectly

}