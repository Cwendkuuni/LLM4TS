
import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.parsers.fpl.MainParser;

public class LibraryTest {
    
    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        boolean result = library.loadFromDirectory("path/to/directory");
        Assert.assertTrue(result);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        Assert.assertFalse(dictionaries.isEmpty());
    }
    
    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        Assert.assertNotNull(dictionaries);
    }
    
    @Test
    public void testGetDictionary() {
        Library library = new Library();
        DictionaryInfo dictionary = library.getDictionary("version");
        Assert.assertNull(dictionary);
        List<File> files = new ArrayList<>();
        files.add(new File("path/to/file.xml"));
        MainParser.parse(dictionary, files.get(0));
        dictionary = library.getDictionary("version");
        Assert.assertNotNull(dictionary);
        Assert.assertEquals(dictionary.getVersion(), "version");
    }
    
    @Test
    public void testArrangeFiles() {
        Library library = new Library();
        File[] files = new File[5];
        files[0] = new File("Fields.xml");
        files[1] = new File("Enums.xml");
        files[2] = new File("Components.xml");
        files[3] = new File("MsgType.xml");
        files[4] = new File("MsgContents.xml");
        List<File> arrangedFiles = library.arrangeFiles(files);
        Assert.assertEquals(arrangedFiles.size(), 5);
        Assert.assertEquals(arrangedFiles.get(0).getName(), "Fields.xml");
        // Add more assertions for other files
    }
}
