import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;

public class LibraryTest {
  
  @Test
  public void testLoadFromDirectory() {
    Library library = new Library();
    boolean result = library.loadFromDirectory("/path/to/directory");
    Assert.assertTrue(result);
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
  }
}