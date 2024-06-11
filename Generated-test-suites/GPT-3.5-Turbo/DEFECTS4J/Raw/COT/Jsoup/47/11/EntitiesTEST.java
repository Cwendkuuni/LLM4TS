
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

  @Test
  public void testIsNamedEntity() {
    assertTrue(Entities.isNamedEntity("lt"));
    assertFalse(Entities.isNamedEntity("fakeEntity"));
  }

  @Test
  public void testIsBaseNamedEntity() {
    assertTrue(Entities.isBaseNamedEntity("lt"));
    assertFalse(Entities.isBaseNamedEntity("fakeEntity"));
  }

  @Test
  public void testGetCharacterByName() {
    assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
    assertNull(Entities.getCharacterByName("fakeEntity"));
  }
}
