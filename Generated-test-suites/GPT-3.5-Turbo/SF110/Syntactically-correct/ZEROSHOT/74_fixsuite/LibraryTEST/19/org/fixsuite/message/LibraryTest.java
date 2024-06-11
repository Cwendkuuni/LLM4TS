import static org.junit.Assert.*;
import org.junit.*;

public class LibraryTest {
  
  @Test
  public void testLoadFromDirectory_emptyDirectory_returnsFalse() {
    Library library = new Library();
    boolean result = library.loadFromDirectory("/path/to/empty/directory");
    assertFalse(result);
  }
  
  @Test
  public void testLoadFromDirectory_notDirectory_returnsFalse() {
    Library library = new Library();
    boolean result = library.loadFromDirectory("/path/to/file.txt");
    assertFalse(result);
  }
  
  @Test
  public void testLoadFromDirectory_validDirectory_loadsDictionaries() {
    Library library = new Library();
    boolean result = library.loadFromDirectory("/path/to/directory");
    assertTrue(result);
    List<DictionaryInfo> dictionaries = library.getDictionaries();
    assertNotNull(dictionaries);
    assertEquals(1, dictionaries.size());
  }
  
  @Test
  public void testGetDictionaries_emptyDictionaries_returnsEmptyList() {
    Library library = new Library();
    List<DictionaryInfo> dictionaries = library.getDictionaries();
    assertNotNull(dictionaries);
    assertEquals(0, dictionaries.size());
  }
  
  @Test
  public void testGetDictionary_nonExistingVersion_returnsNull() {
    Library library = new Library();
    DictionaryInfo dictionary = library.getDictionary("nonExistingVersion");
    assertNull(dictionary);
  }
  
  @Test
  public void testGetDictionary_existingVersion_returnsDictionaryInfo() {
    Library library = new Library();
    library.loadFromDirectory("/path/to/directory");
    DictionaryInfo dictionary = library.getDictionary("version1");
    assertNotNull(dictionary);
    assertEquals("version1", dictionary.getVersion());
  }
  
  @Test
  public void testLoadDirectory_invalidFiles_skipsLoading() {
    Library library = new Library();
    library.loadDirectory(Arrays.asList(new File("/path/to/invalid/file1.xml"), new File("/path/to/invalid/file2.xml")), "version1");
    DictionaryInfo dictionary = library.getDictionary("version1");
    assertNull(dictionary);
  }
  
  @Test
  public void testLoadDirectory_validFiles_loadsDictionary() {
    Library library = new Library();
    library.loadDirectory(Arrays.asList(new File("/path/to/Fields.xml"), new File("/path/to/Enums.xml"), new File("/path/to/Components.xml"), new File("/path/to/MsgType.xml"), new File("/path/to/MsgContents.xml")), "version1");
    DictionaryInfo dictionary = library.getDictionary("version1");
    assertNotNull(dictionary);
    assertTrue(dictionary.isLoaded());
  }
  
  @Test
  public void testLoadDirectory_validFiles_arrangesFilesCorrectly() {
    Library library = new Library();
    library.loadDirectory(Arrays.asList(new File("/path/to/Fields.xml"), new File("/path/to/Enums.xml"), new File("/path/to/Components.xml"), new File("/path/to/MsgType.xml"), new File("/path/to/MsgContents.xml")), "version1");
    DictionaryInfo dictionary = library.getDictionary("version1");
    List<File> arrangedFiles = dictionary.getArrangedFiles();
    assertNotNull(arrangedFiles);
    assertEquals(5, arrangedFiles.size());
    assertTrue(arrangedFiles.get(0).getName().equals("Fields.xml"));
    assertTrue(arrangedFiles.get(1).getName().equals("Enums.xml"));
    assertTrue(arrangedFiles.get(2).getName().equals("Components.xml"));
    assertTrue(arrangedFiles.get(3).getName().equals("MsgType.xml"));
    assertTrue(arrangedFiles.get(4).getName().equals("MsgContents.xml"));
  }
}