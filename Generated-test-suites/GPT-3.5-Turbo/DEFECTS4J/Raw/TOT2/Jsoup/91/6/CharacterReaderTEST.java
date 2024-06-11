import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CharacterReaderTest {
    
    @Test
    public void testPos() {
        Reader reader = new StringReader("Example text");
        CharacterReader charReader = new CharacterReader(reader);
        
        int expectedPos = 0;
        int actualPos = charReader.pos();
        
        assertEquals(expectedPos, actualPos);
    }
}