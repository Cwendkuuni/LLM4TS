
import static org.junit.Assert.*;
import org.junit.*;
import org.mockito.*;
import java.util.*;

public class LibraryTest {

  @Test
  public void testLoadFromDirectory_Success() {
    // Arrange
    Library library = new Library();
    String libraryPath = "path/to/directory";

    // Act
    boolean result = library.loadFromDirectory(libraryPath);

    // Assert
    assertTrue(result);
    assertEquals(1, library.getDictionaries().size());
    assertNotNull(library.getDictionary("version"));
  }

  @Test
  public void testLoadFromDirectory_EmptyDirectory() {
    // Arrange
    Library library = new Library();
    String libraryPath = "path/to/empty/directory";

    // Act
    boolean result = library.loadFromDirectory(libraryPath);

    // Assert
    assertFalse(result);
    assertEquals(0, library.getDictionaries().size());
    assertNull(library.getDictionary("version"));
  }

  @Test
  public void testLoadFromDirectory_PathNotDirectory() {
    // Arrange
    Library library = new Library();
    String libraryPath = "path/to/file";

    // Act
    boolean result = library.loadFromDirectory(libraryPath);

    // Assert
    assertFalse(result);
    assertEquals(0, library.getDictionaries().size());
    assertNull(library.getDictionary("version"));
  }

  @Test
  public void testLoadFromDirectory_UnableToLoadDictionary() {
    // Arrange
    Library library = new Library();
    String libraryPath = "path/to/directory";
    // Mocking parse method to throw an exception
    MainParser mainParser = Mockito.mock(MainParser.class);
    Mockito.doThrow(new RuntimeException()).when(mainParser).parse(Mockito.any(DictionaryInfo.class), Mockito.any(File.class));

    // Act
    boolean result = library.loadFromDirectory(libraryPath);

    // Assert
    assertFalse(result);
    assertEquals(0, library.getDictionaries().size());
    assertNull(library.getDictionary("version"));
  }

  @Test
  public void testGetDictionaries() {
    // Arrange
    Library library = new Library();
    String version = "1.0";
    DictionaryInfo dictionary = new DictionaryInfo(version);
    library.getDictionaries().add(dictionary);

    // Act
    List<DictionaryInfo> dictionaries = library.getDictionaries();

    // Assert
    assertEquals(1, dictionaries.size());
    assertEquals(version, dictionaries.get(0).getVersion());
  }

  @Test
  public void testGetDictionary_Exists() {
    // Arrange
    Library library = new Library();
    String version = "1.0";
    DictionaryInfo dictionary = new DictionaryInfo(version);
    library.getDictionaries().add(dictionary);

    // Act
    DictionaryInfo retrievedDictionary = library.getDictionary(version);

    // Assert
    assertNotNull(retrievedDictionary);
    assertEquals(version, retrievedDictionary.getVersion());
  }

  @Test
  public void testGetDictionary_NotExists() {
    // Arrange
    Library library = new Library();
    String version = "1.0";
    DictionaryInfo dictionary = new DictionaryInfo(version);
    library.getDictionaries().add(dictionary);

    // Act
    DictionaryInfo retrievedDictionary = library.getDictionary("2.0");

    // Assert
    assertNull(retrievedDictionary);
  }

  @Test
  public void testArrangeFiles() {
    // Arrange
    Library library = new Library();
    File[] files = new File[5];
    // containing files with different names in different order
    files[0] = new File("MsgContents.xml");
    files[1] = new File("Fields.xml");
    files[2] = new File("MsgType.xml");
    files[3] = new File("Enums.xml");
    files[4] = new File("Components.xml");

    // Act
    List<File> arrangedFiles = library.arrangeFiles(files);

    // Assert
    assertEquals(files[1], arrangedFiles.get(0));
    assertEquals(files[3], arrangedFiles.get(1));
    assertEquals(files[4], arrangedFiles.get(2));
    assertEquals(files[2], arrangedFiles.get(3));
    assertEquals(files[0], arrangedFiles.get(4));
  }
}

