import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class LibraryTest {
    
    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        assertTrue(library.loadFromDirectory("/path/to/directory"));
    }
    
    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertEquals(0, dictionaries.size());
    }
    
    @Test
    public void testGetDictionary() {
        Library library = new Library();
        DictionaryInfo dictionary = library.getDictionary("1.0");
        assertNull(dictionary);
    }

    @Test
    public void testArrangedFiles() {
        Library library = new Library();
        List<File> files = library.arrangeFiles(new File[]{new File("Enums.xml"), new File("MsgType.xml"), new File("Fields.xml")});
        assertNotNull(files);
        assertEquals(3, files.size());
    }
}