import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import junit.framework.TestCase;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class CollectionDeserializerTest extends TestCase {

    public void testDeserialize() {
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(Collection.class, new JsonDeserializer<Object>(), null, null);
        JsonParser p = new JsonParser() {
            @Override
            public void close() throws IOException {

            }

            @Override
            public JsonToken nextToken() throws IOException {
                return null;
            }

            @Override
            public JsonParser skipChildren() throws IOException {
                return null;
            }

            @Override
            public JsonParser configure(JsonParser.Feature feature, boolean state) {
                return null;
            }

            @Override
            public boolean isEnabled(JsonParser.Feature feature) {
                return false;
            }

            @Override
            public JsonParser.Config getConfig() {
                return null;
            }
        };
        DeserializationContext ctxt = null;
        try {
            Collection<Object> result = collectionDeserializer.deserialize(p, ctxt);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testDeserializeWithType() {
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(Collection.class, new JsonDeserializer<Object>(), null, null);
        JsonParser jp = new JsonParser() {
            @Override
            public void close() throws IOException {

            }

            @Override
            public JsonToken nextToken() throws IOException {
                return null;
            }

            @Override
            public JsonParser skipChildren() throws IOException {
                return null;
            }

            @Override
            public JsonParser configure(JsonParser.Feature feature, boolean state) {
                return null;
            }

            @Override
            public boolean isEnabled(JsonParser.Feature feature) {
                return false;
            }

            @Override
            public JsonParser.Config getConfig() {
                return null;
            }
        };
        DeserializationContext ctxt = null;
        try {
            Object result = collectionDeserializer.deserializeWithType(jp, ctxt, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}