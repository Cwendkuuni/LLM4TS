import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import java.util.*;
import com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = TypeFactory.defaultInstance().constructArrayType(Integer.class);
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(result);
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = TypeFactory.defaultInstance().constructParametricType(Map.class, String.class, Integer.class);
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(result);
    }
}