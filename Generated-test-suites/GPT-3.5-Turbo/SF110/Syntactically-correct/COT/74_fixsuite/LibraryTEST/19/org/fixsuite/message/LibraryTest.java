import static org.junit.Assert.*;
import org.junit.*;

public class LibraryTest {

    private Library library;
  
    @Before
    public void setUp() {
        library = new Library();
    }

    @Test
    public void testLoadFromDirectory_emptyDirectory_returnsFalse() {
        assertFalse(library.loadFromDirectory("empty_directory"));
    }

    @Test
    public void testLoadFromDirectory_nonExistentDirectory_returnsFalse() {
        assertFalse(library.loadFromDirectory("non_existent_directory"));
    }

    @Test
    public void testLoadFromDirectory_unsupportedFiles_returnsFalse() {
        assertFalse(library.loadFromDirectory("unsupported_files_directory"));
    }

    @Test
    public void testLoadFromDirectory_malformedXML_returnsFalse() {
        assertFalse(library.loadFromDirectory("malformed_xml_directory"));
    }

    @Test
    public void testLoadFromDirectory_multipleDictionaries_loadsCorrectNumber() {
        assertTrue(library.loadFromDirectory("multiple_dictionaries_directory"));
        assertEquals(2, library.getDictionaries().size());
    }

    @Test
    public void testGetDictionaries_emptyDictionaries_returnsEmptyList() {
        assertEquals(0, library.getDictionaries().size());
    }

    @Test
    public void testGetDictionaries_nonEmptyDictionaries_returnsNonEmptyList() {
        library.loadFromDirectory("non_empty_dictionaries_directory");
        assertEquals(2, library.getDictionaries().size());
    }

    @Test
    public void testGetDictionary_existingDictionary_returnsCorrectDictionary() {
        library.loadFromDirectory("existing_dictionaries_directory");
        DictionaryInfo dictionary = library.getDictionary("FIX.4.4");
        assertNotNull(dictionary);
        assertEquals("FIX.4.4", dictionary.getVersion());
    }

    @Test
    public void testGetDictionary_nonExistentDictionary_returnsNull() {
        library.loadFromDirectory("existing_dictionaries_directory");
        assertNull(library.getDictionary("NON_EXISTENT_VERSION"));
    }
}