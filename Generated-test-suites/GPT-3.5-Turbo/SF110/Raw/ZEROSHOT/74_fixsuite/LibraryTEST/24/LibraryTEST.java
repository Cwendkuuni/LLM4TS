import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.parsers.fpl.MainParser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LibraryTest {

    private Library library;

    @Before
    public void setUp() {
        library = new Library();
    }

    @Test
    public void testLoadFromDirectory() {
        boolean result = library.loadFromDirectory("path/to/library");
        assertTrue(result);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertFalse(dictionaries.isEmpty());
    }

    @Test
    public void testGetDictionaries() {
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertTrue(dictionaries.isEmpty());

        library.loadFromDirectory("path/to/library");
        dictionaries = library.getDictionaries();
        assertFalse(dictionaries.isEmpty());
    }

    @Test
    public void testGetDictionary() {
        DictionaryInfo dictionary = library.getDictionary("version");
        assertNull(dictionary);

        library.loadFromDirectory("path/to/library");
        dictionary = library.getDictionary("version");
        assertNotNull(dictionary);
    }

    @Test
    public void testLoadDirectory() {
        List<File> files = new ArrayList<>();
        files.add(new File("path/to/files/Fields.xml"));
        files.add(new File("path/to/files/Enums.xml"));
        files.add(new File("path/to/files/Components.xml"));
        files.add(new File("path/to/files/MsgType.xml"));
        files.add(new File("path/to/files/MsgContents.xml"));

        library.loadDirectory(files, "version");
        DictionaryInfo dictionary = library.getDictionary("version");
        assertNotNull(dictionary);
        assertTrue(dictionary.isLoaded());
    }

    @Test
    public void testArrangeFiles() {
        File[] files = new File[5];
        files[0] = new File("path/to/files/Fields.xml");
        files[1] = new File("path/to/files/Enums.xml");
        files[2] = new File("path/to/files/Components.xml");
        files[3] = new File("path/to/files/MsgType.xml");
        files[4] = new File("path/to/files/MsgContents.xml");

        List<File> arrangedFiles = library.arrangeFiles(files);
        assertEquals(files[0], arrangedFiles.get(0));
        assertEquals(files[1], arrangedFiles.get(1));
        assertEquals(files[2], arrangedFiles.get(2));
        assertEquals(files[3], arrangedFiles.get(3));
        assertEquals(files[4], arrangedFiles.get(4));
    }
}