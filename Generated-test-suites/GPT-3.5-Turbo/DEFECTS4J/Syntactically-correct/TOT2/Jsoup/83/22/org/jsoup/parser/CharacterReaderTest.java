import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.StringReader;
import java.io.Reader;
import org.jsoup.parser.CharacterReader;

@RunWith(MockitoJUnitRunner.class)
public class CharacterReaderTest {

  private CharacterReader characterReader;

  @Before
  public void setUp() {
    Reader reader = new StringReader("Test String");
    characterReader = new CharacterReader(reader);
  }

  @Test
  public void testPos() {
    assertEquals(0, characterReader.pos());
    characterReader.advance();
    assertEquals(1, characterReader.pos());
  }

  @Test
  public void testIsEmpty() {
    assertFalse(characterReader.isEmpty());
    while(!characterReader.isEmpty()) {
      characterReader.consume();
    }
    assertTrue(characterReader.isEmpty());
  }

  @Test
  public void testCurrent() {
    assertEquals('T', characterReader.current());
    characterReader.advance();
    assertEquals('e', characterReader.current());
  }

  // Add more test methods for other public methods

}