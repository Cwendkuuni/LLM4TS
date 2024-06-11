import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class LibraryTest {
  
  // Test case for loadFromDirectory method
  
  @Test
  public void testLoadFromDirectory() {
    Library library = new Library();
    boolean result = library.loadFromDirectory("test/path");
    assertFalse(result); // check if false is returned for empty directory
   
    result = library.loadFromDirectory("test/nonDirectory");
    assertFalse(result); // check if false is returned for non-directory path
    
    result = library.loadFromDirectory("test/directoryWithFiles");
    assertTrue(result); // check if true is returned for valid directory
    
    List<DictionaryInfo> dictionaries = library.getDictionaries();
    assertEquals(5, dictionaries.size()); // check if all dictionaries are loaded
    
    assertTrue(dictionaries.get(0).isLoaded()); // check if first dictionary is loaded
    
    DictionaryInfo dictionary = library.getDictionary("version1");
    assertNotNull(dictionary); // check if dictionary exists
    
    dictionary = library.getDictionary("nonExistentVersion");
    assertNull(dictionary); // check if non-existent dictionary returns null
  }
  
  // Test case for getDictionaries method
  
  @Test
  public void testGetDictionaries() {
    Library library = new Library();
    boolean result = library.loadFromDirectory("test/directoryWithFiles");
    assertTrue(result);
    
    List<DictionaryInfo> dictionaries = library.getDictionaries();
    assertEquals(5, dictionaries.size());
    
    for (DictionaryInfo dictionary : dictionaries) {
      assertNotNull(dictionary.getVersion());
    }
  }
  
  // Test case for getDictionary method
  
  @Test
  public void testGetDictionary() {
    Library library = new Library();
    boolean result = library.loadFromDirectory("test/directoryWithFiles");
    assertTrue(result);
    
    DictionaryInfo dictionary = library.getDictionary("version1");
    assertNotNull(dictionary);
    
    dictionary = library.getDictionary("version2");
    assertNotNull(dictionary);
    
    dictionary = library.getDictionary("nonExistentVersion");
    assertNull(dictionary);
  }
  
  // Test case for arrangeFiles method
  
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
    
    assertEquals("Fields.xml", arrangedFiles.get(0).getName());
    assertEquals("Enums.xml", arrangedFiles.get(1).getName());
    assertEquals("Components.xml", arrangedFiles.get(2).getName());
    assertEquals("MsgType.xml", arrangedFiles.get(3).getName());
    assertEquals("MsgContents.xml", arrangedFiles.get(4).getName());
  }
}