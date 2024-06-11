import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeBuilderTest {

 private JSTypeRegistry registry;

 @Before
 public void setUp() {
  registry = new JSTypeRegistry();
 }

 @Test
 public void testAddProperty_typicalCase() {
  RecordTypeBuilder builder = new RecordTypeBuilder(registry);
  JSType expectedType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
  Node propertyNode = new Node();

  builder.addProperty("x", expectedType, propertyNode);
  JSType actualType = builder.build().getPropertyType("x");

  assertEquals(expectedType, actualType);
 }

 @Test
 public void testAddProperty_duplicateName() {
  RecordTypeBuilder builder = new RecordTypeBuilder(registry);
  JSType type1 = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
  JSType type2 = registry.getNativeType(JSTypeNative.STRING_TYPE);
  Node propertyNode = new Node();

  builder.addProperty("x", type1, propertyNode);
  assertNull(builder.addProperty("x", type2, propertyNode));
 }

 @Test
 public void testBuild_emptyRecord() {
  RecordTypeBuilder builder = new RecordTypeBuilder(registry);
  JSType expectedType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

  JSType actualType = builder.build();

  assertEquals(expectedType, actualType);
 }

 @Test
 public void testBuild_nonEmptyRecord() {
  RecordTypeBuilder builder = new RecordTypeBuilder(registry);
  JSType type1 = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
  JSType type2 = registry.getNativeType(JSTypeNative.STRING_TYPE);
  Node propertyNode = new Node();

  builder.addProperty("x", type1, propertyNode);
  builder.addProperty("y", type2, propertyNode);

  RecordType expectedType = new RecordType(registry, Maps.newHashMap());
  expectedType.addProperty("x", type1, propertyNode);
  expectedType.addProperty("y", type2, propertyNode);

  JSType actualType = builder.build();

  assertEquals(expectedType, actualType);
 }
}