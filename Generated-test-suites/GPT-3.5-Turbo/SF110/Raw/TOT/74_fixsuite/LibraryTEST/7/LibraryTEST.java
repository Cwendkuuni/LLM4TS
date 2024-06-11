import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LibraryTest {

    private Library library;
    
    @Before
    public void setUp() {
        library = new Library();
    }
    
    @Test
    public void testLoadFromDirectory_emptyDirectory() {
        String libraryPath = "path/to/empty/directory";
        
        assertFalse(library.loadFromDirectory(libraryPath));
    }
    
    @Test
    public void testLoadFromDirectory_notDirectory() {
        String libraryPath = "path/to/non/directory/file.txt";
        
        assertFalse(library.loadFromDirectory(libraryPath));
    }
    
    @Test
    public void testLoadFromDirectory_successful() {
        String libraryPath = "path/to/directory/with/files";
        
        assertTrue(library.loadFromDirectory(libraryPath));
        
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertEquals(5, dictionaries.size());
        
        DictionaryInfo dictionary = library.getDictionary("version");
        assertNotNull(dictionary);
        assertEquals("version", dictionary.getVersion());
    }
    
    @Test
    public void testGetDictionaries_empty() {
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        
        assertNotNull(dictionaries);
        assertEquals(0, dictionaries.size());
    }
    
    @Test
    public void testGetDictionary_notFound() {
        DictionaryInfo dictionary = library.getDictionary("nonexistent version");
        
        assertNull(dictionary);
    }
    
    @Test
    public void testGetDictionary_found() {
        library.loadFromDirectory("path/to/directory/with/files");
        
        DictionaryInfo dictionary = library.getDictionary("version");
        
        assertNotNull(dictionary);
        assertEquals("version", dictionary.getVersion());
    }
    
    @Test
    public void testArrangeFiles_fields() {
        File[] files = {new File("Fields.xml")};
        
        List<File> arrangedFiles = library.arrangeFiles(files);
        
        assertEquals(1, arrangedFiles.size());
        assertEquals(1, arrangedFiles.get(0).getName());
    }
    
    @Test
    public void testArrangeFiles_enums() {
        File[] files = {new File("Enums.xml")};
        
        List<File> arrangedFiles = library.arrangeFiles(files);
        
        assertEquals(1, arrangedFiles.size());
        assertEquals(2, arrangedFiles.get(0).getName());
    }
    
    @Test
    public void testArrangeFiles_components() {
        File[] files = {new File("Components.xml")};
        
        List<File> arrangedFiles = library.arrangeFiles(files);
        
        assertEquals(1, arrangedFiles.size());
        assertEquals(3, arrangedFiles.get(0).getName());
    }
    
    @Test
    public void testArrangeFiles_msgType() {
        File[] files = {new File("MsgType.xml")};
        
        List<File> arrangedFiles = library.arrangeFiles(files);
        
        assertEquals(1, arrangedFiles.size());
        assertEquals(4, arrangedFiles.get(0).getName());
    }
    
    @Test
    public void testArrangeFiles_msgContents() {
        File[] files = {new File("MsgContents.xml")};
        
        List<File> arrangedFiles = library.arrangeFiles(files);
        
        assertEquals(1, arrangedFiles.size());
        assertEquals(5, arrangedFiles.get(0).getName());
    }
}