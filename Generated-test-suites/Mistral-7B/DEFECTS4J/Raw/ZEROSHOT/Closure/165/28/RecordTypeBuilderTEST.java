import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class RecordTypeBuilderTest {

 @Mock
 private JSTypeRegistry registry;

 @Before
 public void setUp() {
 }

 @Test
 public void testEmptyRecord() {
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
 JSType recordType = recordTypeBuilder.build();
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getType());
 }

 @Test
 public void testAddSingleProperty() {
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
 String propertyName = "testProperty";
 JSType propertyType = Mockito.mock(JSType.class);
 Node propertyNode = Mockito.mock(Node.class);
 RecordProperty expectedRecordProperty = new RecordProperty(propertyType, propertyNode);
 RecordTypeBuilder resultRecordTypeBuilder = recordTypeBuilder
     .addProperty(propertyName, propertyType, propertyNode);
 Map<String, RecordProperty> resultProperties = resultRecordTypeBuilder.build()
     .getProperties();

 Mockito.verify(registry).addNative(propertyType, propertyNode);
 assertEquals(1, resultProperties.size());
 assertEquals(expectedRecordProperty, resultProperties.get(propertyName));
 }

 @Test
 public void testAddDuplicateProperty() {
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
 String propertyName = "testProperty";
 JSType propertyType = Mockito.mock(JSType.class);
 Node propertyNode = Mockito.mock(Node.class);
 RecordProperty expectedRecordProperty = new RecordProperty(propertyType, propertyNode);
 RecordTypeBuilder resultRecordTypeBuilder = recordTypeBuilder
     .addProperty(propertyName, propertyType, propertyNode);
 RecordTypeBuilder duplicateRecordTypeBuilder = recordTypeBuilder
     .addProperty(propertyName, propertyType, propertyNode);

 Mockito.verify(registry, Mockito.times(1))
     .addNative(propertyType, propertyNode);
 assertEquals(1, resultRecordTypeBuilder.build().getProperties().size());
 assertNull(duplicateRecordTypeBuilder);
 }

 @Test
 public void testAddPropertyWithNullName() {
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
 String nullPropertyName = null;
 JSType propertyType = Mockito.mock(JSType.class);
 Node propertyNode = Mockito.mock(Node.class);
 RecordTypeBuilder resultRecordTypeBuilder = recordTypeBuilder
     .addProperty(nullPropertyName, propertyType, propertyNode);

 assertEquals(0, resultRecordTypeBuilder.build().getProperties().size());
 }

 @Test
 public void testAddPropertyWithNullType() {
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
 String propertyName = "testProperty";
 JSType nullPropertyType = null;
 Node propertyNode = Mockito.mock(Node.class);
 RecordTypeBuilder resultRecordTypeBuilder = recordTypeBuilder
     .addProperty(propertyName, nullPropertyType, propertyNode);

 assertEquals(0, resultRecordTypeBuilder.build().getProperties().size());
 }

 @Test
 public void testAddPropertyWithNullNode() {
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
 String propertyName = "testProperty";
 JSType propertyType = Mockito.mock(JSType.class);
 Node nullPropertyNode = null;
 RecordTypeBuilder resultRecordTypeBuilder = recordTypeBuilder
     .addProperty(propertyName, propertyType, nullPropertyNode);

 assertEquals(0, resultRecordTypeBuilder.build().getProperties().size());
 }
}