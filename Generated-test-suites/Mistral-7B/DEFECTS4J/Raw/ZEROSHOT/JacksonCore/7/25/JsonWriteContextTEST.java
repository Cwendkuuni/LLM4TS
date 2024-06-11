import com.fasterxml.jackson.core.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

 private JsonWriteContext rootContext;
 private JsonWriteContext arrayContext;
 private JsonWriteContext objectContext;
 private JsonWriteContext objectContextWithName;
 private DupDetector noDupDetector;
 private DupDetector dupDetector;

 @Before
 public void setup() {
 noDupDetector = new DupDetector();
 dupDetector = new DupDetector(noDupDetector);
 rootContext = JsonWriteContext.createRootContext(dupDetector);
 arrayContext = rootContext.createChildArrayContext();
 objectContext = rootContext.createChildObjectContext();
 objectContextWithName = objectContext.reset(JsonWriteContext.TYPE_OBJECT);
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 String name = "testField";

 // Root context should not accept field name
 assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, rootContext.writeFieldName(name));

 // Array context should accept field name
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, arrayContext.writeFieldName(name));

 // Object context should accept field name
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, objectContext.writeFieldName(name));

 // Object context with name should store name
 assertEquals(name, objectContextWithName.getCurrentName());
 }

 @Test
 public void testWriteValue() throws JsonProcessingException {
 assertEquals(JsonWriteContext.STATUS_OK_AS_IS, rootContext.writeValue());
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, arrayContext.writeValue());
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, rootContext.writeValue());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameWithDupDetector() throws JsonProcessingException {
 String name = "testField";
 objectContextWithName.writeFieldName(name);
 objectContextWithName.writeFieldName(name);
 }
}