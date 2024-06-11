import org.junit.*;
import static org.junit.Assert.*;
import java.io.File;
import java.util.List;


public class LibraryTest {

    Library library;

    @Before
    public void setUp() {
        library = new Library();
    }

    @Test
    public void testLoadFromDirectory() {
        assertTrue(library.loadFromDirectory("testDirectory"));
    }

    @Test
    public void testGetDictionaries() {
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertEquals(0, dictionaries.size());
    }

    @Test
    public void testGetDictionary() {
        assertNull(library.getDictionary("invalidVersion"));
    }

    @Test
    public void testLoadDirectory() {
        List<File> files = library.arrangeFiles(new File("testDirectory").listFiles());
        library.loadDirectory(files, "testVersion");
        assertFalse(library.getDictionaries().isEmpty());
    }

    @Test
    public void testArrangeFiles() {
        List<File> files = library.arrangeFiles(new File("testDirectory").listFiles());
        assertNotNull(files);
        assertFalse(files.isEmpty());
    }
}