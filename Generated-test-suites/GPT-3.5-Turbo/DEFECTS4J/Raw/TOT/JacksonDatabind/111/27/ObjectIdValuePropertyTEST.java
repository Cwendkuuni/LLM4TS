
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.ReferenceType;
import com.fasterxml.jackson.databind.util.ImmutableEmptyMap;

public class ObjectIdValuePropertyTest {
    
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    private ObjectIdValueProperty property;
    private ObjectIdReader objectIdReader;
    private PropertyMetadata metadata;
    private JsonDeserializer<?> deserializer;
    private NullValueProvider nullValueProvider;
    private PropertyName newName;
    
    @Before
    public void setUp() {
        objectIdReader = new ObjectIdReader(null, null, null, null);
        metadata = PropertyMetadata.STD_REQUIRED;
        deserializer = null;
        nullValueProvider = null;
        newName = new PropertyName("newName");
        
        property = new ObjectIdValueProperty(objectIdReader, metadata);
    }
    
    @Test
    public void testWithNullProvider() {
        ObjectIdValueProperty newProperty = property.withNullProvider(nullValueProvider);
        assertNull(newProperty.getNullProvider());
        assertSame(property._valueDeserializer, newProperty._valueDeserializer);
        assertSame(property._objectIdReader, newProperty._objectIdReader);
    }
    
    @Test
    public void testWithName() {
        ObjectIdValueProperty newProperty = property.withName(newName);
        assertEquals(newName, newProperty.getFullName());
        assertSame(property._valueDeserializer, newProperty._valueDeserializer);
        assertSame(property._objectIdReader, newProperty._objectIdReader);
    }
    
    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty newProperty = property.withValueDeserializer(deserializer);
        assertNull(newProperty.getNullProvider());
        assertSame(deserializer, newProperty._valueDeserializer);
        assertSame(property._objectIdReader, newProperty._objectIdReader);
    }
    
    @Test
    public void testDeserializeAndSet_NullValue() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("{}");
        DeserializationContext deserializationContext = new ObjectMapper().tokenStreamFactory().createDeserializationContext();
        Object instance = new Object();
        
        assertNull(property.deserializeSetAndReturn(jsonParser, deserializationContext, instance));
    }
    
    @Test
    public void testDeserializeAndSet_NullIdProperty() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("{\"id\":null}");
        DeserializationContext deserializationContext = new ObjectMapper().tokenStreamFactory().createDeserializationContext();
        Object instance = new Object();
        ObjectIdValueProperty newProperty = property.withValueDeserializer(deserializer);
        ObjectIdReader newObjectIdReader = new ObjectIdReader(null, null, null, null);
        newProperty._objectIdReader = newObjectIdReader;
        
        Object actualResult = newProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
        
        assertSame(instance, actualResult);
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("{\"id\":1}");
        DeserializationContext deserializationContext = new ObjectMapper().tokenStreamFactory().createDeserializationContext();
        Object instance = new Object();
        ReadableObjectId readableObjectId = new ReadableObjectId(null);
        SettableBeanProperty idProperty = new SettableBeanProperty.ReadOnlyProperty(null, ReferenceType.construct(Object.class), null, null, null, 0);
        objectIdReader.setIdProperty(idProperty);
        deserializer = jsonParser.readValueAs(idProperty.getType(), (TypeDeserializer) null);
        Object expectedResult = new Object();
        readableObjectId.bindItem(expectedResult);
        
        objectIdReader.generator = readableObjectId;
        Object actualResult = property.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
        
        assertSame(expectedResult, actualResult);
    }
    
    @Test
    public void testSetAndReturn_IdPropertyNull() throws IOException {
        Object instance = new Object();
        assertNull(property.setAndReturn(instance, null));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn_NoIdProperty() throws IOException {
        SettableBeanProperty idProp = null;
        ObjectIdValueProperty newProperty = property.withValueDeserializer(deserializer);
        ObjectIdReader newObjectIdReader = new ObjectIdReader(null, idProp, null, null);
        newProperty._objectIdReader = newObjectIdReader;
        Object instance = new Object();
        
        newProperty.setAndReturn(instance, new Object());
    }
    
}
