import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class LibraryTest {

    private Library library;

    @Before
    public void setUp() {
        library = new Library();
    }

    @Test
    public void testLoadFromDirectory() {
        boolean result = library.loadFromDirectory("path/to/library");
        Assert.assertTrue(result);
    }

    @Test
    public void testGetDictionaries() {
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        Assert.assertNotNull(dictionaries);
    }

    @Test
    public void testGetDictionary() {
        DictionaryInfo dictionary = library.getDictionary("version");
        Assert.assertNull(dictionary);
    }

    @Test
    public void testArrangeFiles() {
        File[] files = {
                new File("Fields.xml"),
                new File("Enums.xml"),
                new File("Components.xml"),
                new File("MsgType.xml"),
                new File("MsgContents.xml")
        };
        List<File> arrangedFiles = library.arrangeFiles(files);
        Assert.assertNotNull(arrangedFiles);
    }
    
    // Additional test cases for edge cases and exceptions
    
    @Test
    public void testLoadFromDirectory_EmptyDirectory() {
        boolean result = library.loadFromDirectory("path/to/empty/directory");
        Assert.assertFalse(result);
    }
    
    @Test
    public void testLoadFromDirectory_NotADirectory() {
        boolean result = library.loadFromDirectory("path/to/file.txt");
        Assert.assertFalse(result);
    }
    
    @Test
    public void testGetDictionary_InvalidVersion() {
        DictionaryInfo dictionary = library.getDictionary("invalid_version");
        Assert.assertNull(dictionary);
    }

}