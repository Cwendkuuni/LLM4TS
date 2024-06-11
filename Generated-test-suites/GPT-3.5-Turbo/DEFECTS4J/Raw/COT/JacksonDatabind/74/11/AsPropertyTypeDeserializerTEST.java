
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsPropertyTypeDeserializerTest {
    
    // Test method: AsPropertyTypeDeserializer(JavaType bt, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl, As inclusion)
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
        assertEquals(bt, deserializer.getBaseType());
        assertEquals(idRes, deserializer.getIdResolver());
        assertEquals(typePropertyName, deserializer._typePropertyName);
        assertEquals(typeIdVisible, deserializer._typeIdVisible);
        assertEquals(defaultImpl, deserializer.getDefaultImpl());
        assertEquals(inclusion, deserializer._inclusion);
    }
    
    // Test method: forProperty(BeanProperty prop)
    @Test
    public void testForProperty() {
        // TODO
    }
    
    // Test method: getTypeInclusion()
    @Test
    public void testGetTypeInclusion() {
        // TODO
    }
    
    // Test method: deserializeTypedFromObject(JsonParser p, DeserializationContext ctxt)
    @Test
    public void testDeserializeTypedFromObject() {
        // TODO
    }
    
    // Test method: _deserializeTypedUsingDefaultImpl(JsonParser p, DeserializationContext ctxt, TokenBuffer tb)
    @Test
    public void testDeserializeTypedUsingDefaultImpl() {
        // TODO
    }
    
    // Test method: deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt)
    @Test
    public void testDeserializeTypedFromAny() {
        // TODO
    }
    
    // Test method: deserializeTypedFromObject(JsonParser p, DeserializationContext ctxt)
    @Test(expected = JsonParseException.class)
    public void testDeserializeTypedFromObjectThrowsJsonParseException() {
        // TODO
    }
}
