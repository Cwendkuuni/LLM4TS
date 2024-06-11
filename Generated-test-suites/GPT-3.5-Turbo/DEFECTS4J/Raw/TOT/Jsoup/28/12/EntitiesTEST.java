import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
  
  @Test
  public void testIsNamedEntity() {
    assertFalse(Entities.isNamedEntity("abc")); // testing for a non-existent entity
    
    assertTrue(Entities.isNamedEntity("lt")); // testing for an existing entity
    assertTrue(Entities.isNamedEntity("gt"));
  }
  
  @Test
  public void testGetCharacterByName() {
    assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt")); // testing for an existing entity
    
    assertNull(Entities.getCharacterByName("abc")); // testing for a non-existent entity
  }
  
  @Test
  public void testEscape() {
    String input1 = "Hello \"world\"";
    String expectedOutput1 = "Hello &quot;world&quot;"; // testing for special character "
    
    String input2 = "<p>This is a paragraph</p>";
    String expectedOutput2 = "&lt;p&gt;This is a paragraph&lt;/p&gt;"; // testing for special character < and >
    
    assertEquals(expectedOutput1, Entities.escape(input1, Document.OutputSettings.xhtml));
    assertEquals(expectedOutput2, Entities.escape(input2, Document.OutputSettings.xhtml));
  }
  
  @Test
  public void testUnescape() {
    String input1 = "Hello &quot;world&quot;";
    String expectedOutput1 = "Hello \"world\""; // testing for special character "
    
    String input2 = "&lt;p&gt;This is a paragraph&lt;/p&gt;";
    String expectedOutput2 = "<p>This is a paragraph</p>"; // testing for special character < and >
    
    assertEquals(expectedOutput1, Entities.unescape(input1));
    assertEquals(expectedOutput2, Entities.unescape(input2));
  }
}