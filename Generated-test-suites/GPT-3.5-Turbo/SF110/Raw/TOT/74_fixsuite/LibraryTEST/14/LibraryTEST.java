
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.util.List;
import static org.junit.Assert.*;

public class LibraryTest {
    
    private Library library;
    private String libraryPath;
    
    @Before
    public void setUp() {
        library = new Library();
        libraryPath = "path/to/library/files";
    }
    
    @Test
    public void testLoadFromDirectory_ValidDirectory_Success() {
        library.loadFromDirectory(libraryPath);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertFalse(dictionaries.isEmpty());
    }
    
    @Test
    public void testLoadFromDirectory_EmptyDirectory_Failure() {
        String emptyDirectoryPath = "path/to/empty/directory";
        boolean result = library.loadFromDirectory(emptyDirectoryPath);
        assertFalse(result);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertTrue(dictionaries.isEmpty());
    }
    
    @Test
    public void testLoadFromDirectory_NonExistentDirectory_Failure() {
        String nonExistentDirectoryPath = "path/to/nonexistent/directory";
        boolean result = library.loadFromDirectory(nonExistentDirectoryPath);
        assertFalse(result);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertTrue(dictionaries.isEmpty());
    }
    
    @Test
    public void testLoadFromDirectory_NonDirectoryPath_Failure() {
        String nonDirectoryPath = "path/to/nonDirectory";
        boolean result = library.loadFromDirectory(nonDirectoryPath);
        assertFalse(result);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertTrue(dictionaries.isEmpty());
    }
    
    @Test
    public void testGetDictionaries_NonEmptyDictionaries_Success() {
        library.loadFromDirectory(libraryPath);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertFalse(dictionaries.isEmpty());
        assertEquals(dictionaries.size(), 5);
    }
    
    @Test
    public void testGetDictionaries_EmptyDictionaries_Success() {
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertTrue(dictionaries.isEmpty());
    }
    
    @Test
    public void testGetDictionary_ExistingVersion_Success() {
        library.loadFromDirectory(libraryPath);
        String version = "FIX.4.4";
        DictionaryInfo dictionary = library.getDictionary(version);
        assertNotNull(dictionary);
        assertEquals(dictionary.getVersion(), version);
    }
    
    @Test
    public void testGetDictionary_NonexistentVersion_Success() {
        String version = "NonexistentVersion";
        DictionaryInfo dictionary = library.getDictionary(version);
        assertNull(dictionary);
    }
    
    @Test
    public void testLoadDirectory_ValidFiles_Success() {
        List<File> files = new ArrayList<File>();
        files.add(new File("path/to/Fields.xml"));
        files.add(new File("path/to/Enums.xml"));
        files.add(new File("path/to/Components.xml"));
        files.add(new File("path/to/MsgType.xml"));
        files.add(new File("path/to/MsgContents.xml"));
        String version = "FIX.4.4";
        
        library.loadDirectory(files, version);
        DictionaryInfo dictionary = library.getDictionary(version);
        
        assertNotNull(dictionary);
        assertEquals(dictionary.getVersion(), version);
        assertEquals(dictionary.getFiles().size(), 5);
    }
    
    @Test
    public void testLoadDirectory_InvalidFiles_Failure() {
        List<File> files = new ArrayList<File>();
        String version = "FIX.4.4";
        
        library.loadDirectory(files, version);
        DictionaryInfo dictionary = library.getDictionary(version);
        
        assertNull(dictionary);
    }
    
    @Test
    public void testArrangeFiles_StandardFiles_Success() {
        File fieldsFile = new File("path/to/Fields.xml");
        File enumsFile = new File("path/to/Enums.xml");
        File componentsFile = new File("path/to/Components.xml");
        File msgTypeFile = new File("path/to/MsgType.xml");
        File msgContentsFile = new File("path/to/MsgContents.xml");
        File[] files = {fieldsFile, enumsFile, componentsFile, msgTypeFile, msgContentsFile};
        
        List<File> arrangedFiles = library.arrangeFiles(files);
        
        assertNotNull(arrangedFiles);
        assertEquals(arrangedFiles.size(), 5);
        assertEquals(arrangedFiles.get(0).getName(), "Fields.xml");
        assertEquals(arrangedFiles.get(1).getName(), "Enums.xml");
        assertEquals(arrangedFiles.get(2).getName(), "Components.xml");
        assertEquals(arrangedFiles.get(3).getName(), "MsgType.xml");
        assertEquals(arrangedFiles.get(4).getName(), "MsgContents.xml");
    }
    
    @Test
    public void testArrangeFiles_MissingFiles_Success() {
        File fieldsFile = new File("path/to/Fields.xml");
        File enumsFile = new File("path/to/Enums.xml");
        File[] files = {fieldsFile, enumsFile};
        
        List<File> arrangedFiles = library.arrangeFiles(files);
        
        assertNotNull(arrangedFiles);
        assertEquals(arrangedFiles.size(), 2);
        assertEquals(arrangedFiles.get(0).getName(), "Fields.xml");
        assertEquals(arrangedFiles.get(1).getName(), "Enums.xml");
    }
}
