import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class RecordTypeBuilderTest {

 private JSTypeRegistry registry;
 private RecordTypeBuilder builder;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 builder = new RecordTypeBuilder(registry);
 }

 @Test
 public void testEmptyRecord() {
 JSType recordType = builder.build();
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getType());
 }

 @Test
 public void testAddProperty() {
 String name = "name";
 String expectedType = "String";
 JSType propertyType = registry.getSimpleType(expectedType);
 Node propertyNode = Node.newText(name);

 RecordTypeBuilder recordBuilder = new RecordTypeBuilder(registry);
 RecordTypeBuilder returnedBuilder = recordBuilder.addProperty(name, propertyType, propertyNode);
 assertNotNull(returnedBuilder);
 JSType recordType = recordBuilder.build();

 RecordProperty property = recordType.getProperties().get(name);
 assertNotNull(property);
 assertEquals(propertyType, property.getType());
 }

 @Test
 public void testAddDuplicateProperty() {
 String name = "name";
 String expectedType = "String";
 JSType propertyType = registry.getSimpleType(expectedType);
 Node propertyNode = Node.newText(name);

 RecordTypeBuilder recordBuilder = new RecordTypeBuilder(registry);
 RecordProperty firstProperty = recordBuilder.addProperty(name, propertyType, propertyNode);
 RecordProperty secondProperty = recordBuilder.addProperty(name, propertyType, propertyNode);
 assertNull(secondProperty);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullType() {
 String name = "name";
 Node propertyNode = Node.newText(name);

 RecordTypeBuilder recordBuilder = new RecordTypeBuilder(registry);
 recordBuilder.addProperty(name, null, propertyNode);
 }
}