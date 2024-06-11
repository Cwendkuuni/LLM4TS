import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testForProperty() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 BeanProperty property = new BeanProperty.StdGetter(MyClass.class, "myProperty");
 TypeDeserializer forProperty = typeDeserializerBase.forProperty(property);
 assertNotNull(forProperty);
 assertEquals(MyClass.class, forProperty.getType(mapper.getTypeFactory()));
 }

 @Test
 public void testGetTypeInclusion() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 assertEquals(JsonTypeInfo.As.WRAP_EXISTING_PROPERTY, typeDeserializerBase.getTypeInclusion());
 }

 @Test
 public void testBaseTypeName() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 assertEquals("my.package.MyBaseClass", typeDeserializerBase.baseTypeName());
 }

 @Test
 public void testGetDefaultImpl() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 assertNull(typeDeserializerBase.getDefaultImpl());
 }

 @Test
 public void testToString() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 String toString = typeDeserializerBase.toString();
 assertTrue(toString.contains("[com.mypackage.TypeDeserializerBase; base-type:"));
 }

 @Test
 public void testFindDeserializer_NullContext() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 assertNull(typeDeserializerBase._findDeserializer(null, "typeId"));
 }

 @Test
 public void testFindDeserializer_NullTypeId() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(mapper.getDeserializationContext(), null);
 assertNotNull(deserializer);
 // Check that it is a default deserializer
 assertSame(NullifyingDeserializer.instance, deserializer);
 }

 @Test
 public void testFindDeserializer_ValidTypeId() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(mapper.getDeserializationContext(), "typeId");
 assertNotNull(deserializer);
 // Check that it is a custom deserializer
 assertNotNull(deserializer.getClass().getAnnotation(JsonDeserializer.Class.class));
 }

 @Test(expected = IOException.class)
 public void testFindDeserializer_UnknownTypeId() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 typeDeserializerBase._findDeserializer(mapper.getDeserializationContext(), "unknownTypeId");
 fail("Expected IOException not thrown");
 }

 @Test
 public void testFindDefaultImplDeserializer_NullContext() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 assertNull(typeDeserializerBase._findDefaultImplDeserializer(null));
 }

 @Test
 public void testFindDefaultImplDeserializer_WithDefault() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 assertNotNull(typeDeserializerBase._findDefaultImplDeserializer(mapper.getDeserializationContext()));
 }

 @Test
 public void testDeserializeWithNativeTypeId_NullContext() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 assertNull(typeDeserializerBase._deserializeWithNativeTypeId(null, mapper.getDeserializationContext()));
 }

 @Test
 public void testDeserializeWithNativeTypeId_EmptyTypeId() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 assertNull(typeDeserializerBase._deserializeWithNativeTypeId(mapper.getJsonFactory().createParser(""), mapper.getDeserializationContext()));
 }

 @Test
 public void testDeserializeWithNativeTypeId_ValidTypeId() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 Object deserialized = typeDeserializerBase._deserializeWithNativeTypeId(mapper.getJsonFactory().createParser("{\"typeId\":\"MyTypeDeserializer\"}"), mapper.getDeserializationContext());
 assertNotNull(deserialized);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNativeTypeId_InvalidTypeId() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 typeDeserializerBase._deserializeWithNativeTypeId(mapper.getJsonFactory().createParser("{\"typeId\":\"InvalidTypeDeserializer\"}"), mapper.getDeserializationContext());
 fail("Expected IOException not thrown");
 }

 @Test
 public void testHandleUnknownTypeId() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 assertNull(typeDeserializerBase._handleUnknownTypeId(mapper.getDeserializationContext(), "unknownTypeId"));
 }

 @Test
 public void testHandleMissingTypeId() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 assertNull(typeDeserializerBase._handleMissingTypeId(mapper.getDeserializationContext(), ""));
 }

}

class MyTypeDeserializerBase extends TypeDeserializerBase {
 // Empty implementation
}

class MyClass {
 String myProperty;
}