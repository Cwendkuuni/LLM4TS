import org.fixsuite.message.info.*;
import java.io.*;
import java.text.*;
import org.fixsuite.message.parsers.fpl.*;
import java.util.*;
import org.slf4j.*;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class LibraryTest {
    
    private static Library library;
    
    @BeforeClass
    public static void setUp() {
        library = new Library();
    }
    
    @Test
    public void testLoadFromDirectory() {
        boolean result = library.loadFromDirectory("test_directory");
        assertTrue(result);
    }
    
    @Test
    public void testGetDictionaries() {
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
    }
    
    @Test
    public void testGetDictionary() {
        DictionaryInfo dictionary = library.getDictionary("FIX.4.4");
        assertNull(dictionary);
        
        library.loadFromDirectory("test_directory");
        dictionary = library.getDictionary("FIX.4.4");
        assertNotNull(dictionary);
        assertEquals("FIX.4.4", dictionary.getVersion());
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
        assertNotNull(arrangedFiles);
        assertEquals(5, arrangedFiles.size());
        
        assertEquals("Fields.xml", arrangedFiles.get(0).getName());
        assertEquals("Enums.xml", arrangedFiles.get(1).getName());
        assertEquals("Components.xml", arrangedFiles.get(2).getName());
        assertEquals("MsgType.xml", arrangedFiles.get(3).getName());
        assertEquals("MsgContents.xml", arrangedFiles.get(4).getName());
    }
    
    @Test
    public void testLoadDirectory() {
        List<File> files = new ArrayList<>();
        files.add(new File("Fields.xml"));
        
        library.loadDirectory(files, "FIX.4.4");
        DictionaryInfo dictionary = library.getDictionary("FIX.4.4");
        assertNull(dictionary);
        
        files.add(new File("Enums.xml"));
        files.add(new File("Components.xml"));
        files.add(new File("MsgType.xml"));
        files.add(new File("MsgContents.xml"));
        
        library.loadDirectory(files, "FIX.4.4");
        dictionary = library.getDictionary("FIX.4.4");
        assertNotNull(dictionary);
        assertEquals("FIX.4.4", dictionary.getVersion());
    }
}