import org.junit.Before;
import org.junit.Test;
import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class LibraryTest {
    
    private Library library;
    
    @Before
    public void setUp() {
        library = new Library();
    }
    
    @Test
    public void testLoadFromDirectory_typical() {
        String libraryPath = "path/to/directory";
        
        assertTrue(library.loadFromDirectory(libraryPath));
        
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        
        assertEquals(5, dictionaries.size());
        
        DictionaryInfo fieldsDictionary = library.getDictionary("Fields");
        assertNotNull(fieldsDictionary);
        
        DictionaryInfo enumsDictionary = library.getDictionary("Enums");
        assertNotNull(enumsDictionary);
        
        DictionaryInfo componentsDictionary = library.getDictionary("Components");
        assertNotNull(componentsDictionary);
        
        DictionaryInfo msgTypeDictionary = library.getDictionary("MsgType");
        assertNotNull(msgTypeDictionary);
        
        DictionaryInfo msgContentsDictionary = library.getDictionary("MsgContents");
        assertNotNull(msgContentsDictionary);
    }
    
    @Test
    public void testLoadFromDirectory_emptyDirectory() {
        String libraryPath = "path/to/empty/directory";
        
        assertFalse(library.loadFromDirectory(libraryPath));
        
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        
        assertEquals(0, dictionaries.size());
    }
    
    @Test
    public void testLoadFromDirectory_invalidPath() {
        String libraryPath = "path/to/invalid/directory";
        
        assertFalse(library.loadFromDirectory(libraryPath));
        
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        
        assertEquals(0, dictionaries.size());
    }
    
    @Test
    public void testGetDictionaries() {
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        
        assertEquals(0, dictionaries.size());
    }
    
    @Test
    public void testGetDictionary_existingVersion() {
        String version = "Version1";
        DictionaryInfo dictionaryInfo = new DictionaryInfo(version);
        library.getDictionaries().add(dictionaryInfo);
        
        DictionaryInfo retrievedDictionary = library.getDictionary(version);
        
        assertNotNull(retrievedDictionary);
        assertEquals(dictionaryInfo, retrievedDictionary);
    }
    
    @Test
    public void testGetDictionary_nonExistingVersion() {
        String version = "Version1";
        DictionaryInfo dictionaryInfo = new DictionaryInfo(version);
        library.getDictionaries().add(dictionaryInfo);
        
        DictionaryInfo retrievedDictionary = library.getDictionary("Version2");
        
        assertNull(retrievedDictionary);
    }
    
    @Test
    public void testArrangeFiles() {
        File[] files = new File[5];
        files[0] = new File("Fields.xml");
        files[1] = new File("Enums.xml");
        files[2] = new File("Components.xml");
        files[3] = new File("MsgType.xml");
        files[4] = new File("MsgContents.xml");
        
        List<File> arrangedFiles = library.arrangeFiles(files);
        
        assertEquals(5, arrangedFiles.size());
        assertEquals(files[0], arrangedFiles.get(0));
        assertEquals(files[1], arrangedFiles.get(1));
        assertEquals(files[2], arrangedFiles.get(2));
        assertEquals(files[3], arrangedFiles.get(3));
        assertEquals(files[4], arrangedFiles.get(4));
    }
}