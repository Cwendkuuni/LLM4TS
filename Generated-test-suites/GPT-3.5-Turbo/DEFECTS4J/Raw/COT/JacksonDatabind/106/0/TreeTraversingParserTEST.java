
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;

public class TreeTraversingParserTest {
	
	@Test
	public void testSetCodec() {
		TreeTraversingParser parser = new TreeTraversingParser(null);
		ObjectMapper codec = new ObjectMapper();
		parser.setCodec(codec);
	}

	@Test
	public void testGetCodec() {
		TreeTraversingParser parser = new TreeTraversingParser(null);
		parser.getCodec();
	}

	@Test
	public void testVersion() {
		TreeTraversingParser parser = new TreeTraversingParser(null);
		parser.version();
	}

	@Test
	public void testClose() throws Exception {
		TreeTraversingParser parser = new TreeTraversingParser(null);
		parser.close();
	}

	@Test
	public void testNextToken() throws Exception {
		JsonNode jsonNode = new ObjectMapper().readTree("{}");
		TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
		parser.nextToken();
	}

	@Test
	public void testSkipChildren() throws Exception {
		TreeTraversingParser parser = new TreeTraversingParser(null);
		parser.skipChildren();
	}

	@Test
	public void testIsClosed() {
		TreeTraversingParser parser = new TreeTraversingParser(null);
		parser.isClosed();
	}

	@Test
	public void testGetCurrentName() {
		TreeTraversingParser parser = new TreeTraversingParser(null);
		parser.getCurrentName();
	}

	@Test
	public void testOverrideCurrentName() {
		TreeTraversingParser parser = new TreeTraversingParser(null);
		parser.overrideCurrentName("name");
	}

	@Test
	public void testGetParsingContext() {
		TreeTraversingParser parser = new TreeTraversingParser(null);
		parser.getParsingContext();
	}

	@Test
	public void testGetTokenLocation() {
		TreeTraversingParser parser = new TreeTraversingParser(null);
		parser.getTokenLocation();
	}

	@Test
	public void testGetCurrentLocation() {
		TreeTraversingParser parser = new TreeTraversingParser(null);
		parser.getCurrentLocation();
	}

	@Test
	public void testGetText() {
		TreeTraversingParser parser = new TreeTraversingParser(null);
		parser.getText();
	}

	@Test
	public void testGetTextCharacters() throws Exception {
		TreeTraversingParser parser = new TreeTraversingParser(null);
		parser.getTextCharacters();
	}

	@Test
	public void testGetTextLength() throws Exception {
		TreeTraversingParser parser = new TreeTraversingParser(null);
		parser.getTextLength();
	}

	@Test
	public void testGetTextOffset() throws Exception {
		TreeTraversingParser parser = new TreeTraversingParser(null);
		parser.getTextOffset();
	}

	@Test
	public void testHasTextCharacters() {
		TreeTraversingParser parser = new TreeTraversingParser(null);
		parser.hasTextCharacters();
	}

	@Test
	public void testGetNumberType() throws Exception {
		TreeTraversingParser parser = new TreeTraversingParser(null);
		parser.getNumberType();
	}

	@Test
	public void testGetBigIntegerValue() throws Exception {
		TreeTraversingParser parser = new TreeTraversingParser(null);
		parser.getBigIntegerValue();
	}

	@Test
	public void testGetDecimalValue() throws Exception {
		TreeTraversingParser parser = new TreeTraversingParser(null);
		parser.getDecimalValue();
	}

	@Test
	public void testGetDoubleValue() throws Exception {
		TreeTraversingParser parser = new TreeTraversingParser(null);
		parser.getDoubleValue();
	}

	@Test
	public void testGetFloatValue() throws Exception {
		TreeTraversingParser parser = new TreeTraversingParser(null);
		parser.getFloatValue();
	}

	@Test
	public void testGetIntValue() throws Exception {
		TreeTraversingParser parser = new TreeTraversingParser(null);
		parser.getIntValue();
	}

	@Test
	public void testGetLongValue() throws Exception {
		TreeTraversingParser parser = new TreeTraversingParser(null);
		parser.getLongValue();
	}

	@Test
	public void testGetNumberValue() throws Exception {
		TreeTraversingParser parser = new TreeTraversingParser(null);
		parser.getNumberValue();
	}

	@Test
	public void testGetEmbeddedObject() throws Exception {
		TreeTraversingParser parser = new TreeTraversingParser(null);
		parser.getEmbeddedObject();
	}

	@Test
	public void testIsNaN() {
		TreeTraversingParser parser = new TreeTraversingParser(null);
		parser.isNaN();
	}

	@Test
	public void testGetBinaryValue() throws Exception {
		TreeTraversingParser parser = new TreeTraversingParser(null);
		parser.getBinaryValue(null);
	}

	@Test
	public void testReadBinaryValue() throws Exception {
		TreeTraversingParser parser = new TreeTraversingParser(null);
		parser.readBinaryValue(null, null);
	}

}
