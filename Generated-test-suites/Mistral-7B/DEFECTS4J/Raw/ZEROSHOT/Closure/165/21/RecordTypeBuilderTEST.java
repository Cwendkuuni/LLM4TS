import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;
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
 public void testAddPropertyDuplicate() {
 String name = "testProperty";
 JSType type = new JSTypeString();
 Node propertyNode = new Node();

 builder.addProperty(name, type, propertyNode);
 assertNull(builder.addProperty(name, type, propertyNode));
 }

 @Test
 public void testAddPropertyNullType() {
 String name = "testProperty";
 Node propertyNode = new Node();

 assertNull(builder.addProperty(name, null, propertyNode));
 }

 @Test
 public void testAddPropertyNullPropertyNode() {
 String name = "testProperty";
 JSType type = new JSTypeString();

 assertNull(builder.addProperty(name, type, null));
 }

 @Test
 public void testAddPropertyNullName() {
 JSType type = new JSTypeString();
 Node propertyNode = new Node();

 assertNull(builder.addProperty(null, type, propertyNode));
 }

 @Test
 public void testBuildEmptyRecord() {
 JSType recordType = builder.build();
 assertNotNull(recordType);
 }

 @Test
 public void testBuildRecordWithOneProperty() {
 String name = "testProperty";
 JSType type = new JSTypeString();
 Node propertyNode = new Node();

 RecordProperty recordProperty = builder.addProperty(name, type, propertyNode);
 JSType recordType = builder.build();

 assertNotNull(recordType);
 assertNotNull(recordType.getMembers());
 RecordProperty property = (RecordProperty) recordType.getMembers().get(name);
 assertNotNull(property);
 assertNotNull(property.getType());
 assertNotNull(property.getPropertyNode());
 }

 @Test
 public void testBuildRecordWithTwoProperties() {
 String name1 = "testProperty1";
 String name2 = "testProperty2";
 JSType type1 = new JSTypeString();
 JSType type2 = new JSTypeNumber();
 Node propertyNode1 = new Node();
 Node propertyNode2 = new Node();

 RecordProperty property1 = builder.addProperty(name1, type1, propertyNode1);
 RecordProperty property2 = builder.addProperty(name2, type2, propertyNode2);
 JSType recordType = builder.build();

 assertNotNull(recordType);
 assertNotNull(recordType.getMembers());
 RecordProperty property1FromRecord = (RecordProperty) recordType.getMembers().get(name1);
 RecordProperty property2FromRecord = (RecordProperty) recordType.getMembers().get(name2);
 assertNotNull(property1FromRecord);
 assertNotNull(property1FromRecord.getType());
 assertNotNull(property1FromRecord.getPropertyNode());
 assertNotNull(property2FromRecord);
 assertNotNull(property2FromRecord.getType());
 assertNotNull(property2FromRecord.getPropertyNode());
 }
}