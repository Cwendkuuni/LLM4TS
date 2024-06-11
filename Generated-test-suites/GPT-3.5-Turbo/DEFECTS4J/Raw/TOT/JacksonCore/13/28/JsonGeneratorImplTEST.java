import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JsonGeneratorImplTest {
	
	private JsonGeneratorImpl generator;
	
	@Before
	public void setUp() {
		generator = new JsonGeneratorImpl();
	}
	
	@After
	public void tearDown() {
		generator = null;
	}
	
	@Test
	public void testWriteStringField() throws IOException {
		generator.writeStringField("fieldName", "value");
		assertEquals("Field name is not written correctly", "fieldName", generator.getLastWrittenFieldName());
		assertEquals("String value is not written correctly", "value", generator.getLastWrittenString());
	}
	
	@Test
	public void testWriteStringField_withEmptyFieldName() throws IOException {
		generator.writeStringField("", "value");
		assertEquals("Empty field name is not written correctly", "", generator.getLastWrittenFieldName());
		assertEquals("String value is not written correctly", "value", generator.getLastWrittenString());
	}
	
	@Test
	public void testWriteStringField_withNullFieldName() throws IOException {
		generator.writeStringField(null, "value");
		assertNull("Null field name is not written correctly", generator.getLastWrittenFieldName());
		assertEquals("String value is not written correctly", "value", generator.getLastWrittenString());
	}
	
	@Test
	public void testWriteStringField_withEmptyValue() throws IOException {
		generator.writeStringField("fieldName", "");
		assertEquals("Field name is not written correctly", "fieldName", generator.getLastWrittenFieldName());
		assertEquals("Empty string value is not written correctly", "", generator.getLastWrittenString());
	}
	
	@Test
	public void testWriteStringField_withNullValue() throws IOException {
		generator.writeStringField("fieldName", null);
		assertEquals("Field name is not written correctly", "fieldName", generator.getLastWrittenFieldName());
		assertNull("Null string value is not written correctly", generator.getLastWrittenString());
	}

}