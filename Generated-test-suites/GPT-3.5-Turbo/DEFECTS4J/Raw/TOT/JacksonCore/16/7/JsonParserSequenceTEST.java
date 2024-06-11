import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JsonParserSequenceTest {

    @Mock
    private JsonParser mockParser1;
    
    @Mock
    private JsonParser mockParser2;
    
    @Mock
    private JsonParser mockParser3;
    
    private JsonParserSequence parserSequence;

    @Before
    public void setUp() {
        JsonParser[] parsers = { mockParser1, mockParser2, mockParser3 };
        parserSequence = new JsonParserSequence(parsers);
    }

    @Test
    public void testClose() throws IOException {
        parserSequence.close();
        Mockito.verify(mockParser1).close();
        Mockito.verify(mockParser2).close();
        Mockito.verify(mockParser3).close();
    }

    @Test
    public void testNextToken_Successful() throws IOException, JsonParseException {
        JsonToken mockToken = Mockito.mock(JsonToken.class);
        Mockito.when(mockParser1.nextToken()).thenReturn(null);
        Mockito.when(mockParser2.nextToken()).thenReturn(mockToken);
        assertEquals(mockToken, parserSequence.nextToken());
        Mockito.verify(mockParser1).nextToken();
        Mockito.verify(mockParser2).nextToken();
        Mockito.verify(mockParser3, Mockito.never()).nextToken();
    }

    @Test
    public void testNextToken_EndOfParsers() throws IOException, JsonParseException {
        Mockito.when(mockParser1.nextToken()).thenReturn(null);
        Mockito.when(mockParser2.nextToken()).thenReturn(null);
        Mockito.when(mockParser3.nextToken()).thenReturn(null);
        assertNull(parserSequence.nextToken());
        Mockito.verify(mockParser1).nextToken();
        Mockito.verify(mockParser2).nextToken();
        Mockito.verify(mockParser3).nextToken();
    }

    @Test
    public void testContainedParsersCount() {
        assertEquals(3, parserSequence.containedParsersCount());
    }

    @Test
    public void testSwitchToNext_Successful() {
        assertTrue(parserSequence.switchToNext());
        assertEquals(mockParser2, parserSequence.delegate);
        assertTrue(parserSequence.switchToNext());
        assertEquals(mockParser3, parserSequence.delegate);
        assertFalse(parserSequence.switchToNext());
    }

    @Test
    public void testSwitchToNext_EndOfParsers() {
        parserSequence._nextParser = 3;
        assertFalse(parserSequence.switchToNext());
    }
}