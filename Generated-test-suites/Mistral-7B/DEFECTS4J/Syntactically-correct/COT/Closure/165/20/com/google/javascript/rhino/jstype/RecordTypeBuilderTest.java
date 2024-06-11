import org.junit.Test;
import org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import java.util.HashMap;
import java.util.Map;

public class RecordTypeBuilderTest {

 @Test
 public void testRecordTypeBuilderWithEmptyRecord() {
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry());
 JSType actualRecordType = recordTypeBuilder.build();
 JSType expectedRecordType = recordTypeRegistry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 assertEquals("Expected an empty record to be of Object type.", expectedRecordType, actualRecordType);
 }

 @Test
 public void testRecordTypeBuilderWithSingleProperty() {
 RecordTypeRegistry recordTypeRegistry = new RecordTypeRegistry();
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(recordTypeRegistry);
 String propertyName = "name";
 JSType propertyType = recordTypeRegistry.getStringType();
 Node propertyNode = new Node(); // Assuming a valid Node for the property
 RecordTypeBuilder addPropertyResult = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
 JSType actualRecordType = addPropertyResult.build();
 RecordType expectedRecordType = new RecordType(recordTypeRegistry, Map.of(propertyName, new RecordProperty(propertyType, propertyNode)));
 assertEquals("Record type with single property not built correctly.", expectedRecordType, actualRecordType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRecordTypeBuilderWithDuplicateProperty() {
 RecordTypeRegistry recordTypeRegistry = new RecordTypeRegistry();
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(recordTypeRegistry);
 String propertyName = "name";
 JSType propertyType = recordTypeRegistry.getStringType();
 Node propertyNode = new Node(); // Assuming a valid Node for the property
 RecordTypeBuilder addPropertyResult1 = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
 RecordTypeBuilder addPropertyResult2 = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
 }

 @Test
 public void testRecordTypeBuilderWithMultipleProperties() {
 RecordTypeRegistry recordTypeRegistry = new RecordTypeRegistry();
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(recordTypeRegistry);
 String propertyName1 = "name1";
 JSType propertyType1 = recordTypeRegistry.getStringType();
 Node propertyNode1 = new Node(); // Assuming a valid Node for the property 1
 String propertyName2 = "name2";
 JSType propertyType2 = recordTypeRegistry.getNumberType();
 Node propertyNode2 = new Node(); // Assuming a valid Node for the property 2
 RecordProperty addPropertyResult1 = recordTypeBuilder.addProperty(propertyName1, propertyType1, propertyNode1);
 RecordProperty addPropertyResult2 = recordTypeBuilder.addProperty(propertyName2, propertyType2, propertyNode2);
 JSType actualRecordType = recordTypeBuilder.build();
 RecordType expectedRecordType = new RecordType(recordTypeRegistry, Map.of(propertyName1, addPropertyResult1.getProperty(), propertyName2, addPropertyResult2.getProperty()));
 assertEquals("Record type with multiple properties not built correctly.", expectedRecordType, actualRecordType);
 }

 @Test(expected = NullPointerException.class)
 public void testRecordTypeBuilderWithNullType() {
 RecordTypeRegistry recordTypeRegistry = new RecordTypeRegistry();
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(recordTypeRegistry);
 String propertyName = "name";
 Node propertyNode = new Node(); // Assuming a valid Node for the property
 RecordTypeBuilder addPropertyResult = recordTypeBuilder.addProperty(propertyName, null, propertyNode);
 }
}