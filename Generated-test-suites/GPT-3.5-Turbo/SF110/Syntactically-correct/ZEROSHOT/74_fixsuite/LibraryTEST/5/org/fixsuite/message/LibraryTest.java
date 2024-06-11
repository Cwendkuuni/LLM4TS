import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.util.*;
import org.fixsuite.message.info.DictionaryInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LibraryTest {
    
    private Library library;
    
    @Before
    public void setUp() {
        library = new Library();
    }
    
    @Test
    public void testLoadFromDirectory() {
        assertFalse(library.loadFromDirectory(null));
        assertFalse(library.loadFromDirectory(""));
        assertFalse(library.loadFromDirectory("InvalidPath"));
        assertTrue(library.loadFromDirectory("ValidPath"));
    }
    
    @Test
    public void testGetDictionaries() {
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertEquals(0, dictionaries.size());
        
        library.loadFromDirectory("ValidPath");
        dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertTrue(dictionaries.size() > 0);
    }
    
    @Test
    public void testGetDictionary() {
        DictionaryInfo dictionary = library.getDictionary("InvalidVersion");
        assertNull(dictionary);
        
        library.loadFromDirectory("ValidPath");
        dictionary = library.getDictionary("ValidVersion");
        assertNotNull(dictionary);
        assertEquals("ValidVersion", dictionary.getVersion());
    }
    
    @Test
    public void testLoadDirectory() throws FileNotFoundException {
        List<File> files = new ArrayList<>();
        String version = "ValidVersion";
        library.loadDirectory(files, version);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertEquals(0, dictionaries.size());
        
        files.add(new File("Fields.xml"));
        library.loadDirectory(files, version);
        dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertTrue(dictionaries.size() > 0);
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