import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import java.io.File;

public class LibraryTest {
    
    @Test
    public void testLibraryConstructor() {
        Library library = new Library();
        assertNotNull(library);
        assertEquals(0, library.getDictionaries().size());
    }
    
    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        assertTrue(library.loadFromDirectory("testDirectory"));
    }
    
    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        assertTrue(library.loadFromDirectory("testDirectory"));
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertEquals(5, dictionaries.size());
    }
    
    @Test
    public void testGetDictionary() {
        Library library = new Library();
        assertTrue(library.loadFromDirectory("testDirectory"));
        DictionaryInfo dictionary = library.getDictionary("1.0");
        assertNotNull(dictionary);
        assertEquals("1.0", dictionary.getVersion());
    }
    
    @Test
    public void testLoadDirectory() {
        Library library = new Library();
        List<File> files = new ArrayList<File>();
        files.add(new File("testFile1.xml"));
        files.add(new File("testFile2.xml"));
        library.loadDirectory(files, "1.0");
        assertNotNull(library.getDictionary("1.0"));
    }
    
    @Test
    public void testArrangeFiles() {
        Library library = new Library();
        File[] files = new File[5];
        files[0] = new File("Fields.xml");
        files[1] = new File("Enums.xml");
        files[2] = new File("Components.xml");
        files[3] = new File("MsgType.xml");
        files[4] = new File("MsgContents.xml");
        List<File> arrangedFiles = library.arrangeFiles(files);
        assertEquals(5, arrangedFiles.size());
    }
}