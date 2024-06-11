import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.io.IOException;
import java.util.Map;

public class SettableAnyPropertyTest {
    
    private ObjectMapper objectMapper;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private JsonDeserializer<Object> valueDeserializer;
    private TypeDeserializer valueTypeDeserializer;
    private KeyDeserializer keyDeserializer;
    
    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        jsonParser = objectMapper.getFactory().createParser("");
        deserializationContext = objectMapper.getDeserializationContext();
        valueDeserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        valueTypeDeserializer = null;
        keyDeserializer = null;
    }
    
    @Test
    public void testConstructorWithAllParameters() {
        BeanProperty property = null;
        AnnotatedMember setter = null;
        JavaType type = null;
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertNotNull(settableAnyProperty);
        assertEquals(property, settableAnyProperty.getProperty());
        assertEquals(setter, settableAnyProperty._setter);
        assertEquals(type, settableAnyProperty._type);
        assertEquals(valueDeserializer, settableAnyProperty._valueDeserializer);
        assertEquals(valueTypeDeserializer, settableAnyProperty._valueTypeDeserializer);
        assertEquals(keyDeserializer, settableAnyProperty._keyDeserializer);
        assertEquals(setter instanceof AnnotatedField, settableAnyProperty._setterIsField);
    }
    
    @Test
    public void testConstructorWithFiveParameters() {
        BeanProperty property = null;
        AnnotatedMember setter = null;
        JavaType type = null;
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, valueDeserializer, valueTypeDeserializer);
        assertNotNull(settableAnyProperty);
        assertEquals(property, settableAnyProperty.getProperty());
        assertEquals(setter, settableAnyProperty._setter);
        assertEquals(type, settableAnyProperty._type);
        assertEquals(valueDeserializer, settableAnyProperty._valueDeserializer);
        assertEquals(valueTypeDeserializer, settableAnyProperty._valueTypeDeserializer);
        assertNull(settableAnyProperty._keyDeserializer);
        assertEquals(setter instanceof AnnotatedField, settableAnyProperty._setterIsField);
    }
    
    @Test
    public void testWithDeserializer() {
        BeanProperty property = null;
        AnnotatedMember setter = null;
        JavaType type = null;
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertNotNull(settableAnyProperty);
        JsonDeserializer<Object> newDeserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        SettableAnyProperty newSettableAnyProperty = settableAnyProperty.withValueDeserializer(newDeserializer);
        assertNotNull(newSettableAnyProperty);
        assertEquals(property, newSettableAnyProperty.getProperty());
        assertEquals(setter, newSettableAnyProperty._setter);
        assertEquals(type, newSettableAnyProperty._type);
        assertEquals(newDeserializer, newSettableAnyProperty._valueDeserializer);
        assertEquals(valueTypeDeserializer, newSettableAnyProperty._valueTypeDeserializer);
        assertEquals(keyDeserializer, newSettableAnyProperty._keyDeserializer);
        assertEquals(setter instanceof AnnotatedField, newSettableAnyProperty._setterIsField);
    }
    
    @Test
    public void testFixAccess() {
        BeanProperty property = null;
        AnnotatedMember setter = null;
        JavaType type = null;
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertNotNull(settableAnyProperty);
        DeserializationConfig config = new DeserializationConfig(null, null, null);
        settableAnyProperty.fixAccess(config);
    }
    
    @Test
    public void testReadResolve() {
        BeanProperty property = null;
        AnnotatedMember setter = null;
        JavaType type = null;
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertNotNull(settableAnyProperty);
        Object resolvedObj = settableAnyProperty.readResolve();
        assertSame(settableAnyProperty, resolvedObj);
    }
    
    @Test
    public void testGetProperty() {
        BeanProperty property = null;
        AnnotatedMember setter = null;
        JavaType type = null;
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertNotNull(settableAnyProperty);
        assertEquals(property, settableAnyProperty.getProperty());
    }
    
    @Test
    public void testHasValueDeserializer() {
        BeanProperty property = null;
        AnnotatedMember setter = null;
        JavaType type = null;
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertNotNull(settableAnyProperty);
        assertTrue(settableAnyProperty.hasValueDeserializer());
    }
    
    @Test
    public void testGetType() {
        BeanProperty property = null;
        AnnotatedMember setter = null;
        JavaType type = null;
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertNotNull(settableAnyProperty);
        assertEquals(type, settableAnyProperty.getType());
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        BeanProperty property = null;
        AnnotatedMember setter = null;
        JavaType type = null;
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertNotNull(settableAnyProperty);
        JsonParser p = null;
        Object instance = null;
        String propName = null;
        settableAnyProperty.deserializeAndSet(p, deserializationContext, instance, propName);
    }
    
    @Test
    public void testDeserialize() throws IOException {
        BeanProperty property = null;
        AnnotatedMember setter = null;
        JavaType type = null;
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertNotNull(settableAnyProperty);
        JsonParser p = null;
        settableAnyProperty.deserialize(p, deserializationContext);
    }
    
    @Test
    public void testSet() throws IOException {
        BeanProperty property = null;
        AnnotatedMember setter = null;
        JavaType type = null;
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertNotNull(settableAnyProperty);
        Object instance = null;
        Object propName = null;
        Object value = null;
        settableAnyProperty.set(instance, propName, value);
    }
    
    @Test
    public void test_throwAsIOE() throws IOException {
        BeanProperty property = null;
        AnnotatedMember setter = null;
        JavaType type = null;
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertNotNull(settableAnyProperty);
        Exception e = new IllegalArgumentException();
        Object propName = null;
        Object value = null;
        settableAnyProperty._throwAsIOE(e, propName, value);
    }
    
    @Test
    public void testGetClassName() {
        BeanProperty property = null;
        AnnotatedMember setter = null;
        JavaType type = null;
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertNotNull(settableAnyProperty);
        assertEquals("_UNKNOWN_", settableAnyProperty.getClassName());
    }
    
    @Test
    public void testAnySetterReferring() throws IOException {
        BeanProperty property = null;
        AnnotatedMember setter = null;
        JavaType type = null;
        SettableAnyProperty parent = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertNotNull(parent);
        UnresolvedForwardReference reference = null;
        Class<?> type1 = null;
        Object instance = null;
        String propName = null;
        SettableAnyProperty.AnySetterReferring referring = new SettableAnyProperty.AnySetterReferring(parent, reference, type1, instance, propName);
        assertNotNull(referring);
        Object id = null;
        Object value = null;
        referring.handleResolvedForwardReference(id, value);
    }
}