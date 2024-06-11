import org.fixsuite.message.info.DictionaryInfo;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;

import java.io.File;
import java.util.*;

public class LibraryTest {
    private Library library;

    @Before
    public void setUp() {
        library = new Library();
    }

    @Test
    public void testLoadFromDirectory() {
        String libraryPath = "path/to/library";
        boolean result = library.loadFromDirectory(libraryPath);
        assertTrue(result);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
    }

    @Test
    public void testGetDictionaries() {
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
    }

    @Test
    public void testGetDictionary() {
        DictionaryInfo dictionary = library.getDictionary("version");
        assertNull(dictionary);
    }

    @Test
    public void testLoadDirectory() {
        List<File> files = new ArrayList<>();
        String version = "version";
        library.loadDirectory(files, version);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertEquals(0, dictionaries.size());
    }

    @Test
    public void testArrangeFiles() {
        File[] files = new File[5];
        File fieldsFile = new File("Fields.xml");
        File enumsFile = new File("Enums.xml");
        File componentsFile = new File("Components.xml");
        File msgTypeFile = new File("MsgType.xml");
        File msgContentsFile = new File("MsgContents.xml");
        files[0] = fieldsFile;
        files[1] = enumsFile;
        files[2] = componentsFile;
        files[3] = msgTypeFile;
        files[4] = msgContentsFile;
        List<File> arrangedFiles = library.arrangeFiles(files);
        assertEquals(5, arrangedFiles.size());
        assertEquals(1, arrangedFiles.indexOf(fieldsFile));
        assertEquals(2, arrangedFiles.indexOf(enumsFile));
        assertEquals(3, arrangedFiles.indexOf(componentsFile));
        assertEquals(4, arrangedFiles.indexOf(msgTypeFile));
        assertEquals(5, arrangedFiles.indexOf(msgContentsFile));
    }
}