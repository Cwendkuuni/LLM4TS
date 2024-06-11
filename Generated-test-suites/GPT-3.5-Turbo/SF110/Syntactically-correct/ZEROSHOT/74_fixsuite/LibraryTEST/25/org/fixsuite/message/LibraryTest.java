import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.Map;
import java.io.File;

public class LibraryTest {
    
    private Library library;
    
    @Before
    public void setUp() {
        library = new Library();
    }
    
    @Test
    public void testLoadFromDirectory() {
        boolean result = library.loadFromDirectory("testDirectory");
        assertTrue(result);
    }
    
    @Test
    public void testGetDictionaries() {
        library.loadFromDirectory("testDirectory");
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
    }
    
    @Test
    public void testGetDictionary() {
        library.loadFromDirectory("testDirectory");
        DictionaryInfo dictionary = library.getDictionary("version");
        assertNotNull(dictionary);
    }
    
    @Test
    public void testLoadDirectory() {
        List<File> files = new ArrayList<>();
        files.add(new File("file1"));
        files.add(new File("file2"));
        library.loadDirectory(files, "version");
        DictionaryInfo dictionary = library.getDictionary("version");
        assertNotNull(dictionary);
    }
    
    @Test
    public void testArrangeFiles() {
        File[] files = {new File("Fields.xml"), new File("Enums.xml"), new File("Components.xml"), 
                        new File("MsgType.xml"), new File("MsgContents.xml")};
        List<File> arrangedFiles = library.arrangeFiles(files);
        assertNotNull(arrangedFiles);
    }
    
}