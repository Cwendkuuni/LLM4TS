
import org.junit.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryTest {

    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        
        // Test loading from a valid directory
        boolean result = library.loadFromDirectory("path/to/valid/directory");
        assertTrue(result);
        
        // Test loading from an empty directory
        result = library.loadFromDirectory("path/to/empty/directory");
        assertFalse(result);
        
        // Test loading from a non-existent directory
        result = library.loadFromDirectory("path/to/non/existent/directory");
        assertFalse(result);
    }
    
    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        
        // Test getting dictionaries when none are present
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertTrue(dictionaries.isEmpty());
        
        // Test getting dictionaries when some are present
        DictionaryInfo dictionary1 = new DictionaryInfo("version1");
        DictionaryInfo dictionary2 = new DictionaryInfo("version2");
        library.dictionaries.put(dictionary1.getVersion(), dictionary1);
        library.dictionaries.put(dictionary2.getVersion(), dictionary2);
        dictionaries = library.getDictionaries();
        assertEquals(2, dictionaries.size());
    }
    
    @Test
    public void testGetDictionary() {
        Library library = new Library();
        
        // Test getting a dictionary when none are present
        DictionaryInfo dictionary = library.getDictionary("version");
        assertNull(dictionary);
        
        // Test getting a dictionary when it is present
        DictionaryInfo expected = new DictionaryInfo("version");
        library.dictionaries.put(expected.getVersion(), expected);
        dictionary = library.getDictionary("version");
        assertEquals(expected, dictionary);
        
        // Test getting a dictionary when a different version is requested
        dictionary = library.getDictionary("different version");
        assertNull(dictionary);
    }
    
    @Test
    public void testLoadDirectory() {
        Library library = new Library();
        
        // Test loading a directory with all valid files
        List<File> files = new ArrayList<>();
        files.add(new File("Fields.xml"));
        files.add(new File("Enums.xml"));
        files.add(new File("Components.xml"));
        files.add(new File("MsgType.xml"));
        files.add(new File("MsgContents.xml"));
        library.loadDirectory(files, "version");
        DictionaryInfo dictionary = library.getDictionary("version");
        assertNotNull(dictionary);
        
        // Test loading a directory with a missing file
        files.remove(new File("Components.xml"));
        library.loadDirectory(files, "version");
        dictionary = library.getDictionary("version");
        assertNull(dictionary);
        
        // Test loading a directory with all empty files
        files.add(new File("Components.xml"));
        files.add(new File("Fields.xml"));
        files.add(new File("Enums.xml"));
        files.add(new File("MsgType.xml"));
        files.add(new File("MsgContents.xml"));
        library.loadDirectory(files, "version");
        dictionary = library.getDictionary("version");
        assertNotNull(dictionary);
        assertFalse(dictionary.isLoaded());
    }
    
    @Test
    public void testArrangeFiles() {
        Library library = new Library();
        
        // Test arranging files in the correct order
        List<File> files = new ArrayList<>();
        files.add(new File("Fields.xml"));
        files.add(new File("Enums.xml"));
        files.add(new File("Components.xml"));
        files.add(new File("MsgType.xml"));
        files.add(new File("MsgContents.xml"));
        List<File> arrangedFiles = library.arrangeFiles(files);
        
        assertEquals("Fields.xml", arrangedFiles.get(0).getName());
        assertEquals("Enums.xml", arrangedFiles.get(1).getName());
        assertEquals("Components.xml", arrangedFiles.get(2).getName());
        assertEquals("MsgType.xml", arrangedFiles.get(3).getName());
        assertEquals("MsgContents.xml", arrangedFiles.get(4).getName());
        
        // Test arranging files when some files are missing
        files.remove(new File("Components.xml"));
        arrangedFiles = library.arrangeFiles(files);
        
        assertEquals(4, arrangedFiles.size());
        assertEquals("Fields.xml", arrangedFiles.get(0).getName());
        assertEquals("Enums.xml", arrangedFiles.get(1).getName());
        assertEquals("MsgType.xml", arrangedFiles.get(2).getName());
        assertEquals("MsgContents.xml", arrangedFiles.get(3).getName());
    }
}
