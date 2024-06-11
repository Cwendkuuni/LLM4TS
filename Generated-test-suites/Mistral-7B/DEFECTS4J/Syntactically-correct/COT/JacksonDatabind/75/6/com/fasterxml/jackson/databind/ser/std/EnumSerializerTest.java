import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.StdScalarSerializer;
import com.fasterxml.jackson.databind.ser.impl.EnumValues;
import com.fasterxml.jackson.databind.util.JsonMappingException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@JacksonStdImpl
public class EnumSerializerTest {

 private EnumSerializer enumSerializer;
 private EnumValues enumValues;
 private SerializerProvider serializers;
 private ObjectNode schemaNode;

 @Before
 public void setup() {
 Enum enumType = Enum.class;
 Class<Enum<?>> enumClass = (Class<Enum<?>>) enumType;
 enumValues = new EnumValues(enumClass);
 serializers = mock(SerializerProvider.class);
 enumSerializer = new EnumSerializer(enumValues, false);
 }

 @Test
 public void testConstructor() {
 EnumSerializer serializer = new EnumSerializer(enumValues, null);
 }

 @Test
 public void testConstructStatic() throws JsonProcessingException {
 Class<?> enumClass = Enum.class;
 ObjectNode schemaNode = mock(ObjectNode.class);
 SerializationConfig config = mock(SerializationConfig.class);
 BeanDescription beanDesc = mock(BeanDescription.class);
 JsonFormat.Value format = mock(JsonFormat.Value.class);
 EnumSerializer serializer = EnumSerializer.construct(enumClass, config, beanDesc, format);
 }

 @Test
 public void testCreateContextual() throws JsonProcessingException {
 BeanProperty property = mock(BeanProperty.class);
 JsonFormat.Value format = mock(JsonFormat.Value.class);
 JsonSerializer<EnumSerializer> serializedSerializer = enumSerializer.createContextual(serializers, property);
 assertEquals(enumSerializer.getClass(), serializedSerializer.getClass());

 JsonFormat.Value format2 = mock(JsonFormat.Value.class);
 JsonSerializer<EnumSerializer> serializedSerializer2 = enumSerializer.createContextual(serializers, null);
 assertEquals(enumSerializer.getClass(), serializedSerializer2.getClass());

 JsonFormat.Value format3 = mock(JsonFormat.Value.class);
 when(format3.getShape()).thenReturn(JsonFormat.Shape.INTEGER);
 JsonSerializer<EnumSerializer> serializedSerializer3 = enumSerializer.createContextual(serializers, null);
 assertNull(serializedSerializer3);
 }

 @Test
 public void testSerialize() throws JsonProcessingException {
 JsonGenerator gen = mock(JsonGenerator.class);
 enumSerializer.serialize(Enum.values()[0], gen, serializers);
 String expected = enumValues.serializedValueFor(Enum.values()[0]);
 when(gen.writeString(expected)).thenReturn(expected);
 assertEquals(expected, gen.getOutputText());

 when(gen.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);
 when(gen.writeNumber(0)).thenReturn(0);
 enumSerializer.serialize(Enum.values()[0], gen, serializers);
 assertEquals(0, gen.getOutputNumberValue());

 when(gen.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);
 when(gen.writeString(Enum.values()[0].toString())).thenReturn(Enum.values()[0].toString());
 enumSerializer.serialize(Enum.values()[0], gen, serializers);
 assertEquals(Enum.values()[0].toString(), gen.getOutputText());
 }

 @Test
 public void testGetSchema() throws JsonProcessingException {
 schemaNode = mock(ObjectNode.class);
 when(schemaNode.get("type")).thenReturn("string");
 when(schemaNode.putArray("enum")).thenReturn(schemaNode);
 when(schemaNode.get("type")).thenReturn("integer");
 when(schemaNode.putArray("enum")).thenReturn(schemaNode);
 JavaType javaType = mock(JavaType.class);
 when(javaType.isEnumType()).thenReturn(true);
 when(serializers.constructType(javaType)).thenReturn(javaType);
 assertEquals(schemaNode, enumSerializer.getSchema(serializers, javaType));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSchemaWithInvalidShape() throws JsonProcessingException {
 schemaNode = mock(ObjectNode.class);
 when(schemaNode.get("type")).thenReturn("hexadecimal");
 JavaType javaType = mock(JavaType.class);
 when(javaType.isEnumType()).thenReturn(true);
 when(serializers.constructType(javaType)).thenReturn(javaType);
 enumSerializer.getSchema(serializers, javaType);
 }
}