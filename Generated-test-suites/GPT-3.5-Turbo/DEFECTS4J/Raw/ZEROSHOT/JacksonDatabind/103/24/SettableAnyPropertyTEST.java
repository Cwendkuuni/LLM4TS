import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.AnnotatedField;
import java.lang.reflect.AnnotatedMember;
import java.lang.reflect.AnnotatedMethod;
import java.util.HashMap;
import java.util.Map;

public class SettableAnyPropertyTest {
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = TypeFactory.defaultInstance().constructType(Object.class);
        SettableAnyProperty sap = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        DeserializationContext context = new DeserializationContext(objectMapper);
        JsonParser parser = Mockito.mock(JsonParser.class);

        sap.deserializeAndSet(parser, context, new HashMap<>(), "property");

        Mockito.verify(keyDeserializer).deserializeKey("property", context);
        Mockito.verify(valueDeserializer).deserialize(parser, context);
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = TypeFactory.defaultInstance().constructType(Object.class);
        SettableAnyProperty sap = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        DeserializationContext context = new DeserializationContext(objectMapper);
        JsonParser parser = Mockito.mock(JsonParser.class);

        sap.deserialize(parser, context);

        Mockito.verify(valueDeserializer).deserialize(parser, context);
    }

    @Test
    public void testSet() throws IOException {
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedField setterField = Mockito.mock(AnnotatedField.class);
        JavaType type = TypeFactory.defaultInstance().constructType(Object.class);
        SettableAnyProperty sap = new SettableAnyProperty(property, setterField, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        sap.set(new HashMap<>(), "property", "value");

        Map<Object, Object> map = new HashMap<>();
        map.put("property", "value");
        Mockito.verify(setterField).getValue(map);
    }

    @Test
    public void testFixAccess() {
        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = TypeFactory.defaultInstance().constructType(Object.class);
        SettableAnyProperty sap = new SettableAnyProperty(property, setter, type, null, null, null);

        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
        Mockito.when(config.isEnabled(Mockito.any())).thenReturn(true);

        sap.fixAccess(config);

        Mockito.verify(setter).fixAccess(true);
    }

    @Test
    public void testReadResolve() {
        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = TypeFactory.defaultInstance().constructType(Object.class);
        SettableAnyProperty sap = new SettableAnyProperty(property, setter, type, null, null, null);

        SettableAnyProperty deserialized = (SettableAnyProperty) sap.readResolve();

        assert deserialized != null;
        assert deserialized.equals(sap);
        assert deserialized.getClass().equals(sap.getClass());
    }

    @Test
    public void testGetProperty() {
        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = TypeFactory.defaultInstance().constructType(Object.class);
        SettableAnyProperty sap = new SettableAnyProperty(property, setter, type, null, null, null);

        assert sap.getProperty().equals(property);
    }

    @Test
    public void testHasValueDeserializer() {
        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = TypeFactory.defaultInstance().constructType(Object.class);
        SettableAnyProperty sap = new SettableAnyProperty(property, setter, type, null, null, null);

        assert !sap.hasValueDeserializer();
    }

    @Test
    public void testGetType() {
        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = TypeFactory.defaultInstance().constructType(Object.class);
        SettableAnyProperty sap = new SettableAnyProperty(property, setter, type, null, null, null);

        assert sap.getType().equals(type);
    }
}