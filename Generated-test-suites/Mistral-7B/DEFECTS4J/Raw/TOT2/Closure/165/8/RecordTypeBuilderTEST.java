import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import org.mockito.Mockito;

public class RecordTypeBuilderTest {

  @Test
  public void testRecordTypeBuilder() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    RecordType type = builder.build();
    verify(registry).getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertEquals(JSType.OBJECT_TYPE, type.getType());
  }

  @Test
  public void testAddProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    String name = "testName";
    JSType type = new IntType();
    Node propertyNode = new Node();
    RecordProperty property = builder.addProperty(name, type, propertyNode);
    assertEquals(type, property.getType());
  }

  @Test
  public void testAddDuplicateProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    String name = "testName";
    JSType type = new IntType();
    Node propertyNode = new Node();
    RecordProperty property = builder.addProperty(name, type, propertyNode);
    assertNull(builder.addProperty(name, type, propertyNode));
  }

  @Test
  public void testAddPropertyWithNullRegistry() {
    assertNull(new RecordTypeBuilder(null));
  }

  @Test(expected = NullPointerException.class)
  public void testAddPropertyWithNullName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    builder.addProperty(null, new IntType(), new Node());
  }

  @Test(expected = NullPointerException.class)
  public void testAddPropertyWithNullType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    builder.addProperty("testName", null, new Node());
  }

  @Test(expected = NullPointerException.class)
  public void testAddPropertyWithNullPropertyNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    builder.addProperty("testName", new IntType(), null);
  }

  @Test
  public void testAddPropertyWithExistingName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    String name = "testName";
    JSType type = new IntType();
    Node propertyNode = new Node();
    RecordProperty property1 = builder.addProperty(name, type, propertyNode);
    assertNull(builder.addProperty(name, type, propertyNode));
  }

  @Test
  public void testBuildEmptyRecord() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType emptyRecord = builder.build();
    assertEquals(JSType.OBJECT_TYPE, emptyRecord.getType());
  }

  @Test
  public void testBuildRecordWithProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    RecordProperty property1 = builder.addProperty("testName1", new IntType(), new Node());
    RecordProperty property2 = builder.addProperty("testName2", new StringType(), new Node());
    RecordType record = builder.build();
    assertEquals(2, record.getProperties().size());
    assertEquals(property1.getType(), record.getProperty("testName1").getType());
    assertEquals(property2.getType(), record.getProperty("testName2").getType());
  }

  @Test
  public void testBuildRecordWithSameNameButDifferentTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    RecordProperty property1 = builder.addProperty("testName", new IntType(), new Node());
    RecordProperty property2 = builder.addProperty("testName", new StringType(), new Node());
    RecordType record = builder.build();
    assertEquals(2, record.getProperties().size());
    assertNotSame(property1.getType(), property2.getType());
  }

  @Test
  public void testBuildRecordWithSameNameButNullTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    RecordProperty property1 = builder.addProperty("testName", new IntType(), new Node());
    RecordProperty property2 = builder.addProperty("testName", null, new Node());
    RecordType record = builder.build();
    assertEquals(2, record.getProperties().size());
    assertNotNull(property1.getType());
    assertNull(property2.getType());
  }

  @Test
  public void testBuildRecordWithEmptyPropertiesList() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    RecordType record = builder.build();
    assertEquals(JSType.OBJECT_TYPE, record.getType());
  }

  @Test
  public void testBuildRecordWithNullPropertiesList() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    RecordType record = builder.build();
    assertEquals(JSType.OBJECT_TYPE, record.getType());
  }

  @Test
  public void testBuildRecordWithEmptyRegistry() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    RecordType record = builder.build();
    assertEquals(JSType.OBJECT_TYPE, record.getType());
  }

}