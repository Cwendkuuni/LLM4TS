import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.parsers.fpl.MainParser;
import org.junit.Assert;
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

public class LibraryTest {

    private Library library;

    @Before
    public void setUp() {
        library = new Library();
    }

    @Test
    public void testLoadFromDirectory() {
        boolean result = library.loadFromDirectory(".");
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
    public void testLoadDirectory() {
        List<File> files = new ArrayList<>();
        files.add(new File("Fields.xml"));
        files.add(new File("Enums.xml"));
        files.add(new File("Components.xml"));
        files.add(new File("MsgType.xml"));
        files.add(new File("MsgContents.xml"));

        library.loadDirectory(files, "version");
        DictionaryInfo dictionary = library.getDictionary("version");
        Assert.assertNotNull(dictionary);
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
        Assert.assertEquals(arrangedFiles.size(), 5);
    }
}