import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LibraryTest {

  private Library library;

  @Before
  public void setUp() {
    library = new Library();
  }

  @Test
  public void testLoadFromDirectory_ValidPath_ReturnsTrue() {
    String libraryPath = "path/to/library";
    boolean result = library.loadFromDirectory(libraryPath);
    assertTrue(result);
  }

  @Test
  public void testLoadFromDirectory_InvalidPath_ReturnsFalse() {
    String libraryPath = "invalid/path";
    boolean result = library.loadFromDirectory(libraryPath);
    assertFalse(result);
  }

  @Test
  public void testGetDictionaries_ReturnsListOfDictionaryInfo() {
    List<DictionaryInfo> dictionaries = library.getDictionaries();
    assertNotNull(dictionaries);
    assertEquals(ArrayList.class, dictionaries.getClass());
  }

  @Test
  public void testGetDictionary_ValidVersion_ReturnsDictionaryInfo() {
    String version = "1.0";
    DictionaryInfo dictionary = library.getDictionary(version);
    assertNotNull(dictionary);
    assertEquals(DictionaryInfo.class, dictionary.getClass());
    assertEquals(version, dictionary.getVersion());
  }

  @Test
  public void testLoadDirectory_ValidFiles_LoadsDictionary() {
    List<File> files = new ArrayList<File>();
    files.add(new File("path/to/Fields.xml"));
    files.add(new File("path/to/Enums.xml"));
    files.add(new File("path/to/Components.xml"));
    files.add(new File("path/to/MsgType.xml"));
    files.add(new File("path/to/MsgContents.xml"));
    String version = "1.0";
    library.loadDirectory(files, version);
    DictionaryInfo dictionary = library.getDictionary(version);
    assertNotNull(dictionary);
    assertTrue(dictionary.isLoaded());
  }

  @Test
  public void testLoadDirectory_InvalidFiles_DoesNotLoadDictionary() {
    List<File> files = new ArrayList<File>();
    files.add(new File("path/to/Invalid.xml"));
    String version = "1.0";
    library.loadDirectory(files, version);
    DictionaryInfo dictionary = library.getDictionary(version);
    assertNull(dictionary);
  }

  @Test
  public void testArrangeFiles_ValidFiles_ReturnsArrangedFiles() {
    File[] files = new File[5];
    files[0] = new File("path/to/Fields.xml");
    files[1] = new File("path/to/Enums.xml");
    files[2] = new File("path/to/Components.xml");
    files[3] = new File("path/to/MsgType.xml");
    files[4] = new File("path/to/MsgContents.xml");
    List<File> arrangedFiles = library.arrangeFiles(files);
    assertNotNull(arrangedFiles);
    assertEquals(5, arrangedFiles.size());
    assertEquals("Fields.xml", arrangedFiles.get(0).getName());
    assertEquals("Enums.xml", arrangedFiles.get(1).getName());
    assertEquals("Components.xml", arrangedFiles.get(2).getName());
    assertEquals("MsgType.xml", arrangedFiles.get(3).getName());
    assertEquals("MsgContents.xml", arrangedFiles.get(4).getName());
  }
}