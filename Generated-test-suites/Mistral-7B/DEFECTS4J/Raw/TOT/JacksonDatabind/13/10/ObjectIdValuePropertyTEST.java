import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReaderForString;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ObjectIdValuePropertyTest {

    private JsonFactory jsonFactory;
    private ObjectMapper objectMapper;
    private ObjectIdReader objectIdReader;

    @Before
    public void setup() throws Exception {
        jsonFactory = new JsonFactory();
        objectMapper = new ObjectMapper(jsonFactory);
        objectIdReader = new ObjectIdReaderForString();
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        Object testInstance = new Object() {};

        String idValue = "someIdValue";
        JsonParser jp = mock(JsonParser.class);
        when(jp.getValueAsString()).thenReturn(idValue);

        Object deserializedId = property.deserializeAndSet(jp, new DeserializationContext(objectMapper), testInstance);

        ObjectIdReader idReader = property._objectIdReader;
        ObjectId id = idReader.readerForId(idValue);
        Object idInstance = idReader.resolveId(id, Object.class);
        BeanPropertyDefinition idPropertyDefinition = AnnotationIntrospector.findProperty(Object.class, idReader.getIdName());
        Field idField = ClassUtil.findSyntheticAccessibleField(Object.class, idPropertyDefinition.getName());

        Object expectedInstance = BeanUtil.setFieldValue(testInstance, idField, idInstance);
        assertThat(deserializedId, is(instanceOf(ObjectId.class)));
        assertThat(testInstance, is(expectedInstance));

        property.set(testInstance, idInstance);
        Object returnedInstance = property.get(testInstance);
        assertThat(returnedInstance, is(expectedInstance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        Object testInstance = new Object() {};

        Object idValue = "someIdValue";
        when(objectMapper.readValue(mock(JsonParser.class).mock(), new TypeReference<Object>() {}))
                .thenReturn(idValue);

        property.deserializeAndSet(mock(JsonParser.class), new DeserializationContext(objectMapper), testInstance);

        try {
            property.set(testInstance, idValue);
        } finally {
            throw new AssertionError("Unexpected success");
        }
    }
}