import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class MappingIteratorTest {
	
	private ObjectMapper objectMapper;

	@Before
	public void setUp() {
		objectMapper = new ObjectMapper();
	}

	@Test
	public void testHasNext() throws Exception {
		MappingIterator<String> iterator = createIterator("['a', 'b', 'c']");
		
		assertTrue(iterator.hasNext());
	}

	@Test
	public void testNoNext() throws Exception {
		MappingIterator<String> iterator = createIterator("[]");
		
		assertFalse(iterator.hasNext());
	}

	@Test(expected = NoSuchElementException.class)
	public void testNoSuchElement() throws Exception {
		MappingIterator<String> iterator = createIterator("[]");
		
		iterator.next();
	}

	@Test
	public void testNext() throws Exception {
		MappingIterator<String> iterator = createIterator("['a', 'b', 'c']");
		
		assertEquals("a", iterator.next());
		assertEquals("b", iterator.next());
		assertEquals("c", iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testRemove() throws Exception {
		MappingIterator<String> iterator = createIterator("['a', 'b', 'c']");
		
		iterator.remove();
	}

	@Test
	public void testClose() throws Exception {
		MappingIterator<String> iterator = createIterator("['a', 'b', 'c']");
		
		iterator.close();
		
		assertFalse(iterator.hasNext());
	}
	
	@Test
	public void testReadAll() throws Exception {
		MappingIterator<String> iterator = createIterator("['a', 'b', 'c']");
		
		List<String> resultList = iterator.readAll();
		
		assertNotNull(resultList);
		assertEquals(3, resultList.size());
		assertEquals("a", resultList.get(0));
		assertEquals("b", resultList.get(1));
		assertEquals("c", resultList.get(2));
	}

	@Test
	public void testReadAllWithResultList() throws Exception {
		MappingIterator<String> iterator = createIterator("['a', 'b', 'c']");
		
		List<String> resultList = new ArrayList<String>();
		List<String> result = iterator.readAll(resultList);
		
		assertNotNull(result);
		assertSame(resultList, result);
		assertEquals(3, resultList.size());
		assertEquals("a", resultList.get(0));
		assertEquals("b", resultList.get(1));
		assertEquals("c", resultList.get(2));
	}
	
	@Test
	public void testReadAllWithResults() throws Exception {
		MappingIterator<String> iterator = createIterator("['a', 'b', 'c']");
		
		List<String> resultsList = new ArrayList<String>();
		List<String> result = iterator.readAll(resultsList);
		
		assertNotNull(result);
		assertSame(resultsList, result);
		assertEquals(3, resultsList.size());
		assertEquals("a", resultsList.get(0));
		assertEquals("b", resultsList.get(1));
		assertEquals("c", resultsList.get(2));
	}
	
	@Test
	public void testGetParser() throws Exception {
		MappingIterator<String> iterator = createIterator("['a', 'b', 'c']");
		
		assertNotNull(iterator.getParser());
	}

	@Test
	public void testGetParserSchema() throws Exception {
		MappingIterator<String> iterator = createIterator("['a', 'b', 'c']");
		
		assertNull(iterator.getParserSchema());
	}

	@Test
	public void testGetCurrentLocation() throws Exception {
		MappingIterator<String> iterator = createIterator("['a', 'b', 'c']");
		
		assertNotNull(iterator.getCurrentLocation());
	}
	
	// Helper method to create a MappingIterator for testing
	private MappingIterator<String> createIterator(String json) throws Exception {
		return objectMapper.readValues(json, String.class);
	}

	// Helper class for testing deserialization
	@JsonDeserialize(using = CustomDeserializer.class)
	private static class CustomDeserializedObject {
		String value;
	}

	// Helper deserializer class for testing
	private static class CustomDeserializer extends StdDeserializer<CustomDeserializedObject> {
		private static final long serialVersionUID = 1L;

		protected CustomDeserializer() {
			super(CustomDeserializedObject.class);
		}

		@Override
		public CustomDeserializedObject deserialize(com.fasterxml.jackson.core.JsonParser p,
				com.fasterxml.jackson.databind.DeserializationContext ctxt)
				throws com.fasterxml.jackson.core.JsonProcessingException, java.io.IOException {
			CustomDeserializedObject object = new CustomDeserializedObject();
			object.value = p.getValueAsString();
			return object;
		}
	}
}