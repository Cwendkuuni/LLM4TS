import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import java.util.List;

public class LibraryTest {

    private Library library;

    @Before
    public void setUp() {
        library = new Library();
    }

    @Test
    public void testLoadFromDirectory() {
        assertFalse(library.loadFromDirectory(null));
        assertFalse(library.loadFromDirectory(""));
        assertTrue(library.loadFromDirectory("path/to/directory"));
    }

    @Test
    public void testGetDictionaries() {
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertEquals(0, dictionaries.size());

        library.loadFromDirectory("path/to/directory");
        dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertEquals(1, dictionaries.size());
    }

    @Test
    public void testGetDictionary() {
        assertNull(library.getDictionary("version"));

        library.loadFromDirectory("path/to/directory");
        assertNotNull(library.getDictionary("version"));
    }

    @Test
    public void testLoadDirectory() {
        List<File> files = new ArrayList<>();
        files.add(new File("path/to/file1.xml"));
        files.add(new File("path/to/file2.xml"));

        library.loadDirectory(files, "version");
        assertNotNull(library.getDictionary("version"));
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
}