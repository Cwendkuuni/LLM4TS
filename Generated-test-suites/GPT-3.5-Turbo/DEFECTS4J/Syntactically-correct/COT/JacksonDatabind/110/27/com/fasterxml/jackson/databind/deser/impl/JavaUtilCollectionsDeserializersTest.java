import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNull(result);
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNull(result);
    }

    @Test
    public void testConverter() {
        int kind = 1;
        JavaType concreteType = null;
        Class<?> rawSuper = null;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter result = JavaUtilCollectionsDeserializers.converter(kind, concreteType, rawSuper);
        assertNotNull(result);
    }
}