
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.AnnotatedMember;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.JsonDeserializer;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet_NullValue_ReturnsNull() throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("null");

        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        ObjectIdValueProperty prop = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_REQUIRED);

        Object instance = new Object();
        prop.deserializeAndSet(jsonParser, ctxt, instance);

        assertNull(prop.deserializeSetAndReturn(jsonParser, ctxt, instance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet_ObjectIdPropertyNotSettable_ThrowsUnsupportedOperationException() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        ObjectIdValueProperty prop = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_REQUIRED);

        prop.set(new Object(), new Object());
    }
}
