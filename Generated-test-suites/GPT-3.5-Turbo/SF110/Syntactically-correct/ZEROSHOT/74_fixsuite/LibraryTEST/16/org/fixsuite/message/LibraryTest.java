import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.parsers.fpl.MainParser;

public class LibraryTest {
    
    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        boolean result = library.loadFromDirectory("path/to/directory");
        assertTrue(result);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertFalse(dictionaries.isEmpty());
    }
    
    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertTrue(dictionaries.isEmpty());
    }
    
    @Test
    public void testGetDictionary() {
        Library library = new Library();
        DictionaryInfo dictionary = library.getDictionary("1.0");
        assertNull(dictionary);
    }
    
    @Test
    public void testLoadDirectory() {
        Library library = new Library();
        List<File> files = new ArrayList<File>();
        // add files to the list
        String version = "1.0";
        library.loadDirectory(files, version);
        DictionaryInfo dictionary = library.getDictionary(version);
        assertNotNull(dictionary);
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
    }
}