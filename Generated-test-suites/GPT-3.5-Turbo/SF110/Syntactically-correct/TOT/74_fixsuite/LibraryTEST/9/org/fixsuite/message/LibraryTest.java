import org.junit.*;
import static org.junit.Assert.*;

public class LibraryTest {

    private Library library;

    @Before
    public void setUp() {
        library = new Library();
    }

    @Test
    public void testLoadFromDirectory_typical() {
        assertTrue(library.loadFromDirectory("path/to/directory"));
        assertEquals(0, library.getDictionaries().size());
    }

    @Test
    public void testLoadFromDirectory_emptyDirectory() {
        assertFalse(library.loadFromDirectory("path/to/emptyDirectory"));
        assertEquals(0, library.getDictionaries().size());
    }

    @Test
    public void testLoadFromDirectory_invalidPath() {
        assertFalse(library.loadFromDirectory("invalid/path"));
        assertEquals(0, library.getDictionaries().size());
    }

    @Test
    public void testGetDictionaries_typical() {
        DictionaryInfo dictionary1 = new DictionaryInfo("version1");
        DictionaryInfo dictionary2 = new DictionaryInfo("version2");
        library.getDictionaries().add(dictionary1);
        library.getDictionaries().add(dictionary2);

        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertEquals(2, dictionaries.size());
        assertTrue(dictionaries.contains(dictionary1));
        assertTrue(dictionaries.contains(dictionary2));
    }

    @Test
    public void testGetDictionary_existingVersion() {
        DictionaryInfo dictionary = new DictionaryInfo("version1");
        library.getDictionaries().add(dictionary);

        DictionaryInfo result = library.getDictionary("version1");
        assertNotNull(result);
        assertEquals(dictionary, result);
    }

    @Test
    public void testGetDictionary_nonExistingVersion() {
        assertNull(library.getDictionary("nonExistingVersion"));
    }

    @Test
    public void testLoadDirectory_typical() {
        List<File> files = new ArrayList<File>();
        files.add(new File("path/to/Fields.xml"));
        files.add(new File("path/to/Enums.xml"));
        files.add(new File("path/to/Components.xml"));
        files.add(new File("path/to/MsgType.xml"));
        files.add(new File("path/to/MsgContents.xml"));

        library.loadDirectory(files, "version1");

        DictionaryInfo dictionary = library.getDictionary("version1");
        assertNotNull(dictionary);
        assertTrue(dictionary.isLoaded());
    }

    @Test
    public void testLoadDirectory_missingFiles() {
        List<File> files = new ArrayList<File>();
        files.add(new File("path/to/Fields.xml"));

        library.loadDirectory(files, "version1");

        DictionaryInfo dictionary = library.getDictionary("version1");
        assertNotNull(dictionary);
        assertFalse(dictionary.isLoaded());
    }

    @Test
    public void testArrangeFiles_typical() {
        File[] files = new File[5];
        files[0] = new File("path/to/Fields.xml");
        files[1] = new File("path/to/Enums.xml");
        files[2] = new File("path/to/Components.xml");
        files[3] = new File("path/to/MsgType.xml");
        files[4] = new File("path/to/MsgContents.xml");

        List<File> arrangedFiles = library.arrangeFiles(files);

        assertEquals(5, arrangedFiles.size());
        assertEquals(files[0], arrangedFiles.get(0));
        assertEquals(files[1], arrangedFiles.get(1));
        assertEquals(files[2], arrangedFiles.get(2));
        assertEquals(files[3], arrangedFiles.get(3));
        assertEquals(files[4], arrangedFiles.get(4));
    }

    @Test
    public void testArrangeFiles_missingFiles() {
        File[] files = new File[2];
        files[0] = new File("path/to/Fields.xml");
        files[1] = new File("path/to/Components.xml");

        List<File> arrangedFiles = library.arrangeFiles(files);

        assertEquals(2, arrangedFiles.size());
        assertEquals(files[0], arrangedFiles.get(0));
        assertEquals(files[1], arrangedFiles.get(1));
    }
}