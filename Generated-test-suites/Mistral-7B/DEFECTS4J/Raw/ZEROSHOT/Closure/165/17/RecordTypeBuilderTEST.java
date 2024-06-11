import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import static com.google.javascript.rhino.NodeFactory.createString;
import static com.google.javascript.rhino.NodeFactory.createObject;
import static com.google.javascript.rhino.NodeFactory.createNamedProperty;
import static com.google.javascript.rhino.NodeFactory.createList;
import static com.google.javascript.rhino.NodeFactory.createProperty;
import static com.google.javascript.rhino.JSTypeNative.OBJECT_TYPE;
import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.RecordType;
import com.google.javascript.rhino.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {

 private JSTypeRegistry registry;
 private RecordTypeBuilder recordTypeBuilder;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 recordTypeBuilder = new RecordTypeBuilder(registry);
 }

 @Test
 public void testBuildEmptyRecord() {
 JSType recordType = recordTypeBuilder.build();
 assertEquals(OBJECT_TYPE, recordType.getType());
 }

 @Test
 public void testAddProperty() {
 String name = "name";
 JSType stringType = String.getInstance();
 Node propertyNode = createNamedProperty(createString(name), stringType);
 recordTypeBuilder.addProperty(name, stringType, propertyNode);
 RecordType recordType = (RecordType) recordTypeBuilder.build();
 Map<String, JSType> properties = recordType.getProperties();
 assertEquals(1, properties.size());
 assertEquals(stringType, properties.get(name));
 }

 @Test
 public void testAddDuplicateProperty() {
 String name = "name";
 JSType stringType = String.getInstance();
 recordTypeBuilder.addProperty(name, stringType, null);
 Node propertyNode = createNamedProperty(createString(name), stringType);
 recordTypeBuilder.addProperty(name, stringType, propertyNode);
 RecordType recordType = (RecordType) recordTypeBuilder.build();
 Map<String, JSType> properties = recordType.getProperties();
 assertEquals(1, properties.size());
 }

 @Test
 public void testAddMultipleProperties() {
 String name1 = "name1";
 JSType stringType1 = String.getInstance();
 Node propertyNode1 = createNamedProperty(createString(name1), stringType1);
 recordTypeBuilder.addProperty(name1, stringType1, propertyNode1);

 String name2 = "name2";
 JSType stringType2 = String.getInstance();
 Node propertyNode2 = createNamedProperty(createString(name2), stringType2);
 recordTypeBuilder.addProperty(name2, stringType2, propertyNode2);

 RecordType recordType = (RecordType) recordTypeBuilder.build();
 Map<String, JSType> properties = recordType.getProperties();
 assertEquals(2, properties.size());
 assertEquals(stringType1, properties.get(name1));
 assertEquals(stringType2, properties.get(name2));
 }

 @Test
 public void testAddPropertyWithList() {
 String name = "name";
 JSType listType = new ArrayType(String.getInstance());
 Node propertyNode = createProperty(createString(name), listType);
 recordTypeBuilder.addProperty(name, listType, propertyNode);
 RecordType recordType = (RecordType) recordTypeBuilder.build();
 Map<String, JSType> properties = recordType.getProperties();
 assertEquals(1, properties.size());
 assertEquals(listType, properties.get(name));
 }
}