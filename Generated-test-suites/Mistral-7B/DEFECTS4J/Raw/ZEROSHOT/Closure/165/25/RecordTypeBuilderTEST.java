import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RecordTypeBuilderTest {

 private JSTypeRegistry registry;
 private RecordTypeBuilder recordTypeBuilder;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 recordTypeBuilder = new RecordTypeBuilder(registry);
 }

 @Test
 public void testEmptyRecord() {
 JSType recordType = recordTypeBuilder.build();
 assertSame(JSTypeNative.OBJECT_TYPE, recordType.getClass());
 }

 @Test
 public void testAddProperty() {
 String propertyName = "name";
 JSType propertyType = registry.getSimpleType("String");
 Node propertyNode = Node.newText("name: String");

 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
 recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
 JSType recordType = recordTypeBuilder.build();
 RecordType expectedRecordType = new RecordType(
 registry, Map.of(propertyName, new RecordProperty(propertyType, propertyNode))
 );
 assertSame(expectedRecordType, recordType);
 }

 @Test(expected = NullPointerException.class)
 public void testAddDuplicateProperty() {
 String propertyName = "name";
 JSType propertyType = registry.getSimpleType("String");
 Node propertyNode = Node.newText("name: String");
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
 recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
 recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
 }
}