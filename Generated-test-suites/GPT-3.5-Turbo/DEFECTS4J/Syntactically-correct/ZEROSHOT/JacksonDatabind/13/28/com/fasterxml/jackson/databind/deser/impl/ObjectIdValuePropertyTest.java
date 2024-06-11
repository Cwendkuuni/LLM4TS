import org.junit.Test;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import java.io.IOException;
import java.lang.annotation.Annotation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.deser.PropertyMetadata;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null, null);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);

        JsonParser jp = new ObjectMapper().getFactory().createParser("{}");
        DeserializationContext ctxt = new DeserializationContext(null, null);
        Object instance = new Object();

        objectIdValueProperty.deserializeAndSet(jp, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null, null);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);

        JsonParser jp = new ObjectMapper().getFactory().createParser("{}");
        DeserializationContext ctxt = new DeserializationContext(null, null);
        Object instance = new Object();

        objectIdValueProperty.deserializeSetAndReturn(jp, ctxt, instance);
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null, null);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);

        Object instance = new Object();
        Object value = new Object();

        objectIdValueProperty.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null, null);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);

        Object instance = new Object();
        Object value = new Object();

        objectIdValueProperty.setAndReturn(instance, value);
    }
}