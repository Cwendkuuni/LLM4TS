import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.io.File;

public class LibraryTest {
    
    private Library library;
    
    @Before
    public void setUp() {
        library = new Library();
    }
    
    @Test
    public void testLoadFromDirectory_validDirectory_success() {
        assertTrue(library.loadFromDirectory("directory_with_files"));
    }
    
    @Test
    public void testLoadFromDirectory_emptyDirectory_failure() {
        assertFalse(library.loadFromDirectory("empty_directory"));
    }
    
    @Test
    public void testLoadFromDirectory_invalidPath_failure() {
        assertFalse(library.loadFromDirectory("invalid_path"));
    }
    
    @Test
    public void testGetDictionaries_emptyDictionariesList_success() {
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertTrue(dictionaries.isEmpty());
    }
    
    @Test
    public void testGetDictionary_existingVersion_success() {
        library.loadFromDirectory("directory_with_files");
        DictionaryInfo dictionary = library.getDictionary("1.0");
        assertNotNull(dictionary);
        assertEquals("1.0", dictionary.getVersion());
    }
    
    @Test
    public void testGetDictionary_nonExistingVersion_success() {
        library.loadFromDirectory("directory_with_files");
        DictionaryInfo dictionary = library.getDictionary("2.0");
        assertNull(dictionary);
    }
    
    @Test
    public void testLoadDirectory_validFiles_success() {
        List<File> files = library.arrangeFiles(new File[] {
            new File("Fields.xml"),
            new File("Enums.xml"),
            new File("Components.xml"),
            new File("MsgType.xml"),
            new File("MsgContents.xml")
        });
        assertEquals(5, files.size());
    }
    
    @Test
    public void testLoadDirectory_missingFile_failure() {
        List<File> files = library.arrangeFiles(new File[] {
            new File("Fields.xml"),
            new File("Enums.xml"),
            new File("Components.xml"),
            new File("MsgContents.xml")
        });
        assertEquals(4, files.size());
    }
    
    @Test
    public void testLoadDirectory_invalidFileOrder_success() {
        List<File> files = library.arrangeFiles(new File[] {
            new File("Enums.xml"),
            new File("MsgContents.xml"),
            new File("Fields.xml"),
            new File("Components.xml"),
            new File("MsgType.xml")
        });
        assertEquals(5, files.size());
    }
    
}