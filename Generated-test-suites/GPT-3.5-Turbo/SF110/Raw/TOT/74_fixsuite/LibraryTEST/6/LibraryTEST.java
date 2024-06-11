import org.junit.*;

public class LibraryTest {

    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        
        // Typical use case - load from existing directory
        boolean result1 = library.loadFromDirectory("C:\\path\\to\\test\\directory");
        Assert.assertTrue(result1);

        // Edge case - load from empty directory
        boolean result2 = library.loadFromDirectory("C:\\path\\to\\empty\\directory");
        Assert.assertFalse(result2);

        // Error scenario - load from non-existing directory
        boolean result3 = library.loadFromDirectory("C:\\non\\existing\\directory");
        Assert.assertFalse(result3);
    }

    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        
        // Typical use case - dictionaries loaded and present
        library.loadFromDirectory("C:\\path\\to\\test\\directory");
        List<DictionaryInfo> dictionaries1 = library.getDictionaries();
        Assert.assertNotNull(dictionaries1);
        Assert.assertFalse(dictionaries1.isEmpty());

        // Edge case - no dictionaries loaded
        library.loadFromDirectory("C:\\path\\to\\empty\\directory");
        List<DictionaryInfo> dictionaries2 = library.getDictionaries();
        Assert.assertNotNull(dictionaries2);
        Assert.assertTrue(dictionaries2.isEmpty());
    }

    @Test
    public void testGetDictionary() {
        Library library = new Library();
        
        // Typical use case - dictionary found
        library.loadFromDirectory("C:\\path\\to\\test\\directory");
        DictionaryInfo dictionary1 = library.getDictionary("1.0");
        Assert.assertNotNull(dictionary1);
        Assert.assertEquals("1.0", dictionary1.getVersion());

        // Edge case - dictionary not found
        library.loadFromDirectory("C:\\path\\to\\test\\directory");
        DictionaryInfo dictionary2 = library.getDictionary("2.0");
        Assert.assertNull(dictionary2);
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
        
        // Typical use case - files arranged correctly
        List<File> arrangedFiles1 = library.arrangeFiles(files);
        Assert.assertEquals(5, arrangedFiles1.size());
        Assert.assertEquals(files[0], arrangedFiles1.get(0));
        Assert.assertEquals(files[1], arrangedFiles1.get(1));
        Assert.assertEquals(files[2], arrangedFiles1.get(2));
        Assert.assertEquals(files[3], arrangedFiles1.get(3));
        Assert.assertEquals(files[4], arrangedFiles1.get(4));

        // Edge case - files not arranged correctly
        File[] files2 = new File[5];
        files2[0] = new File("Enums.xml");
        files2[1] = new File("Fields.xml");
        files2[2] = new File("Components.xml");
        files2[3] = new File("MsgType.xml");
        files2[4] = new File("MsgContents.xml");
        List<File> arrangedFiles2 = library.arrangeFiles(files2);
        Assert.assertEquals(5, arrangedFiles2.size());
        Assert.assertEquals(files2[0], arrangedFiles2.get(0));
        Assert.assertEquals(files2[1], arrangedFiles2.get(1));
        Assert.assertEquals(files2[2], arrangedFiles2.get(2));
        Assert.assertEquals(files2[3], arrangedFiles2.get(3));
        Assert.assertEquals(files2[4], arrangedFiles2.get(4));
    }
}