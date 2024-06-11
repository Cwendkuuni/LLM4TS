import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.io.File;
import org.fixsuite.message.info.DictionaryInfo;

public class LibraryTest {

   @Test
   public void testLoadFromDirectory() {
      Library library = new Library();
      assertTrue(library.loadFromDirectory("some_directory")); // Typical use case
      
      assertFalse(library.loadFromDirectory("non_existing_directory")); // Error scenario - directory does not exist
   }
   
   @Test
   public void testGetDictionaries() {
      Library library = new Library();
      assertTrue(library.loadFromDirectory("some_directory")); 
      
      List<DictionaryInfo> dictionaries = library.getDictionaries();
      assertNotNull(dictionaries); // Typical use case
      
      assertEquals(0, dictionaries.size());
   }
   
   @Test
   public void testGetDictionary() {
      Library library = new Library();
      assertTrue(library.loadFromDirectory("some_directory")); 
      
      DictionaryInfo dictionary = library.getDictionary("1.0");
      assertNotNull(dictionary); // Typical use case
      
      dictionary = library.getDictionary("2.0");
      assertNull(dictionary); // Error scenario - dictionary with version does not exist
   }
   
   @Test
   public void testArrangeFiles() {
      Library library = new Library();
      File[] files = {new File("Fields.xml"), new File("Enums.xml"), new File("Components.xml"), new File("MsgType.xml"), new File("MsgContents.xml")};
      
      List<File> arrangedFiles = library.arrangeFiles(files);
      assertNotNull(arrangedFiles); // Typical use case
      
      assertEquals(5, arrangedFiles.size());
      assertEquals("Fields.xml", arrangedFiles.get(0).getName());
      assertEquals("Enums.xml", arrangedFiles.get(1).getName());
      assertEquals("Components.xml", arrangedFiles.get(2).getName());
      assertEquals("MsgType.xml", arrangedFiles.get(3).getName());
      assertEquals("MsgContents.xml", arrangedFiles.get(4).getName());
   }

}