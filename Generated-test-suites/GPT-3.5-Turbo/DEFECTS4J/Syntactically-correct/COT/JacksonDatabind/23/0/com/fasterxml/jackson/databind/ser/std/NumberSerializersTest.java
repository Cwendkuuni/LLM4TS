
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class NumberSerializersTest {

	private NumberSerializers numberSerializers;

	@Test
	public void testShortSerializer() {
		ShortSerializer shortSerializer = new NumberSerializers.ShortSerializer();
		assertEquals(Short.class, shortSerializer.handledType());
		assertTrue(shortSerializer.isUnwrappingSerializer());
		
		try {
			JsonFactory factory = new JsonFactory();
			JsonParser parser = factory.createParser("1");
			SerializerProvider provider = new ObjectMapper().getSerializerProvider();
			shortSerializer.serialize(Short.valueOf(parser.getShortValue()), parser, provider);
			assertEquals(JsonToken.VALUE_NUMBER_INT, parser.currentToken());
		} catch (Exception e) {
			fail("Unexpected exception: " + e.getMessage());
		}
	}

	@Test
	public void testIntegerSerializer() {
		IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer();
		assertEquals(Integer.class, integerSerializer.handledType());
		assertTrue(integerSerializer.isUnwrappingSerializer());

		try {
			JsonFactory factory = new JsonFactory();
			JsonParser parser = factory.createParser("1");
			SerializerProvider provider = new ObjectMapper().getSerializerProvider();
			integerSerializer.serialize(Integer.valueOf(parser.getIntValue()), parser, provider);
			assertEquals(JsonToken.VALUE_NUMBER_INT, parser.currentToken());
		} catch (Exception e) {
			fail("Unexpected exception: " + e.getMessage());
		}
	}

	@Test
	public void testIntLikeSerializer() {
		IntLikeSerializer intLikeSerializer = new NumberSerializers.IntLikeSerializer();
		assertEquals(Number.class, intLikeSerializer.handledType());
		assertTrue(intLikeSerializer.isUnwrappingSerializer());

		try {
			JsonFactory factory = new JsonFactory();
			JsonParser parser = factory.createParser("1");
			SerializerProvider provider = new ObjectMapper().getSerializerProvider();
			intLikeSerializer.serialize(Integer.valueOf(parser.getIntValue()), parser, provider);
			assertEquals(JsonToken.VALUE_NUMBER_INT, parser.currentToken());
		} catch (Exception e) {
			fail("Unexpected exception: " + e.getMessage());
		}
	}

	@Test
	public void testLongSerializer() {
		LongSerializer longSerializer = new NumberSerializers.LongSerializer();
		assertEquals(Long.class, longSerializer.handledType());
		assertTrue(longSerializer.isUnwrappingSerializer());

		try {
			JsonFactory factory = new JsonFactory();
			JsonParser parser = factory.createParser("1");
			SerializerProvider provider = new ObjectMapper().getSerializerProvider();
			longSerializer.serialize(Long.valueOf(parser.getLongValue()), parser, provider);
			assertEquals(JsonToken.VALUE_NUMBER_INT, parser.currentToken());
		} catch (Exception e) {
			fail("Unexpected exception: " + e.getMessage());
		}
	}

	@Test
	public void testFloatSerializer() {
		FloatSerializer floatSerializer = new NumberSerializers.FloatSerializer();
		assertEquals(Float.class, floatSerializer.handledType());
		assertTrue(floatSerializer.isUnwrappingSerializer());

		try {
			JsonFactory factory = new JsonFactory();
			JsonParser parser = factory.createParser("1.0");
			SerializerProvider provider = new ObjectMapper().getSerializerProvider();
			floatSerializer.serialize(Float.valueOf(parser.getFloatValue()), parser, provider);
			assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.currentToken());
		} catch (Exception e) {
			fail("Unexpected exception: " + e.getMessage());
		}
	}

	@Test
	public void testDoubleSerializer() {
		DoubleSerializer doubleSerializer = new NumberSerializers.DoubleSerializer();
		assertEquals(Double.class, doubleSerializer.handledType());
		assertTrue(doubleSerializer.isUnwrappingSerializer());

		try {
			JsonFactory factory = new JsonFactory();
			JsonParser parser = factory.createParser("1.0");
			SerializerProvider provider = new ObjectMapper().getSerializerProvider();
			doubleSerializer.serialize(Double.valueOf(parser.getDoubleValue()), parser, provider);
			assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.currentToken());
		} catch (Exception e) {
			fail("Unexpected exception: " + e.getMessage());
		}
	}

}
