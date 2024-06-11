import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class LibraryTest {

    private Library library;

    @Before
    public void setUp() {
        library = new Library();
    }

    @Test
    public void testLoadFromDirectory() {
        // Test typical use case
        boolean result = library.loadFromDirectory("path/to/directory");
        Assert.assertTrue(result);

        // Test edge case: empty directory
        result = library.loadFromDirectory("path/to/empty/directory");
        Assert.assertFalse(result);

        // Test error scenario: non-existing directory
        result = library.loadFromDirectory("path/to/non-existing/directory");
        Assert.assertFalse(result);
    }

    @Test
    public void testGetDictionaries() {
        // Test typical use case
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        Assert.assertNotNull(dictionaries);
        Assert.assertEquals(0, dictionaries.size());

        // Test edge case: dictionary loaded
        library.loadFromDirectory("path/to/directory");
        dictionaries = library.getDictionaries();
        Assert.assertNotNull(dictionaries);
        Assert.assertEquals(1, dictionaries.size());
        Assert.assertEquals("1.0", dictionaries.get(0).getVersion());
    }

    @Test
    public void testGetDictionary() {
        // Test typical use case
        DictionaryInfo dictionary = library.getDictionary("1.0");
        Assert.assertNull(dictionary);

        // Test edge case: dictionary loaded
        library.loadFromDirectory("path/to/directory");
        dictionary = library.getDictionary("1.0");
        Assert.assertNotNull(dictionary);
        Assert.assertEquals("1.0", dictionary.getVersion());

        // Test error scenario: non-existing dictionary
        dictionary = library.getDictionary("2.0");
        Assert.assertNull(dictionary);
    }

    @Test
    public void testLoadDirectory() {
        // Test typical use case
        List<File> files = new ArrayList<>();
        files.add(new File("Fields.xml"));
        files.add(new File("Enums.xml"));
        files.add(new File("Components.xml"));
        files.add(new File("MsgType.xml"));
        files.add(new File("MsgContents.xml"));
        library.loadDirectory(files, "1.0");
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        Assert.assertNotNull(dictionaries);
        Assert.assertEquals(1, dictionaries.size());
        Assert.assertEquals("1.0", dictionaries.get(0).getVersion());

        // Test error scenario: invalid file list
        files.remove(0);
        library.loadDirectory(files, "1.0");
        dictionaries = library.getDictionaries();
        Assert.assertNotNull(dictionaries);
        Assert.assertEquals(0, dictionaries.size());
    }

    @Test
    public void testArrangeFiles() {
        // Test typical use case
        File[] files = new File[5];
        files[0] = new File("Fields.xml");
        files[1] = new File("Enums.xml");
        files[2] = new File("Components.xml");
        files[3] = new File("MsgType.xml");
        files[4] = new File("MsgContents.xml");
        List<File> arrangedFiles = library.arrangeFiles(files);
        Assert.assertNotNull(arrangedFiles);
        Assert.assertEquals(5, arrangedFiles.size());
        Assert.assertEquals("Fields.xml", arrangedFiles.get(0).getName());
        Assert.assertEquals("Enums.xml", arrangedFiles.get(1).getName());
        Assert.assertEquals("Components.xml", arrangedFiles.get(2).getName());
        Assert.assertEquals("MsgType.xml", arrangedFiles.get(3).getName());
        Assert.assertEquals("MsgContents.xml", arrangedFiles.get(4).getName());

        // Test error scenario: missing file
        files[0] = new File("Enums.xml");
        arrangedFiles = library.arrangeFiles(files);
        Assert.assertNotNull(arrangedFiles);
        Assert.assertEquals(4, arrangedFiles.size());
        Assert.assertEquals("Enums.xml", arrangedFiles.get(0).getName());
        Assert.assertEquals("Components.xml", arrangedFiles.get(1).getName());
        Assert.assertEquals("MsgType.xml", arrangedFiles.get(2).getName());
        Assert.assertEquals("MsgContents.xml", arrangedFiles.get(3).getName());
    }
}