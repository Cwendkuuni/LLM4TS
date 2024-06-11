import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.parsers.fpl.MainParser;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class LibraryTest {

    private static final String FIELDS_FILE_NAME = "Fields.xml";
    private static final String ENUMS_FILE_NAME = "Enums.xml";
    private static final String COMPONENTS_FILE_NAME = "Components.xml";
    private static final String MSG_TYPE_FILE_NAME = "MsgType.xml";
    private static final String MSG_CONTENTS_FILE_NAME = "MsgContents.xml";
    private static final int FIELDS_FILE_KEY = 1;
    private static final int ENUMS_FILE_KEY = 2;
    private static final int COMPONENTS_FILE_KEY = 3;
    private static final int MSG_TYPE_FILE_KEY = 4;
    private static final int MSG_CONTENTS_FILE_KEY = 5;
    private static Logger logger;
    private Library library;

    @Before
    public void setUp() {
        library = new Library();
    }

    @Test
    public void testLoadFromDirectory() {
        // Test empty directory
        assertFalse(library.loadFromDirectory(""));

        // Test directory with files
        assertTrue(library.loadFromDirectory("/path/to/directory"));
    }

    @Test
    public void testGetDictionaries() {
        // Load dictionaries
        library.loadFromDirectory("/path/to/directory");

        // Test if dictionaries are returned
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertEquals(1, dictionaries.size());
    }

    @Test
    public void testGetDictionary() {
        // Load dictionaries
        library.loadFromDirectory("/path/to/directory");

        // Test if dictionary is returned by version
        DictionaryInfo dictionary = library.getDictionary("version");
        assertNotNull(dictionary);
    }

    @Test
    public void testLoadDirectory() {
        // Create sample files
        File file1 = new File("Fields.xml");
        File file2 = new File("Enums.xml");

        // Create sample version
        String version = "1.0";

        // Load directory
        library.loadDirectory(List.of(file1, file2), version);

        // Test if dictionary is loaded
        DictionaryInfo dictionary = library.getDictionary(version);
        assertNotNull(dictionary);
    }

    @Test
    public void testArrangeFiles() {
        // Create sample files
        File file1 = new File("Fields.xml");
        File file2 = new File("Enums.xml");
        File file3 = new File("Components.xml");
        File file4 = new File("MsgType.xml");

        // Arrange files
        List<File> arrangedFiles = library.arrangeFiles(new File[]{file4, file2, file3, file1});

        // Test if files are arranged correctly
        assertEquals(file1, arrangedFiles.get(0));
        assertEquals(file2, arrangedFiles.get(1));
        assertEquals(file3, arrangedFiles.get(2));
        assertEquals(file4, arrangedFiles.get(3));
    }
    
    @Before
    public void setUp() {
        library = new Library();
    }
    
    @Test
    public void test() {
        // Test loadFromDirectory
        assertFalse(library.loadFromDirectory(""));

        assertTrue(library.loadFromDirectory("/path/to/directory"));

        // Test getDictionaries
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertEquals(1, dictionaries.size());

        // Test getDictionary
        DictionaryInfo dictionary = library.getDictionary("version");
        assertNotNull(dictionary);

        // Test loadDirectory
        File file1 = new File("Fields.xml");
        File file2 = new File("Enums.xml");
        String version = "1.0";
        library.loadDirectory(List.of(file1, file2), version);
        dictionary = library.getDictionary(version);
        assertNotNull(dictionary);

        // Test arrangeFiles
        File file3 = new File("Components.xml");
        File file4 = new File("MsgType.xml");
        List<File> arrangedFiles = library.arrangeFiles(new File[]{file4, file2, file3, file1});
        assertEquals(file1, arrangedFiles.get(0));
        assertEquals(file2, arrangedFiles.get(1));
        assertEquals(file3, arrangedFiles.get(2));
        assertEquals(file4, arrangedFiles.get(3));
    } 
}