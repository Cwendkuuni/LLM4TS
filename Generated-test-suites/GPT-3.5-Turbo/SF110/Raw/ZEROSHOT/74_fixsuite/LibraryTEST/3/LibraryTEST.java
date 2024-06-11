import org.junit.*;
import java.io.*;
import org.fixsuite.message.*;
import org.fixsuite.message.info.*;
import org.slf4j.*;
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
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testGetDictionaries() {
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        
        Assert.assertNotNull(dictionaries);
    }
    
    @Test
    public void testGetDictionary() {
        String version = "FIX.4.4";
        DictionaryInfo dictionary = library.getDictionary(version);
        
        Assert.assertNull(dictionary);
    }
    
    @Test
    public void testLoadDirectory() {
        List<File> files = new ArrayList<File>();
        files.add(new File("path/to/Fields.xml"));
        files.add(new File("path/to/Enums.xml"));
        files.add(new File("path/to/Components.xml"));
        files.add(new File("path/to/MsgType.xml"));
        files.add(new File("path/to/MsgContents.xml"));
        
        String version = "FIX.4.4";
        library.loadDirectory(files, version);
        
        DictionaryInfo dictionary = library.getDictionary(version);
        List<FieldInfo> fields = dictionary.getFields();
        List<EnumInfo> enums = dictionary.getEnums();
        List<ComponentInfo> components = dictionary.getComponents();
        List<MsgTypeInfo> msgTypes = dictionary.getMsgTypes();
        List<MsgContentInfo> msgContents = dictionary.getMsgContents();
        
        Assert.assertNotNull(dictionary);
        Assert.assertNotNull(fields);
        Assert.assertNotNull(enums);
        Assert.assertNotNull(components);
        Assert.assertNotNull(msgTypes);
        Assert.assertNotNull(msgContents);
    }
    
    @Test
    public void testArrangeFiles() {
        File[] files = {
            new File("path/to/Fields.xml"),
            new File("path/to/Enums.xml"),
            new File("path/to/Components.xml"),
            new File("path/to/MsgType.xml"),
            new File("path/to/MsgContents.xml")
        };
        
        List<File> arrangedFiles = library.arrangeFiles(files);
        
        Assert.assertEquals(files.length, arrangedFiles.size());
        Assert.assertEquals(files[0], arrangedFiles.get(0));
        Assert.assertEquals(files[1], arrangedFiles.get(1));
        Assert.assertEquals(files[2], arrangedFiles.get(2));
        Assert.assertEquals(files[3], arrangedFiles.get(3));
        Assert.assertEquals(files[4], arrangedFiles.get(4));
    }
}