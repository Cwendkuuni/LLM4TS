import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() {
        JsonParser parser1 = Mockito.mock(JsonParser.class);
        JsonParser parser2 = Mockito.mock(JsonParser.class);
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        
        assertNotNull(sequence);
        assertEquals(2, sequence.containedParsersCount());
    }

    @Test
    public void testSwitchToNext() throws Exception {
        JsonParser parser1 = Mockito.mock(JsonParser.class);
        JsonParser parser2 = Mockito.mock(JsonParser.class);
        
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        
        assertTrue(sequence.switchToNext());
        assertFalse(sequence.switchToNext());
    }

    @Test
    public void testClose() throws Exception {
        JsonParser parser1 = Mockito.mock(JsonParser.class);
        JsonParser parser2 = Mockito.mock(JsonParser.class);
        Mockito.doNothing().when(parser1).close();
        Mockito.doNothing().when(parser2).close();
        
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        
        sequence.close();
        
        Mockito.verify(parser1, Mockito.times(1)).close();
        Mockito.verify(parser2, Mockito.times(1)).close();
    }
}