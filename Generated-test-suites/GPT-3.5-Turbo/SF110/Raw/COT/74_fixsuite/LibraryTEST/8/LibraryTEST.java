
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class LibraryTest {
    
    @Test
    public void testLoadFromDirectory_emptyDirectory() {
        Library library = new Library();
        
        boolean result = library.loadFromDirectory("path/to/empty/directory");
        
        assertFalse(result);
    }
    
    @Test
    public void testLoadFromDirectory_notADirectory() {
        Library library = new Library();
        
        boolean result = library.loadFromDirectory("path/to/file.txt");
        
        assertFalse(result);
    }
    
    @Test
    public void testLoadFromDirectory_success() {
        Library library = new Library();
        
        boolean result = library.loadFromDirectory("path/to/directory");
        
        assertTrue(result);
        assertNotNull(library.getDictionaries());
        assertEquals(1, library.getDictionaries().size());
    }
    
    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        library.loadFromDirectory("path/to/directory");
        
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        
        assertNotNull(dictionaries);
        assertEquals(1, dictionaries.size());
    }
    
    @Test
    public void testGetDictionary() {
        Library library = new Library();
        library.loadFromDirectory("path/to/directory");
        
        DictionaryInfo dictionary = library.getDictionary("version");
        
        assertNotNull(dictionary);
        assertEquals("version", dictionary.getVersion());
    }
    
    @Test
    public void testLoadDirectory_noFiles() {
        Library library = new Library();
        
        List<File> files = new ArrayList<>();
        library.loadDirectory(files, "version");
        
        assertTrue(library.getDictionaries().isEmpty());
    }
    
    @Test
    public void testLoadDirectory_success() {
        Library library = new Library();
        
        List<File> files = new ArrayList<>();
        files.add(new File("path/to/Fields.xml"));
        files.add(new File("path/to/Enums.xml"));
        library.loadDirectory(files, "version");
        
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        
        assertNotNull(dictionaries);
        assertEquals(1, dictionaries.size());
        assertNotNull(library.getDictionary("version"));
    }
    
    @Test
    public void testArrangeFiles() {
        Library library = new Library();
        File[] files = new File[] {
            new File("Fields.xml"),
            new File("Enums.xml"),
            new File("Components.xml"),
            new File("MsgType.xml"),
            new File("MsgContents.xml")
        };
        
        List<File> arrangedFiles = library.arrangeFiles(files);
        
        assertNotNull(arrangedFiles);
        assertEquals(5, arrangedFiles.size());
        assertEquals("Fields.xml", arrangedFiles.get(0).getName());
        assertEquals("Enums.xml", arrangedFiles.get(1).getName());
        assertEquals("Components.xml", arrangedFiles.get(2).getName());
        assertEquals("MsgType.xml", arrangedFiles.get(3).getName());
        assertEquals("MsgContents.xml", arrangedFiles.get(4).getName());
    }
}