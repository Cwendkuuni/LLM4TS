import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;

public class LibraryTest {

    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        assertTrue(library.loadFromDirectory("test-directory"));
    }

    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        library.loadFromDirectory("test-directory");
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertFalse(dictionaries.isEmpty());
    }

    @Test
    public void testGetDictionary() {
        Library library = new Library();
        library.loadFromDirectory("test-directory");
        DictionaryInfo dictionary = library.getDictionary("version");
        assertNotNull(dictionary);
    }

    @Test
    public void testLoadDirectory() {
        Library library = new Library();
        List<File> files = new ArrayList<>();
        // Add files to test
        library.loadDirectory(files, "version");
        assertNotNull(library.getDictionary("version"));
    }

    @Test
    public void testArrangeFiles() {
        Library library = new Library();
        File[] files = new File[5];
        // Initialize files
        List<File> arrangedFiles = library.arrangeFiles(files);
        assertNotNull(arrangedFiles);
        assertEquals(5, arrangedFiles.size());
    }
}