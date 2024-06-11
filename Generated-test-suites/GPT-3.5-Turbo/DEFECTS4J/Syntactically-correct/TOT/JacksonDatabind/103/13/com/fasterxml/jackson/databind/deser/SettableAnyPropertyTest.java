import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class SettableAnyPropertyTest {
	
	private SettableAnyProperty settable;

	@Before
	public void setUp() throws Exception {
		settable = new SettableAnyProperty(null, null, null, null, null, null);
	}

	@Test
	public void testDeserializeAndSet_NullKey_Success() throws Exception {
		JsonParser parser = new JsonFactory().createParser("{}");
		DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
		Object instance = new TestBean();
		String propName = "prop";
		
		settable.deserializeAndSet(parser, ctxt, instance, propName);
		
		TestBean testBean = (TestBean) instance;
		assertEquals(1, testBean.properties.size());
		assertNull(testBean.properties.get(null));
	}
	
	@Test
	public void testDeserializeAndSet_NullValue_Success() throws Exception {
		JsonParser parser = new JsonFactory().createParser("{\"prop\":null}");
		DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
		Object instance = new TestBean();
		String propName = "prop";
		
		settable.deserializeAndSet(parser, ctxt, instance, propName);
		
		TestBean testBean = (TestBean) instance;
		assertEquals(1, testBean.properties.size());
		assertNull(testBean.properties.get("prop"));
	}
	
	@Test
	public void testDeserializeAndSet_KeyDeserializer_Success() throws Exception {
		JsonParser parser = new JsonFactory().createParser("{\"prop\":123}");
		DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
		Object instance = new TestBean();
		String propName = "prop";
		settable._keyDeserializer = new TestKeyDeserializer();
		
		settable.deserializeAndSet(parser, ctxt, instance, propName);
		
		TestBean testBean = (TestBean) instance;
		assertEquals(1, testBean.properties.size());
		assertTrue(testBean.properties.containsKey("custom key"));
		assertEquals(123, testBean.properties.get("custom key"));
	}
	
	@Test(expected = JsonMappingException.class)
	public void testDeserializeAndSet_UnresolvedForwardReferenceWithoutIdentityInfo_Exception() throws Exception {
		JsonParser parser = new JsonFactory().createParser("{\"prop\":123}");
		DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
		Object instance = new TestBean();
		String propName = "prop";
		settable._valueDeserializer = new TestDeserializer();
		
		settable.deserializeAndSet(parser, ctxt, instance, propName);
	}
	
	@Test
	public void testDeserialize_ValueNull_Success() throws Exception {
		JsonParser parser = new JsonFactory().createParser("null");
		DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
		
		Object result = settable.deserialize(parser, ctxt);
		
		assertNull(result);
	}
	
	@Test
	public void testDeserialize_WithType_DeserializerWithNullContext_Success() throws Exception {
		JsonParser parser = new JsonFactory().createParser("{\"type\":\"TypeA\",\"value\":\"123\"}");
		DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
		settable._valueDeserializer = new TestDeserializer();
		settable._valueTypeDeserializer = new TestTypeDeserializer();
		
		Object result = settable.deserialize(parser, ctxt);
		
		assertEquals("TypeA:123", result);
	}
	
	@Test
	public void testSet_Field_Success() throws Exception {
		settable._setterIsField = true;
		settable._setter = TestBean.class.getDeclaredField("properties");
		TestBean instance = new TestBean();
		Map<Object, Object> val = new HashMap<>();
		((java.lang.reflect.Field) settable._setter).set(instance, val);
		
		settable.set(instance, "prop", 123);
		
		assertTrue(instance.properties.containsKey("prop"));
		assertEquals(123, instance.properties.get("prop"));
	}
	
	@Test
	public void testSet_Method_Success() throws Exception {
		settable._setterIsField = false;
		settable._setter = TestBean.class.getDeclaredMethod("setProperties", Map.class);
		TestBean instance = new TestBean();
		
		settable.set(instance, "prop", 123);
		
		assertTrue(instance.properties.containsKey("prop"));
		assertEquals(123, instance.properties.get("prop"));
	}

	@Test(expected = IOException.class)
	public void testSet_ExceptionThrown_IOExceptionWrapped() throws Exception {
		settable._setterIsField = false;
		settable._setter = TestBean.class.getDeclaredMethod("throwException", Object.class);
		TestBean instance = new TestBean();
		
		settable.set(instance, "prop", 123);
	}
	
	@Test(expected = JsonMappingException.class)
	public void testSet_ExceptionThrown_IllegalArgumentExceptionWrapped() throws Exception {
		settable._setterIsField = false;
		settable._setter = TestBean.class.getDeclaredMethod("throwIllegalArgumentException", Object.class);
		TestBean instance = new TestBean();
		
		settable.set(instance, "prop", 123);
	}
	
	private static class TestBean {
		private Map<Object, Object> properties;

		public void setProperties(Map<Object, Object> properties) {
			this.properties = properties;
		}
		
		public void throwException(Object value) throws Exception {
			throw new Exception("Test Exception");
		}
		
		public void throwIllegalArgumentException(Object value) throws Exception {
			throw new IllegalArgumentException("Test Exception");
		}
	}
	
	private static class TestKeyDeserializer {

		public Object deserializeKey(String key, DeserializationContext ctxt) {
			return "custom key";
		}
	}
	
	private static class TestDeserializer extends JsonDeserializer<Object> {

		@Override
		public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
			return p.readValueAs(Integer.class);
		}
		
		@Override
		public Object getNullValue(DeserializationContext ctxt) throws JsonMappingException {
			return null;
		}
	}
	
	private static class TestTypeDeserializer extends TypeDeserializer {

		@Override
		public Object deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt) throws IOException {
			JsonNode node = p.readValueAsTree();
			return node.get("type").asText() + ":" + node.get("value").asText();
		}

	}
}