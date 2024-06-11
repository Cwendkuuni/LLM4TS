import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonMappingExceptionTest {

 // Test constructor with message only
 @Test
 public void testJsonMappingExceptionWithMessage() {
 JsonMappingException e = new JsonMappingException("Test JsonMappingException with message only");
 assertEquals("Test JsonMappingException with message only", e.getMessage());
 }

 // Test constructor with message and root cause
 @Test
 public void testJsonMappingExceptionWithMessageAndRootCause() {
 IOException rootCause = new IOException("Root cause test");
 JsonMappingException e = new JsonMappingException("Test JsonMappingException with message and root cause", rootCause);
 assertEquals("Test JsonMappingException with message and root cause", e.getMessage());
 assertEquals(rootCause, e.getCause());
 }

 // Test constructor with message, location and root cause
 @Test
 public void testJsonMappingExceptionWithMessageLocationAndRootCause() {
 LocationLocation loc = new Location("TestJsonMappingExceptionTest.java", 1, 1);
 JsonMappingException e = new JsonMappingException(loc, "Test JsonMappingException with message, location and root cause", new IOException("Root cause test"));
 assertEquals("Test JsonMappingException with message, location and root cause", e.getMessage());
 assertEquals(loc, e.getLocation());
 assertEquals(new IOException("Root cause test"), e.getCause());
 }

 // Test constructor from JsonParser
 @Test
 public void testJsonMappingExceptionFromJsonParser() {
 JsonParser parser = new JsonFactory().createParser("{}");
 JsonMappingException e = JsonMappingException.from(parser, "Test JsonMappingException from JsonParser");
 assertEquals("Test JsonMappingException from JsonParser", e.getMessage());
 }

 // Test constructor from JsonParser with message and root cause
 @Test
 public void testJsonMappingExceptionFromJsonParserWithMessageAndRootCause() {
 JsonParser parser = new JsonFactory().createParser("{}");
 IOException rootCause = new IOException("Root cause test");
 JsonMappingException e = JsonMappingException.from(parser, "Test JsonMappingException from JsonParser with message and root cause", rootCause);
 assertEquals("Test JsonMappingException from JsonParser with message and root cause", e.getMessage());
 assertEquals(rootCause, e.getCause());
 }

 // Test constructor from JsonParser with message, location and root cause
 @Test
 public void testJsonMappingExceptionFromJsonParserWithMessageLocationAndRootCause() {
 JsonParser parser = new JsonFactory().createParser("{}");
 LocationLocation loc = new Location("TestJsonMappingExceptionTest.json", 1, 1);
 JsonMappingException e = JsonMappingException.from(parser, loc, "Test JsonMappingException from JsonParser with message, location and root cause", new IOException("Root cause test"));
 assertEquals("Test JsonMappingException from JsonParser with message, location and root cause", e.getMessage());
 assertEquals(loc, e.getLocation());
 assertEquals(new IOException("Root cause test"), e.getCause());
 }

 // Test constructor from JsonGenerator
 @Test
 public void testJsonMappingExceptionFromJsonGenerator() {
 JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
 JsonMappingException e = JsonMappingException.from(generator, "Test JsonMappingException from JsonGenerator");
 assertEquals("Test JsonMappingException from JsonGenerator", e.getMessage());
 }

 // Test constructor from JsonGenerator with message and root cause
 @Test
 public void testJsonMappingExceptionFromJsonGeneratorWithMessageAndRootCause() {
 JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
 IOException rootCause = new IOException("Root cause test");
 JsonMappingException e = JsonMappingException.from(generator, "Test JsonMappingException from JsonGenerator with message and root cause", rootCause);
 assertEquals("Test JsonMappingException from JsonGenerator with message and root cause", e.getMessage());
 assertEquals(rootCause, e.getCause());
 }

 // Test constructor from JsonGenerator with message, location and root cause
 @Test
 public void testJsonMappingExceptionFromJsonGeneratorWithMessageLocationAndRootCause() {
 JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
 LocationLocation loc = new Location("TestJsonMappingExceptionTest.json", 1, 1);
 JsonMappingException e = JsonMappingException.from(generator, loc, "Test JsonMappingException from JsonGenerator with message, location and root cause", new IOException("Root cause test"));
 assertEquals("Test JsonMappingException from JsonGenerator with message, location and root cause", e.getMessage());
 assertEquals(loc, e.getLocation());
 assertEquals(new IOException("Root cause test"), e.getCause());
 }

 // Test factory method fromUnexpectedIOE
 @Test
 public void testJsonMappingExceptionFromUnexpectedIOE() {
 IOException ioException = new IOException("Unexpected IO exception");
 JsonMappingException e = JsonMappingException.fromUnexpectedIOE(ioException);
 assertEquals("Unexpected IOException (of type IOException): Unexpected IO exception", e.getMessage());
 }

 // Test method wrapWithPath
 Reference reference1 = new Reference(new Object(), "fieldName1");
 Reference reference2 = new Reference(new Object(), 2);
 @Test
 public void testJsonMappingExceptionWrapWithPath() {
 IOException ioException = new IOException("IO exception test");
 JsonMappingException e1 = JsonMappingException.wrapWithPath(ioException, new Object(), "fieldName1");
 assertEquals("IOException (was IOException): (was Object)->fieldName1", e1.getMessage());
 JsonMappingException e2 = JsonMappingException.wrapWithPath(ioException, new Object(), 2);
 assertEquals("IOException (was IOException): [Object, index=2]", e2.getMessage());
 }

 // Test getPath() method
 @Test
 public void testJsonMappingExceptionGetPath() {
 JsonMappingException e = new JsonMappingException(new Object(), "Test JsonMappingException");
 e.prependPath(new Object(), "fieldName1");
 e.prependPath(new Object(), "fieldName2");
 List<Reference> path = e.getPath();
 assertEquals(2, path.size());
 assertEquals("Object", path.get(0).getFrom().getClass().getName());
 assertEquals("fieldName1", path.get(0).getFieldName());
 assertEquals("Object", path.get(1).getFrom().getClass().getName());
 assertEquals("fieldName2", path.get(1).getFieldName());
 }

 // Test getPathReference() method
 @Test
 public void testJsonMappingExceptionGetPathReference() {
 JsonMappingException e = new JsonMappingException(new Object(), "Test JsonMappingException");
 e.prependPath(new Object(), "fieldName1");
 e.prependPath(new Object(), "fieldName2");
 StringBuilder sb = new StringBuilder();
 e.getPathReference(sb);
 assertEquals("(through reference chain: Object->fieldName1->Object->fieldName2)", sb.toString());
 }
}