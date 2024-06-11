import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
  
  // Test isNamedEntity method
  @Test
  public void testIsNamedEntity() {
    boolean result = Entities.isNamedEntity("lt");
    assertTrue(result);
    
    result = Entities.isNamedEntity("amp");
    assertTrue(result);

    result = Entities.isNamedEntity("foo");
    assertFalse(result);
  }
  
  // Test isBaseNamedEntity method
  @Test
  public void testIsBaseNamedEntity() {
    boolean result = Entities.isBaseNamedEntity("lt");
    assertTrue(result);
    
    result = Entities.isBaseNamedEntity("amp");
    assertTrue(result);

    result = Entities.isBaseNamedEntity("foo");
    assertFalse(result);
  }
  
  // Test getCharacterByName method
  @Test
  public void testGetCharacterByName() {
    Character result = Entities.getCharacterByName("lt");
    assertEquals(result, Character.valueOf('<'));
    
    result = Entities.getCharacterByName("amp");
    assertEquals(result, Character.valueOf('&'));

    result = Entities.getCharacterByName("foo");
    assertNull(result);
  }

}