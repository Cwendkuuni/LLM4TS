import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testConstructor() {
        JavaType bt = null;
        TypeIdResolver idRes = null;
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = null;
        As inclusion = As.PROPERTY;

        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl, inclusion);

        assertNotNull(deserializer);
    }

    @Test
    public void testForProperty() {
        AsPropertyTypeDeserializer src = new AsPropertyTypeDeserializer(null, null, null, true, null, As.PROPERTY);
        BeanProperty property = null;
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(src, property);

        assertEquals(deserializer, deserializer.forProperty(property));
    }

    @Test
    public void testGetTypeInclusion() {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, true, null, As.PROPERTY);

        assertEquals(As.PROPERTY, deserializer.getTypeInclusion());
    }

    @Test
    public void testDeserializeTypedFromObject() {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, true, null, As.PROPERTY);

        try {
            deserializer.deserializeTypedFromObject(p, ctxt);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testDeserializeTypedForId() {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        TokenBuffer tb = null;
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, true, null, As.PROPERTY);

        try {
            deserializer._deserializeTypedForId(p, ctxt, tb);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl() {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        TokenBuffer tb = null;
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, true, null, As.PROPERTY);

        try {
            deserializer._deserializeTypedUsingDefaultImpl(p, ctxt, tb);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testDeserializeTypedFromAny() {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, true, null, As.PROPERTY);

        try {
            deserializer.deserializeTypedFromAny(p, ctxt);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
}