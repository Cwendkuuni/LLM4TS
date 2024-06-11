import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.parsers.fpl.MainParser;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class LibraryTest {
    
    private Library library;
    
    @Before
    public void setUp() {
        library = new Library();
    }
    
    @Test
    public void testLoadFromDirectory() {
        boolean result = library.loadFromDirectory("path/to/directory");
        assertTrue(result);
    }
    
    @Test
    public void testGetDictionaries() {
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
    }
    
    @Test
    public void testGetDictionary() {
        DictionaryInfo dictionary = library.getDictionary("version");
        assertNull(dictionary);
    }
    
    @Test
    public void testLoadDirectory() {
        List<File> files = new ArrayList<>();
        File file1 = new File("Fields.xml");
        File file2 = new File("Enums.xml");
        File file3 = new File("Components.xml");
        files.add(file1);
        files.add(file2);
        files.add(file3);
        
        library.loadDirectory(files, "version");
        
        DictionaryInfo dictionary = library.getDictionary("version");
        assertNotNull(dictionary);
        assertTrue(dictionary.isLoaded());
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
        
        assertEquals("Fields.xml", arrangedFiles.get(0).getName());
        assertEquals("Enums.xml", arrangedFiles.get(1).getName());
        assertEquals("Components.xml", arrangedFiles.get(2).getName());
        assertEquals("MsgType.xml", arrangedFiles.get(3).getName());
        assertEquals("MsgContents.xml", arrangedFiles.get(4).getName());
    }
}