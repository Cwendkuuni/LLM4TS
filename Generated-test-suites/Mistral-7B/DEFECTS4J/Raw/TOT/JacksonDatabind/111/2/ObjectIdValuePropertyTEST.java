import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.std.ObjectIdReaderForString;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ObjectIdValuePropertyTest {

    private ObjectMapper mapper;
    private AnnotationIntrospector introspector;
    private ObjectIdReader objectIdReader;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        introspector = mapper.getAnnotationIntrospector();
        objectIdReader = new ObjectIdReaderForString();
    }

    @Test
    public void testDeserializeAndSetNullId() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);

        JsonParser parser = mapper.getFactory().createParser("null");
        Object testInstance = new Object();

        property.deserializeAndSet(parser, mapper.getDeserializationContext(), testInstance);

        assertThat(testInstance).isNull();
    }

    @Test
    public void testDeserializeAndSetValidId() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);

        JsonParser parser = mapper.getFactory().createParser("123");
        Object testInstance = new Object();

        Object id = property.deserializeSetAndReturn(parser, mapper.getDeserializationContext(), testInstance);

        ObjectNode testInstanceNode = mapper.readValue(mapper.writeValueAsString(testInstance), ObjectNode.class);
        ObjectNode idNode = testInstanceNode.path("id");
        assertThat(idNode).isNotNull();
        assertThat(idNode.asText()).isEqualTo("123");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);
        Object testInstance = new Object();
        Object id = new Object();

        property.set(testInstance, id);
    }

    @Test
    public void testSetAndGetId() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);
        Object testInstance = new Object();
        Object id = new Object();

        property.set(testInstance, id);
        Object gotId = property.getValue(testInstance);

        ObjectNode testInstanceNode = mapper.readValue(mapper.writeValueAsString(testInstance), ObjectNode.class);
        ObjectNode idNode = testInstanceNode.path("id");
        assertThat(idNode).isNotNull();
        assertThat(idNode.asText()).isEqualTo(gotId.toString());
    }

    @Test
    public void testAnnotations() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);

        AnnotatedMember member = introspector.findMember("id", Object.class);
        A ann = member.getAnnotation(JsonProperty.class);

        assertThat(ann).isInstanceOf(JsonProperty.class);
        JsonProperty jsonProperty = (JsonProperty) ann;
        assertThat(jsonProperty.value()).isEqualTo("id");
    }
}