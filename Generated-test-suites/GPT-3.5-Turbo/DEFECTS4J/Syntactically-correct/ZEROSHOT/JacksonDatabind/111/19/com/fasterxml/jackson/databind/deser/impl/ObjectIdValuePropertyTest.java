import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdWriter;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.NullValueProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty property;
    private Object instance;
    private JsonParser parser;
    private DeserializationContext context;

    @Before
    public void setUp() {
        ObjectIdReader objectIdReader = new ObjectIdReader(new ObjectIdInfo(null, null, null, null), null);
        property = new ObjectIdValueProperty(objectIdReader, null);
        instance = new Object();
        parser = null;
        context = null;
    }

    @Test
    public void testWithName() {
        property.withName(new PropertyName("newName"));
        Assert.assertEquals("newName", property.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = new StdDeserializer<>(Object.class);
        property.withValueDeserializer(deser);
        Assert.assertEquals(deser, property.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws JsonMappingException {
                return null;
            }
        };
        property.withNullProvider(nva);
        Assert.assertEquals(nva, property.getNullProvider());
    }

    @Test
    public void testGetAnnotation() {
        Assert.assertNull(property.getAnnotation(Override.class));
    }

    @Test
    public void testGetMember() {
        Assert.assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = new JsonFactory();
        JsonGenerator generator = factory.createGenerator(null, null);
        TokenBuffer tokenBuffer = TokenBuffer.forGeneration();

        property.deserializeAndSet(tokenBuffer.asParser(), context, instance);

        Assert.assertNotNull(instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = new JsonFactory();
        JsonGenerator generator = factory.createGenerator(null, null);
        TokenBuffer tokenBuffer = TokenBuffer.forGeneration();

        Object result = property.deserializeSetAndReturn(tokenBuffer.asParser(), context, instance);

        Assert.assertNotNull(result);
    }

    @Test
    public void testSet() throws IOException {
        property = new ObjectIdValuePropertyTest.ObjectIdValuePropertyMock(new ObjectIdReader(new ObjectIdInfo(null, null, null, null), null), null);
        property.set(instance, null);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        property = new ObjectIdValuePropertyTest.ObjectIdValuePropertyMock(new ObjectIdReader(new ObjectIdInfo(null, null, null, null), null), null);
        Object result = property.setAndReturn(instance, null);
        Assert.assertNull(result);
    }

    private static class ObjectIdValuePropertyMock extends ObjectIdValueProperty {
        public ObjectIdValuePropertyMock(ObjectIdReader objectIdReader, PropertyMetadata metadata) {
            super(objectIdReader, metadata);
        }
        public void set(Object instance, Object value) throws IOException {
            throw new UnsupportedOperationException("Mock method");
        }
        public Object setAndReturn(Object instance, Object value) throws IOException {
            throw new UnsupportedOperationException("Mock method");
        }
    }

}