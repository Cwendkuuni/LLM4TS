import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {
  private XmlRootNameLookup rootNames;
  private SerializationConfig config;
  private SerializerFactory factory;
  private DefaultSerializerProvider src;

  @Before
  public void setup() {
    rootNames = mock(XmlRootNameLookup.class);
    config = mock(SerializationConfig.class);
    factory = mock(SerializerFactory.class);
    src = new DefaultSerializerProvider(rootNames);
  }

  @Test
  public void testCopy() {
    DefaultSerializerProvider copy = src.copy();
    assertNotSame(copy, src);
  }

  @Test
  public void testCreateInstance() {
    DefaultSerializerProvider instance = src.createInstance(config, factory);
    assertNotSame(instance, src);
  }

  @Test
  public void testSerializeValueWithNullValue() throws IOException {
    JsonGenerator gen = mock(JsonGenerator.class);
    Object value = null;
    src.serializeValue(gen, value);
    verify(gen, times(1)).writeShort(anyShort());
  }

  @Test
  public void testSerializeValueWithNotNullValue() throws IOException {
    JsonGenerator gen = mock(JsonGenerator.class);
    Object value = mock(Object.class);
    src.serializeValue(gen, value);
    verify(gen, times(1)).writeShort(anyShort());
  }

  @Test
  public void testSerializeValueWithNullGen() throws IOException {
    JsonGenerator gen = null;
    Object value = mock(Object.class);
    src.serializeValue(gen, value);
  }

  @Test
  public void testSerializeValueWithGenAndRootType() throws IOException {
    JsonGenerator gen = mock(JsonGenerator.class);
    Object value = mock(Object.class);
    JavaType rootType = mock(JavaType.class);
    JsonSerializer<Object> ser = null;
    src.serializeValue(gen, value, rootType, ser);
    verify(gen, times(1)).writeShort(anyShort());
  }

  @Test
  public void testSerializeValueWitGenRootTypeAndSer() throws IOException {
    JsonGenerator gen = mock(JsonGenerator.class);
    Object value = mock(Object.class);
    JavaType rootType = mock(JavaType.class);
    JsonSerializer<Object> ser = mock(JsonSerializer.class);
    src.serializeValue(gen, value, rootType, ser);
    verify(gen, times(1)).writeShort(anyShort());
  }

  @Test
  public void testSerializeValueWitGenNullRoot() throws IOException {
    JsonGenerator gen = mock(JsonGenerator.class);
    Object value = mock(Object.class);
    JavaType rootType = mock(JavaType.class);
    JsonSerializer<Object> ser = mock(JsonSerializer.class);
    when(rootNames.findRootName(any(), any())).thenReturn(null);
    src.serializeValue(gen, value, rootType, ser);
    verify(gen, times(1)).writeShort(anyShort());
  }

  @Test
  public void testSerializeValueWitGenAndArray() throws IOException {
    JsonGenerator gen = mock(JsonGenerator.class);
    Object value = mock(Object.class);
    JavaType rootType = mock(JavaType.class);
    when(TypeUtil.isIndexedType(any())).thenReturn(true);
    src.serializeValue(gen, value, rootType, null);
    verify(gen, times(1)).writeEndObject();
  }

  @Test
  public void testSerializeValueWitGenAndNonArray() throws IOException {
    JsonGenerator gen = mock(JsonGenerator.class);
    Object value = mock(Object.class);
    JavaType rootType = mock(JavaType.class);
    when(TypeUtil.isIndexedType(any())).thenReturn(false);
    src.serializeValue(gen, value, rootType, null);
    verify(gen, times(0)).writeEndObject();
  }

  @Test
  public void testSerializeValueWitGenAndNonXmlGen() throws IOException {
    JsonGenerator gen = mock(JsonGenerator.class);
    Object value = mock(Object.class);
    JavaType rootType = mock(JavaType.class);
    when(gen instanceof com.fasterxml.jackson.dataformat.xml.ser.XmlGenerator).thenReturn(false);
    src.serializeValue(gen, value, rootType, null);
    verify(gen, times(0)).writeEndObject();
  }

  @Test
  public void testSerializeValueWitGenAndXmlGen() throws IOException {
    JsonGenerator gen = mock(JsonGenerator.class);
    Object value = mock(Object.class);
    JavaType rootType = mock(JavaType.class);
    when(gen instanceof com.fasterxml.jackson.dataformat.xml.ser.XmlGenerator).thenReturn(true);
    when(rootNames.findRootName(any(), any())).thenReturn(null);
    src.serializeValue(gen, value, rootType, null);
    verify(gen, times(0)).writeEndObject();
  }

  @Test
  public void test_serializeXmlNull() throws IOException {
    JsonGenerator gen = mock(JsonGenerator.class);
    src._serializeXmlNull(gen);
    verify(gen, times(1)).writeShort(anyShort());
  }

  @Test
  public void test_initWithRootName() throws IOException {
    com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator xgen = mock(com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator.class);
    QName rootName = mock(QName.class);
    src._initWithRootName(xgen, rootName);
    verify(xgen, times(1)).initGenerator();
  }

  @Test
  public void test_initWithRootName_setNextNameIfMissing() throws IOException {
    com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator xgen = mock(com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator.class);
    QName rootName = mock(QName.class);
    when(xgen.setNextNameIfMissing(any())).thenReturn(true);
    src._initWithRootName(xgen, rootName);
    verify(xgen, times(0)).setNextName(any());
  }

  @Test
  public void test_initWithRootName_setNextNameInRoot() throws IOException {
    com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator xgen = mock(com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator.class);
    QName rootName = mock(QName.class);
    when(xgen.inRoot()).thenReturn(true);
    src._initWithRootName(xgen, rootName);
    verify(xgen, times(1)).setNextName(any());
  }

  @Test
  public void test_startRootArray() throws IOException {
    com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator xgen = mock(com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator.class);
    QName rootName = mock(QName.class);

    src._startRootArray(xgen, rootName);

    verify(xgen, times(1)).writeStartObject();
    verify(xgen, times(1)).writeFieldName("item");
  }
}