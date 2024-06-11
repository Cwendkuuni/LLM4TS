import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RecordTypeBuilderTest {

 private JSTypeRegistry registry;
 private RecordTypeBuilder recordTypeBuilder;

 @Before
 public void setup() {
 registry = Mockito.mock(JSTypeRegistry.class);
 recordTypeBuilder = new RecordTypeBuilder(registry);
 }

 @Test
 public void testAddPropertyWithEmptyRecordType() {
 // Test adding a property to an empty record type
 JSType type = Mockito.mock(JSType.class);
 Node propertyNode = Mockito.mock(Node.class);

 when(registry.getNativeObjectType(any(JSTypeNative.class))).thenReturn(type);

 RecordProperty recordProperty = recordTypeBuilder.addProperty("testProperty", type, propertyNode);
 assertNotNull(recordProperty);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPropertyWithDuplicateName() {
 // Test adding a property with a duplicate name
 JSType type = Mockito.mock(JSType.class);
 Node propertyNode = Mockito.mock(Node.class);
 String propertyName = "testProperty";

 when(registry.getNativeObjectType(any(JSTypeNative.class))).thenReturn(type);

 recordTypeBuilder.addProperty(propertyName, type, propertyNode);
 recordTypeBuilder.addProperty(propertyName, type, propertyNode);
 }

 @Test
 public void testBuildEmptyRecordType() {
 // Test building an empty record type
 JSType recordType = recordTypeBuilder.build();
 assertNotNull(recordType);
 }

 @Test
 public void testBuildRecordTypeWithProperties() {
 // Test building a record type with properties
 JSType stringType = Mockito.mock(JSType.class);
 JSType numberType = Mockito.mock(JSType.class);
 Node propertyNode1 = Mockito.mock(Node.class);
 Node propertyNode2 = Mockito.mock(Node.class);
 String propertyName1 = "testProperty1";
 String propertyName2 = "testProperty2";

 when(registry.getNativeObjectType(any(JSTypeNative.class))).thenReturn(JSType.OBJECT);
 when(registry.getQualifiedName(any(Node.class))).then(invocation -> invocation.getArgument(0).toString());

 RecordProperty recordProperty1 = recordTypeBuilder.addProperty(propertyName1, stringType, propertyNode1);
 RecordProperty recordProperty2 = recordTypeBuilder.addProperty(propertyName2, numberType, propertyNode2);

 JSType recordType = recordTypeBuilder.build();

 verify(registry, times(1)).getNativeObjectType(any(JSTypeNative.class));
 verify(registry, times(2)).getQualifiedName(any(Node.class));

 assertNotNull(recordType);
 Map<String, JSType> expectedProperties = new HashMap<>();
 expectedProperties.put(propertyName1, stringType);
 expectedProperties.put(propertyName2, numberType);

 RecordType expectedRecordType = new RecordType(registry, Collections.unmodifiableMap(expectedProperties));
 assertEquals(expectedRecordType, recordType);
 }
}