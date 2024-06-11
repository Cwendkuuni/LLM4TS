
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        Reader input = new StringReader("Testing pos method");
        CharacterReader characterReader = new CharacterReader(input);
        
        assertEquals(0, characterReader.pos());
        
        characterReader.advance();
        
        assertEquals(1, characterReader.pos());
    }

}
