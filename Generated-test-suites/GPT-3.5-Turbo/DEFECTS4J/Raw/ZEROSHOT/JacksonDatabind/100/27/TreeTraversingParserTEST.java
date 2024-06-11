import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TreeTraversingParserTest {
	
	private TreeTraversingParser parser;
	
	@Before
	public void setup() {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.createObjectNode().put("name", "John Doe").put("age", 30);
		parser = new TreeTraversingParser(node);
	}
	
	@Test
	public void testNextToken() throws Exception {
		parser.nextToken();
	}
	
	@Test
	public void testSkipChildren() throws Exception {
		parser.nextToken();
		parser.skipChildren();
	}
	
	@Test
	public void testGetCurrentName() throws Exception {
		parser.nextToken();
		parser.getCurrentName();
	}
	
	@Test
	public void testOverrideCurrentName() throws Exception {
		parser.nextToken();
		parser.overrideCurrentName("newName");
	}
	
	@Test
	public void testGetParsingContext() throws Exception {
		parser.nextToken();
		parser.getParsingContext();
	}
	
	@Test
	public void testGetTokenLocation() throws Exception {
		parser.nextToken();
		parser.getTokenLocation();
	}
	
	@Test
	public void testGetCurrentLocation() throws Exception {
		parser.nextToken();
		parser.getCurrentLocation();
	}
	
	@Test
	public void testGetText() throws Exception {
		parser.nextToken();
		parser.getText();
	}
	
	@Test
	public void testGetTextCharacters() throws Exception {
		parser.nextToken();
		parser.getTextCharacters();
	}
	
	@Test
	public void testGetTextLength() throws Exception {
		parser.nextToken();
		parser.getTextLength();
	}
	
	@Test
	public void testGetTextOffset() throws Exception {
		parser.nextToken();
		parser.getTextOffset();
	}
	
	@Test
	public void testHasTextCharacters() throws Exception {
		parser.nextToken();
		parser.hasTextCharacters();
	}
	
	@Test
	public void testGetNumberType() throws Exception {
		parser.nextToken();
		parser.getNumberType();
	}
	
	@Test
	public void testGetBigIntegerValue() throws Exception {
		parser.nextToken();
		parser.getBigIntegerValue();
	}
	
	@Test
	public void testGetDecimalValue() throws Exception {
		parser.nextToken();
		parser.getDecimalValue();
	}
	
	@Test
	public void testGetDoubleValue() throws Exception {
		parser.nextToken();
		parser.getDoubleValue();
	}
	
	@Test
	public void testGetFloatValue() throws Exception {
		parser.nextToken();
		parser.getFloatValue();
	}
	
	@Test
	public void testGetLongValue() throws Exception {
		parser.nextToken();
		parser.getLongValue();
	}
	
	@Test
	public void testGetIntValue() throws Exception {
		parser.nextToken();
		parser.getIntValue();
	}
	
	@Test
	public void testGetNumberValue() throws Exception {
		parser.nextToken();
		parser.getNumberValue();
	}
	
	@Test
	public void testGetEmbeddedObject() throws Exception {
		parser.nextToken();
		parser.getEmbeddedObject();
	}
	
	@Test
	public void testIsNaN() throws Exception {
		parser.nextToken();
		parser.isNaN();
	}
	
	@Test
	public void testGetBinaryValue() throws Exception {
		parser.nextToken();
		parser.getBinaryValue(null);
	}
	
	@Test
	public void testReadBinaryValue() throws Exception {
		parser.nextToken();
		parser.readBinaryValue(null, null);
	}
	
	@Test
	public void testClose() throws Exception {
		parser.close();
	}
	
	@Test
	public void testIsClosed() throws Exception {
		parser.isClosed();
	}
}