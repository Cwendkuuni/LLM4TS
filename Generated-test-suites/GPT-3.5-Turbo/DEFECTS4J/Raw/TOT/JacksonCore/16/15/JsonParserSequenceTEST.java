import static org.junit.Assert.*;
import java.io.IOException;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonParser;

public class JsonParserSequenceTest {

	@Test
	public void testCreateFlattened() throws IOException, JsonParseException {
		JsonParser first = new JsonParser(){ /*... mock JsonParser object */ };
		JsonParser second = new JsonParser(){ /*... mock JsonParser object */ };
		
		JsonParserSequence seq = JsonParserSequence.createFlattened(first, second);
		
		int expectedCount = 2;
		int actualCount = seq.containedParsersCount();
		
		assertEquals(expectedCount, actualCount);
		
		
	}
	
	@Test
	public void testClose() throws IOException, JsonParseException {
		JsonParser first = new JsonParser(){ /*... mock JsonParser object */ };
		JsonParser second = new JsonParser(){ /*... mock JsonParser object */ };
		
		JsonParserSequence seq = JsonParserSequence.createFlattened(first, second);
		
		seq.close();
		
		assertTrue(first.isClosed());
		assertTrue(second.isClosed());
	}
	
	@Test
	public void testNextToken() throws IOException, JsonParseException {
		JsonParser first = new JsonParser(){ /*... mock JsonParser object */ };
		JsonParser second = new JsonParser(){ /*... mock JsonParser object */ };
		
		JsonParserSequence seq = JsonParserSequence.createFlattened(first, second);
		
		JsonToken expectedToken1 = JsonToken.START_OBJECT;
		JsonToken expectedToken2 = JsonToken.FIELD_NAME;
		
		JsonToken actualToken1 = seq.nextToken();
		JsonToken actualToken2 = seq.nextToken();
		
		assertEquals(expectedToken1, actualToken1);
		assertEquals(expectedToken2, actualToken2);
	}
}