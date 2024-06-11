import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.util.List;
import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo;

public class LibraryTest {

    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        assertTrue(library.loadFromDirectory("test_directory"));
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
    public void testLoadDirectory() {
        Library library = new Library();
        List<File> files = new ArrayList<>();
        File file1 = new File("test_file1.xml");
        File file2 = new File("test_file2.xml");
        files.add(file1);
        files.add(file2);
        library.loadDirectory(files, "test_version");
        assertNotNull(library.getDictionary("test_version"));
    }

    @Test
    public void testArrangeFiles() {
        Library library = new Library();
        List<File> files = new ArrayList<>();
        File file1 = new File("Fields.xml");
        File file2 = new File("Enums.xml");
        files.add(file1);
        files.add(file2);
        List<File> arrangedFiles = library.arrangeFiles(files.toArray(new File[0]));
        assertEquals(2, arrangedFiles.size());
        assertEquals("Fields.xml", arrangedFiles.get(0).getName());
        assertEquals("Enums.xml", arrangedFiles.get(1).getName());
    }
}