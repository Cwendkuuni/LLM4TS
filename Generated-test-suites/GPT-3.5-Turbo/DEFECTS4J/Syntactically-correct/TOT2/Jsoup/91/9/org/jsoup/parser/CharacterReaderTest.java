import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        Reader input = new StringReader("Test input");
        CharacterReader reader = new CharacterReader(input);

        assertEquals(0, reader.pos());
    }
}